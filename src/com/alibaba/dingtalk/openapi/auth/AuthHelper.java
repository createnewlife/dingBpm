package com.alibaba.dingtalk.openapi.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.dingtalk.openapi.utils.HttpHelper;



//
import javax.servlet.http.HttpServletRequest;
//
import com.alibaba.dingtalk.openapi.Env;
import com.alibaba.dingtalk.openapi.OApiException;
import com.alibaba.dingtalk.openapi.OApiResultException;
import com.alibaba.dingtalk.openapi.auth.AuthHelper;
import com.alibaba.dingtalk.openapi.utils.FileUtils;
import com.alibaba.dingtalk.openapi.utils.DbHttpHelper;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.open.client.ServiceFactory;
import com.dingtalk.open.client.api.model.corp.JsapiTicket;
import com.dingtalk.open.client.api.service.corp.CorpConnectionService;
import com.dingtalk.open.client.api.service.corp.JsapiService;
import com.dingtalk.open.client.common.SdkInitException;
import com.dingtalk.open.client.common.ServiceException;
import com.dingtalk.open.client.common.ServiceNotExistException;

public class AuthHelper {

	// public static String jsapiTicket = null;
	// public static String accessToken = null;
	public static Timer timer = null;
	// 鐠嬪啯鏆ｉ崚锟�1鐏忓繑妞�50閸掑棝鎸�
	public static final long cacheTime = 1000 * 60 * 55 * 2;
	public static long currentTime = 0 + cacheTime + 1;
	public static long lastTime = 0;
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*
	 * 閸︺劍顒濋弬瑙勭《娑擃叏绱濇稉杞扮啊闁灝鍘ゆ０鎴犵畳閼惧嘲褰嘺ccess_token閿涳拷
	 * 閸︺劏绐涚粋璁崇瑐娑擄拷濞喡ゅ箯閸欐溂ccess_token閺冨爼妫块崷銊よ⒈娑擃亜鐨弮鏈电閸愬懐娈戦幆鍛枌閿涳拷
	 * 鐏忓棛娲块幒銉ょ矤閹镐椒绠欓崠鏍х摠閸屻劋鑵戠拠璇插絿access_token
	 * 
	 * 閸ョ姳璐焌ccess_token閸滃sapi_ticket閻ㄥ嫯绻冮張鐔告闂傛挳鍏橀弰锟�7200缁夛拷
	 * 閹碉拷娴犮儱婀懢宄板絿access_token閻ㄥ嫬鎮撻弮鏈电瘍閸樻槒骞忛崣鏍︾啊jsapi_ticket
	 * 濞夘煉绱癹sapi_ticket閺勵垰婀崜宥囶伂妞ょ敻娼癑SAPI閸嬫碍娼堥梽鎰扮崣鐠囦線鍘ょ純顔炬畱閺冭泛锟芥瑩娓剁憰浣峰▏閻€劎娈�
	 * 閸忚渹缍嬫穱鈩冧紖鐠囬攱鐓￠惇瀣磻閸欐垼锟藉懏鏋冨锟�--閺夊啴妾烘宀冪槈闁板秶鐤�
	 */
	public static String getAccessToken() throws OApiException {
		long curTime = System.currentTimeMillis();
		JSONObject accessTokenValue = (JSONObject) FileUtils.getValue("accesstoken", Env.CORP_ID);
		String accToken = "";
		String jsTicket = "";
		JSONObject jsontemp = new JSONObject();
		if (accessTokenValue == null || curTime - accessTokenValue.getLong("begin_time") >= cacheTime) {
			try
			{
			ServiceFactory serviceFactory = ServiceFactory.getInstance();
	        CorpConnectionService corpConnectionService = serviceFactory.getOpenService(CorpConnectionService.class);
	        accToken = corpConnectionService.getCorpToken(Env.CORP_ID, Env.CORP_SECRET);
			// save accessToken
			JSONObject jsonAccess = new JSONObject();
			jsontemp.clear();
			jsontemp.put("access_token", accToken);
			jsontemp.put("begin_time", curTime);
			jsonAccess.put(Env.CORP_ID, jsontemp);
			FileUtils.write2File(jsonAccess, "accesstoken");
			System.out.println("accToken:-->"+accToken);
			if(accToken.length() > 0){
				
				JsapiService jsapiService = serviceFactory.getOpenService(JsapiService.class);

				JsapiTicket JsapiTicket = jsapiService.getJsapiTicket(accToken, "jsapi");
				jsTicket = JsapiTicket.getTicket();
				JSONObject jsonTicket = new JSONObject();
				jsontemp.clear();
				jsontemp.put("ticket", jsTicket);
				jsontemp.put("begin_time", curTime);
				jsonTicket.put(Env.CORP_ID, jsontemp);
				FileUtils.write2File(jsonTicket, "jsticket");
				
			}
		} catch (SdkInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		} else {
			return accessTokenValue.getString("access_token");
		}

		return accToken;
	}

	// 濮濓絽鐖堕惃鍕剰閸愬吀绗呴敍瀹﹕api_ticket閻ㄥ嫭婀侀弫鍫熸埂娑擄拷7200缁夋帪绱濋幍锟芥禒銉ョ磻閸欐垼锟藉懘娓剁憰浣告躬閺屾劒閲滈崷鐗堟煙鐠佹崘顓告稉锟芥稉顏勭暰閺冭泛娅掗敍灞界暰閺堢喎骞撻弴瀛樻煀jsapi_ticket
	public static String getJsapiTicket(String accessToken) throws OApiException {
		JSONObject jsTicketValue = (JSONObject) FileUtils.getValue("jsticket", Env.CORP_ID);
		long curTime = System.currentTimeMillis();
		String jsTicket = "";

		 if (jsTicketValue == null || curTime -
		 jsTicketValue.getLong("begin_time") >= cacheTime) {
			ServiceFactory serviceFactory;
			try {
				serviceFactory = ServiceFactory.getInstance();
				JsapiService jsapiService = serviceFactory.getOpenService(JsapiService.class);

				JsapiTicket JsapiTicket = jsapiService.getJsapiTicket(accessToken, "jsapi");
				jsTicket = JsapiTicket.getTicket();

				JSONObject jsonTicket = new JSONObject();
				JSONObject jsontemp = new JSONObject();
				jsontemp.clear();
				jsontemp.put("ticket", jsTicket);
				jsontemp.put("begin_time", curTime);
				jsonTicket.put(Env.CORP_ID, jsontemp);
				FileUtils.write2File(jsonTicket, "jsticket");
			} catch (SdkInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceNotExistException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return jsTicket;
		 } else {
			 return jsTicketValue.getString("ticket");
		 }
	}

	public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws OApiException {
		String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
				+ "&url=" + url;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.reset();
			sha1.update(plain.getBytes("UTF-8"));
			return bytesToHex(sha1.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new OApiResultException(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			throw new OApiResultException(e.getMessage());
		}
	}

	private static String bytesToHex(byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
	//鐢熸垚闅忔満瀛楃涓�
	public static String getRandomString(int length){
	      String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	      Random random=new Random();
	      StringBuffer sb=new StringBuffer();
	      for(int i=0;i<length;i++){
	        int number=random.nextInt(52);
	        sb.append(str.charAt(number));
	      }
	      return sb.toString();
	  }
	
	/*public static String getConfig(HttpServletRequest request) {
		String urlString = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		String queryStringEncode = null;
		String url;
		if (queryString != null) {
			queryStringEncode = URLDecoder.decode(queryString);
			url = urlString + "?" + queryStringEncode;
		} else {
			url = urlString;
		}
		//System.out.println("url:"+url);
		String nonceStr =getRandomString(15) "abcdefg";;
		System.out.println("nonceStr:-->"+nonceStr);
		long timeStamp = System.currentTimeMillis() / 1000;
		String signedUrl = url;
		String accessToken = null;
		String ticket = null;
		String signature = null;
		String agentid = null;

		try {
			accessToken = AuthHelper.getAccessToken();
	       
			ticket = AuthHelper.getJsapiTicket(accessToken);
			signature = AuthHelper.sign(ticket, nonceStr, timeStamp, signedUrl);
			agentid =seting.agentid "33066786";
			
		} catch (OApiException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String configValue = "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr + "',timeStamp:'"
		+ timeStamp + "',corpId:'" + Env.CORP_ID + "',agentid:'" + agentid+  "' }";
		System.out.println("configValue:"+configValue);
		return configValue;
	}*/
    
	//db涓婂崟鐐圭櫥褰曡繛鎺ュ埌edp涓�
	public static String getConfigdb() {
		//String urlString = request.getRequestURL().toString();
		//String queryString = request.getQueryString();

		//String queryStringEncode = null;
		//String url;
	/*	if (queryString != null) {
			queryStringEncode = URLDecoder.decode(queryString);
			url = urlString + "?" + queryStringEncode;
		} else {
			url = urlString;
		}*/
		//System.out.println("url:"+url);
		String nonceStr =getRandomString(15) /*"abcdefg";*/;
		System.out.println("nonceStr:-->"+nonceStr);
		long timeStamp = System.currentTimeMillis() / 1000;
		String signedUrl ="http://zz.yuxinpc.net:802/index.jsp";//seting.url; //"http://db.yuxinpc.net:802/index.jsp";seting.url;
		String accessToken = null;
		String ticket = null;
		String signature = null;
		String corpId="ding7e41e12d38947c97";
		String agentid = "28690557";
		com.fr.json.JSONObject js=null;

		try {
			js=DbHttpHelper.httpGet("http://edp.yuxinpc.net:802/servlet/DingAssessToken");//("http://edp.yuxinpc.net:802/servlet/DingAssessToken")(seting.assessTokenAgent);
			if(js.has("ticket"))
				ticket = js.optString("ticket");
			else 
				ticket=null;
			if(js.has("access_token"))
				accessToken = js.optString("access_token");
			else
				accessToken="";
	       
			
			signature = AuthHelper.sign(ticket, nonceStr, timeStamp, signedUrl);
			agentid ="28690557" /*"33066786"*/;
			
		} catch (OApiException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String configValue = "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr + "',timeStamp:'"
		+ timeStamp + "',corpId:'" + corpId + "',agentid:'" + agentid+  "' }";
		System.out.println("configValue:"+configValue);
		return configValue;
	}
	/*
	public static String getPcConfig(HttpServletRequest request){
		
	}*/

	public static String getSsoToken() throws OApiException {
		String url = "https://oapi.dingtalk.com/sso/gettoken?corpid=" + Env.CORP_ID + "&corpsecret=" + Env.SSO_Secret;
		JSONObject response = HttpHelper.httpGet(url);
		String ssoToken;
		if (response.containsKey("access_token")) {
			ssoToken = response.getString("access_token");
		} else {
			throw new OApiResultException("Sso_token");
		}
		return ssoToken;

	}

}

