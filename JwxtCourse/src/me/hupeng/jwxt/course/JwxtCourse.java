package me.hupeng.jwxt.course;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JwxtCourse {
	
	private List<Course>list;
	private HttpRequester httpRequester;
	private String cookie;
	private Configuration  configuration;
	/**
	 * 得到课表列表
	 * */
	public List<Course>getCourseList(Configuration configuration){
		this.configuration = configuration;
		list = new ArrayList<>();
		String htmlString = getCouseHtmlString();
		if (htmlString == null) {
			return null;
		}
		if (htmlString!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(htmlString);
            htmlString = m.replaceAll("");
        }
		htmlString = htmlString.replaceAll("\n", "");
		htmlString = htmlString.replaceAll("\t", "");
		htmlString = htmlString.replaceAll(" ", "");
		Pattern pattern = Pattern
				.compile("<td>&nbsp;[\\s\\S]{0,100}</td>");
		Matcher matcher = pattern.matcher(htmlString);
		String resultString = "";
		int sum = 0;
		while (matcher.find()) {
			resultString = matcher.group();
			//System.out.println(resultString);
			for (int i = 0; i < resultString.split("</td><td>").length; i++) {
				String tempString = resultString.split("</td><td>")[i];
				tempString = tempString.replaceAll("<br>", "");
				tempString = tempString.replaceAll("<td>", "");
				tempString = tempString.replaceAll("</td>", "");
				tempString = tempString.replaceAll("</tr>", "");
				tempString = tempString.replaceAll("</table>", "");
				tempString = tempString.replaceAll("&nbsp;", "");
				Course course = new Course();
				course.setDay(sum%7 + 1);
				course.setNo(sum/7 + 1);
				course.setCourseInfo(tempString);
				list.add(course);
				sum++;
				if (sum >= 77) {
					break;
				}
			}
			if (sum >= 77) {
				break;
			}
		}

		return list;
	}
	
	public JwxtCourse(){
		httpRequester = new HttpRequester();
	}
	
	private LoginResult login(){
		LoginResult loginResult = new LoginResult();
		HttpResponse httpResponse;
		
		try {
			cookie = httpRequester.sendGet(Configuration.getUrl()).getCookie();
			cookie = cookie.replaceAll("; path=/", "");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
		Map<String, String>params = new HashMap<String, String>();
		Map<String, String>propertys = new HashMap<>();
		propertys.put("cookie", cookie);
		params.put("zjh", configuration.getStudentNum());
		params.put("mm", configuration.getPassword());
		try {
			httpResponse = httpRequester.sendPost(Configuration.getUrl()+"loginAction.do", params,propertys);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginResult.state = 0;
			return loginResult;
		}
		if (httpResponse.getContent().contains("URP 综合教务系统 - 登录")) {
			loginResult.state = 0;
		}else{
			loginResult.state = 1;
			loginResult.cookie = httpResponse.getCookie();
		}
		return loginResult;
	}
	
	public String getCouseHtmlString(){
		LoginResult loginResult = login();
		if (loginResult == null || loginResult.state == 0) {
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String> propertys =new HashMap<String, String>();
		params.put("actionType", "6");
		propertys.put("cookie",cookie);
		try {
			return httpRequester.sendGet(Configuration.getUrl()+"xkAction.do", params, propertys).getContent();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	class LoginResult{
		int state;
		String cookie;
	}
}

