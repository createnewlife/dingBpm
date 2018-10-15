package com.alibaba.dingtalk.openapi.utils;
import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.alibaba.dingtalk.openapi.bpm.BPM;
import com.alibaba.dingtalk.openapi.microapp.visibleScopes;

import db.Batchsql;

import java.util.Date;


public class ScheduledOperationJob implements Job{
	private static Logger logger= Logger.getLogger(ScheduledOperationJob.class);
	 public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException 
	 { 
			try {
				Infrastructure.synchronizationInfrastructure();
				BPM.cycleReadProcess();
				//同步微应用可见范围
				visibleScopes.sysMicAppScope();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e.toString());
				e.printStackTrace();
			}
			
	 } 
}
