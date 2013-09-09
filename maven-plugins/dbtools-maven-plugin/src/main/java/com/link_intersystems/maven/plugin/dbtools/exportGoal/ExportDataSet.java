package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import org.apache.maven.plugins.annotations.Parameter;

import com.link_intersystems.maven.plugin.dbtools.DataSet;

public class ExportDataSet extends DataSet {

	@Parameter(defaultValue = "${project.build.directory}/datasets/export")
	private String outputDirectory;

	private String outputFileNameFormat;

	private String[] dependentTables;

	public String getOutputFileNameFormat() {
		return outputFileNameFormat;
	}

	public void setOutputFileNameFormat(String outputFileNameFormat) {
		this.outputFileNameFormat = outputFileNameFormat;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String output) {
		this.outputDirectory = output;
	}

	public String[] getDependentTables() {
		return dependentTables;
	}

	public void setDependentTables(String[] dependentTable) {
		this.dependentTables = dependentTable;
	}

}
