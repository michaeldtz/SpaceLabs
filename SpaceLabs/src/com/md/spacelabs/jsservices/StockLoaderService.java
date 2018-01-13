package com.md.spacelabs.jsservices;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.mozilla.javascript.ScriptableObject;

import com.google.appengine.labs.repackaged.org.json.JSONObject;



public class StockLoaderService extends ScriptableObject {

	public StockLoaderService() {
		String[] functionNames = { "loadStocks" };
		defineFunctionProperties(functionNames, StockLoaderService.class, ScriptableObject.DONTENUM);
	}

	public String loadStocks(String symbol, String from, String to) throws Exception{

		String urlString = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20%3D%20%22"
				+ symbol
				+ "%22%20and%20startDate%20%3D%20%22"
				+ from
				+ "%22%20and%20endDate%20%3D%20%22"
				+ to + "%22&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

		URL url = new URL(urlString);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String line = "";
		String content = "";
		while ((line = br.readLine()) != null)
			content += line;

		JSONObject json = new JSONObject(content);
		System.out.println(content);
		
		return content;
		
	}

//	public static void main(String[] args) throws Exception{
//		new StockLoaderService().loadStocks("YHOO", "2014-01-01", "2014-05-14");
//	}

	@Override
	public String getClassName() {
		return "StockLoaderService";
	}
}
