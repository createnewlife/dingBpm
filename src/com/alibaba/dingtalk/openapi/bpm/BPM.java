package com.alibaba.dingtalk.openapi.bpm;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.alibaba.dingtalk.openapi.OApiException;
import com.alibaba.dingtalk.openapi.auth.AuthHelper;
//import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.SmartworkBpmsProcessinstanceListRequest;
import com.dingtalk.api.response.SmartworkBpmsProcessinstanceListResponse;
import com.fr.json.JSONException;
import com.fr.json.JSONObject;
import com.taobao.api.ApiException;

import db.Batchsql;
import db.DBConnectionManager;

public class BPM {
	private static Logger logger= Logger.getLogger(BPM.class);
	public static final String bpmSql="  IF NOT EXISTS(SELECT * FROM [dbo].[TBPM实例] WHERE [钉钉实例ID] = ? )"
			+ "INSERT INTO [dbo].[TBPM实例]([实例ID],[钉钉流程ID],[钉钉实例ID],[实例名称],[开始时间],[结束时间],[发起者ID],[发起部门ID],[实例状态ID],[步骤处理者ID],[实例抄送者ID],[实例表单]) "
			+ "VALUES(newid(),?,?,?,?,?,(select thr员工.员工ID from dbo.thr员工 where rtrim(ltrim(员工编码)) in (select top 1 jobnumber from TZDD钉钉人员  where userid=?)),?,(select 字典ID from [TZSM数据字典] where 字典名称=?),?,?,?) "
			+ "else "
			+ "update [dbo].[TBPM实例] set [实例名称]=?,[开始时间]=?,[结束时间]=?,[发起者ID]=(select thr员工.员工ID from dbo.thr员工 where rtrim(ltrim(员工编码)) in (select top 1 jobnumber from TZDD钉钉人员  where userid=?)),[发起部门ID]=?,[实例状态ID]=(select 字典ID from [TZSM数据字典] where 字典名称=?),[步骤处理者ID]=?,[实例抄送者ID]=?,[实例表单]=? where [钉钉实例ID]=? ;";
	public static void getProcessInstance(String processCode,int synchronizationLeadTime) throws Exception{//getFly
		DingTalkClient client =  new com.dingtalk.api.DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		SmartworkBpmsProcessinstanceListRequest req = new SmartworkBpmsProcessinstanceListRequest();
		req.setProcessCode(processCode);//PROC-EF6YNNYRN2-IMEFCZ5RNDUES35WUOWI2-PO2KX0VI-C1  and [status]<>'RUNNING'
		//String start_time="201706251200";
		//String end_time="202901011200";
		String getedJson=null;
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
		
		try {
			req.setStartTime(startTime( processCode, synchronizationLeadTime));
		} catch (Exception e3) {
			logger.debug(e3.toString());
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		req.setSize(10L);
		req.setCursor(0L);
		SmartworkBpmsProcessinstanceListResponse rsp = null;
		try {
			rsp = client.execute(req,AuthHelper.getAccessToken());
		} catch (ApiException e2) {
			logger.debug(e2.toString());
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (OApiException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}//getAccessToken()
		getedJson=rsp.getBody();
		try{
		Batchsql.BatchSQLProcess(bpmSql, BPMHelper.getInstances(getedJson,processCode));
		System.out.print(bpmSql);
			while(getNextCursor(getedJson)!=-1){
				try{
					//System.out.println(getNextCursor(getedJson));
					req.setCursor( (long) getNextCursor(getedJson));
					//System.out.println(req.getCursor());
					try {
						rsp = client.execute(req, AuthHelper.getAccessToken());
					} catch (ApiException e) {
						// TODO Auto-generated catch block
						logger.debug(e.toString());
						e.printStackTrace();
					} catch (OApiException e) {
						// TODO Auto-generated catch block
						logger.debug(e.toString());
						e.printStackTrace();
					}
					getedJson=rsp.getBody();
					//a=new JSONObject(rsp.getBody());
					//getedJson=rsp.getBody();
					//System.out.println(getedJson);
					
					Batchsql.BatchSQLProcess(bpmSql, BPMHelper.getInstances(getedJson,processCode));
				}catch(NumberFormatException e1){
					logger.debug(e1.toString());
					e1.printStackTrace();
				}
			}
		}catch(Exception e){
			logger.debug(e.toString());
		}
	}
	/*
	 * 获取下一页页码
	 */
	public static int getNextCursor(String jasonObject) throws Exception{
		int nextCursor=-1;
		try {
			JSONObject jsb=new JSONObject(jasonObject);
			JSONObject jsb1=new JSONObject(jsb.getString("dingtalk_smartwork_bpms_processinstance_list_response"));
			JSONObject jsb2=new JSONObject(jsb1.getString("result"));
			JSONObject jsb3=new JSONObject(jsb2.getString("result"));
			if(jsb3.has("next_cursor"))
			{
				nextCursor=jsb3.getInt("next_cursor");
			}
		} catch (JSONException e) {
			logger.debug(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nextCursor;
	}
	
	public static Long startTime(String processCode,int synchronizationLeadTime) throws Exception{
		//System.out.println(processCode+":"+synchronizationLeadTime);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
		Long startTime1=198510221200L;
		/*
		Long startTime = null;
		try {
			startTime = sdf.parse(startTime1).getTime();
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String dbStartTime;
		Connection con=null;
		
		String querySql="select top 1 create_time from [dbo].[ProcessInstance] where processCode='"+processCode+"' and [status]!='COMPLETED' and [status] !='TERMINATED' and [status]!='CANCELED' order by create_time desc";
		try{
			con=DBConnectionManager.getConnection();
			//con.prepareStatement(querySql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			Statement st=con.createStatement();	
			ResultSet rs=st.executeQuery(querySql);
			//System.out.println(startTime);
			//System.out.println(querySql);
			//System.out.println(rs.next());
			if(rs.next()){	
				try {
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					try{
						dbStartTime = rs.getString("create_time");
					}catch(Exception e){
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
						dbStartTime=df.format(new Date());
						System.out.println("create_time is empty");
					}
						//System.out.println("create_time is empty");
						dbStartTime = rs.getString("create_time");
						Date start_time= sdf1.parse(dbStartTime);
						Calendar rightNow = Calendar.getInstance();
						System.out.println(rightNow.toString());
						rightNow.setTime(start_time);
						System.out.println(rightNow.toString());
						if(synchronizationLeadTime>=0)
							rightNow.add(Calendar.MONTH, -1);
						else
							rightNow.add(Calendar.MONTH, synchronizationLeadTime);
						Date dt1 = rightNow.getTime();
						System.out.println(dt1.toString());
						startTime=dt1.getTime();
				} catch (ParseException e3) {
					logger.debug(e3.toString());
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
			}else {
				startTime=sdf.parse(startTime1).getTime();
			}
			
		}catch(Exception e){
			logger.error(e.toString());
		}
		*/
		return startTime1;//startTime;
	}
	
	public static void cycleReadProcess() throws Exception {
		String querySql="select distinct 钉钉流程ID  from TBPM流程  where 钉钉流程ID is not null";//"select processCode,synchronizationLeadTime from Process";
		String processCode=null;
		int synchronizationLeadTime=0;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Connection con=null;
		try{
			con=DBConnectionManager.getConnection();
			con.prepareStatement(querySql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			Statement st=con.createStatement();	
			Statement st1=con.createStatement();	
			ResultSet rs=st.executeQuery(querySql);
			while(rs.next()){
				processCode=rs.getString("钉钉流程ID").trim();
				try{
					synchronizationLeadTime=0;//Integer.parseInt(rs.getString("synchronizationLeadTime"));
				}catch(Exception e){
					synchronizationLeadTime=0;
				}
				System.out.println("钉钉流程ID"+":"+synchronizationLeadTime);
				try{
					getProcessInstance(processCode,synchronizationLeadTime);
					//st1.executeUpdate("update Process set lastUpdatedTime='"+df.format(new Date())+"' where processCode='"+processCode+"'");
					//st1.executeUpdate("insert into  TZDD钉钉同步([同步信息ID],[同步对象ID],[最后同步时间]) values (newid(),'C731C07D-02E1-40B0-BAAB-05BC8DBE1892','"+df.format(new Date())+"')");
					//System.out.println(synchronizationLeadTime);
				}catch(Exception e){
					logger.error(e.toString());
					continue; 
					//e.printStackTrace();
				}
				
				//System.out.println(processCode+":"+synchronizationLeadTime);
				
			}
			st1.executeUpdate("insert into  TZDD钉钉同步([同步信息ID],[同步对象ID],[最后同步时间]) values (newid(),'C731C07D-02E1-40B0-BAAB-05BC8DBE1892','"+df.format(new Date())+"')");
			st1.close();
			st.close();
			con.close();
		}catch(Exception e){
			logger.debug(e.toString());
		}finally{
			
		}
	}
}

