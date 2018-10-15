package com.alibaba.dingtalk.openapi.bpm;

import java.util.Date;

public class Instance {
	public String getProcess_instance_id() {
		return process_instance_id;
	}
	public void setProcess_instance_id(String process_instance_id) {
		this.process_instance_id = process_instance_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getFinish_time() {
		return finish_time;
	}
	public void setFinish_time(String finish_time) {
		this.finish_time = finish_time;
	}
	public String getOriginator_userid() {
		return originator_userid;
	}
	public void setOriginator_userid(String originator_userid) {
		this.originator_userid = originator_userid;
	}
	public String getOriginator_dept_id() {
		return originator_dept_id;
	}
	public void setOriginator_dept_id(String originator_dept_id) {
		this.originator_dept_id = originator_dept_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getApprover_userid_list() {
		return approver_userid_list;
	}
	public void setApprover_userid_list(String approver_userid_list) {
		this.approver_userid_list = approver_userid_list;
	}
	public String getCc_userid_list() {
		return cc_userid_list;
	}
	public void setCc_userid_list(String cc_userid_list) {
		this.cc_userid_list = cc_userid_list;
	}
	public String getForm_component_values() {
		return form_component_values;
	}
	public void setForm_component_values(String form_component_values) {
		this.form_component_values = form_component_values;
	}
	private String process_instance_id;
	private String title;
	private String create_time;
	private String finish_time;
	private String originator_userid;
	private String originator_dept_id;
	private String status;
	private String approver_userid_list;
	private String cc_userid_list;
	private String form_component_values;
	private String processCode;
	public String getProcessCode() {
		return processCode;
	}
	public void setProcessCode(String processCode) {
		this.processCode = processCode;
	}
	public Instance(String processCode,String process_instance_id,String title,String create_time,String finish_time,String originator_userid,String originator_dept_id,String status,String approver_userid_list,String cc_userid_list,String form_component_values){
		this.approver_userid_list=approver_userid_list;
		this.cc_userid_list=cc_userid_list;
		this.create_time=create_time;
		this.finish_time=finish_time;
		this.form_component_values=form_component_values;
		this.originator_dept_id=originator_dept_id;
		this.originator_userid=originator_userid;
		this.process_instance_id=process_instance_id;
		this.status=status;
		this.title=title;
		this.processCode=processCode;
	}
}
