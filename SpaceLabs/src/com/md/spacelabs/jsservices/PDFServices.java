package com.md.spacelabs.jsservices;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.Splitter;
import org.datanucleus.util.Base64;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.annotations.JSFunction;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;


@SuppressWarnings("serial")
public class PDFServices extends ScriptableObject {

	@SuppressWarnings("unused")
	private ServletContext servletContext;
	private HashMap<Integer, PDDocument> pdfCache = new HashMap<Integer, PDDocument>();
	private int handleCnt = 10002;

	public PDFServices(ServletContext servletContext) {
		this.servletContext = servletContext;

		String[] functionNames = { "loadPDFFromURL", "splitPDF", "pagesOfPDF" };
		defineFunctionProperties(functionNames, PDFServices.class, ScriptableObject.DONTENUM);
	}

	@JSFunction
	public int loadPDFFromURL(String urlString, String userpass) throws JSONException, IOException {

		URL url = new URL(urlString);
		URLConnection con = url.openConnection();

		con.setConnectTimeout(25000);
		con.setReadTimeout(25000);
		con.setDoOutput(false);

		if (userpass.contains(":")) {
			String encoding = new String(Base64.encode(userpass.getBytes()));
			con.setRequestProperty("Authorization", "Basic " + encoding);
		}

		con.connect();
		InputStream is = con.getInputStream();

		PDDocument pdf = PDDocument.load(is);
		pdfCache.put(++handleCnt, pdf);

		is.close();

		return handleCnt;
	}

	@JSFunction
	public int pagesOfPDF(int handle) throws JSONException, IOException {

		PDDocument pdf = pdfCache.get(handle);
		return pdf.getNumberOfPages();

	}

	@JSFunction
	public JSONArray splitPDF(int handle) throws JSONException, IOException {
		
		PDDocument pdf = pdfCache.get(handle);
		Splitter splitter = new Splitter();
		
		List<PDDocument> splits = splitter.split(pdf);
		JSONArray handles = new JSONArray();
		
		for (int i = 0; i < splits.size(); i++) {
			PDDocument pdfPage = splits.get(i);
			pdfCache.put(++handleCnt, pdfPage);
			handles.put(handleCnt);
		}
		
		return handles;
	}

	@Override
	public String getClassName() {
		return "PDFServices";
	}

}
