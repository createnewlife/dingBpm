package db;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.dingtalk.openapi.OApiException;
import com.alibaba.dingtalk.openapi.auth.AuthHelper;
import com.alibaba.dingtalk.openapi.bpm.Instance;
import com.alibaba.dingtalk.openapi.role.Role;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.CorpRoleListRequest;
import com.dingtalk.api.request.CorpRoleSimplelistRequest;
import com.dingtalk.api.response.CorpRoleListResponse;
import com.dingtalk.api.response.CorpRoleSimplelistResponse;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;
import com.microsoft.sqlserver.jdbc.*;
import com.taobao.api.ApiException;


public  class Batchsql {
	/**
	 *      ([Id]
           ,[Name]
           ,[Parentid]
           ,[DeptManagerUseridList]
           ,[DeptPerimits]
           ,[OrgDeptOwner]
           ,[Order]
           ,[AutoAddUser]
           ,[CreateDeptGroup]
           ,[DeptHiding])
	 * @param sqlTemplate
	 * @param list
	 */
	private static Logger logger= Logger.getLogger(Batchsql.class);
	public static void batchSqlDept(String sqlTemplate,List<DepartmentDetail> list){
		Connection conn=DBConnectionManager.getConnection();
		PreparedStatement ps = null;
		try{
			//同步部门
			ps=conn.prepareStatement(sqlTemplate);
			conn.setAutoCommit(false);
			int size=list.size();
			//DepartmentDetail o=null;
			for(int i=0;i<size;i++){
				//o=list.get(i);
				//判断是否存在
				ps.setObject(1,list.get(i).getId());
				//insert
				ps.setObject(2,list.get(i).getId());
				ps.setObject(3,list.get(i).getName());
				ps.setObject(4,list.get(i).getParentid());
				ps.setObject(5,list.get(i).getDeptManagerUseridList());
				ps.setObject(6,list.get(i).getDeptPerimits());
				ps.setObject(7,list.get(i).getOrgDeptOwner());
				ps.setObject(8,list.get(i).getOrder());
				ps.setObject(9,list.get(i).getAutoAddUser());
				ps.setObject(10,list.get(i).getCreateDeptGroup());
				ps.setObject(11,list.get(i).getDeptHiding());
				//update
				ps.setObject(12,list.get(i).getName());
				ps.setObject(13,list.get(i).getParentid());
				ps.setObject(14,list.get(i).getDeptManagerUseridList());
				ps.setObject(15,list.get(i).getDeptPerimits());
				ps.setObject(16,list.get(i).getOrgDeptOwner());
				ps.setObject(17,list.get(i).getOrder());
				ps.setObject(18,list.get(i).getAutoAddUser());
				ps.setObject(19,list.get(i).getCreateDeptGroup());
				ps.setObject(20,list.get(i).getDeptHiding());
				ps.setObject(21,list.get(i).getId());
				
				ps.addBatch();
			}
			//ps.close();
			ps.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(SQLException e){
			logger.error(e.toString());
			e.printStackTrace();
			try{
				conn.rollback();
				conn.setAutoCommit(true);
			}catch(SQLException e1){
				e1.printStackTrace();
			}
		}finally{
			closeAll(null, ps, conn);
		}
		
	}
	//更新人员
	public static void batchSqlUser(String sqlTemplate,List<CorpUserDetail> list){
		Connection conn=DBConnectionManager.getConnection();
		PreparedStatement ps = null;
		try{
			//同步部门
			ps=conn.prepareStatement(sqlTemplate);
			conn.setAutoCommit(false);
			int size=list.size();
			//DepartmentDetail o=null;
			for(int i=0;i<size;i++){
				//o=list.get(i);
				//判断是否存在
				ps.setObject(1,list.get(i).getUserid());
				//insert
				ps.setObject(2,list.get(i).getUserid());
				ps.setObject(3,list.get(i).getName());
				ps.setObject(4,list.get(i).getDepartment().toString());
				ps.setObject(5,list.get(i).getDingId());
				ps.setObject(6,"");
				ps.setObject(7,list.get(i).getTel());
				ps.setObject(8,list.get(i).getJobnumber());
				ps.setObject(9,"");//MapToString(list.get(i).getExtattr())
				ps.setObject(10,list.get(i).getActive());
				ps.setObject(11,list.get(i).getAvatar());
				ps.setObject(12,list.get(i).getIsAdmin());
				ps.setObject(13,list.get(i).getIsBoss());
				ps.setObject(14,list.get(i).getMobile());
				ps.setObject(15,list.get(i).getPosition());
				ps.setObject(16,list.get(i).getRemark());
				ps.setObject(17,list.get(i).getWorkPlace());
				//update
				ps.setObject(18,list.get(i).getName());
				ps.setObject(19,list.get(i).getDepartment().toString());
				ps.setObject(20,list.get(i).getDingId());
				ps.setObject(21,"");
				ps.setObject(22,list.get(i).getTel());
				ps.setObject(23,list.get(i).getJobnumber());
				ps.setObject(24,"");
				ps.setObject(25,list.get(i).getActive());
				ps.setObject(26,list.get(i).getAvatar());
				ps.setObject(27,list.get(i).getIsAdmin());
				ps.setObject(28,list.get(i).getIsBoss());
				ps.setObject(29,list.get(i).getMobile());
				ps.setObject(30,list.get(i).getPosition());
				ps.setObject(31,list.get(i).getRemark());
				ps.setObject(32,list.get(i).getWorkPlace());
				ps.setObject(33,list.get(i).getUserid());
				
				ps.addBatch();
			}
			//ps.close();
			ps.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(SQLException e){
			logger.error(e.toString());
			e.printStackTrace();
			try{
				conn.rollback();
				conn.setAutoCommit(true);
			}catch(SQLException e1){
				e1.printStackTrace();
			}
		}finally{
			closeAll(null, ps, conn);
		}
		
	}
	private static void closeAll(Object object, PreparedStatement ps,Connection conn) {
		// TODO Auto-generated method stub
		try {
			//if(!ps.isClosed())
				ps.close();
			//if(!conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			logger.error(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String  MapToString(Map<String,String> map){
		String m="";
		
		for (Object obj : map.keySet()) {
			m+=(String)obj.toString()+":|"+map.get(obj);
		}
		return m;

	} 

	public static String role(String access_token){
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpRoleListRequest req = new CorpRoleListRequest();
		req.setSize(20L);
		req.setOffset(0L);
		CorpRoleListResponse rsp = null;
		try {
			rsp = client.execute(req, access_token);
		} catch (ApiException e) {
			logger.error(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rsp.getBody());
		return rsp.getBody().toString();
	}
	
	
	public static void BatchSQLProcess(String sqlTemplate,List<Instance> list) throws Exception{
		Connection conn=DBConnectionManager.getConnection();
		PreparedStatement ps = null;
		try{
			//同步部门
			ps=conn.prepareStatement(sqlTemplate);
			conn.setAutoCommit(false);
			int size=list.size();
			//DepartmentDetail o=null;
			for(int i=0;i<size;i++){
				//o=list.get(i);
				//判断是否存在
				ps.setObject(1,list.get(i).getProcess_instance_id());
				//insert
				ps.setObject(2,list.get(i).getProcessCode());
				ps.setObject(3,list.get(i).getProcess_instance_id());
				ps.setObject(4,list.get(i).getTitle());
				ps.setObject(5,list.get(i).getCreate_time());
				ps.setObject(6,list.get(i).getFinish_time());
				ps.setObject(7,list.get(i).getOriginator_userid());
				ps.setObject(8,list.get(i).getOriginator_dept_id());
				ps.setObject(9,list.get(i).getStatus());
				ps.setObject(10,list.get(i).getApprover_userid_list());
				ps.setObject(11,list.get(i).getCc_userid_list());
				ps.setObject(12,list.get(i).getForm_component_values());
				//update
				ps.setObject(13,list.get(i).getTitle());
				ps.setObject(14,list.get(i).getCreate_time());
				ps.setObject(15,list.get(i).getFinish_time());
				ps.setObject(16,list.get(i).getOriginator_userid());
				ps.setObject(17,list.get(i).getOriginator_dept_id());
				ps.setObject(18,list.get(i).getStatus());
				ps.setObject(19,list.get(i).getApprover_userid_list());
				ps.setObject(20,list.get(i).getCc_userid_list());
				ps.setObject(21,list.get(i).getForm_component_values());
				
				ps.setObject(22,list.get(i).getProcess_instance_id());
				//ps.setObject(23,list.get(i).getStatus());
				
				
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(SQLException e){
			logger.error(e.toString());
			e.printStackTrace();
			try{
				conn.rollback();
				conn.setAutoCommit(true);
			}catch(SQLException e1){
				logger.error(e1.toString());
				e1.printStackTrace();
			}
		}finally{
			closeAll(null, ps, conn);
		}
	}
	public static void BatchSQLRole(String sqlTemplate,List<Role> list){
		Connection conn=DBConnectionManager.getConnection();
		PreparedStatement ps = null;
		try{
			//同步部门
			ps=conn.prepareStatement(sqlTemplate);
			conn.setAutoCommit(false);
			int size=list.size();
			//DepartmentDetail o=null;
			for(int i=0;i<size;i++){
				//o=list.get(i);
				//判断是否存在
				ps.setObject(1,list.get(i).getRoleid());
				//insert
				ps.setObject(2,list.get(i).getRoleid());
				ps.setObject(3,list.get(i).getRoleName());
				ps.setObject(4,list.get(i).getGroupName());
				ps.setObject(5,list.get(i).getUserRole());
				//update
				ps.setObject(6,list.get(i).getRoleName());
				ps.setObject(7,list.get(i).getGroupName());
				ps.setObject(8,list.get(i).getUserRole());
				ps.setObject(9,list.get(i).getRoleid());
				
				ps.addBatch();
			}
			//ps.close();
			ps.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(SQLException e){
			logger.error(e.toString());
			e.printStackTrace();
			try{
				conn.rollback();
				conn.setAutoCommit(true);
			}catch(SQLException e1){
				e1.printStackTrace();
			}
		}finally{
			closeAll(null, ps, conn);
		}
		
	}
	
	
	public static void readJson2Map(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
		Map<String, Map<String, Object>> maps = objectMapper.readValue(json, Map.class);
		//System.out.println(maps.size());
		Set<String> key = maps.keySet();
		Iterator<String> iter = key.iterator();
		while (iter.hasNext()) {
			String field = iter.next();
		System.out.println(field + ":" + maps.get(field));
		}
		} catch (JsonParseException e) {
			logger.error(e.toString());
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e){
			e.printStackTrace();
		}
	}
	/*
	public static List<Role> getRole(String access_token){
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpRoleListRequest req = new CorpRoleListRequest();
		//req.setSize(20L);
		//req.setOffset(0L);
		CorpRoleListResponse rsp = null;
		try {
			rsp = client.execute(req, access_token);
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(rsp.getBody());
		return rsp.getBody().toString();
	}
	*/
	
	public static String getRole(String access_token){
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpRoleListRequest req = new CorpRoleListRequest();
		String json=null;
		//req.setSize(20L);
		//req.setOffset(0L);
		CorpRoleListResponse rsp = null;
		try {
			rsp = client.execute(req, access_token);
		} catch (ApiException e) {
			logger.error(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsp.getBody();
	}
	
	public static String getRoleUser(Long roleId,Long offset){
		String json=null;
		DingTalkClient client = new DefaultDingTalkClient("https://eco.taobao.com/router/rest");
		CorpRoleSimplelistRequest req = new CorpRoleSimplelistRequest();
		req.setRoleId(roleId);
		req.setSize(20L);
		req.setOffset(offset);
		CorpRoleSimplelistResponse rsp = null;
		try {
			rsp = client.execute(req, AuthHelper.getAccessToken());
		} catch (ApiException e) {
			logger.error(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rsp.getBody();//第一次读取出角色对于的人员信息
	}
}

