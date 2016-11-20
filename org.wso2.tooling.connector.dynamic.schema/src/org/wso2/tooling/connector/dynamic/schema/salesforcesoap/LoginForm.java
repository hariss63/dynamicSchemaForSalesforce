package org.wso2.tooling.connector.dynamic.schema.salesforcesoap;

public class LoginForm {
	public static String userName, password, loginUrl, securityToken;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		LoginForm.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		LoginForm.password = password;
	}

	public String getLoginURL() {
		return loginUrl;
	}

	public void setLoginURL(String loginUrl) {
		LoginForm.loginUrl = loginUrl;
	}

	public String getSecurityToken() {
		return securityToken;
	}

	public void setSecurityToken(String securityToken) {
		LoginForm.securityToken = securityToken;
	}
}
