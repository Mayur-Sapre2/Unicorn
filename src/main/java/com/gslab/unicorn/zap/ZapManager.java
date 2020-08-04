package com.gslab.unicorn.zap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseSet;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;
import com.gslab.unicorn.exceptions.UnicornZapException;
import com.gslab.unicorn.logger.LogManager;
import com.gslab.unicorn.utils.CommonUtils;
import com.gslab.unicorn.utils.Config;

/**
 * @author Mayur_Sapre
 * This provides the all functioning to work with Zap penetration testing tool
 */

public class ZapManager {
		
    static final String ZAP_HOSTNAME = "localhost";
    private static final String ZAP_API_KEY = "";
    private static ZapManager instance = null;
    String WEB_APP_URL = "https://192.168.43.55:8443/orchestrator/#/discovery";
    String SPIDER_WEB_APP_URL="https://192.168.43.55:8443/orchestrator/#/signin";
    int CONNECTION_TIMEOUT = 15000; //milliseconds
    int PORT=8905;
    Process process;
    private ClientApi api;
    private PrintWriter writer;

    private ZapManager() {
        new LogManager().setSuitLogger();
    }

    public static synchronized ZapManager getInstance() {
        if (instance == null) instance = new ZapManager();
        return instance;
    }

    private PrintWriter createWriter() throws IOException {
        System.out.println("Security report: " + Config.getReportsLocation());
        return new PrintWriter(new BufferedWriter(new FileWriter(new File(Config.getReportsLocation(), "Penetration-Test-Report.html"))));
    }
    
    private final static String[] policyNames = 
        {"directory-browsing","cross-site-scripting",
         "sql-injection","path-traversal","remote-file-inclusion",
         "server-side-include","script-active-scan-rules",
         "server-side-code-injection","external-redirect",
         "crlf-injection"};

    /**
     * This starts zap server
     *
     * @param zapHomeDirectoryPath zap installed home directory path
     * @return zap listening session port number
     * @throws Exception
     */
    public int startZapServer(String zapHomeDirectoryPath) throws Exception {
        String zapExecutable = null;
        try {
            if (process == null) {
                if (!zapHomeDirectoryPath.contains("zap.bat") || !zapHomeDirectoryPath.contains("zap.sh")) {
                    if (CommonUtils.getOSType().contains("windows"))
                        zapExecutable = zapHomeDirectoryPath + File.separator + "zap.bat";
                    else if (CommonUtils.getOSType().contains("linux") || CommonUtils.getOSType().contains("mac"))
                        zapExecutable = zapHomeDirectoryPath + File.separator + "zap.sh";
                } else {
                    zapExecutable = zapHomeDirectoryPath;
                }

                File zapProgramFile = new File(zapExecutable);
                // port = findOpenPortOnAllLocalInterfaces();
                List<String> params = new ArrayList<>();
                params.add(zapProgramFile.getAbsolutePath());
                params.add("-daemon");
                params.add("-host");
                params.add(ZAP_HOSTNAME);
                params.add("-port");
                params.add("8905");
                //params.add(String.valueOf(Config.getZapSessionPort()));
                params.add("-config");
                params.add("api.disablekey=true");
                LogManager.suitLogger.info("Start ZAProxy [" + zapProgramFile.getAbsolutePath() + "] on port: " +PORT );
                ProcessBuilder pb = new ProcessBuilder().inheritIO();
                pb.directory(zapProgramFile.getParentFile());
                process = pb.command(params.toArray(new String[params.size()])).start();
                waitForSuccessfulConnectionToZap();
                api = new ClientApi(ZAP_HOSTNAME, PORT, ZAP_API_KEY);

            } else {
                LogManager.suitLogger.warn("ZAP already started.");
            }
            return PORT;
        } catch (Exception e) {
            throw new UnicornZapException("Exception while starting zap server. ", e);
        }
    }

    /**
     * Stop active session of the zap server
     */
    public void stopZapServer() {
        if (process == null) return; //ZAP not running
        try {
            LogManager.suitLogger.info("Stopping ZAP");
            api.core.shutdown();
            Thread.sleep(2000);
            process.destroy();
        } catch (final Exception e) {
            LogManager.suitLogger.error("Error shutting down ZAP.", e);
        }
    }

