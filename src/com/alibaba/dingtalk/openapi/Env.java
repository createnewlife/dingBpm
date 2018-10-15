package com.alibaba.dingtalk.openapi;

import utils.FileUtils;

import com.alibaba.fastjson.JSONObject;

//import com.access.fr.seting;


public class Env {
	
	static JSONObject jsobj=FileUtils.read2JSON("seting");
	public static final String OAPI_HOST = "https://oapi.dingtalk.com";
	public static final String OA_BACKGROUND_URL = ""; 
	public static  String CORP_ID=jsobj.getString("corpID");//="ding54800e84cdf96b03"; //= seting.CORP_ID; ding7e41e12d38947c97
	
	public static  String CORP_SECRET=jsobj.getString("corpSec") ;//="-o9ZDQJgSSLkA8Dy9LhI5Gzlx6fhS7PiWBlMmrat7b9K16CmTqQcmdLCJq3DhK-P";// seting.CORP_SECRET;
	public static  String SSO_Secret ="-o9ZDQJgSSLkA8Dy9LhI5Gzlx6fhS7PiWBlMmrat7b9K16CmTqQcmdLCJq3DhK-P";// seting.SSO_Secret; TAgv2U0uReKtINHEFTGPm0hBx_1Ru5r70p6Sx5a9TdxH9fDk2DVby2bp6bqwGkIH

	
	public static String suiteTicket; 
	public static String authCode; 
	public static String suiteToken; 

	public static final String CREATE_SUITE_KEY = "suite4xxxxxxxxxxxxxxx";
	public static final String SUITE_KEY = "";
	public static final String SUITE_SECRET = "";
	public static final String TOKEN = "";
	public static final String ENCODING_AES_KEY = "";
	
	
	
}