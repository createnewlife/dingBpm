import java.awt.EventQueue;

import com.alibaba.dingtalk.openapi.OApiException;
import com.alibaba.dingtalk.openapi.department.*;
import com.alibaba.dingtalk.openapi.microapp.visibleScopes;
import com.alibaba.dingtalk.openapi.role.Role;
import com.alibaba.dingtalk.openapi.role.RoleHelper;
import com.alibaba.dingtalk.openapi.user.UserHelper;
import com.alibaba.dingtalk.openapi.utils.Infrastructure;
import com.alibaba.dingtalk.openapi.utils.ScheduledOperationJob;
import com.alibaba.dingtalk.openapi.auth.*;
import com.alibaba.dingtalk.openapi.bpm.BPM;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Font;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import db.Batchsql;
import db.DBConnectionManager;
import entity.DingtalkCorpRoleListResponse;
import entity.Seting;




















import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPasswordField;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;




import com.dingtalk.open.client.ServiceFactory;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;
import com.dingtalk.open.client.api.model.corp.CorpUserDetailList;
import com.dingtalk.open.client.api.model.corp.Department;
import com.dingtalk.open.client.api.model.corp.DepartmentDetail;
import com.dingtalk.open.client.api.service.corp.CorpUserService;
import com.dingtalk.open.client.common.SdkInitException;
import com.dingtalk.open.client.common.ServiceException;
import com.dingtalk.open.client.common.ServiceNotExistException;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;




import sumileJson.SumileJson;
import utils.FileUtils;


