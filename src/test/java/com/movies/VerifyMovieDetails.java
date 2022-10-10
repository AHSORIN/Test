package com.movies;

import java.util.Hashtable;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import com.movies.base.BaseTest;
import com.util.Constants;
import com.util.DataUtil;
import com.util.Xls_Reader;

public class VerifyMovieDetails extends BaseTest {

	IMDBWikiKeywords imdbWikiKey;

	@BeforeTest
	public void init() {
		System.out.println("init method");
		xls = new Xls_Reader(Constants.IMDBWiki_XLS);
		System.out.println("end xls");
		testName = "VerifyMovieDetails";
	}

	@Test(dataProvider = "getData")
	public void verifyMovieDetails(Hashtable<String, String> data) throws Exception {
		System.out.println("method");
		test = rep.startTest(testName);
		test.log(LogStatus.INFO, "Verifying movie details from Wikipidea and IMDB websites");
		if (DataUtil.isSkip(xls, testName) || data.get("Runmode").equals("N")) {
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		imdbWikiKey = new IMDBWikiKeywords(test, testName);
		imdbWikiKey.executeKeywords(testName, xls, data);
	}
}
