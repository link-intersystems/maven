package com.link_intersystems.maven.plugin.dbtools.datasetMigrationGoal;

import org.apache.maven.plugin.MojoExecutionException;

import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;

public interface MergeParams {

	public RichMergeDataSet getRichMergeDataset() throws MojoExecutionException;

	public RichDriverConfig getRichDriverConfig();

	public RichFlyway getRichFlyway() throws MojoExecutionException;

}
