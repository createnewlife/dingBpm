package com.alibaba.dingtalk.openapi.microapp;


import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import com.alibaba.dingtalk.openapi.OApiException;
import com.alibaba.dingtalk.openapi.auth.AuthHelper;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpRoleSimplelistRequest;
import com.dingtalk.api.response.CorpRoleSimplelistResponse;
import com.taobao.api.ApiException;

import db.Batchsql;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.print(httpGet("https://oapi.dingtalk.com/microapp/visible_scopes?access_token=30d8675f50a53a78a8212d498e26bbba").toString());
		/*
		String URL="";
		try {
			URL = "https://oapi.dingtalk.com/microapp/visible_scopes?access_token="+AuthHelper.getAccessToken();
		} catch (OApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String Json = findXmlInfo();
		Json = "{'agentId':8892437}";
		String postResult =doHttpPost(Json,URL);
		System.out.println("postResult:"+postResult);
		*/
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpRoleSimplelistRequest req = new CorpRoleSimplelistRequest();
		req.setRoleId(224718353L);
		req.setSize(20L);
		req.setOffset(120L);
		CorpRoleSimplelistResponse rsp = null;
		try {
			rsp = client.execute(req, AuthHelper.getAccessToken());
		} catch (ApiException e) {
			//logger.error(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//roleusers.add(rsp.getBody());
		
		//return roleusers;
		 System.out.print(rsp.getBody());
	}
	
	   /*
	private static String findXmlInfo() {
	        // TODO Auto-generated method stub
	        return null;
	    }
	 
	 
	    public static String doHttpPost(String xmlInfo,String URL){         
	         System.out.println("发起的数据:"+xmlInfo);       
	         byte[] xmlData = xmlInfo.getBytes();            
	         InputStream instr  = null; 
	         java.io.ByteArrayOutputStream out = null;              
	          try{                          
	                 URL url = new URL(URL);                
	                 URLConnection urlCon = url.openConnection();               
	                 urlCon.setDoOutput(true);              
	                 urlCon.setDoInput(true);               
	                 urlCon.setUseCaches(false);                            
	                 urlCon.setRequestProperty("Content-Type", "text/xml");       
	                 urlCon.setRequestProperty("Content-length",String.valueOf(xmlData.length)); 
	                 System.out.println(String.valueOf(xmlData.length));
	                 DataOutputStream printout = new DataOutputStream(urlCon.getOutputStream());            
	                 printout.write(xmlData);               
	                 printout.flush();              
	                 printout.close();              
	                 instr = urlCon.getInputStream();   
	                 byte[] bis = IOUtils.toByteArray(instr);
	                 String ResponseString = new String(bis, "UTF-8");  
	                if ((ResponseString == null) || ("".equals(ResponseString.trim()))) {
	                     System.out.println("返回空");
	                    }
	               System.out.println("返回数据为:" + ResponseString);
	              return ResponseString;    
	             
	          }catch(Exception e){              
	                 e.printStackTrace();
	                return "0";
	          }             
	          finally {             
	                 try {     
	                	  if(out!=null){
	                        out.close();   
	                        instr.close();
	                	  }
	                         
	                 }catch (Exception ex) {      
	                        return "0";
	                     }                  
	                 }                  
	          }                     
*/
}
