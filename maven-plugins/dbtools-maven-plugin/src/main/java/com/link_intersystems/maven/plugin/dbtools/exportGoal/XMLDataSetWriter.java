package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import java.io.Writer;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSetWriter;

public class XMLDataSetWriter extends TypedDataSetWriter<Writer> {

	@Override
	protected void write(IDataSet dataSet, Writer writeTarget) throws Exception {
		XmlDataSetWriter xmlDataSetWriter = new XmlDataSetWriter(writeTarget);
		xmlDataSetWriter.write(dataSet);
	}

}
