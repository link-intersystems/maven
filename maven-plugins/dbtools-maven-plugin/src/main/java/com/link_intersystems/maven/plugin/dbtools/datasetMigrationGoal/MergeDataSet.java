package com.link_intersystems.maven.plugin.dbtools.datasetMigrationGoal;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.link_intersystems.maven.plugin.dbtools.DataSet;

public class MergeDataSet extends DataSet {

	private String outputDirectory;

	private String inputDirectory;

	private String[] schemas;

	private String[] locations;

	private String mergeFromVersion;

	private String mergeToVersion;

	private String migrationPrefix;

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public String getInputDirectory() {
		return inputDirectory;
	}

	public void setInputDirectory(String file) {
		this.inputDirectory = file;
	}

	public String[] getSchemas() {
		return schemas;
	}

	public void setSchemas(String[] schemas) {
		this.schemas = schemas;
	}

	public String getMigrationPrefix() {
		return migrationPrefix;
	}

	public void setMigrationPrefix(String migrationPrefix) {
		this.migrationPrefix = migrationPrefix;
	}

	public String getFromVersion() {
		return mergeFromVersion;
	}

	public void setFromVersion(String fromVersion) {
		this.mergeFromVersion = fromVersion;
	}

	public String getToVersion() {
		return mergeToVersion;
	}

	public void setToVersion(String toVersion) {
		this.mergeToVersion = toVersion;
	}

	public void validate() {
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String[] getLocations() {
		return locations;
	}

	public void setLocations(String[] locations) {
		this.locations = locations;
	}

}