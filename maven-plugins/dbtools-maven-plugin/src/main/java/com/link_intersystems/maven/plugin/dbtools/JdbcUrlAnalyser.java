package com.link_intersystems.maven.plugin.dbtools;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.db2.Db2DataTypeFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

public class JdbcUrlAnalyser {

	private URI jdbcURI;

	public enum DBType {
		DB2("db2"), ORACLE("oracle"), MYSQL("mysql"), POSTGRES("postgres"), HSQL(
				"hsql"), H2("h2");

		private String protocol;

		private DBType(String protocol) {
			this.protocol = protocol;
		}

		String getProtocolName() {
			return protocol;
		}
	}

	public JdbcUrlAnalyser(String jdbcUrl) {
		if (StringUtils.isBlank(jdbcUrl)) {
			throw new IllegalArgumentException("jdbcUrl must not be blank");
		}
		jdbcUrl = jdbcUrl.trim();
		if (!jdbcUrl.startsWith("jdbc:")) {
			throw new IllegalArgumentException(
					"Argument jdbcUrl is not a valid jdbc url: " + jdbcUrl);
		}
		try {
			this.jdbcURI = new URI(jdbcUrl);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("jdbcUrl is not a valid URI", e);
		}
	}

	public DBType getDBType() {
		String protocol = getProtocol();
		DBType dbType = null;

		DBType[] dbTypes = DBType.values();
		for (DBType candidate : dbTypes) {
			if (candidate.getProtocolName().equals(protocol)) {
				dbType = candidate;
				break;
			}
		}
		if (dbType == null) {
			String message = MessageFormat
					.format("{0} is not supported. Please implement the protpcol handling for jdbc url {1}",
							protocol, jdbcURI);
			throw new UnsupportedOperationException(message);
		}
		return dbType;
	}

	/**
	 * Guess the dictionary class name to use based on the product string.
	 */
	public IDataTypeFactory getDataTypeFactory() {
		String protocol = getProtocol();
		IDataTypeFactory dataTypeFactory = null;
		DBType dbType = getDBType();

		switch (dbType) {
		case DB2:
			dataTypeFactory = new Db2DataTypeFactory();
			break;
		case ORACLE:
			dataTypeFactory = new OracleDataTypeFactory();
			break;
		case MYSQL:
			dataTypeFactory = new MySqlDataTypeFactory();
			break;
		case POSTGRES:
			dataTypeFactory = new PostgresqlDataTypeFactory();
			break;
		case HSQL:
			dataTypeFactory = new HsqldbDataTypeFactory();
			break;
		case H2:
			dataTypeFactory = new H2DataTypeFactory();
			break;
		default:
			String message = MessageFormat
					.format("{0} is not supported. Please implement {1} selection for jdbc url: {2}",
							protocol, IDataTypeFactory.class.getSimpleName(),
							jdbcURI);
			throw new UnsupportedOperationException(message);
		}

		return dataTypeFactory;
	}

	/*
	 * Returns the "jdbc:" protocol of the url parameter. Looks for the prefix
	 * string up to the 3rd ':' or the 1st '@', '/' or '\', whichever comes
	 * first.
	 *
	 * This method is package qualified so that TestDictionaryFactory class can
	 * access and test this method behavior.
	 */
	public String getProtocol() {
		String schemeSpecificPart = jdbcURI.getSchemeSpecificPart();
		String dbProtocol = StringUtils
				.substringBefore(schemeSpecificPart, ":");
		return dbProtocol;
	}

	public String getHostname() {
		String schemeSpecificPart = jdbcURI.getSchemeSpecificPart();
		String protocol = StringUtils.substringBefore(schemeSpecificPart, ":");
		String schemeSpecificPartWithoutProtocol = StringUtils.removeStart(
				schemeSpecificPart, protocol + "://");
		String hostname = StringUtils.substringBefore(
				schemeSpecificPartWithoutProtocol, "/");

		if (hostname.contains("@")) {
			hostname = StringUtils.substringAfter(hostname, "@");
		}
		if (hostname.contains(":")) {
			hostname = StringUtils.substringBefore(hostname, ":");
		}
		return hostname;
	}

	public URI getJDBCUri() {
		return jdbcURI;
	}
}
