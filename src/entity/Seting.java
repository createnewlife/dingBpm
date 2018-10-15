package entity;

public class Seting {
	private String address;
	private String userName;
	private String password;
	private String CorpID;
	private String CorpSec;
	private String startTime;
	private String cycle;
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public Seting(String address,String userName,String password,String CorpID,String CorpSec,String startTime,String cycle){
		this.setAddress(address);
		this.setUserName(userName);
		this.setPassword(password);
		this.setCorpID(CorpID);
		this.setCorpSec(CorpSec);
		this.setStartTime(startTime);
		this.setCycle(cycle);
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCorpID() {
		return CorpID;
	}
	public void setCorpID(String corpID) {
		CorpID = corpID;
	}
	public String getCorpSec() {
		return CorpSec;
	}
	public void setCorpSec(String corpSec) {
		CorpSec = corpSec;
	}


}
