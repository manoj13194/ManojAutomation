package demo.acn.internal.reports;

import java.io.File;
import java.util.Date;
import com.relevantcodes.extentreports.DisplayOrder;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtentManager {
	private static ExtentReports extent;
	static String VARIABLE_Reportpath = System.getProperty("user.dir") + "//TestNG_Reports//";
	
	public static ExtentReports getInstance() {
		if (extent == null) {
			Date d = new Date();
			String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
			extent = new ExtentReports(VARIABLE_Reportpath + fileName, true, DisplayOrder.NEWEST_FIRST);
			extent.loadConfig(new File(System.getProperty("user.dir") + "//ReportsConfig.xml")); 

			// optional
			extent.addSystemInfo("Selenium Version", "3.8.1").addSystemInfo("Environment", "Test");
		}
		return extent;
	}
}
