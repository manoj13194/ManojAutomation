package demo.acn.internal.utilities;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class GenericKeywords {
	public WebDriver driver;
	public Properties prop = null;
	public ExtentTest extentTest = null;
	DesiredCapabilities cap = null;
	String mainWinID = null;
	public String userName = null;
	public int endResult = 0;

	@SuppressWarnings("deprecation")
	public WebDriver openbrowser(String browserkey) {
		reportInfo("Opening the browser: " + browserkey);
//		System.out.println("Browser: " + browserkey);
		File fl = null;
		if (browserkey == null) {
			reportError("Browser is blank");
			
		} else if (browserkey.equalsIgnoreCase("mozilla")) {
			ProfilesIni allprof = new ProfilesIni();
			FirefoxProfile profile = allprof.getProfile("default");
			profile.setAcceptUntrustedCertificates(true);
			profile.setAssumeUntrustedCertificateIssuer(true);
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "//geckodriver.exe");
			driver = new FirefoxDriver();
		} else if (browserkey.equalsIgnoreCase("chrome")) {
			fl = new File(System.getProperty("user.dir") + "//chromedriver.exe");
			if (!fl.exists()) {
				reportError("Chrome driver executable file does not exist in the project path");
			}
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")
					+ "//chromedriver.exe");
		
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--start-maximized");

			HashMap<String, Object> prefs = new HashMap<String, Object>();
			prefs.put("credentials_enable_service", false);
			prefs.put("profile.password_manager_enabled", false);
			options.setExperimentalOption("prefs", prefs);
			cap = null;
			cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(cap);

		} else if (browserkey.equalsIgnoreCase("ie")) {
			fl = new File(System.getProperty("user.dir") + "\\IEDriverServer.exe");
			if (!fl.exists()) {
				reportError("IE driver executable file does not exist in the project path");
//				Assert.fail("IE driver executable file does not exist in the project path");
			}
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "\\IEDriverServer.exe");
			cap = DesiredCapabilities.internetExplorer();
			cap.setCapability("ignoreZoomSetting", true);
			cap.setJavascriptEnabled(true);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			driver = new InternetExplorerDriver(cap);
		}

		if (driver == null) {
			reportError("Specified browser is not configured in the test script");
//			Assert.fail("Specified browser is not configured in test");
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
		
	}

	public void openurl(String urlkey) {
		reportInfo("Opening the URL " + prop.getProperty(urlkey));
		driver.navigate().to(prop.getProperty(urlkey));
		waitTillPageLoaded();
		reportInfo("Opened URL " + prop.getProperty(urlkey));

	}

	public void clickelement(String locatorkey) {
		reportInfo("Clicking on element located by " + prop.getProperty(locatorkey));

		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.visibilityOf(getElement(locatorkey)));
			getElement(locatorkey).click();
			
			reportInfo("Clicked on element: " + prop.getProperty(locatorkey));
		} catch (Exception e ) {
			reportFailure("Could not click on element: " + prop.getProperty(locatorkey));
			
		}
		waitTillPageLoaded();
	}


	public void clearcontent(String locatorkey) {
		WebElement e = getElement(locatorkey);
		e.clear();

	}

	public void input(String locatorkey, String value) {
		reportInfo("Entering the required data in element: " + prop.getProperty(locatorkey));
		WebElement e = getElement(locatorkey);

		try {
			e.clear();
			e.sendKeys(value);				

		} catch (Exception e1) {
			e1.printStackTrace();
			reportFailure("Failed to input the data in the element located by " + prop.getProperty(locatorkey));
		}
		
		capturescreenshot();
		reportInfo("Entered the data in element located by " + prop.getProperty(locatorkey));
	}

	public void quitsession() {
		if (driver != null) {
			reportInfo("Closing the session");
			driver.quit();

		} 

	}


	public boolean isElementPresent(String locatorkey) {
		List<WebElement> ele = null;
		if (locatorkey.endsWith("_xpath"))
			ele = driver.findElements(By.xpath(prop.getProperty(locatorkey)));
		else if (locatorkey.endsWith("_id"))
			ele = driver.findElements(By.id(prop.getProperty(locatorkey)));
			
		if (ele.size() == 0){
			reportInfo("Element is not present");
			
			return false;
		} else {
			reportInfo("Element is present");
			return true;
		}
			
	}


	public boolean isElementPresent(WebElement WebEle, String locatorKey) {
		List<WebElement> ele = null;
		if (locatorKey.endsWith("_xpath"))
			ele = WebEle.findElements(By.xpath(prop.getProperty(locatorKey)));
		
		if (ele.size() == 0){
			System.out.println("Element is not present");
			return false;
		} else {
			System.out.println("Element is present");
			return true;
		}
	}
	
	
	public WebElement getElement(String locatorkey) {
		WebElement ele = null;
		try {
			if (locatorkey.endsWith("_xpath")) 
				ele = driver.findElement(By.xpath(prop.getProperty(locatorkey)));
			else if (locatorkey.endsWith("_id"))
				ele = driver.findElement(By.id(prop.getProperty(locatorkey)));
			
		} catch (Exception e) {
			reportFailure("Error in locating the Webelement " + locatorkey);
			
		}

		return ele;
	}

	public void capturescreenshot() {
		// fileName of the screenshot
		Date d = new Date();
		String screenshotFilename = d.toString().replace(":", "_").replace(" ", "_") + ".png";

		// store screenshot in that file
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String savefileloc = System.getProperty("user.dir") + "//screenshots//" + screenshotFilename;
		try {
			FileUtils.copyFile(scrFile, new File(savefileloc));
		} catch (Exception e) {

			e.printStackTrace();
		}
		// put screenshot file in reports
		reportInfo("Screenshot-> " + extentTest.addScreenCapture(savefileloc));

	}
	
	public void waitTillPageLoaded(){	
		// Check if page is fully loaded
//		System.out.println("Page is loading...");
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		String status = "in-progress";
		while (!status.equals("complete")) {
			try {
			Thread.sleep(500);
			
			} catch (InterruptedException e) {
			e.printStackTrace();
			}
			status = (String) jse.executeScript ("return document.readyState");
			if (status.equalsIgnoreCase("complete"))
				break;
		}
//		System.out.println("Page load status is: " + status);
	}

