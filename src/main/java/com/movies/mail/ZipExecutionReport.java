package com.movies.mail;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.testng.Assert;
import org.zeroturnaround.zip.ZipUtil;
import com.util.Constants;

public class ZipExecutionReport {

	public void zipReports(String suite) {
		try {
			String buildParameter = System.getProperty("suiteName");
			String source = Constants.REPORT_PATH;
			String destination = null;
			if (buildParameter != null) {
				if (buildParameter.contains("testng.xml")) {// For Complete Suite (testng.xml)
					if (suite.contains("SmartNotifications")) { // To be done at last when executing (SmartNotifications.xml)
						destination = "C:\\Compressed reports\\" + getZipFileName(buildParameter);
					}
				} else {
					destination = "C:\\Compressed reports\\" + getZipFileName(suite); // For other test suites.
				}

				if (destination != null) {
					ZipUtil.pack(new File(source), new File(destination));
					System.out.println("Compressed Archive ==>" + destination);
					cleanReportFolder();
				}
			}
		} catch (Exception e) {
			System.err.println("Issue in compressing execution report in zip");
			e.printStackTrace();
		}
	}

	public void cleanReportFolder() {
		try {
			boolean deleted = false;
			deleted = deleteAllFilesWithSpecificExtension(Constants.SCREENSHOT_PATH, "png");
			deleted = deleteAllFilesWithSpecificExtension(Constants.REPORT_PATH, "html");
			Assert.assertTrue(deleted, "Not able to delete report files");
			System.out.println("Report files deleted from " + Constants.REPORT_PATH);
		} catch (Exception e) {
			System.err.println("Issue in deleting report folder");
			e.printStackTrace();
		}
	}

	public boolean deleteAllFilesWithSpecificExtension(String pathToDir, String extension) {
		boolean success = false;
		File folder = new File(pathToDir);
		File[] fList = folder.listFiles();
		for (File file : fList) {
			String pes = file.getName();
			if (pes.endsWith("." + extension)) {
				success = (new File(String.valueOf(file)).delete());
			}
		}
		return success;
	}

	public String getZipFileName(String suiteClassPath) {
		try {
			String zipNameVar = "%%%Date%%% v.%%%Version%%% (%%%SuiteName%%%).zip";
			String suiteName = "";

			if (suiteClassPath.contains("SmokeTestSuite.xml")) {
				suiteName = "Smoke Suite";
			} else if (suiteClassPath.contains("testng.xml")) {
				suiteName = "Complete Suite";
			} else {
				String[] arr = suiteClassPath.split("\\\\");
				suiteName = arr[arr.length - 1].replace(".xml", "") + " Suite";
			}

			String finalStr = zipNameVar.replace("%%%Date%%%", getCurrentDate())
					.replace("%%%Version%%%",
							getPropertyValue(Constants.Simpplr_Data_Properties, "Simpplr_InstalledPackageId"))
					.replace("%%%SuiteName%%%", suiteName);
			return finalStr;
		} catch (Exception e) {
			System.err.println("Issue getting zip file name - " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public String getPropertyValue(String propertyFileName, String Key) {
		try {
			Properties prop = new Properties();
			FileInputStream fs = new FileInputStream(propertyFileName);
			prop.load(fs);
			return prop.getProperty(Key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getCurrentDate() {
		try {
			Date d = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			String date = formatter.format(d).toString();
			return date;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
