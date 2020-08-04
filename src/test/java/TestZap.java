import com.gslab.unicorn.driver.web.WebDriverManager;
import com.gslab.unicorn.enums.BROWSER;
import com.gslab.unicorn.zap.ZapManager;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.ClientApi;

public class TestZap {
    static final String ZAP_HOSTNAME = "localhost";
    static final int ZAP_SESSION_PORT = 5555;
    static final String ZAP_PROXY = ZAP_HOSTNAME + ":" + ZAP_SESSION_PORT;
    private static final String ZAP_ADDRESS = "localhost";
    private static final int ZAP_PORT = 5555;
    private static final String ZAP_API_KEY = ""; // Change this if you have set the apikey in ZAP via Options / API
    //private static final String TARGET = "http://10.43.11.83";

    ClientApi api;
    String WEB_APP_URL = "https://192.168.43.55:8443/orchestrator/#/signin";

    /**
     * This method used to login to the web app you're trying to scan.
     *
     * @param username Username of the login form.
     * @param password Password of the login form.
     */
    public void login(String username,String password) throws Exception {
/*        org.openqa.selenium.WebDriver driver;
        org.openqa.selenium.Proxy proxym = new org.openqa.selenium.Proxy();
        proxym.setHttpProxy(ZAP_PROXY).setSslProxy(ZAP_PROXY);

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setAcceptInsecureCerts(true);
        firefoxOptions.setCapability("marionette", true);
        firefoxOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        firefoxOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);
        firefoxOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
        firefoxOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
        firefoxOptions.setLogLevel(FirefoxDriverLogLevel.TRACE);
        firefoxOptions.setProxy(proxym);

        WebDriverManager.getInstance(DriverManagerType.FIREFOX).setup();*/
        org.openqa.selenium.WebDriver driver = new WebDriverManager().initWebDriver(BROWSER.FIREFOX);// new FirefoxDriver(firefoxOptions);

        driver.get(WEB_APP_URL);

        //You'll have to check the HTML of dvwa login form to get the correct ID's for the code below:
        driver.findElement(By.xpath("//*[@name='username']")).sendKeys(username);
        driver.findElement(By.xpath("//*[@name='password']")).sendKeys(password);
        driver.findElement(By.xpath("//*[text()='SIGN IN']")).click();
        //driver.quit();
    }

    /**
     * Automatically crawls and scans DVWA web app and prints the alerts in console.
     *
     * @throws Exception
     */
    public void scan() throws Exception {

        login("administrator","$ecure@Av19");
        if (ZapManager.getInstance().spiderScan() == false) {
            System.out.println("Spider Failed - see console for details. Continuing...");
        }
        if (ZapManager.getInstance().activeScan() == false) {
            System.out.println("Spider Failed - see console for details. Continuing...");
        }
    }


    @Test
    public void main() throws Exception {
        try {
            ZapManager.getInstance().startZapServer("/usr/local/bin");
            ZapManager.getInstance().setContext("zap-context", WEB_APP_URL);
            ZapManager.getInstance().setExcludeFromSpider(WEB_APP_URL);
            ZapManager.getInstance().setLoggedInIndicator("zap-context", WEB_APP_URL);
            ZapManager.getInstance().setLoggedOutIndicator("zap-context", WEB_APP_URL);
            ZapManager.getInstance().addNewUser("zap-context", "Test11");
            ZapManager.getInstance().removeUser("zap-context", "Test22");
            ZapManager.getInstance().setFormBasedUserAuthentication("zap-context", WEB_APP_URL, "administrator", "$ecure@Av19");
            scan();
            ZapManager.getInstance().generateHtmlReport();
        } finally {
            ZapManager.getInstance().stopZapServer();
        }
    }

}