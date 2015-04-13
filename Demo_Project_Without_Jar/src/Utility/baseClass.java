package Utility;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.utils.Directory;
import atu.testng.reports.utils.Utils;

public class baseClass {
	{
        System.setProperty("atu.reporter.config", "C:\\Automation_Framework\\WorkSpace\\Demo_Project\\atu.properties");
     }
	
    protected WebDriver threadDriver = null;
    public Datatable dataTable = null;
    
    //protected ThreadLocal<RemoteWebDriver> threadDriver = null;
    
    //Below code is remote web driver
	/*
    threadDriver = new ThreadLocal<WebDriver>();
    DesiredCapabilities dc = new DesiredCapabilities();
    FirefoxProfile fp = new FirefoxProfile();
    dc.setCapability(FirefoxDriver.PROFILE, fp);
    dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
    threadDriver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), dc));
    */
    
    @Parameters({ "URL", "browser", "driver", "DatatablePath"})
    
    @BeforeMethod
    public void setUp(String URL, String browser, String driver, String DatatablePath) throws MalformedURLException {
    	if (browser.contains("Firefox"))
    	{
    		threadDriver = new FirefoxDriver();
    	}
    	else if (browser.contains("IE"))
    	{
    		System.setProperty("webdriver.ie.driver",driver + "IEDriverServer.exe");
    		threadDriver = new InternetExplorerDriver();
    	}
    	else if (browser.contains("Chrome"))
    	{
    		System.setProperty("webdriver.chrome.driver",driver + "chromedriver.exe");
    		threadDriver = new ChromeDriver();
    	}
    	
    	threadDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);	
    	    	
    	ATUReports.setWebDriver(threadDriver);
    	setAuthorInfoForReports();
    	ATUReports.indexPageDescription = "My Project Description";
    	
    	//Code for Web Driver Listener
    	EventFiringWebDriver eventFiringWD= new EventFiringWebDriver(threadDriver);
    	EventListenerType2 eventListener1=new EventListenerType2();
    	eventFiringWD.register(eventListener1);
    	threadDriver = eventFiringWD;//Assigning back to Global variable
    	
    	threadDriver.get(URL);
    	
    	if (browser.contains("IE"))
    	{
    		threadDriver.get("javascript:document.getElementById('overridelink').click();");
    	}
    	
    	dataTable = new Datatable();
    	dataTable.Import(DatatablePath + this.getClass().getSimpleName() + ".xls");
    }
 
    public WebDriver getDriver() {
        return threadDriver;
    }
 
    @AfterMethod
    public void closeBrowser() {
            getDriver().quit();     
    }
        
    private void setAuthorInfoForReports() 
    {
    	   ATUReports.setAuthorInfo("Automation Tester", Utils.getCurrentTime(),"1.0");
    }
}
