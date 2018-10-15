package com.alibaba.dingtalk.openapi.utils;
import java.util.HashMap;

import com.fr.general.http.HttpClient;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;

public class DbHttpHelper {
	public static JSONObject httpGet(String url) {
	    HashMap localHashMap = new HashMap();
	    
	    
	    HttpClient localHttpClient = new HttpClient(url, localHashMap);
	    localHttpClient.asGet();
	    String str3 = localHttpClient.getResponseText();
	    try
	    {
	      return new JSONObject(str3);
	    }
	    catch (JSONException localJSONException)
	    {
	      //FRLogger.getLogger().error(localJSONException.getMessage());
	    }
	    return null;
	}
}
