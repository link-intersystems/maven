package com.link_intersystems.maven.plugin.dbtools;

import java.sql.SQLException;

import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableIterator;

public class DependencyAwareDataset extends AbstractDataSet {

	private IDataSet dataSet;

	public DependencyAwareDataset(IDataSet dataSet,
			IDatabaseConnection databaseConnection) throws DataSetException,
			SQLException {
		DatabaseSequenceFilter databaseSequenceFilter = new DatabaseSequenceFilter(
				databaseConnection);
		this.dataSet = new FilteredDataSet(databaseSequenceFilter, dataSet);

	}

	@Override
	protected ITableIterator createIterator(boolean reversed)
			throws DataSetException {
		return dataSet.iterator();
	}

}