    /**
     * This waits for zap server is up and fully running
     */
    private void waitForSuccessfulConnectionToZap() throws Exception {
        int timeoutInMs = CONNECTION_TIMEOUT;
        int connectionTimeoutInMs = timeoutInMs;
        int pollingIntervalInMs = 1000;
        boolean connectionSuccessful = false;
        long startTime = System.currentTimeMillis();
        Socket socket = null;
        do {
            try {
                LogManager.suitLogger.info("Attempting to connect to ZAP API on: " + ZAP_HOSTNAME + " port: " +PORT);
                socket = new Socket();
                socket.connect(new InetSocketAddress(ZAP_HOSTNAME, PORT), connectionTimeoutInMs);
                connectionSuccessful = true;
                LogManager.suitLogger.info("Connected to ZAP successfully");
            } catch (SocketTimeoutException ignore) {
                throw new UnicornZapException("Unable to connect to ZAP's proxy after " + timeoutInMs + " milliseconds.");
            } catch (IOException ignore) {
                // and keep trying but wait some time first...
                try {
                    Thread.sleep(pollingIntervalInMs);
                } catch (InterruptedException e) {
                    throw new UnicornZapException("The task was interrupted while sleeping between connection polling.", e);
                }

                long ellapsedTime = System.currentTimeMillis() - startTime;
                if (ellapsedTime >= timeoutInMs) {
                    throw new UnicornZapException("Unable to connect to ZAP's proxy after " + timeoutInMs + " milliseconds.");
                }
                connectionTimeoutInMs = (int) (timeoutInMs - ellapsedTime);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } while (!connectionSuccessful);
    }


    /**
     * Opens a url with a specified proxy settings.
     *
     * @param proxy  the proxy by which to connect to the apiurl.
     * @param apiurl the url to which we want to connect.
     * @return List of response strings returned by the clientApi.
     * @throws Exception
     */
    private List<String> openUrlViaProxy(Proxy proxy, String apiurl)
            throws Exception {

        List<String> response = new ArrayList<>();
        URL url = new URL(apiurl);
        HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
        uc.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                uc.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.add(inputLine);
        }
        in.close();
        return response;
    }

