package com.link_intersystems.maven.plugin.tools.svn;

public class SvnRepository {

	private String url;
	private String authenticationId;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

}
