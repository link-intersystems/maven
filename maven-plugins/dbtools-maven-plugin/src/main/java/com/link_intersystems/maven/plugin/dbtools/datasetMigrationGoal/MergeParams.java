package com.link_intersystems.maven.plugin.dbtools.datasetMigrationGoal;

import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;
import com.link_intersystems.maven.plugin.system.GoalExecutionException;

public interface MergeParams {

	public RichMergeDataSet getRichMergeDataset() throws GoalExecutionException;

	public RichDriverConfig getRichDriverConfig();

	public RichFlyway getRichFlyway() throws GoalExecutionException;

}
