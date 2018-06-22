package xysoft.im.utils;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonUtil {

	public static JSONArray readJsonsFromUrl(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String resultBody = EntityUtils.toString(entity,"utf-8");
				JSONArray jsons = new JSONArray(resultBody);
				return jsons;
			}
		} catch (IOException e) {
			return null;
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (IOException e) {
				
			}			
		}
		return null;
	}
	
	public static JSONObject readJSONObjectFromUrl(String url) {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String resultBody = EntityUtils.toString(entity,"utf-8");
				JSONObject json = new JSONObject(resultBody);
				return json;
			}
		} catch (IOException e) {
			return null;
		} finally {
			try {
				response.close();
				httpclient.close();
			} catch (IOException e) {
				
			}			
		}
		return null;
	}
	
	
}
