package demo.acn.internal.utilities;

import java.util.Hashtable;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Keywords {
	ExtentTest exText = null;
	AppKeywords appKey;
	XlsReader xlsx = null;
	List <WebElement> list = null;
	public Keywords(XlsReader x, ExtentTest t) {
		exText = t;
		xlsx = x;
	}

	@Test
	public void executeKeywords(String TestName, Hashtable <String,String> Htable) {
		// Create an object of App Keyword class
		exText.log(LogStatus.INFO, "Executing keywords in the Test - " + TestName);
		appKey = new AppKeywords(exText);

		// Reading an excel file in the project
		String keywordsSheet="Keywords";
		int rownum = xlsx.getRowCount(keywordsSheet); // index of the last row
		int Testnamerow = 1;
		// Reading the Test details start row >>>> code moved to Data utility class
		while(!xlsx.getCellData(keywordsSheet, 0, Testnamerow).equalsIgnoreCase(TestName)){
			Testnamerow ++;
			
		}
		
		// starting for loop for the excel file
		for(int rNum=Testnamerow;rNum<rownum + Testnamerow;rNum++){
			String tcid = xlsx.getCellData(keywordsSheet, "TCID", rNum);
			if(tcid.equalsIgnoreCase(TestName)){
				String keyword = xlsx.getCellData(keywordsSheet, "Keyword", rNum);
				String object = xlsx.getCellData(keywordsSheet, "Object", rNum);
				String data = xlsx.getCellData(keywordsSheet, "Data", rNum);
				appKey.reportInfo("Executing test step: " + TestName +" --> "+ tcid + " -- " + keyword 
							+ " -- " + object + " -- " + data);

				if (keyword.equals("openbrowser"))
					appKey.openbrowser(Htable.get(data));
				else if (keyword.equals("openurl"))
					appKey.openurl(object);
				else if (keyword.equals("clickelement"))
					appKey.clickelement(object);
				else if (keyword.equals("clearcontent"))
					appKey.clearcontent(object);
				else if (keyword.equals("input"))
					appKey.input(object, Htable.get(data));
				else if (keyword.equals("verifylogin"))
					appKey.verifylogin(TestName, object, Htable.get(data));
	
				
				else {
					appKey.reportError("Keyword is not present in the script");	
					
				}
			}
			
		}
				
	}

	public void quitsession() {
		appKey.quitsession();
	}
	
	public void FinalTestResult(){
		appKey.FinalTestResult();
		
	}
}
