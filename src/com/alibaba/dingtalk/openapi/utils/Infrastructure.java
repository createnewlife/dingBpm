package com.alibaba.dingtalk.openapi.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.alibaba.dingtalk.openapi.OApiException;
import com.alibaba.dingtalk.openapi.auth.AuthHelper;
import com.alibaba.dingtalk.openapi.department.DepartmentHelper;
import com.alibaba.dingtalk.openapi.role.Role;
import com.alibaba.dingtalk.openapi.role.RoleHelper;
import com.dingtalk.open.client.ServiceFactory;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;
import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;
import com.dingtalk.open.client.api.service.corp.CorpUserService;
import com.dingtalk.open.client.common.SdkInitException;
import com.dingtalk.open.client.common.ServiceException;
import com.dingtalk.open.client.common.ServiceNotExistException;

import db.Batchsql;

public class Infrastructure {
	private static Logger logger= Logger.getLogger(Infrastructure.class);
	public static void synchronizationInfrastructure(){
		try {
			//��ȡ���Ĳ�����Ϣ
			List<Department> lsDepart = null;
			List<DepartmentDetail> ld=null;
			try {
				lsDepart = DepartmentHelper.listDepartments(AuthHelper.getAccessToken(), "1");
			} catch (ServiceNotExistException | SdkInitException
					| ServiceException e1) {
				logger.debug(e1.toString());
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				 ld=DepartmentHelper.listDepartmentsDetails(AuthHelper.getAccessToken(), lsDepart);
			} catch (ServiceNotExistException | SdkInitException
					| ServiceException e) {
				logger.debug(e.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//��ȡ������Ա��ϢCorpUserDetailList 
			CorpUserService corpUserService=null;
			try {
				corpUserService = ServiceFactory.getInstance().getOpenService(CorpUserService.class);
			} catch (ServiceNotExistException | SdkInitException e) {
				logger.debug(e.toString());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<CorpUserDetail> corpUserDetailTolal= new ArrayList<>();
			//��ȡ����ɫ��Ϣ
			List<Role> roles= RoleHelper.getRoles(Batchsql.role(AuthHelper.getAccessToken()));
			
			for(int i=0;i<ld.size();i++){
				CorpUserDetailList cdl = null;
				try {
					cdl = corpUserService.getCorpUserList(AuthHelper.getAccessToken(), ld.get(i).getId(),(long) 0, 100, "entry_asc");
				} catch (ServiceException e) {
					logger.debug(e.toString());
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(int j=0;j<cdl.getUserlist().size();j++){
					//System.out.println((j+1)+"  :"+cdl.getUserlist().get(j));
					corpUserDetailTolal.add(cdl.getUserlist().get(j));
				}
			}		
			//ͬ������SQL
			String deptSql="  IF NOT EXISTS(SELECT * FROM [dbo].[TZDD��������] WHERE Id = ? )"
					+ "INSERT INTO [dbo].[TZDD��������]([Id],[Name],[Parentid],[DeptManagerUseridList],[DeptPerimits],[OrgDeptOwner],[Order],[AutoAddUser],[CreateDeptGroup],[DeptHiding]) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?) "
					+ "else "
					+ "update  dbo.[TZDD��������] set [Name]=?,[Parentid]=?,[DeptManagerUseridList]=?,[DeptPerimits]=?,[OrgDeptOwner]=?,[Order]=?,[AutoAddUser]=?,[CreateDeptGroup]=?,[DeptHiding]=? where [Id]=?;";
			//ͬ����Ա����SQL
			String userSql="  IF NOT EXISTS(SELECT * FROM [dbo].[TZDD������Ա] WHERE [userid] = ? )"
					+ "INSERT INTO [dbo].[TZDD������Ա]([userid],[name],[department],[dingId],[email],[tel],[jobnumber],[extattr],[active],[avatar],[isAdmin],[isBoss],[mobile],[position],[remark],[workPlace]) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
					+ "else "
					+ "update  [dbo].[TZDD������Ա] set [name]=?,[department]=?,[dingId]=?,[email]=?,[tel]=?,[jobnumber]=?,[extattr]=?,[active]=?,[avatar]=?,[isAdmin]=?,[isBoss]=?,[mobile]=?,[position]=?,[remark]=?,[workPlace]=? where [userid]=?;";

			//ͬ����ɫ��Ϣ
			String roleString="  IF NOT EXISTS(SELECT * FROM [dbo].[TZDD������ɫ] WHERE [id] = ? ) "
					+" INSERT INTO [dbo].[TZDD������ɫ]([id],[role_name],[group_name],[user_role])  "
					+" VALUES(?,?,?,?) "
					+" else"
					+" update [dbo].[TZDD������ɫ] set [role_name]=?,[group_name]=?,[user_role]=? where [id]=?;";
			Batchsql.batchSqlDept(deptSql, ld);
			Batchsql.batchSqlUser(userSql, corpUserDetailTolal);
			Batchsql.BatchSQLRole(roleString, roles);
			//JOptionPane.showMessageDialog(null,"ͬ���ɹ�", "��ʾ", JOptionPane.ERROR_MESSAGE);
			
		} catch (Exception e) {
			logger.error(e.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
