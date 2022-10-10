package com.movies;

import com.relevantcodes.extentreports.ExtentTest;
import com.movies.GenericKeywords;

public class AppKeywords extends GenericKeywords {

	public AppKeywords(ExtentTest test, String testCase) {
		super(test, testCase);
	}

	public void launchIMDB() {
		try {
			navigate("url_imdb");
		} catch (Throwable e) {
			reportFailure("Issue in launching IMDB URL", e);
		}
	}

	public void launchWiki() {
		try {
			navigate("url_wiki");
		} catch (Throwable e) {
			reportFailure("Issue in launching Wikipidea URL", e);
		}
	}
}
