package demo.acn.internal.tests;

import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import demo.acn.internal.reports.ExtentManager;
import demo.acn.internal.utilities.DataUtility;
import demo.acn.internal.utilities.Keywords;
import demo.acn.internal.utilities.XlsReader;


public class Sample_Test_3 {

	ExtentReports extRep = null;
	ExtentTest extTest = null;
	Keywords testKeywords;
	String testNameVARIABLE = "Sample_Test_3";
	String pathVARIABLE = System.getProperty("user.dir") + "//Data//Test_Data_project.xlsx";
	XlsReader xlsx = new XlsReader(pathVARIABLE);
	DataUtility dataUtil = new DataUtility();
	int itr = 1;

	
	@BeforeMethod
	public void startTest() {
		testKeywords = null;
		extRep = ExtentManager.getInstance();
		extTest = extRep.startTest(testNameVARIABLE + " - iteration " + String.valueOf(itr));
		itr ++;
	}

	@Test (dataProvider="getData")
	public void LoginTest(Hashtable <String,String> hashtable) {
		//Skipping the test case with Runmode as 'N'
		dataUtil.TestCaseIsRun(testNameVARIABLE, xlsx, extTest);
		//Skipping the test RUN with Runmode as 'N'
		if (hashtable.get("Runmode").equalsIgnoreCase("N")){
			extTest.log(LogStatus.SKIP, "Testrun is skipped as Runmode is set to 'N'");			
			throw new SkipException("Skipping the testrun as Runmode is set to 'N'");
		}
	
		extTest.log(LogStatus.INFO, "Test execution started with the data as: " + hashtable.toString());
		testKeywords = new Keywords(xlsx, extTest);
		testKeywords.executeKeywords(testNameVARIABLE, hashtable); 
		///
	}
	
	@DataProvider
	public Object[][] getData(){
		return dataUtil.data_reader(testNameVARIABLE, xlsx);
	}

	@AfterMethod
	public void endTest() {
		if (testKeywords != null) {
		try {
			testKeywords.FinalTestResult();
			testKeywords.quitsession();
		} catch (Exception e) {
			e.getMessage();
		} finally {
			extRep.endTest(extTest);
			extRep.flush();	
		}
		} else {
			extRep.endTest(extTest);
			extRep.flush();
		}
	}
	
}

