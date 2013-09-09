package com.link_intersystems.maven.plugin.system;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public interface GoalExecutor<PARAMS> {

	public void execute(MavenContext mavenContext, PARAMS executionParams) throws MojoExecutionException,
			MojoFailureException;
}
