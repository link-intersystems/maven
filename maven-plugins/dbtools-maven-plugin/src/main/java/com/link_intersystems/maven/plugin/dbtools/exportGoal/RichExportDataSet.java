package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import static java.text.MessageFormat.format;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.search.TablesDependencyHelper;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;

import com.link_intersystems.maven.plugin.dbtools.DataSet;
import com.link_intersystems.maven.plugin.dbtools.DataSet.Format;
import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;
import com.link_intersystems.maven.plugin.system.MavenContext;

public class RichExportDataSet {

	private MavenContext mavenContext;
	private ExportDataSet exportDataSet;
	private RichDriverConfig richDriverConfig;

	public RichExportDataSet(MavenContext mavenContext,
			ExportDataSet exportDataSet, RichDriverConfig richDriverConfig) {
		this.mavenContext = mavenContext;
		this.exportDataSet = exportDataSet;
		this.richDriverConfig = richDriverConfig;
	}

	public DataSetWriter createDataSetWriter() {
		Format format = getFormat();
		DataSetWriter dataSetWriter = null;
		switch (format) {
		case XLS:
			dataSetWriter = new XLSDataSetWriter();
			break;
		case FLAT:
			dataSetWriter = new FlatXmlDataSetWriter();
			break;
		case XML:
			dataSetWriter = new XMLDataSetWriter();
			break;
		case CSV:
			dataSetWriter = new CSVDataSetWriter();
			break;
		}
		return dataSetWriter;
	}

	public WriteTarget getWriteTarget() throws MojoExecutionException {
		String output = exportDataSet.getOutputDirectory();
		if (output == null) {
			String targetFolder = mavenContext.getTargetFolder();
			output = targetFolder + File.separator + "dbunit";
		}
		File outputDirectory = new File(output);
		outputDirectory.mkdirs();

		String outputFileNameFormat = exportDataSet.getOutputFileNameFormat();
		if (outputFileNameFormat == null) {
			outputFileNameFormat = "{0}_{2}-{3}.{1}";
		}

		String filename = formatFileName(outputFileNameFormat);
		File outputFile = new File(outputDirectory, filename);
		FileWriteTarget fileWriteTarget = new FileWriteTarget(outputFile);
		return fileWriteTarget;
	}

	private String formatFileName(String outputFileNameFormat)
			throws MojoExecutionException {
		String arg0 = richDriverConfig.getSchema();
		if (StringUtils.isBlank(arg0)) {
			arg0 = richDriverConfig.getHostname();
		}
		String arg1 = getFormat().getFileExtension();
		Date date = new Date();
		java.text.Format dateFormat = new SimpleDateFormat("ddMMyy");
		String arg2 = dateFormat.format(date);
		java.text.Format timeFormat = new SimpleDateFormat("HHmmss");
		String arg3 = timeFormat.format(date);
		String filename = format(outputFileNameFormat, arg0, arg1, arg2, arg3);
		return filename;
	}

	public IDataSet createDataSet() throws MojoExecutionException {
		IDatabaseConnection connection = richDriverConfig.createConnection();
		try {
			String[] dependentTables = exportDataSet.getDependentTables();
			IDataSet dataSet = null;
			if (dependentTables == null) {
				dataSet = connection.createDataSet();
			} else {
				String[] depTableNames = TablesDependencyHelper
						.getAllDependentTables(connection, dependentTables);
				dataSet = connection.createDataSet(depTableNames);
			}

			String[] tableNames = dataSet.getTableNames();
			List<String> filteredTableNameList = new ArrayList<String>(
					Arrays.asList(tableNames));

			String[] excludeTables = exportDataSet.getExcludeTables();
			filteredTableNameList.removeAll(Arrays.asList(excludeTables));

			String[] filteredTableNames = (String[]) filteredTableNameList
					.toArray(new String[filteredTableNameList.size()]);

			FilteredDataSet filteredDataSet = new FilteredDataSet(
					filteredTableNames, dataSet);
			return filteredDataSet;
		} catch (Exception e) {
			try {
				connection.close();
			} catch (SQLException e1) {
			}
			throw new MojoExecutionException("Unable to create data set", e);
		}
	}

	public Format getFormat() {
		String formatStr = exportDataSet.getFormat();
		Format format = DataSet.Format.valueOf(formatStr);
		return format;
	}
}
