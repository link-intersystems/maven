package com.link_intersystems.maven.plugin.tools.svn;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.replicator.ISVNReplicationHandler;
import org.tmatesoft.svn.core.replicator.SVNRepositoryReplicator;

import com.link_intersystems.maven.plugin.system.GoalExecutor;
import com.link_intersystems.maven.plugin.system.MavenContext;

public class SvnCopyExecutor implements GoalExecutor<SvnCopyParams> {

	public void execute(final MavenContext mavenContext, SvnCopyParams executionParams)
			throws MojoExecutionException, MojoFailureException {

		RichSvnRepository targetRepository = executionParams
				.getTargetRepository();
		RichSvnRepository sourceRepository = executionParams
				.getSourceRepository();

		try {
			SVNRepository targetSvnRepository = targetRepository
					.getSVNRepository();
			SVNRepository sourceSvnRepository = sourceRepository
					.getSVNRepository();

			SVNRepositoryReplicator replicator = SVNRepositoryReplicator
					.newInstance();
			ISVNReplicationHandler isvnReplicationHandler = new ISVNReplicationHandler() {

				public void revisionReplicating(SVNRepositoryReplicator source,
						SVNLogEntry logEntry) throws SVNException {
					Log log = mavenContext.getLog();
					log.info("Start replicating revision " + logEntry.getRevision());
				}

				public void revisionReplicated(SVNRepositoryReplicator source,
						SVNCommitInfo commitInfo) throws SVNException {
					Log log = mavenContext.getLog();
					log.info("Finished replicating revision as " + commitInfo.getNewRevision());
				}

				public void checkCancelled() throws SVNCancelException {
				}
			};
			replicator.setReplicationHandler(isvnReplicationHandler);
			replicator.replicateRepository(sourceSvnRepository,
					targetSvnRepository, true);
		} catch (SVNException e) {
			throw new MojoExecutionException("Unable to replicate repository",
					e);
		}
	}
}
