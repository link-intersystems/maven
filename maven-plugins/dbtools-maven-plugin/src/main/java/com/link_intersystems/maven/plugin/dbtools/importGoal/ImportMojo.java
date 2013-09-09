package com.link_intersystems.maven.plugin.dbtools.importGoal;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.link_intersystems.maven.plugin.dbtools.AbstractDBUnitMojo;

@Mojo(name = "import")
public class ImportMojo extends
		AbstractDBUnitMojo<ImportExecutor, ImportParams> implements
		ImportParams {

	/**
	 * The export data set configuration<br/>
	 * Config example<br/>
	 *
	 * <pre>
	 * <importDataSet>
	 *    <format>XLS</format>
	 *    <!-- 0 = Schema name if configured. Otherwise jdbc url hostname -->
	 *    <!-- 1 = Format in lower case -->
	 *    <!-- 2 = Current date in format ddMMyy -->
	 *    <!-- 3 = Current time in format HHmmss-->
	 *    <!-- default value is {0}_{2}-{3}.{1} -->
	 *    <outputFileNameFormat>export-{0}_{2}-{3}.{1}</outputFileNameFormat>
	 *    <outputDirectory>${project.build.directory}/dbunit</outputDirectory>
	 *    <exludeTables>
	 *    	<excludeTable></excludeTable>
	 *    </excludeTables>
	 * </importDataSet>
	 * </pre>
	 *
	 */
	@Parameter
	private ImportDataSet importDataSet;

	/**
	 * For command line only. Overrides xml config.
	 */
	@Parameter(property = "dataSetFile")
	private String dataSetFile;

	/**
	 * For command line only. Overrides xml config.
	 */
	@Parameter(property = "dbOperation")
	private String dbOperation;

	@Override
	public RichImportDataSet getRichImportDataset() {
		if (importDataSet == null) {
			importDataSet = new ImportDataSet();
		}

		if (StringUtils.isNotBlank(dataSetFile)) {
			importDataSet.setFile(new File(dataSetFile));
		}
		if (StringUtils.isNotBlank(dbOperation)) {
			importDataSet.setDatabaseOperation(dbOperation);
		}

		RichImportDataSet richImportDataSet = new RichImportDataSet(
				importDataSet);
		return richImportDataSet;
	}

}
