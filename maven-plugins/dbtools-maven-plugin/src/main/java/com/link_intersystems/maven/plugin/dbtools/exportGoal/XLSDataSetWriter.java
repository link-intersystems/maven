package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import java.io.OutputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSetWriter;

public class XLSDataSetWriter extends TypedDataSetWriter<OutputStream> {

	@Override
	protected void write(IDataSet dataSet, OutputStream writeTarget)
			throws Exception {
		XlsDataSetWriter xlsDataSetWriter = new XlsDataSetWriter();
		xlsDataSetWriter.write(dataSet, writeTarget);
	}

}
