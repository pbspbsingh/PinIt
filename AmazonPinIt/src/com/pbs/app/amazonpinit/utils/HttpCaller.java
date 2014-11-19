package com.pbs.app.amazonpinit.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpCaller {

	private final String endPoint;
	private final List<NameValuePair> nvps = new ArrayList<NameValuePair>();

	public HttpCaller(String endPoint) {
		this.endPoint = endPoint;
	}

	public void addParam(String key, String value) {
		nvps.add(new BasicNameValuePair(key, value));
	}

	public String getResponse() throws IOException {
		final HttpClient client = new DefaultHttpClient();
		final HttpPost post = new HttpPost(Constants.SERVER + endPoint);
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps));
			final HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200)
				return EntityUtils.toString(response.getEntity());
			else
				throw new IOException("Server side exception has occured.");
		} catch (Exception e) {
			throw new IOException("Network call failed", e);
		}
	}
}
