package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlWriter;

public class FlatXmlDataSetWriter implements DataSetWriter {

	public void write(IDataSet dataSet, WriteTarget writeTarget)
			throws DataSetException, IOException {
		OutputStream outputStream = writeTarget.getAdapter(OutputStream.class);
		try {
			FlatXmlWriter flatXmlWriter = new FlatXmlWriter(outputStream);
			flatXmlWriter.write(dataSet);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}
	}

}
