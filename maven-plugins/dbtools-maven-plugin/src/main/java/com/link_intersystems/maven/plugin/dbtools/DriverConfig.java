package com.link_intersystems.maven.plugin.dbtools;

public class DriverConfig {

	private String url;

	private String driverClass;

	private String username;

	private String password;

	private String serverId;

	private String dataTypeFactoryName;

	private String schema;

	public DriverConfig() {
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getDataTypeFactoryName() {
		return dataTypeFactoryName;
	}

	public void setDataTypeFactoryName(String dataTypeFactoryName) {
		this.dataTypeFactoryName = dataTypeFactoryName;
	}

}
