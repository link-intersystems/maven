package com.link_intersystems.maven.plugin.dbtools.importGoal;

import java.io.File;

import com.link_intersystems.maven.plugin.dbtools.DataSet;

public class ImportDataSet extends DataSet {

	private File file;

	private String databaseOperation;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getDatabaseOperation() {
		return databaseOperation;
	}

	public void setDatabaseOperation(String databaseOperation) {
		this.databaseOperation = databaseOperation;
	}

}
