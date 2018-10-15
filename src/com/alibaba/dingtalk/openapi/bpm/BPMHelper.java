package com.alibaba.dingtalk.openapi.bpm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.alibaba.dingtalk.openapi.role.Role;

import db.Batchsql;

public class BPMHelper {
	public static List<Instance>  getInstances(String json,String processCode){
		List<Instance> instances=new ArrayList<Instance>();
		String group_name;
		JSONObject jsonObject = JSONObject.fromObject(json);
		//取出dingtalk_smartwork_bpms_processinstance_list_response 对象及其数据
		//System.out.println("dingtalk_smartwork_bpms_processinstance_list_response:"+jsonObject.getString("dingtalk_smartwork_bpms_processinstance_list_response"));
		JSONObject dsbplr =JSONObject.fromObject(jsonObject.getString("dingtalk_smartwork_bpms_processinstance_list_response"));
		String request_id=dsbplr.getString("request_id");
		//System.out.println("request_id:"+request_id);
		//result对象及其数据
		JSONObject result=JSONObject.fromObject(dsbplr.getString("result"));
		String success=result.getString("success");
		String ding_open_errcode=result.getString("ding_open_errcode");
		//System.out.println("ding_open_errcode:"+ding_open_errcode);
		//result1对象及其数据
		JSONObject result1=JSONObject.fromObject(result.getString("result"));
		//System.out.println("result1:"+result.getString("result"));
		//每次读取10条记录，可能不足10条记录，next_cursor可能为空。
		//String next_cursor=result1.getString("next_cursor");
		
		//获取list对象及其数据
		JSONObject list=JSONObject.fromObject(result1.getString("list"));
		//System.out.println("list:"+result1.getString("list"));
		//role_groups
		//System.out.println("process_instance_top_vo:"+list.getString("process_instance_top_vo"));
		JSONArray  process_instance_top_vo=list.getJSONArray("process_instance_top_vo");
		for (int a = 0; a < process_instance_top_vo.size(); a++) {
			JSONObject instance=JSONObject.fromObject(process_instance_top_vo.getJSONObject(a));
			//cc_userid_list
			String process_instance_id;
			try{
				process_instance_id = instance.getString("process_instance_id");
			}catch(Exception e){
				process_instance_id="";
			}
			//String create_time
			String create_time;
			try{
				 create_time = instance.getString("create_time");
			}catch(Exception e){
				create_time="";
			}
			//finish_time
			String finish_time;
			try{
				finish_time = instance.getString("finish_time");
			}catch(Exception e){
				finish_time="";
			}
			//originator_dept_id
			String originator_dept_id;
			try{
				originator_dept_id = instance.getString("originator_dept_id");
			}catch(Exception e){
				originator_dept_id="";
			}
			//originator_dept_id
			String originator_userid;
			try{
				originator_userid = instance.getString("originator_userid");
			}catch(Exception e){
				originator_userid="";
			}

			//originator_dept_id
			String status;
			try{
				status = instance.getString("status");
			}catch(Exception e){
				status="";
			}
			//originator_dept_id
			String title;
			try{
				title = instance.getString("title");
			}catch(Exception e){
				title="";
			}
			//cc_userid_list
			String cc_userid_list;
			try{
				cc_userid_list = instance.getString("cc_userid_list");
			}catch(Exception e){
				cc_userid_list="";
			}
			
			//approver_userid_lists
			String approver_userid_lists;
			try{
				approver_userid_lists = approver_userid_listJsonToArray(JSONObject.fromObject(instance.getString("approver_userid_list")));
			}catch(Exception e){
				approver_userid_lists="";
			}
			//String approver_userid_lists=approver_userid_listJsonToArray(instance.getJSONArray("approver_userid_list"));
			//第一层form_component_values
			//approver_userid_lists
			String form_component_values;
			JSONObject formValueObject=JSONObject.fromObject(instance.getString("form_component_values"));
			JSONArray formValuesArray=formValueObject.getJSONArray("form_component_value_vo");
			try{
				form_component_values = form_component_valuesJsonToArray(formValuesArray);
			}catch(Exception e){
				form_component_values="";
				e.printStackTrace();
			}
			//String form_component_values=form_component_valuesJsonToArray(instance.getJSONArray("form_component_values"));
			//向集合添加单条实例记录""
			Instance instanceRecord=new Instance(processCode,
/*					"1",
					"2",
					"3",
					"4",
					"5",
					"6",
					"7",
					"8",
					"9",
					"10"*/
					 process_instance_id,
					 title,
					 create_time,
					 finish_time,
					 originator_userid,
					 originator_dept_id,
					 status,
					 approver_userid_lists,
					 cc_userid_list,
					 form_component_values
					);
			System.out.println(instanceRecord.getProcessCode()+"|"+instanceRecord.getCreate_time());
			//System.out.println("打印对象数据");
			instances.add(instanceRecord);
	  }
		return instances;
	}
	
	public static String approver_userid_listJsonToArray(JSONObject jsa){
		//StringBuffer sb = new StringBuffer();
		//for(int i=0;i<jsa.size();i++){
			//JSONObject record=JSONObject.fromObject(jsa.getJSONObject(i));
		//	if(i==jsa.size()-1)
			//	sb.append(jsa.getString("string"));
			//else
			//	sb.append(record.getString("string")+",");
		//}
		return jsa.getString("string");
	}
	
	public static String form_component_valuesJsonToArray(JSONArray jsa){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<jsa.size();i++){
			JSONObject record=JSONObject.fromObject(jsa.getJSONObject(i));
			String name;
			String value;
			try{
				name=record.getString("name");
			}catch(Exception e){
				name="";
			}
			try{
				value=record.getString("value");
			}catch(Exception e){
				value="";
			}
			
			//合并值
			if(name!="" && value!="")
				sb.append("name:"+name+";"+"value:"+value);
			else if(name!="" && value=="")
				sb.append("name:"+name);
			else if(value!="" && name=="")
				sb.append("value:"+value);
			if(i!=jsa.size()-1)
				sb.append("|");
		}
		return sb.toString();
	}
	
	public static String  getRolesUser(String json){
		StringBuffer sb = new StringBuffer();
		JSONObject jsonObject = JSONObject.fromObject(json);
		JSONObject dcrsr =JSONObject.fromObject(jsonObject.getString("dingtalk_corp_role_simplelist_response"));
		String request_id=dcrsr.getString("request_id");
		JSONObject result=JSONObject.fromObject(dcrsr.getString("result"));
		String has_more=result.getString("has_more");
		JSONObject list=JSONObject.fromObject(result.getString("list"));
		JSONArray esl=list.getJSONArray("emp_simple_list");
		for(int i=0;i<esl.size();i++){
			JSONObject record=JSONObject.fromObject(esl.getJSONObject(i));
			if(i==esl.size()-1)
				sb.append(record.getString("userid"));
			else
				sb.append(record.getString("userid")+",");
		}
		return sb.toString();
	}
	
}
