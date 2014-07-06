package com.link_intersystems.maven.plugin.dbtools.datasetMigrationGoal;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.link_intersystems.maven.plugin.dbtools.AbstractDBUnitMojo;
import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;
import com.link_intersystems.maven.plugin.system.GoalExecutionException;
import com.link_intersystems.maven.plugin.system.MavenContext;

@Mojo(name = "merge", requiresDependencyCollection = ResolutionScope.COMPILE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class MergeMojo extends AbstractDBUnitMojo<MergeExecutor, MergeParams>
		implements MergeParams {

	/**
	 * The merge data set configuration<br/>
	 * Config example<br/>
	 *
	 * <pre>
	 * 	<mergeDataSet>
	 * 	<format>XLS</format>
	 * 	<outputDirectory>${project.build.directory}/migrated_datasets</outputDirectory>
	 * 	<inputDirectory>${basedir}/src/test/resources/datasets</inputDirectory>
	 * 		<schemas>
	 * 			<schema>
	 * 				${connection.schema}
	 * 			</schema>
	 * 		</schemas>
	 * 		<mergeFromVersion>01.1</mergeFromVersion>
	 * 		<mergeToVersion>02.1</mergeToVersion>
	 * 		<migrationPrefix>V</migrationPrefix>
	 * 		<locations>
	 * 			<location>filesystem:src/main/resources/db/migrations</location>
	 * 		</locations>
	 * 		<excludeTables>
	 * 			<excludeTable>SCHEMA_VERSION</excludeTable>
	 * 		</excludeTables>
	 * 	</mergeDataSet>
	 *
	 * </pre>
	 *
	 */
	@Parameter
	private MergeDataSet mergeDataSet;

	@Override
	public RichMergeDataSet getRichMergeDataset() throws GoalExecutionException {
		MavenContext mavenContext = getMavenContext();
		Log log = mavenContext.getLog();

		if (mergeDataSet == null) {
			throw new GoalExecutionException(
					"You must provide a configuration for this mojo");
		}
		mergeDataSet.validate();
		log.debug("Configuration provided:");
		log.debug(mergeDataSet.toString());
		RichDriverConfig richDriverConfig = getRichDriverConfig();

		RichMergeDataSet richMergeDataSet = new RichMergeDataSet(mavenContext,
				mergeDataSet, richDriverConfig);
		return richMergeDataSet;
	}

	@Override
	public RichFlyway getRichFlyway() throws GoalExecutionException {
		return new RichFlyway(mergeDataSet, getMavenContext().getLog(),
				getRichDriverConfig());
	}

}
