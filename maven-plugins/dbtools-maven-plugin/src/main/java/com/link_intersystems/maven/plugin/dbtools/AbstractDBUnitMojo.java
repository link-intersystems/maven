package com.link_intersystems.maven.plugin.dbtools;

import org.apache.maven.plugins.annotations.Parameter;

import com.link_intersystems.maven.plugin.system.AbstractMavenContextMojo;
import com.link_intersystems.maven.plugin.system.GoalExecutor;
import com.link_intersystems.maven.plugin.system.MavenContext;

public abstract class AbstractDBUnitMojo<T extends GoalExecutor<PARAMS>, PARAMS>
		extends AbstractMavenContextMojo<T, PARAMS> {

	/**
	 * The jdbc driver configuration that should be used to connect to the
	 * database. <br/>
	 * Config example<br/>
	 *
	 * <pre>
	 * &lt;driverConfig&gt;
	 *    &lt;url&gt;jdbc:db2://someserver.net:50000/DATABASE:retrieveMessagesFromServerOnGetMessage=true;&lt;/url&gt;
	 *    &lt;driverClass&gt;com.ibm.db2.jcc.DB2Driver&lt;/driverClass&gt;
	 *    &lt;username&gt;username&lt;/username&gt;
	 *    &lt;password&gt;password&lt;/password&gt;
	 *    &lt;dataTypeFactoryName&gt;org.dbunit.ext.db2.Db2DataTypeFactory&lt;/dataTypeFactoryName&gt;
	 * &lt;/driverConfig&gt;
	 *
	 * or
	 *
	 * &lt;driverConfig&gt;
	 *    &lt;url&gt;jdbc:db2://someserver.net:50000/DATABASE:retrieveMessagesFromServerOnGetMessage=true;&lt;/url&gt;
	 *    &lt;driverClass&gt;com.ibm.db2.jcc.DB2Driver&lt;/driverClass&gt;
	 *    &lt;serverId&gt;settings.xml_serverId&lt;/serverId&gt;
	 *    &lt;dataTypeFactoryName&gt;org.dbunit.ext.db2.Db2DataTypeFactory&lt;/dataTypeFactoryName&gt;
	 * &lt;/driverConfig&gt;
	 *
	 * </pre>
	 *
	 */
	@Parameter
	protected DriverConfig driverConfig;

	public RichDriverConfig getRichDriverConfig() {
		MavenContext mavenContext = getMavenContext();
		RichDriverConfig richDriverConfig = new RichDriverConfig(mavenContext,
				driverConfig);
		return richDriverConfig;
	}
}