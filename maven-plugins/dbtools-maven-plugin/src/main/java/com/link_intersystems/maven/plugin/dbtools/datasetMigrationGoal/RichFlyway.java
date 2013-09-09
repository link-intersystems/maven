package com.link_intersystems.maven.plugin.dbtools.datasetMigrationGoal;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationVersion;
import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;

/**
 * A thin wrapper around Flyway. Provides convenience methods for the migration
 * process.
 *
 */
public class RichFlyway {

	private MergeDataSet mergeDataSet;
	private Log log;
	private Flyway flyway;

	public RichFlyway(MergeDataSet mergeDataSet, Log log,
			RichDriverConfig richDriverConfig) throws MojoExecutionException {
		log.debug("constructing richFlyway instance");
		this.mergeDataSet = mergeDataSet;
		this.log = log;
		Flyway flyway = new Flyway();
		String url = richDriverConfig.getUrl();
		String username = richDriverConfig.getUsername();
		String password = richDriverConfig.getPassword();
		flyway.setDataSource(url, username, password);

		flyway.setSchemas(mergeDataSet.getSchemas());
		flyway.setLocations(mergeDataSet.getLocations());
		flyway.setSqlMigrationPrefix(mergeDataSet.getMigrationPrefix());

		this.flyway = flyway;
	}

	public void mergeToStartVersion() {
		String startVersionString = mergeDataSet.getFromVersion();
		MigrationVersion startVersion = new MigrationVersion(startVersionString);
		log.debug("clean ...");
		flyway.clean();
		log.debug("Migrating to " + startVersionString);
		flyway.setTarget(startVersion);
		flyway.migrate();
	}

	public void mergeToEndVersion() {
		String endVersionString = mergeDataSet.getToVersion();
		log.debug("Migrating to target version " + endVersionString);
		MigrationVersion endVersion = new MigrationVersion(endVersionString);
		flyway.setTarget(endVersion);
		flyway.migrate();
	}

	public void clean() {
		log.debug("cleaning up database");
		flyway.clean();
	}
}
