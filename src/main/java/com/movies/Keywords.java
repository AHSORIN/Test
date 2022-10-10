package com.movies;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.util.Constants;
import com.util.Xls_Reader;

import java.lang.reflect.Method;
import java.util.Hashtable;

public class Keywords {

	ExtentTest test;
	AppKeywords app;

	public Keywords(ExtentTest test) {
		this.test = test;
	}

	public void executeKeywords(String testUnderExecution, Xls_Reader xls, Hashtable<String, String> testData) {
		app = new AppKeywords(test, testUnderExecution);

		int rows = xls.getCellRowLastNum(Constants.KEYWORDS_SHEET, Constants.TCID_COL, testUnderExecution);
		int rowNum = xls.getCellRowNum(Constants.KEYWORDS_SHEET, Constants.TCID_COL, testUnderExecution);
		System.out.println("*****************************************");
		System.out.println("Running Test Case : " + testUnderExecution);
		System.out.println("Test Steps starts from Row # " + rowNum);
		System.out.println("Total Test Steps : " + (rows - rowNum));
		System.out.println("*****************************************");
		for (int rNum = rowNum; rNum <= rows; rNum++) {
			String tcid = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.TCID_COL, rNum);
			if (tcid.equals(testUnderExecution)) {
				String keyword = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.KEYWORD_COL, rNum);
				String object = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.OBJECT_COL, rNum);
				String key = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_COL, rNum);
				String data = testData.get(key);
				String proceedOnFail = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.PROCEED_COL, rNum);

				app.setTestData(testData);
				app.setData(data);
				app.setKey(key);
				app.setObject(object);
				app.setProceedOnFail(proceedOnFail);
				app.setkeyword(keyword);

				try {
					if (app.openBrowser == false & !keyword.contains("openBrowser"))
						break;

					Method method;
					method = app.getClass().getMethod(keyword);
					method.invoke(app);
				} catch (Exception e) {
					System.out.println("Issue with Keyword : " + keyword);
					test.log(LogStatus.FAIL, "Issue with Keyword : " + keyword);
					e.printStackTrace();
					break;
				}

			}
		}
		app.closeBrowser();
	}

	public AppKeywords getGenericKeyWords() {
		return app;
	}

}
