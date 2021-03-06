package com.link_intersystems.maven.plugin.system;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.PluginParameterExpressionEvaluator;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

public abstract class AbstractMavenContextMojo<T extends Goal<PARAMS>, PARAMS>
		extends AbstractMojo {

	/**
	 * The entry point to Aether, i.e. the component doing all the work.
	 */
	@Component
	private RepositorySystem repoSystem;

	/**
	 * The current repository/network configuration of Maven.
	 * 
	 */
	@Parameter(defaultValue = "${repositorySystemSession}")
	private RepositorySystemSession repoSession;

	/**
	 * The project's remote repositories to use for the resolution of plugins
	 * and their dependencies.
	 * 
	 */
	@Parameter(defaultValue = "${project.remotePluginRepositories}")
	private List<RemoteRepository> remoteRepos;

	@Parameter(property = "mojoExecution", readonly = true, required = true)
	private MojoExecution mojoExecution;

	/**
	 * The Maven Session.
	 */
	@Parameter(property = "session")
	private MavenSession mavenSession;

	/**
	 * 
	 * @parameter default-value="${project.build.directory}"
	 */
	private String targetFolder;

	private MavenContext mavenContext = new MavenContextImpl();

	protected MavenContext getMavenContext() {
		return mavenContext;
	}

	public final void execute() throws MojoExecutionException,
			MojoFailureException {
		PARAMS executionParams = getExecutionParams();
		try {
			T goal = getGoal();
			goal.execute(mavenContext, executionParams);
		} catch (InstantiationException e) {
			throw new MojoExecutionException(
					"Unable to instantiate the goal executor.", e);
		} catch (IllegalAccessException e) {
			throw new MojoExecutionException(
					"Unable to instantiate the goal executor.", e);
		} catch (GoalExecutionException e) {
			String fullQualifiedMojoGoal = getFullQualifiedMojoGoal();
			throw new MojoExecutionException("Execution of "
					+ fullQualifiedMojoGoal + " failed", e);
		}
	}

	private String getFullQualifiedMojoGoal() {
		return mojoExecution.getGroupId() + ":" + mojoExecution.getArtifactId()
				+ ":" + mojoExecution.getVersion() + ":"
				+ mojoExecution.getGoal();
	}

	/**
	 * 
	 * @return the {@link Goal} parameter object. If this
	 *         {@link AbstractMavenContextMojo} is an instance of the execution
	 *         parameter type than this {@link AbstractMavenContextMojo} is
	 *         returned.
	 * @throws IllegalStateException
	 *             if the execution parameter object can not be resolved
	 *             automatically. Please override and implement in this case.
	 */
	protected PARAMS getExecutionParams() {
		Class<PARAMS> paramsType = getTypeArgumentClass(1);
		if (paramsType.isInstance(this)) {
			return paramsType.cast(this);
		} else {
			throw new IllegalStateException("Either let " + getClass()
					+ " implement " + paramsType
					+ " or override getExecutionParams()");
		}
	}

	/**
	 * Resolves the {@link Goal} to execute in order to the type argument T.
	 * 
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected T getGoal() throws InstantiationException, IllegalAccessException {
		Class<T> goalExecutorClass = getTypeArgumentClass(0);
		return goalExecutorClass.newInstance();
	}

	@SuppressWarnings("unchecked")
	private <TYPE> Class<TYPE> getTypeArgumentClass(int index) {
		Class<?> class1 = getClass();
		ParameterizedType parameterizedType = (ParameterizedType) class1
				.getGenericSuperclass();
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
		Class<TYPE> type = (Class<TYPE>) actualTypeArguments[index];
		return type;
	}

	private class MavenContextImpl implements MavenContext {
		private ExpressionEvaluator expressionEvaluator;

		public List<RemoteRepository> getRemoteRepositories() {
			return remoteRepos;
		}

		public ExpressionEvaluator getExpressionEvaluator() {
			if (expressionEvaluator == null) {
				expressionEvaluator = new PluginParameterExpressionEvaluator(
						mavenSession, mojoExecution);
			}
			return expressionEvaluator;
		}

		public RepositorySystem getRepositorySystem() {
			return repoSystem;
		}

		public RepositorySystemSession getRepositorySystemSession() {
			return repoSession;
		}

		public MojoExecution getMojoExecution() {
			return mojoExecution;
		}

		public MavenSession getMavenSession() {
			return mavenSession;
		}

		public Log getLog() {
			return new ContextLogImpl(AbstractMavenContextMojo.this.getLog());
		}

		public ContextLog getContextLog(String context) {
			return new ContextLogImpl(AbstractMavenContextMojo.this.getLog(),
					context);
		}

		/**
		 * Returns a rich domain model of a {@link Dependency}.
		 * 
		 * @param dependency
		 * @return
		 */
		public RichDependency getRichDependency(Dependency dependency) {
			List<RemoteRepository> remoteRepos = getRemoteRepositories();
			RepositorySystem repoSystem = getRepositorySystem();
			RepositorySystemSession repoSession = getRepositorySystemSession();
			return new RichDependency(dependency, repoSystem, repoSession,
					remoteRepos);
		}

		public Server getServerSettings(String serverId) {
			MavenSession mavenSession = getMavenSession();
			Settings settings = mavenSession.getSettings();
			Server server = settings.getServer(serverId);
			return server;
		}

		public String getTargetFolder() {
			return targetFolder;
		}
	}

}
