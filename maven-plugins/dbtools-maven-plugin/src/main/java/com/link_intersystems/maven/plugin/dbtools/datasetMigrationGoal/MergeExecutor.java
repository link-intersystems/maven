package com.link_intersystems.maven.plugin.dbtools.datasetMigrationGoal;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import com.link_intersystems.maven.plugin.dbtools.DependencyAwareDataset;
import com.link_intersystems.maven.plugin.dbtools.exportGoal.DataSetWriter;
import com.link_intersystems.maven.plugin.dbtools.exportGoal.FileWriteTarget;
import com.link_intersystems.maven.plugin.system.GoalExecutor;
import com.link_intersystems.maven.plugin.system.MavenContext;

public class MergeExecutor implements GoalExecutor<MergeParams> {

	@Override
	public void execute(MavenContext mavenContext, MergeParams executionParams)
			throws MojoExecutionException, MojoFailureException {
		Log log = mavenContext.getLog();

		RichMergeDataSet richMergeDataset = executionParams
				.getRichMergeDataset();

		MergeDataSet mergeDataSet = richMergeDataset.getMergeDataSet();

		File destinationDirectory = new File(mergeDataSet.getInputDirectory());
		String extension = StringUtils.lowerCase(mergeDataSet.getFormat());
		String path = destinationDirectory.getAbsolutePath();

		log.debug(MessageFormat.format(
				"looking for files in directory {0} and with extension {1}",
				path, extension));

		String[] files = FileUtils.getFilesFromExtension(path,
				new String[] { extension });

		if (files.length == 0) {
			try {
				throw new MojoExecutionException(
						"No files found in directory: "
								+ destinationDirectory.getCanonicalPath());
			} catch (IOException e) {
				throw new MojoExecutionException(
						"Unable to access path of directory", e);
			}
		}

		for (String file : Arrays.asList(files)) {
			log.info("--------------------------------------------");
			log.info("About to migrate file: " + file);

			try {
				IDataSet dataSet = richMergeDataset.getDataSet(file);
				IDatabaseConnection databaseConnection = executionParams
						.getRichDriverConfig().createConnection();
				RichFlyway richFlyway = executionParams.getRichFlyway();
				richFlyway.mergeToStartVersion();

				DependencyAwareDataset dependencyAwareDataset = new DependencyAwareDataset(
						dataSet, databaseConnection);

				log.info("Loading data from file into database");
				DatabaseOperation insert = DatabaseOperation.INSERT;
				log.info("Executing import database operation "
						+ Arrays.toString(dependencyAwareDataset
								.getTableNames()));
				insert.execute(databaseConnection, dependencyAwareDataset);

				richFlyway.mergeToEndVersion();

				log.info("exporting migrated dataset and writing back to target file");
				FileWriteTarget fileWriteTarget = new FileWriteTarget(new File(
						file));
				DataSetWriter writer = richMergeDataset.createDataSetWriter();
				IDataSet exportDataSet = executionParams.getRichMergeDataset()
						.getDataSet();
				writer.write(exportDataSet, fileWriteTarget);

				richFlyway.clean();

				log.info("done");
			} catch (DataSetException e) {
				throw new MojoExecutionException(e.getMessage());
			} catch (IOException e) {
				throw new MojoExecutionException(e.getMessage());
			} catch (Exception e) {
				throw new MojoExecutionException(e.getMessage());
			}
		}
	}
}
