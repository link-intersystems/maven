package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.link_intersystems.maven.plugin.dbtools.AbstractDBUnitMojo;
import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;
import com.link_intersystems.maven.plugin.system.MavenContext;

@Mojo(name = "export")
public class ExportMojo extends
		AbstractDBUnitMojo<ExportExecutor, ExportParams> implements
		ExportParams {

	/**
	 * The export data set configuration<br/>
	 * Config example<br/>
	 *
	 * <pre>
	 * &lt;exportDataSet&gt;
	 *    &lt;!-- Export format type --&gt;
	 *    &lt;!-- Supported formats are XLS, XML, FLAT and CSV --&gt;
	 *    &lt;format&gt;XLS&lt;/format&gt;
	 *    &lt;!-- 0 = Schema name if configured. Otherwise jdbc url hostname --&gt;
	 *    &lt;!-- 1 = Format in lower case --&gt;
	 *    &lt;!-- 2 = Current date in format ddMMyy --&gt;
	 *    &lt;!-- 3 = Current time in format HHmmss--&gt;
	 *    &lt;!-- default value is {0}_{2}-{3}.{1} --&gt;
	 *    &lt;outputFileNameFormat&gt;export-{0}_{2}-{3}.{1}&lt;/outputFileNameFormat&gt;
	 *    &lt;outputDirectory&gt;${project.build.directory}/dbunit&lt;/outputDirectory&gt;
	 *    &lt;exludeTables&gt;
	 *    	&lt;excludeTable&gt;&lt;/excludeTable&gt;
	 *    &lt;/excludeTables&gt;
	 * &lt;/exportDataSet&gt;
	 * </pre>
	 *
	 */
	@Parameter
	private ExportDataSet exportDataSet;

	@Parameter(property = "dbtools.format")
	private String format;

	@Override
	public RichExportDataSet getRichExportDataSet() {
		applyCommandLineArgs(exportDataSet);
		RichDriverConfig richDriverConfig = getRichDriverConfig();
		MavenContext mavenContext = getMavenContext();
		RichExportDataSet richExportDataSet = new RichExportDataSet(
				mavenContext, exportDataSet, richDriverConfig);
		return richExportDataSet;
	}

	private void applyCommandLineArgs(ExportDataSet exportDataSet2) {
		if (StringUtils.isNotBlank(format)) {
			exportDataSet.setFormat(format);
		}
	}

}
