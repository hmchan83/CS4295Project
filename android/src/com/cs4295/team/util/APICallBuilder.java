package com.cs4295.team.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;


public class APICallBuilder {
	
	private String server;
	private String GETpara;
	private String POSTpara;

	public APICallBuilder(String server){
		this.server = server;		
	}

	public String getGETpara() {
		return GETpara;
	}

	public void setGETpara(String gETpara) {
		GETpara = gETpara;
	}

	public String getPOSTpara() {
		return POSTpara;
	}

	public void setPOSTpara(String pOSTpara) {
		POSTpara = pOSTpara;
	}
	
	public String getResponse(){
		String HTML = "";
		String surl = server+"?"+GETpara;
		HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(surl); // URL!	         
		try {
			StringEntity entity = new StringEntity(this.POSTpara, "UTF_8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost, localContext);
			String result = "";			
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				result += line;
				HTML = result;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
		return HTML;
	}
}
