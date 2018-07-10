package demo.acn.internal.utilities;

import java.io.FileInputStream;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.relevantcodes.extentreports.ExtentTest;


public class AppKeywords extends GenericKeywords{

	int num = 0;
// 	Initialize property file object in the constructor of this class
	public AppKeywords(ExtentTest t){
		extentTest = t;
		prop = new Properties();
		reportInfo("Reading data from property file");
		String path = System.getProperty("user.dir")+"//src//test//resources//"
				+ "project.properties";
		
		FileInputStream fis;
		try {
				fis = new FileInputStream(path);
				prop.load(fis);
				reportInfo("Read data from property file");
			} catch (Exception e) {
				e.printStackTrace();
				reportError("Error while reading the Properties file");
			}
	
	}

	public void verifylogin(String testname, String locatorkey, String expectedResult) {
// verify login
		reportInfo("Verifying login status");
		boolean loggedIn = isElementPresent(locatorkey);
		String actualResult;
		if (loggedIn) {
			actualResult = "Success";
			mainWinID = driver.getWindowHandle();
		}
		else 
			actualResult = "Failure";
		
		if (actualResult.equalsIgnoreCase(expectedResult)){
			reportInfo("Login functionality is working as expected. Login " + actualResult);
			if (testname.startsWith("Sample_Test_"))
				endResult = 1;
			reportPass("Login functionality is working as expected");
			} else {
				capturescreenshot();
				if (testname.startsWith("Sample_Test_"))
					endResult = 0;
				reportFailure("Login functionality failing. Expected: Login " + expectedResult
							+ " but Actual: Login " + actualResult);
			}
		
		/////////
/*		
		if (actResult.equalsIgnoreCase(expResult)) {
			reportInfo("Login functionality is working as expected. Login " + actResult);
			if (testName.equalsIgnoreCase("Login_Test"))
				EndResult = 1;
		} else {
			reportInfo("Login functionality is NOT working as expected. Login " + actResult + " BUT expected is "
					+ expResult);
			if (testName.equalsIgnoreCase("Login_Test"))
				EndResult = 0;
			reportFailure("Login functionality is NOT working as expected");
		}
		
		*/
		
		///////
	
	}

	public void waitforelement(String locatorkey) {
		reportInfo("Waiting for the element: " + prop.getProperty(locatorkey));
		WebElement element = getElement(locatorkey);
		if (element != null)
			reportInfo("Waited till the element: " + prop.getProperty(locatorkey) 
					+ " is visible");
	}



	public void closePopupIfPresent(String locatorkey) {
		reportInfo("Waiting for the popup");
		WebDriverWait wait =  new WebDriverWait(driver, 5);
		try {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath
						(prop.getProperty(locatorkey))));
			reportInfo("Closing the popup");
			driver.findElement(By.xpath(prop.getProperty(locatorkey))).click();
			reportInfo("Closed the popup");
			driver.switchTo().defaultContent();
			driver.switchTo().activeElement();
		
		} catch (Exception ex) {
			reportInfo("Popup is not Displayed");
			
		}

	}


	
	public WebElement findFolderAsset() {
		
		WebElement ele = getElement("Subfolder_selected_xpath");
		
		return ele;
	}


	public void selectCheckBoxOption(String locatorKey, String optionKey) {
		if (optionKey.equalsIgnoreCase("Yes")) {
			getElement(locatorKey).click();		
			capturescreenshot();		
		}
					
	}


}
