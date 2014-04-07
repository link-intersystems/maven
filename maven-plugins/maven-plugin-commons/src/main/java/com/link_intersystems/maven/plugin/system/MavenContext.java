package com.link_intersystems.maven.plugin.system;

import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.settings.Server;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public interface MavenContext {

	public List<RemoteRepository> getRemoteRepositories();

	public RepositorySystem getRepositorySystem();

	public RepositorySystemSession getRepositorySystemSession();

	public Log getLog();

	MojoExecution getMojoExecution();

	MavenSession getMavenSession();

	String getTargetFolder();

	ExpressionEvaluator getExpressionEvaluator();

	RichDependency getRichDependency(Dependency dependency);

	Server getServerSettings(String serverId);

	ContextLog getContextLog(String context);


}
