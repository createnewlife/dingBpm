package com.alibaba.dingtalk.openapi.role;

public class Role {
	public String getRoleid() {
		return roleid;
	}
	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	private String roleid;
	private String roleName;
	private String groupName;
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	private String userRole;
	
	public Role(String id,String name,String groupName,String userRole){
		this.roleid=id;
		this.roleName=name;
		this.groupName=groupName;
		this.userRole=userRole;
	}
}
