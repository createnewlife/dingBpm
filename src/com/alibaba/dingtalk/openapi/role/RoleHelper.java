package com.alibaba.dingtalk.openapi.role;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.dingtalk.openapi.utils.Infrastructure;

import db.Batchsql;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RoleHelper {
	private static Logger logger= Logger.getLogger(RoleHelper.class);
	public static List<Role>  getRoles(String json){
		List<Role> roles=new ArrayList<Role>();
		String group_name;
		JSONObject jsonObject = JSONObject.fromObject(json);
		//取出dingtalk_corp_role_list_response 对象及其数据
		JSONObject dcrlr =JSONObject.fromObject(jsonObject.getString("dingtalk_corp_role_list_response"));
		String request_id=dcrlr.getString("request_id");
		//result对象及其数据
		JSONObject result=JSONObject.fromObject(dcrlr.getString("result"));
		String has_more=result.getString("has_more");
		
		//list对象及其数据
		JSONObject list=JSONObject.fromObject(result.getString("list"));
		
		//role_groups
		JSONArray  role_groups=list.getJSONArray("role_groups");
		
		for (int i = 0; i < role_groups.size(); i++) {
			JSONObject role_group=JSONObject.fromObject(role_groups.getJSONObject(i));
			//group_name
			group_name=role_group.getString("group_name");
			//第一层roles
			JSONObject role1=role_group.getJSONObject("roles");
			//第二层roles
			try{
				//System.out.println("roles2:"+role1.getString("roles"));
				JSONArray  role2=role1.getJSONArray("roles");
				//System.out.println(role2.toString()+"   "+role2.size());
				for(int j=0;j<role2.size();j++){
					JSONObject role=role2.getJSONObject(j);//JSONObject.fromObject(role2.getJSONObject(j));
					//role_id
					String id=role.getString("id");
					String role_name=role.getString("role_name");
					System.out.println("group_name:"+group_name+"|id:"+id+"|role_name:"+role_name);
					String userRole="";
					try{
						userRole=getRolesUser(Long.parseLong(id));
					}catch(Exception e1){
						
					}
					//System.out.println(userRole);
					Role listRole=new Role(id,role_name,group_name,userRole);
					roles.add(listRole);
				}
		}catch(Exception e){
			//logger.error(e.toString());
		}finally{
			
		}
	  }
		return roles;
	}
	
	public static String  getRolesUser(Long id){
		StringBuffer sb = new StringBuffer();
		String more="";
		Long total=0L;//计算偏移量位置
			do{
			JSONObject jsonObject = JSONObject.fromObject(Batchsql.getRoleUser(id,total*20));
			JSONObject dcrsr =JSONObject.fromObject(jsonObject.getString("dingtalk_corp_role_simplelist_response"));
			String request_id=dcrsr.getString("request_id");
			JSONObject result=JSONObject.fromObject(dcrsr.getString("result"));
			String has_more=result.getString("has_more");
			//logger.error("currentNum:"+total+"|has_more:"+has_more+"|jsonObject:"+jsonObject);
			more=has_more;
			total=total+1;
			JSONObject list=JSONObject.fromObject(result.getString("list"));
			JSONArray esl=list.getJSONArray("emp_simple_list");
			for(int i=0;i<esl.size();i++){
				JSONObject record=JSONObject.fromObject(esl.getJSONObject(i));
				//if(i==esl.size()-1)
					//sb.append(record.getString("userid"));
			//	else
					sb.append(record.getString("userid")+",");
			}
		}while(Boolean.parseBoolean(more));
		return sb.toString();
	}
	
}
