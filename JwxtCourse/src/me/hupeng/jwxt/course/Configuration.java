package me.hupeng.jwxt.course;

/**
 * 教务系统类的配置管理类
 * 
 * @version 1.0.0
 * @author HUPENG
 * */
public class Configuration {

	private static String url = "jwxt.imu.edu.cn";
	private String studentNum;
	private String password;
	
	private Configuration() {
		// TODO Auto-generated constructor stub
	}
	
	public Configuration(String studentNum,String password){
		this.studentNum = studentNum;
		this.password = password;
	}
	
	public static String getUrl() {
		return "http://" + url + "/";
	}

	/**
	 * 重新设置url
	 * */
	public static void setUrl(String url) {
		Configuration.url = url;
	}

	public String getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(String studentNum) {
		this.studentNum = studentNum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
