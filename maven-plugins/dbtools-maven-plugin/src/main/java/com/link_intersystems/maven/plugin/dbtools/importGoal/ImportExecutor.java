package com.link_intersystems.maven.plugin.dbtools.importGoal;

import java.sql.SQLException;
import java.util.Arrays;

import org.apache.maven.plugin.logging.Log;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import com.link_intersystems.maven.plugin.dbtools.DependencyAwareDataset;
import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;
import com.link_intersystems.maven.plugin.system.Goal;
import com.link_intersystems.maven.plugin.system.GoalExecutionException;
import com.link_intersystems.maven.plugin.system.MavenContext;

public class ImportExecutor implements Goal<ImportParams> {

	@Override
	public void execute(MavenContext mavenContext, ImportParams executionParams) {
		Log log = mavenContext.getLog();
		RichDriverConfig richDriverConfig = executionParams
				.getRichDriverConfig();
		RichImportDataSet richImportDataSet = executionParams
				.getRichImportDataset();

		IDataSet dataSet = richImportDataSet.getDataSet();
		log.info("Loaded dataset " + richImportDataSet.getDataSetFile());
		IDatabaseConnection databaseConnection = richDriverConfig
				.createConnection();
		log.info("Opened database connection " + richDriverConfig.getUrl());
		try {
			dataSet = new DependencyAwareDataset(dataSet, databaseConnection);
		} catch (DataSetException e1) {
			log.error(e1);
			throw new GoalExecutionException(e1.getMessage());
		} catch (SQLException e1) {
			log.error(e1);
			throw new GoalExecutionException(e1.getMessage());
		}
		DatabaseOperation databaseOperation = richImportDataSet
				.getImportOperation();
		try {
			log.info("Executing database operation "
					+ richImportDataSet.getImportOperationName()
					+ " table order: "
					+ Arrays.toString(dataSet.getTableNames()));
			databaseOperation.execute(databaseConnection, dataSet);
		} catch (Exception e) {
			throw new GoalExecutionException(
					"Error occured while importing dataset "
							+ richImportDataSet.toString(), e);
		}
	}

}
