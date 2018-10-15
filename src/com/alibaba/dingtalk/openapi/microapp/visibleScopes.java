package com.alibaba.dingtalk.openapi.microapp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.alibaba.dingtalk.openapi.OApiException;
import com.alibaba.dingtalk.openapi.auth.AuthHelper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import db.DBConnectionManager;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;


public class visibleScopes {
	private static Logger logger= Logger.getLogger(visibleScopes.class);
	
	public static void sysMicAppScope(){
		
		String querySql="select distinct AgentID from TZDD微应用 ";
		String URL="";
		String Json = "";
		Connection con=null;
		try {
			URL = "https://oapi.dingtalk.com/microapp/visible_scopes?access_token="+AuthHelper.getAccessToken();
		} catch (OApiException e1) {
			// TODO Auto-generated catch block
			logger.error(e1.toString());
			e1.printStackTrace();
			
		}
		try
		{
			con=DBConnectionManager.getConnection();
			con.prepareStatement(querySql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			Statement st=con.createStatement();	
			Statement insert=con.createStatement();
			ResultSet rs=st.executeQuery(querySql);
			String agentID="";
			String updateSql=null; 
			JSONObject resltJson=null;
			//logger.error(rs.toString());
			while(rs.next()){
				 agentID=rs.getString("AgentID").toString().trim();
				 //logger.error(agentID);
				 Json = "{'agentId':"+agentID+"}";
				 if(agentID!=""&&URL!=""){
					 //logger.error(agentID+" "+URL);
					 resltJson=JSONObject.parseObject(doHttpPost(Json,URL));
					// logger.error(resltJson.toString());
					// logger.error(resltJson.getString("errmsg"));
					 
					 if("ok".equals(resltJson.getString("errmsg").toString())){
						// logger.error("ok11");
						 updateSql="update TZDD微应用  set 应用名称=应用名称   ";
						 if(resltJson.containsKey("deptVisibleScopes")){
							 updateSql=updateSql + ",部门可见范围='"+JasonAarrayToString(JSONArray.parseArray(resltJson.getString("deptVisibleScopes")))+"' ";
						 }
						 if(resltJson.containsKey("userVisibleScopes")){
							 updateSql=updateSql +" ,人员可见范围='"+JasonAarrayToString(JSONArray.parseArray(resltJson.getString("userVisibleScopes")))+"' ";
						 }
						 if(resltJson.containsKey("isHidden")){
							 updateSql=updateSql +" ,是否隐藏='"+Boolean.parseBoolean(resltJson.getString("isHidden"))+"' ";
						 }
						 
						 updateSql=updateSql +" where AgentID="+agentID;
						 //logger.error(updateSql);
						 insert.execute(updateSql);
					 }
				 }
			}
			st.close();
			insert.close();
			con.close();
		}catch(Exception e){
			logger.error(e.toString());
		}
		finally{
			//st.close();
			//insert.close();
			//con.close();
		}
	}
	///读取微应用开放范围
    public static String doHttpPost(String xmlInfo,String URL){         
        //System.out.println("发起的数据:"+xmlInfo);       
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
                //System.out.println(String.valueOf(xmlData.length));
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
              //System.out.println("返回数据为:" + ResponseString);
             return ResponseString;    
            
         }catch(Exception e){ 
        	 logger.error(e.toString());
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
                		logger.error(ex.toString());
                       return "[]";
                    }                  
                }                  
         }    
	
    public static String JasonAarrayToString(JSONArray jasonA){
    	String ret="";
    	for(int i=0;i<jasonA.size();i++){
    		ret=ret+jasonA.getString(i)+",";
    	}
    	return ret;
    }
}