public class MainFrame {
	SchedulerFactory sf=new StdSchedulerFactory();
	public static Scheduler sched=null;
	private JFrame frame;
	private JTextField dbaddress;
	private JTextField username;
	private JTextField corpID;
	private JTextField corpSec;
	private JPasswordField password;
	private JFormattedTextField cycle;
	private JFormattedTextField starttime;
	private static Logger logger= Logger.getLogger(MainFrame.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("\u540C\u6B65\u9489\u9489\u6D41\u7A0B\u5B9E\u4F8B\u6570\u636E");
		frame.setBounds(100, 100, 450, 373);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel label = new JLabel("\u6570\u636E\u5E93\u5730\u5740:");
		label.setFont(new Font("宋体", Font.PLAIN, 11));
		label.setBounds(13, 28, 102, 15);
		panel.add(label);
		
		dbaddress = new JTextField();
		dbaddress.setBounds(100, 24, 334, 21);
		panel.add(dbaddress);
		dbaddress.setColumns(10);
		
		JLabel label_1 = new JLabel("\u6570\u636E\u5E93\u7528\u6237\u540D:");
		label_1.setFont(new Font("宋体", Font.PLAIN, 11));
		label_1.setBounds(13, 70, 90, 15);
		panel.add(label_1);
		
		username = new JTextField();
		username.setBounds(100, 66, 102, 21);
		panel.add(username);
		username.setColumns(10);
		
		JLabel label_2 = new JLabel("\u6570\u636E\u5E93\u7528\u6237\u5BC6\u7801:");
		label_2.setFont(new Font("宋体", Font.PLAIN, 11));
		label_2.setBounds(13, 101, 105, 15);
		panel.add(label_2);
		
		JLabel lblcorpid = new JLabel("\u9489\u9489corpID:");
		lblcorpid.setFont(new Font("宋体", Font.PLAIN, 11));
		lblcorpid.setBounds(13, 139, 91, 15);
		panel.add(lblcorpid);
		
		corpID = new JTextField();
		corpID.setBounds(100, 136, 265, 21);
		panel.add(corpID);
		corpID.setColumns(10);
		
		JLabel lblcorpsec = new JLabel("\u9489\u9489corpSec:");
		lblcorpsec.setFont(new Font("宋体", Font.PLAIN, 11));
		lblcorpsec.setBounds(13, 167, 78, 15);
		panel.add(lblcorpsec);
		
		corpSec = new JTextField();
		corpSec.setBounds(100, 163, 266, 21);
		panel.add(corpSec);
		corpSec.setColumns(10);
		
		JButton testLink = new JButton("\u6D4B\u8BD5\u6570\u636E\u94FE\u63A5");
		testLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//JOptionPane.showMessageDialog(null, dbaddress.getText(), "测试结果", JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(null, connReuslt(), "测试结果", JOptionPane.ERROR_MESSAGE);
			}
		});
		testLink.setBounds(190, 96, 114, 23);
		panel.add(testLink);
		
		

		JButton start = new JButton("\u542F\u52A8\u670D\u52A1");
		start.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				
				//
				try {
					SchedulerFactory schedFact=new StdSchedulerFactory();
					sched=schedFact.getScheduler();
					sched.start();
					JobDetail jobDetail=new JobDetail("a","b",ScheduledOperationJob.class);
					jobDetail.getJobDataMap().put("name","lucy");CronTrigger trigger=new CronTrigger("c","d");
					
					String expression="";//0 50 23 1/2 * ? 
					String [] startTime=starttime.getText().split(":");
					int min=Integer.parseInt(startTime[0].trim());
					int second=Integer.parseInt(startTime[1].trim());
					int day=Integer.parseInt(cycle.getText().trim());
					if(second<0 ||second>60)
						second=0;
					if(min<0 || min>60)
						min=0;
					System.out.println("second:"+second);
					System.out.println("min:"+min);
					System.out.println("day:"+day);
					expression="0 "+second+" "+min+" 1-30/"+day+"  * ? *";
					System.out.println("expression:"+expression);
					trigger.setCronExpression(expression); // 启动之后立即执行 每一秒继续重复。
					
					//trigger.set
					sched.scheduleJob(jobDetail, trigger);

					}catch(Exception e)
					{
						try {
							sched.shutdown(true);
						} catch (SchedulerException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						logger.error(e.toString());
						e.printStackTrace();
					}
				start.setEnabled(false);
			}
		});
		start.setBounds(72, 289, 93, 23);
		panel.add(start);
		
		JButton excit = new JButton("\u9000\u51FA\u670D\u52A1");
		excit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(sched==null)
				{
					JOptionPane.showMessageDialog(null, "服务未启动，暂时不能退出服务", "提示", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					sched.shutdown(true);
					start.setEnabled(true);
				} catch (SchedulerException e) {
					// TODO Auto-generated catch block
					logger.error(e.toString());
					e.printStackTrace();
				}
				}
		});
		excit.setBounds(234, 289, 93, 23);
		panel.add(excit);
		
		JButton save = new JButton("\u4FDD\u5B58\u914D\u7F6E\u4FE1\u606F");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(connReuslt()!="Success"){
					JOptionPane.showMessageDialog(null, connReuslt(), "提示", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(corpID.getText()=="" || corpSec.getText()=="" || starttime.getText()=="" || cycle.getText()==""){
					JOptionPane.showMessageDialog(null, "钉钉ID、Sec、同步开始时间、同步周期不能为空!", "提示", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(FileUtils.write2File(FileUtils.objToJSONObject(new Seting(dbaddress.getText(),username.getText(),password.getText(),corpID.getText(),corpSec.getText(),starttime.getText(),cycle.getText())), "seting")){
					JOptionPane.showMessageDialog(null, "保存成功", "提示", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		save.setBounds(312, 96, 105, 23);
		panel.add(save);
		
		JLabel label_3 = new JLabel("\u540C\u6B65\u5468\u671F\u8D77\u59CB\u65F6\u95F4:");
		label_3.setFont(new Font("宋体", Font.PLAIN, 11));
		label_3.setBounds(13, 205, 102, 15);
		panel.add(label_3);
		
		JLabel lblNewLabel = new JLabel("\u540C\u6B65\u5468\u671F:");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 11));
		lblNewLabel.setBounds(13, 230, 90, 15);
		panel.add(lblNewLabel);
		
		
		//格式1
		 MaskFormatter mf = null;
		 try
		 {
		 mf = new MaskFormatter("##:##");
		 mf.setAllowsInvalid(false);
		 }
		 catch(Exception e)
		 {
		 e.printStackTrace();
		 }
		 starttime = new JFormattedTextField(mf);
		 starttime.setText("24:00");
		starttime.setBounds(115, 202, 42, 21);
		panel.add(starttime);
			
		 //格式2
		 MaskFormatter mf1 = null;
		 try
		 {
		 mf1 = new MaskFormatter("##");
		 
		 }
		 catch(Exception e)
		 {
		 e.printStackTrace();
		 }
		 cycle = new JFormattedTextField(mf1);
		cycle.setText("01");
		cycle.setBounds(115, 229, 39, 21);
		panel.add(cycle);
		
		JLabel label_4 = new JLabel("\u5929");
		label_4.setFont(new Font("宋体", Font.PLAIN, 11));
		label_4.setBounds(164, 233, 36, 15);
		panel.add(label_4);
		
		JButton Manual = new JButton("\u624B\u52A8\u540C\u6B65\u57FA\u7840\u7ED3\u6784");
		Manual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JSONObject jsobj=FileUtils.read2JSON("seting");
				if(jsobj==null){
					JOptionPane.showMessageDialog(null,"正确设置配置信息并保存", "提示", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try{
					//同步组织结构、人员、部门等信息
					Infrastructure.synchronizationInfrastructure();
					//同步流程审批信息
					BPM.cycleReadProcess();
					//同步微应用可见范围
					visibleScopes.sysMicAppScope();
					JOptionPane.showMessageDialog(null,"同步成功", "提示", JOptionPane.ERROR_MESSAGE);
				}catch(Exception e){
					logger.error(e.toString());
					return ;
				}
				
			}
		});
		Manual.setBounds(254, 194, 151, 23);
		panel.add(Manual);
		
		password = new JPasswordField();
		password.setBounds(100, 95, 80, 21);
		panel.add(password);
		//读取配置参数
		initSeting();
	}
	//Test the connection is succes or fail
	public String connReuslt(){
		@SuppressWarnings("deprecation")
		String testResult=DBConnectionManager.testCon(dbaddress.getText(), username.getText(), password.getText());
		return testResult;
	}
	
	public void initSeting(){
		JSONObject jsobj=FileUtils.read2JSON("seting");
		if(jsobj!=null){
			dbaddress.setText(jsobj.getString("address"));
			username.setText(jsobj.getString("userName"));
			corpID.setText(jsobj.getString("corpID"));
			corpSec.setText(jsobj.getString("corpSec"));
			cycle.setText(jsobj.getString("cycle"));
			password.setText(jsobj.getString("password"));
			starttime.setText(jsobj.getString("startTime"));
			
			
		}
	}
}
