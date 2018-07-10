package demo.acn.internal.utilities;

import java.util.Hashtable;

import org.testng.SkipException;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class DataUtility {



	public Object[][] data_reader(String testcasename, XlsReader xlsx) {
		TestCaseNameCheck(xlsx, testcasename, "Testcases");
		TestCaseNameCheck(xlsx, testcasename, "Keywords");
		TestCaseNameCheck(xlsx, testcasename, "Data");

		String sheetname = "Data";
		int Testnamerow = 1;
		// Reading the Test details start row
		while (!xlsx.getCellData(sheetname, 0, Testnamerow).equalsIgnoreCase(testcasename)) {
			Testnamerow++;
		}

		int Testdatacolnamerow = Testnamerow + 1;
		int Testdatastartrow = Testnamerow + 2;
		int testdatarows = 0;

		// Reading the number of test data rows
		while (!xlsx.getCellData(sheetname, 0, Testdatastartrow + testdatarows).equals("")) {
			testdatarows++;
		}

		// Reading the number of data columns
		int testdatacols = 0;
		while (!xlsx.getCellData(sheetname, testdatacols, Testdatacolnamerow).equals("")) {
			testdatacols++;
		}

		// reading the data from the required cells
		Object[][] table = new Object[testdatarows][1];
		for (int rnum = 0; rnum < testdatarows; rnum++) {
			Hashtable<String, String> datatable = null;
			datatable = new Hashtable<String, String>();
			for (int cnum = 0; cnum < testdatacols; cnum++) {
				String key = xlsx.getCellData(sheetname, cnum, Testdatacolnamerow);
				String value = xlsx.getCellData(sheetname, cnum, Testdatastartrow + rnum);
				if (value == null)
					value = "";
				datatable.put(key, value);
			}
			table[rnum][0] = datatable;
		}
		return table;
	}

	public void TestCaseIsRun(String testcasename, XlsReader xlsx, ExtentTest exTest) {
		String Sheetname = "Testcases";

		// get the row number of the Test case
		int rowNum = 1;
		while (!xlsx.getCellData(Sheetname, 0, rowNum).equalsIgnoreCase(testcasename)) {
			rowNum++;
		}
		String flag = xlsx.getCellData(Sheetname, "Runmode", rowNum);
		if (!flag.equalsIgnoreCase("Y")) {
			exTest.log(LogStatus.SKIP, "Testcase is skipped as Runmode is set to 'N'");
			throw new SkipException("Skipping the Testcase as Runmode is set to 'N'");
		}
	}

	public void TestCaseNameCheck(XlsReader xlsx, String testname, String sheetname) {
		int rows = xlsx.getRowCount(sheetname);
		int Testnamerow = 1;
		while (!xlsx.getCellData(sheetname, 0, Testnamerow).equalsIgnoreCase(testname)) {
			Testnamerow++;
			if (Testnamerow > rows) {
				throw new Error("Testcase name " + testname + " does not exist in the " + sheetname + " sheet");
			}
		}
	}
}
