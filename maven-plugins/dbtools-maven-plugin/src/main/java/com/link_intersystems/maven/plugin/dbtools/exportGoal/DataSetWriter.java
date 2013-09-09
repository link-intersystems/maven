package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import org.dbunit.dataset.IDataSet;

public interface DataSetWriter {

	void write(IDataSet dataSet, WriteTarget writeTarget) throws Exception;

}
