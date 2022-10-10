package com.movies;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.movies.AppKeywords;
import com.util.Constants;
import com.util.Xls_Reader;


public class IMDBWikiKeywords extends AppKeywords {
	AppKeywords app;
	IMDBWikiKeywords imdbWikiKey;
	SoftAssert softassert;

	public IMDBWikiKeywords(ExtentTest test, String testCase) {
		super(test, testCase);
		// TODO Auto-generated constructor stub
	}

	public void executeKeywords(String testUnderExecution, Xls_Reader xls, Hashtable<String, String> testData) {
		test.log(LogStatus.INFO, "Executing keywords from IMDBWiki class.");
		imdbWikiKey = new IMDBWikiKeywords(test, testUnderExecution);

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

				imdbWikiKey.setTestData(testData);
				imdbWikiKey.setData(data);
				imdbWikiKey.setKey(key);
				imdbWikiKey.setObject(object);
				imdbWikiKey.setProceedOnFail(proceedOnFail);
				imdbWikiKey.setkeyword(keyword);

				try {

					Method method;
					method = imdbWikiKey.getClass().getMethod(keyword);
					method.invoke(imdbWikiKey);

				} catch (Exception e) {
					System.out.println("Method not found = " + keyword);
					test.log(LogStatus.FAIL, "Method not found = " + keyword);
					e.printStackTrace();
					test.log(LogStatus.ERROR, ExceptionUtils.getStackTrace(e));

				}

			}
		}
		System.out.println("Closing Browser!");
		imdbWikiKey.closeBrowser();
	}

	public void compareMovieDetails() {
		try {
			test.log(LogStatus.INFO, "Comparing movie details from IMDB and Wikipidea websites for the movie: "+testData.get("MovieName"));
			String movieName = testData.get("MovieName");
			String[] imdb = getMovieDetailsIMDB(movieName);
			for (int i = 0; i < imdb.length; i++) {
				System.out.println(imdb[i]);
			}
			String imdbDateCorrected = returnCorrectDateFormat(imdb[0]);			
			String[] wiki = getMovieDetailsWiki(movieName);
			for (int i = 0; i < wiki.length; i++) {
				System.out.println(wiki[i]);
			}

			assertEquals(imdbDateCorrected, wiki[0],
					"Date for the movie " + movieName + " is mismatching in Wikipidea and IMDB websites.");
			assertEquals(imdb[1], wiki[1],
					"Country for the movie " + movieName + " is mismatching in Wikipidea and IMDB websites.");
			test.log(LogStatus.PASS, "Compared: Movie details from IMDB and Wikipidea websites for the movie: "+testData.get("MovieName"));
		} catch (Throwable e) {
			reportFailure("Issue in comparing movie Details from IMDB and Wikipidea websites for the movie: "+testData.get("MovieName"), e);
		}
	}

	public String[] getMovieDetailsIMDB(String movieName) {
		try {
			test.log(LogStatus.INFO, "Fetching movie details from IMDB website for the movie "+movieName);
			String[] movieDetails = new String[2];
			launchIMDB();
			waitForPageLoad();
			clickAndInput("search_imdb", movieName);
			wait("searchResult_imdb");
			click("searchResult_imdb");
			waitForPageLoad();
			scrollToMid("releaseDate_imdb");
			movieDetails[0] = getElement("releaseDate_imdb").getText();
			movieDetails[1] = getElement("country_imdb").getText();
			test.log(LogStatus.PASS, "Fetched: Movie details from IMDB website for the movie "+movieName);
			return movieDetails;
		} catch (Throwable e) {
			reportFailure("Issue in getting Movie details of" + movieName + " from IMDB", e);
			return null;
		}
	}

	public String[] getMovieDetailsWiki(String movieName) {
		try {
			test.log(LogStatus.INFO, "Fetching movie details from Wikipidea website for the movie "+movieName);
			String[] movieDetails = new String[2];
			launchWiki();
			waitForPageLoad();
			clickAndInput("search_wiki", movieName);
			wait("searchResult_wiki");
			click("searchResult_wiki");
			waitForPageLoad();
			scrollToMid("releaseDate_wiki");
			movieDetails[0] = getElement("releaseDate_wiki").getText();
			movieDetails[1] = getElement("country_wiki").getText();
			test.log(LogStatus.INFO, "Fetched: Movie details from Wikipidea website for the movie "+movieName);
			return movieDetails;
		} catch (Throwable e) {
			reportFailure("Issue in getting Movie details of" + movieName + " from Wikipedia", e);
			return null;
		}
	}
}
