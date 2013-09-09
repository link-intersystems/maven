package com.link_intersystems.maven.plugin.dbtools.datasetMigrationGoal;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;

import com.link_intersystems.maven.plugin.dbtools.DataSet.Format;
import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;
import com.link_intersystems.maven.plugin.dbtools.exportGoal.CSVDataSetWriter;
import com.link_intersystems.maven.plugin.dbtools.exportGoal.DataSetWriter;
import com.link_intersystems.maven.plugin.dbtools.exportGoal.FlatXmlDataSetWriter;
import com.link_intersystems.maven.plugin.dbtools.exportGoal.XLSDataSetWriter;
import com.link_intersystems.maven.plugin.dbtools.exportGoal.XMLDataSetWriter;
import com.link_intersystems.maven.plugin.system.MavenContext;

public class RichMergeDataSet {

	private MavenContext mavenContext;
	private MergeDataSet mergeDataSet;
	private RichDriverConfig richDriverConfig;

	public RichMergeDataSet(MavenContext mavenContext,
			MergeDataSet mergeDataSet, RichDriverConfig richDriverConfig) {
		this.mavenContext = mavenContext;
		this.mergeDataSet = mergeDataSet;
		this.richDriverConfig = richDriverConfig;
	}

	public IDataSet getDataSet() throws MojoExecutionException {
		IDatabaseConnection connection = richDriverConfig.createConnection();
		FilteredDataSet filteredDataSet = null;
		Log log = mavenContext.getLog();
		try {
			IDataSet dataSet = connection.createDataSet();
			String[] tableNames = dataSet.getTableNames();
			List<String> filteredTableNameList = new ArrayList<String>(
					Arrays.asList(tableNames));
			String[] excludeTables = mergeDataSet.getExcludeTables();
			log.debug("excluded tables: " + Arrays.toString(excludeTables));
			filteredTableNameList.removeAll(Arrays.asList(excludeTables));

			String[] filteredTableNames = filteredTableNameList
					.toArray(new String[filteredTableNameList.size()]);
			filteredDataSet = new FilteredDataSet(filteredTableNames, dataSet);

		} catch (SQLException e) {
			try {
				connection.close();
			} catch (SQLException e1) {
				// IGNORE on purpose
			}
			throw new MojoExecutionException("Unable to create data set", e);
		} catch (DataSetException e) {
			throw new MojoExecutionException("Unable to create data set", e);
		}

		return filteredDataSet;
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

	public IDataSet getDataSet(String fileName) throws MojoExecutionException {
		File file = new File(fileName);
		Format format = getFormat();
		IDataSet dataSet = null;
		try {
			switch (format) {
			case XLS:
				dataSet = new XlsDataSet(file);
				break;
			case XML:
				dataSet = new XmlDataSet(new FileInputStream(file));
				break;
			case FLAT:
				FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
				dataSet = flatXmlDataSetBuilder.build(file);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw new MojoExecutionException("Unable to create data set.", e);
		}
		if (dataSet == null) {
			throw new MojoExecutionException("Dataset support for format "
					+ format + " is not implemented");
		}

		return dataSet;
	}

	public Format getFormat() {
		String formatString = mergeDataSet.getFormat();
		return Format.valueOf(formatString);
	}

	public MergeDataSet getMergeDataSet() {
		return mergeDataSet;
	}

	public MavenContext getMavenContext() {
		return mavenContext;
	}

	public RichDriverConfig getRichDriverConfig() {
		return richDriverConfig;
	}
}