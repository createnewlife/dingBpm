package db;
import com.alibaba.fastjson.JSONObject;
import com.microsoft.sqlserver.jdbc.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import org.apache.log4j.Logger;

import utils.FileUtils;

public class DBConnectionManager {
	private static Logger logger= Logger.getLogger(DBConnectionManager.class);
    //SQLServer   
    private static  String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    /*
    private static String url;  
    private static String user;
    private static String password;
    */
    private static String returnMsg="Success";
    public String getDriverName() {  
        return driverName;  
   }  
     /* 
    public void setUrl(String newUrl) {  
        url = newUrl;  
    }  
    public String getUrl() {  
        return url;  
    }  
    public void setUser(String newUser) {  
        user = newUser;  
    }  
    public String getUser() {  
        return user;  
   }  
    public void setPassword(String newPassword) {  
        password = newPassword;  
    }  
    public String getPassword() {  
        return password;  
    } 
    */
    public static String testCon(String url,String user,String password){
    	try{
    		Class.forName(driverName);
    		}catch(Exception e){
    			returnMsg="找不到SQL驱动程序";
    		}
    		try{
    			Connection con=DriverManager.getConnection(url,user,password);
    			Statement stmt = con.createStatement();
    		}catch(Exception e)
    		{
    			logger.debug(e.toString());
    			returnMsg=e.toString();//"数据库连接失败";
    		}
    		return returnMsg;
    	}
    public static  Connection getConnection() {  
        try {  
            Class.forName(driverName).newInstance();  
            JSONObject jsobj=FileUtils.read2JSON("seting");
            return DriverManager.getConnection(jsobj.getString("address"),jsobj.getString("userName"),jsobj.getString("password"));
        } catch (Exception e) {  
           logger.debug(e.toString());
           e.printStackTrace();  
            //message = "数据库连接失败！";  
            return null;  
        }  
       // System.out.println(message);
    } 
}