    /**
     * Crowls the URL passed to it and prints its progression status every 5 seconds.
     *
     * @return Returns true if the crawling succeeds, and returns false if anything goes wrong.
     */
    public boolean spiderScan() throws Exception {
        try {
            // Start spidering the target
            LogManager.suitLogger.info("Spider : " + SPIDER_WEB_APP_URL);
            // It's not necessary to pass the ZAP API key again, already set when creating the
            // ClientApi.
            ApiResponse resp = api.spider.scan(SPIDER_WEB_APP_URL, null, null, null, null);
            String scanid;
            int progress;

            // The scan now returns a scan id to support concurrent scanning
            scanid = ((ApiResponseElement) resp).getValue();
            LogManager.suitLogger.info("scanid : " + scanid);
            // Poll the status until it completes
            while (true) {
                Thread.sleep(1000);
                progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanid)).getValue());
                LogManager.suitLogger.info("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            LogManager.suitLogger.info("Spider scan completed");

            // Give the passive scanner a chance to complete
            Thread.sleep(6000);
            return true;
        } catch (Exception ex) {
            throw new UnicornZapException("Exception while running spider scan", ex);
        }
    }

    /**
     * Scans the URL passed to it and prints status of the scanning progress every 15 seconds.
     *
     * @return Returns true if the scan succeeds, and returns false if anything goes wrong.
     */
    public boolean activeScan() throws Exception {
        try {
            LogManager.suitLogger.info("Active scan starting...");
            // api = new ClientApi(ZAP_HOSTNAME, ZAP_SESSION_PORT, ZAP_API_KEY);
            ApiResponse response = api.ascan.scan(WEB_APP_URL, null, null, null, null, null);

            String scanid;
            int progress;

            // The scan now returns a scan id to support concurrent scanning
            scanid = ((ApiResponseElement) response).getValue();
            LogManager.suitLogger.info("scanid : " + scanid);
            // Poll the status until it completes
            while (true) {
                Thread.sleep(1000);
                progress = Integer.parseInt(((ApiResponseElement) api.ascan.status(scanid)).getValue());
                LogManager.suitLogger.info("Active scan progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            LogManager.suitLogger.info("Active scan completed");
            return true;

        } catch (Exception ex) {
            throw new UnicornZapException("Failed to perform penetration testing active scan .. ", ex);
        }
    }

    /**
     * Generate html report for the penetration testing
     *
     * @return report path
     */

    public String generateHtmlReport() {
        try {
            LogManager.suitLogger.info("Generating HTML report...");
            writer = createWriter();
            writer.println(new String(api.core.htmlreport()));
            writer.flush();
            writer.close();
            Thread.sleep(2000);
            return new File(Config.getReportsLocation() + File.separator + "penetration-test-report.html").getAbsolutePath();
        } catch (Exception e) {
            LogManager.suitLogger.error("Failed to generate penetration test html report. ", e);
        }
        return null;
    }


    /**
     * Set url which needs to be exclude from security scanning
     *
     * @param url excluded url
     * @return ApiResponse
     */
    public ApiResponse setExcludeFromSpider(String url) {
        try {
            return api.spider.excludeFromScan(Pattern.quote(Pattern.compile(url).pattern()) + ".*");
        } catch (Exception e) {
            LogManager.suitLogger.error("Failed to add regex to exclude from spider scan !", e);
        }
        return null;
    }

    /**
     * Set url which needs to be exclude from zap proxy
     *
     * @param url excluded url
     * @return ApiResponse
     */
    public ApiResponse setExcludeFromProxy(String url) {
        try {
            return api.core.excludeFromProxy(Pattern.quote(Pattern.compile(url).pattern()) + ".*");
        } catch (Exception e) {
            LogManager.suitLogger.error("Failed to add regex to exclude from proxy !", e);
        }
        return null;
    }

    /**
     * Set testing context name
     *
     * @param contextName any name
     * @param contextUrl  AUT url
     * @return ApiResponse
     */
    public ApiResponse setContext(String contextName, String contextUrl) {
        try {
            api.context.newContext(contextName);
            return api.context.includeInContext(contextName, Pattern.quote(Pattern.compile(contextUrl).pattern()) + ".*");
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.suitLogger.error("Failed to add context !", e);
        }
        return null;
    }

    /**
     * Get the context information
     *
     * @param contextName name of context
     * @return ApiResponseSet
     * @throws Exception
     */
    public ApiResponseSet getContextInfo(String contextName) throws Exception {
        ApiResponseSet context;
        try {
            context = (ApiResponseSet) api.context.context(contextName);
        } catch (ClientApiException e) {
            throw new UnicornZapException("Zap exception while getting info for context: " + contextName, e);
        }
        return context;
    }

    /**
     * Get the context id
     *
     * @param contextName name of the context
     * @return String id
     * @throws Exception
     */
    public String getContextId(String contextName) throws Exception {
        return getContextInfo(contextName).getValue("id").toString();
    }

    /**
     * This will set the form based authentication to the AUT for security scanning by logged in
     *
     * @param contextName   name of context
     * @param loginUrl      AUT login-url
     * @param userLoginName login name credential
     * @param userPasswd    login password
     * @throws Exception
     */
    public void setFormBasedUserAuthentication(String contextName, String loginUrl, String userLoginName, String userPasswd) throws Exception {
        try {
            // Setup the authentication method
            String contextId = getContextId(contextName);
            String loginRequestData = String.format("username={%s}&password={%s}", userLoginName, userPasswd);

            // Prepare the configuration in a format similar to how URL parameters are formed. This
            // means that any value we add for the configuration values has to be URL encoded.
            StringBuilder formBasedConfig = new StringBuilder();
            formBasedConfig.append("loginUrl=").append(URLEncoder.encode(loginUrl, "UTF-8"));
            formBasedConfig.append("&loginRequestData=").append(URLEncoder.encode(loginRequestData, "UTF-8"));

            System.out.println("Setting form based authentication configuration as: "
                    + formBasedConfig.toString());
            api.authentication.setAuthenticationMethod(contextId, "formBasedAuthentication", formBasedConfig.toString());

            // Check if everything is set up ok
            LogManager.suitLogger.info("Authentication config: " + api.authentication.getAuthenticationMethod(contextId).toString(0));
        } catch (Exception e) {
            throw new UnicornZapException("Zap Exception while setting up form based authentication", e);
        }
    }

   /*
    private void setUserAuthConfig(String contextName, String userName, String loginUserName, String loginUserPasswd) throws Exception {
        String contextId = null;
        try {
            // Make sure we have at least one user
            contextId = getContextId(contextName);
            //  api.users.newUser(contextId,userName);
            String userId = getUserId(contextId, userName);

            // Prepare the configuration in a format similar to how URL parameters are formed. This
            // means that any value we add for the configuration values has to be URL encoded.
            StringBuilder userAuthConfig = new StringBuilder();
            userAuthConfig.append("username=").append(URLEncoder.encode(loginUserName, "UTF-8"));
            userAuthConfig.append("&password=").append(URLEncoder.encode(loginUserPasswd, "UTF-8"));

            UnicornLogManager.suitLogger.info("Setting user authentication configuration as: " + userAuthConfig.toString());
            api.users.setAuthenticationCredentials(contextId, userId, userAuthConfig.toString());

            // Check if everything is set up ok
            UnicornLogManager.suitLogger.info("Authentication config: " + api.users.getUserById(contextId, userId).toString(0));
        } catch (Exception e) {
            throw new UnicornZapException("Exception while setting user auth configuration for user: " + userName + " in context having context id: " + contextId, e);
        }
    }*/

    /**
     * Get created user in id from context
     *
     * @param contextId created context if
     * @param userName  user name
     * @return String user id
     * @throws Exception
     */
    public String getUserId(String contextId, String userName) throws Exception {
        return ((ApiResponseElement) api.users.newUser(contextId, userName)).getValue();
    }

    /**
     * This will set the logged-in(log out url) indicator to zap.
     *
     * @param contextName       name of context
     * @param loggedInIndicator log-out url
     * @throws Exception
     */
    public void setLoggedInIndicator(String contextName, String loggedInIndicator) throws Exception {
        // Prepare values to set, with the logged in indicator as a regex matching the logout link
        String contextId = getContextId(contextName);

        // Actually set the logged in indicator
        api.authentication.setLoggedInIndicator(contextId, java.util.regex.Pattern.quote(loggedInIndicator));

        // Check out the logged in indicator that is set
        LogManager.suitLogger.info("Configured logged in indicator regex: " + ((ApiResponseElement) api.authentication.getLoggedInIndicator(contextId)).getValue());
    }

    /**
     * This will set the logged-out(log-in url) indicator to zap.
     *
     * @param contextName       name of context
     * @param loggedInIndicator log-in url
     * @throws Exception
     */
    public void setLoggedOutIndicator(String contextName, String loggedInIndicator) throws Exception {
        // Prepare values to set, with the logged in indicator as a regex matching the logout link
        String contextId = getContextId(contextName);

        // Actually set the logged in indicator
        api.authentication.setLoggedOutIndicator(contextId, java.util.regex.Pattern.quote(loggedInIndicator));

        // Check out the logged in indicator that is set
        LogManager.suitLogger.info("Configured logged in indicator regex: " + ((ApiResponseElement) api.authentication.getLoggedInIndicator(contextId)).getValue());
    }

    /**
     * Create new user in the context
     *
     * @param contextName name of context
     * @param userName    user name
     * @throws Exception
     */
    public void addNewUser(String contextName, String userName) throws Exception {
        try {
            api.users.newUser(getContextId(contextName), userName);
        } catch (Exception e) {
            throw new UnicornZapException("Exception while adding new user: " + userName + " in context: " + contextName);
        }
    }

    /**
     * Remove the existing user from the context
     *
     * @param contextName name of the context
     * @param userName    name of the created user
     * @throws Exception
     */
    public void removeUser(String contextName, String userName) throws Exception {
        try {
            api.users.removeUser(getContextId(contextName), getUserId(getContextId(contextName), userName));
        } catch (Exception e) {
            throw new UnicornZapException("Exception while removing user: " + userName + " from context: " + contextName);
        }
    }

   /* public void enableUser(String contextName, String userName) throws Exception {
        try {
            api.users.setUserEnabled(getContextId(contextName), getUserId(contextName, userName), "true");
        } catch (Exception e) {
            throw new UnicornZapException("Exception while removing user: " + userName + " from context: " + contextName);
        }
    }*/
    
    
    /*
     * Method to filter the generated alerts based on Risk and Confidence
     */
     private List<Alert> filterAlerts(List<Alert> alerts)
     {
     List<Alert> filteredAlerts = new ArrayList<Alert>();
         for (Alert alert : alerts)
         {
         // Filtering based on Risk: High and Confidence: Not Low
             if (alert.getRisk().equals(Alert.Risk.High) && alert.getConfidence() != Alert.Confidence.Low)
             filteredAlerts.add(alert);
         }
         return filteredAlerts;
     }    
}







