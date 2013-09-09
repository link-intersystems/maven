package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import java.io.File;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSetWriter;

public class CSVDataSetWriter extends TypedDataSetWriter<File> {

	@Override
	protected void write(IDataSet dataSet, File writeTarget) throws Exception {
		CsvDataSetWriter csvDataSetWriter = new CsvDataSetWriter(writeTarget);
		csvDataSetWriter.write(dataSet);
	}

}
