package com.link_intersystems.maven.plugin.dbtools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.settings.Server;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.AbstractStatementFactory;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.datatype.IDataTypeFactory;

import com.link_intersystems.maven.plugin.system.MavenContext;

public class RichDriverConfig {

	private DriverConfig driverConfig;
	private MavenContext mavenContext;
	private JdbcUrlAnalyser jdbcUrlAnalyser;

	public RichDriverConfig(MavenContext mavenContext, DriverConfig driverConfig) {
		this.mavenContext = mavenContext;
		this.driverConfig = driverConfig;
	}

	public String getUrl() throws MojoExecutionException {
		String url = driverConfig.getUrl();
		if (StringUtils.isBlank(url)) {
			throw new MojoExecutionException(
					"A driver url must be configured via driverConfig/url");
		}
		return url.trim();
	}

	public JdbcUrlAnalyser getJdbcUrlAnalyser() throws MojoExecutionException {
		if (jdbcUrlAnalyser == null) {
			jdbcUrlAnalyser = new JdbcUrlAnalyser(getUrl());
		}
		return jdbcUrlAnalyser;
	}

	public String getDriverClass() throws MojoExecutionException {
		String driverClass = driverConfig.getDriverClass();
		if (StringUtils.isBlank(driverClass)) {
			throw new MojoExecutionException(
					"A driver class must be configured via driverConfig/driverClass");
		}
		return driverClass.trim();
	}

	public String getUsername() throws MojoExecutionException {
		String username = driverConfig.getUsername();
		if (StringUtils.isBlank(username)) {
			String serverId = driverConfig.getServerId();
			if (StringUtils.isBlank(serverId)) {
				throw new MojoExecutionException(
						"No username nor a serverId is configured.");
			}
			Server serverSettings = mavenContext.getServerSettings(serverId
					.trim());
			if (serverSettings == null) {
				throw new MojoExecutionException(
						"Maven settings does not contain server configuration "
								+ serverId);
			}
			username = serverSettings.getUsername();
			if (StringUtils.isBlank(username)) {
				throw new MojoExecutionException(
						"Maven settings server configuration " + serverId
								+ " does not contain a username.");
			}
		}
		return username.trim();
	}

	public String getPassword() throws MojoExecutionException {
		String password = driverConfig.getPassword();
		if (password == null) {
			String serverId = driverConfig.getServerId();
			if (StringUtils.isBlank(serverId)) {
				throw new MojoExecutionException(
						"No passowrd nor a serverId is configured.");
			}
			Server serverSettings = mavenContext.getServerSettings(serverId
					.trim());
			if (serverSettings == null) {
				throw new MojoExecutionException(
						"Maven settings does not contain server configuration "
								+ serverId);
			}
			password = serverSettings.getPassword();
		}
		if (password == null) {
			password = "";
		}
		return password;
	}

	@SuppressWarnings("unchecked")
	public IDataTypeFactory createDataTypeFactory()
			throws MojoExecutionException {
		String dataTypeFactoryName = driverConfig.getDataTypeFactoryName();
		try {

			if (StringUtils.isBlank(dataTypeFactoryName)) {
				JdbcUrlAnalyser jdbcUrlAnalyser = getJdbcUrlAnalyser();
				return jdbcUrlAnalyser.getDataTypeFactory();
			}
			try {
				Class<? extends IDataTypeFactory> dataTypeFactoryClass = (Class<? extends IDataTypeFactory>) Class
						.forName(dataTypeFactoryName);
				IDataTypeFactory dataTypeFactory = dataTypeFactoryClass
						.newInstance();
				return dataTypeFactory;
			} catch (ClassNotFoundException cnf) {
				JdbcUrlAnalyser jdbcUrlAnalyser = new JdbcUrlAnalyser(getUrl());
				return jdbcUrlAnalyser.getDataTypeFactory();
			} catch (Exception e) {
				throw new MojoExecutionException(
						"Unable to create dbunit data type factory", e);
			}
		} catch (UnsupportedOperationException e) {
			String message = MessageFormat
					.format("Unable to automatically select a {0}. Please configure one.",
							IDataTypeFactory.class);
			throw new MojoExecutionException(message, e);
		}
	}

	public String getSchema() {
		return driverConfig.getSchema();
	}

	public String getHostname() throws MojoExecutionException {
		JdbcUrlAnalyser jdbcUrlAnalyser = new JdbcUrlAnalyser(getUrl());
		return jdbcUrlAnalyser.getHostname();
	}

	public IDatabaseConnection createConnection() throws MojoExecutionException {
		try {
			Connection jdbcConnection = createJDBCConnection();
			String schema = getSchema();
			IDatabaseConnection connection;
			if (StringUtils.isBlank(schema)) {
				connection = new DatabaseConnection(jdbcConnection);
			} else {
				connection = new DatabaseConnection(jdbcConnection, schema);
			}
			DatabaseConfig config = connection.getConfig();
			IDataTypeFactory dataTypeFactory = createDataTypeFactory();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
					dataTypeFactory);
			IStatementFactory statementFactory = createStatementFactory(config);
			config.setProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY,
					statementFactory);
			return connection;
		} catch (Exception e) {
			throw new MojoExecutionException(
					"Unable to create DBUnit database connection", e);
		}
	}

	public Connection createJDBCConnection() throws MojoExecutionException {
		String driverClass = getDriverClass();
		try {
			Class.forName(driverClass);
			String url = getUrl();
			String user = getUsername();
			String password = getPassword();
			Connection jdbcConnection = DriverManager.getConnection(url, user,
					password);
			return jdbcConnection;
		} catch (Exception e) {
			throw new MojoExecutionException(
					"Unable to create jdbc database connection", e);
		}
	}

	private IStatementFactory createStatementFactory(DatabaseConfig config) {
		IStatementFactory currentStatementFactory = (AbstractStatementFactory) config
				.getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY);
		LoggingStatementFactory loggingStatementFactory = new LoggingStatementFactory(
				currentStatementFactory, mavenContext.getLog());
		return loggingStatementFactory;
	}
}
