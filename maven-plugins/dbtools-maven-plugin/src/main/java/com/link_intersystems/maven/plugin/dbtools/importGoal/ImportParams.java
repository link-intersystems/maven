package com.link_intersystems.maven.plugin.dbtools.importGoal;

import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;


public interface ImportParams {

	public RichImportDataSet getRichImportDataset();

	public RichDriverConfig getRichDriverConfig();
}
