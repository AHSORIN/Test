package com.util;

public class Constants {

	// ---------------------------- Properties file location
	// ---------------------------
	public static final String Project_Properties = System.getProperty("user.dir")
			+ "//src//test//resources//objectRepository//project.properties";
	public static final String Simpplr_Data_Properties = System.getProperty("user.dir")
			+ "//src//test//resources//objectRepository//simpplr_data.properties";

	// ---------------------------- Drivers Path ---------------------------

	public static final String geckodriver_Window = System.getProperty("user.dir") + "//drivers//geckodriver.exe";
	public static final String geckodriver = System.getProperty("user.dir") + "//drivers//geckodriver";

	public static final String chromedriver_Window = System.getProperty("user.dir") + "//drivers//chromedriver.exe";
	public static final String chromedriver = System.getProperty("user.dir") + "//drivers//chromedriver";

	// ---------------- Data Sheets------------------------
	public static final String IMDBWiki_XLS = System.getProperty("user.dir") + "//data//IMDBWiki.xlsx";

	// ---------------------- Reserved Keywords -------------------------------
	public static final String KEYWORDS_SHEET = "Keywords";
	public static final String PROCEED_COL = "ProceedOnFail";
	public static final String TCID_COL = "TCID";
	public static final String KEYWORD_COL = "Keyword";
	public static final String OBJECT_COL = "Object";
	public static final String DATA_COL = "Data";
	public static final String TESTCASES_SHEET = "TestCases";
	public static final String RUNMODE_COL = "Runmode";
	public static final String PASS = "PASS";
	public static final String FAIL = "FAIL";
	public static final String[] FAILED = {};

	// ----------------- AutoIT/Report file locations -----------------
	public static final String DOWNLOAD_FOLDER_PATH = System.getProperty("user.dir") + "//Downloads//";
	public static final String SCREENSHOT_PATH = System.getProperty("user.dir") + "\\reports\\screenshots\\";
	public static final String SCREENSHOT_PATH_1 = ".//reports//screenshots//";
	public static final String SCREENSHOT_PATH_2 = ".//screenshots//";
	public static final String REPORT_PATH_1 = ".//reports//";
	public static final String REPORT_PATH = System.getProperty("user.dir") + "\\reports\\";

}
