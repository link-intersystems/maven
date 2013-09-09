package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import com.link_intersystems.maven.plugin.dbtools.RichDriverConfig;

public interface ExportParams {

	RichDriverConfig getRichDriverConfig();

	RichExportDataSet getRichExportDataSet();

}