/*	public void FinalTestResult(){
		if (endResult == 1) {
			reportPass("Test case is PASSED");
		} else if (endResult == -1) {
			reportError("ERROR in Test execution. Please re-run the test.");
		} else if (endResult == 0) {
			reportFailure("Test case is FAILED");
		}
	}*/
	
	
	public void FinalTestResult(){
		if (endResult == 1) {
			reportPass("Test case is PASSED");
		} else if (endResult == -1) {
			extentTest.log(LogStatus.ERROR, "ERROR in Test execution. Please re-run the test.");
		} else if (endResult == 0) {
			extentTest.log(LogStatus.FAIL, "Test case is FAILED");
		}
	}
	
	////////////////// Execution reporting methods ///////////////
	// report failure and abort the test execution
	public void reportFailure(String failureMessage) {
		extentTest.log(LogStatus.FAIL, failureMessage);
		if (driver != null)
			capturescreenshot();
		Assert.fail(failureMessage);
	}
	
	// report information about the progress of the test execution
	public void reportInfo(String infoMessage) {
		extentTest.log(LogStatus.INFO, infoMessage);
	}

	// report automation setup error
	public void reportError(String errorMessage) {
		extentTest.log(LogStatus.ERROR, errorMessage);
		if (driver != null)
			capturescreenshot();
		throw new Error(errorMessage);
	}

	// report success and capture screenshot
	public void reportPass(String passMessage) {
		extentTest.log(LogStatus.PASS, passMessage);
		if (driver != null)
			capturescreenshot();
	}

	// report warnings and continue test execution
	public void reportWarning(String infoMessage) {
		extentTest.log(LogStatus.WARNING, infoMessage);
		if (driver != null)
			capturescreenshot();
	}
	
}
