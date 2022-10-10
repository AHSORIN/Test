package com.movies.base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.movies.Keywords;
import com.movies.mail.ZipExecutionReport;
import com.util.DataUtil;
import com.util.ExtentManager;
import com.util.Xls_Reader;

public class BaseTest {
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest test;
	public Keywords app;
	public Xls_Reader xls;
	public String testName;
	public static String storedKeyword;
	public static WebDriver driver;

	@AfterMethod
	public void quit() throws Exception {

		if (rep != null) {
			rep.endTest(test);
			rep.flush();
		}
		// quit
		if (app != null) {
			if (app.getGenericKeyWords().getStoredkeyword() != null) {
				storedKeyword = app.getGenericKeyWords().getStoredkeyword();
				driver = app.getGenericKeyWords().driver;
			}
		}
	}

	@AfterSuite
	public void afterSuite(ITestContext context) throws Exception {
		System.out.println("After Suite"); // Saving Execution report in remote machine
		if (System.getProperty("os.name").toLowerCase().contains("windows server")) {
			ZipExecutionReport zip = new ZipExecutionReport();
			String suiteName = context.getCurrentXmlTest().getSuite().getFileName();
			zip.zipReports(suiteName);
		}
	}

	@DataProvider
	public Object[][] getData() {
		return DataUtil.getData(xls, testName);
	}

	public static String getStoredkeyword() {
		return storedKeyword;
	}

	public static WebDriver getWebDriver() {
		return driver;
	}
}
