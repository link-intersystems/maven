package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.dbunit.dataset.IDataSet;

import com.link_intersystems.maven.plugin.dbtools.DataSet.Format;
import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;
import com.link_intersystems.maven.plugin.system.Goal;
import com.link_intersystems.maven.plugin.system.GoalExecutionException;
import com.link_intersystems.maven.plugin.system.MavenContext;

public class ExportExecutor implements Goal<ExportParams> {

	@Override
	public void execute(MavenContext mavenContext, ExportParams executionParams)
			throws GoalExecutionException {
		RichDriverConfig richDriverConfig = executionParams
				.getRichDriverConfig();
		RichExportDataSet richExportDataSet = executionParams
				.getRichExportDataSet();

		IDataSet fullDataSet = richExportDataSet.createDataSet();
		try {
			DataSetWriter dataSetWriter = richExportDataSet
					.createDataSetWriter();
			WriteTarget writeTarget = richExportDataSet.getWriteTarget();
			try {
				dataSetWriter.write(fullDataSet, writeTarget);
				Log log = mavenContext.getLog();
				String[] tableNames = fullDataSet.getTableNames();
				StringBuilder messageBuilder = new StringBuilder();
				messageBuilder.append("Exported tables");
				messageBuilder.append("\n");
				Iterator<String> tableNamesIter = Arrays.asList(tableNames)
						.iterator();
				while (tableNamesIter.hasNext()) {
					String tableName = tableNamesIter.next();
					messageBuilder.append("  - ");
					messageBuilder.append(tableName);
					if (tableNamesIter.hasNext()) {
						messageBuilder.append("\n");
					}
				}
				messageBuilder.append("\n");
				messageBuilder.append("to ");
				messageBuilder.append(writeTarget.toString());
				log.info(messageBuilder);
			} catch (IOException e) {
				Format format = richExportDataSet.getFormat();
				String message = format(
						"Unable to create data set of type {0} from db connection {1}",
						format, richDriverConfig.getUrl());
				throw new MojoExecutionException(message, e);
			}
		} catch (Exception e) {
			Format format = richExportDataSet.getFormat();
			String message = format(
					"Error occured while creating data set of type {0}", format);
			throw new GoalExecutionException(message, e);
		}
	}
}
