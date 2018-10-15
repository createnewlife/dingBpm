package entity;

import java.util.ArrayList;

public class DingtalkCorpRoleListResponse {
	private String request_id;
	private Result result;
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public static class Result{
		private String has_more;
		private DefalultList defalutList;
		public DefalultList getDefalutList() {
			return defalutList;
		}

		public void setDefalutList(DefalultList defalutList) {
			this.defalutList = defalutList;
		}

		public String getHas_more() {
			return has_more;
		}

		public void setHas_more(String has_more) {
			this.has_more = has_more;
		}
	}
	public static class DefalultList{
		private DefalutRoleGroups defalutRoleGroups;

		public DefalutRoleGroups getDefalutRoleGroups() {
			return defalutRoleGroups;
		}

		public void setDefalutRoleGroups(DefalutRoleGroups defalutRoleGroups) {
			this.defalutRoleGroups = defalutRoleGroups;
		}
	}
	
	public static class DefalutRoleGroups{
		private ArrayList<RoleGroup>  roleGroup;

		public ArrayList<RoleGroup> getRoleGroup() {
			return roleGroup;
		}

		public void setRoleGroup(ArrayList<RoleGroup> roleGroup) {
			this.roleGroup = roleGroup;
		}
	}
	
	public static class RoleGroup{
		private String group_name;
		private Roles roles;
		
		public Roles getRoles() {
			return roles;
		}

		public void setRoles(Roles roles) {
			this.roles = roles;
		}

		public String getGroup_name() {
			return group_name;
		}

		public void setGroup_name(String group_name) {
			this.group_name = group_name;
		}
	}
	
	public static class Roles{
		private ArrayList<Role> role;

		public ArrayList<Role> getRole() {
			return role;
		}

		public void setRole(ArrayList<Role> role) {
			this.role = role;
		}
	}
	
	public static class Role{
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getRole_name() {
			return role_name;
		}
		public void setRole_name(String role_name) {
			this.role_name = role_name;
		}
		private String id;
		private String role_name;
	}
	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}
}
