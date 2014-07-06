package com.link_intersystems.maven.plugin.tools.svn;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.replicator.ISVNReplicationHandler;
import org.tmatesoft.svn.core.replicator.SVNRepositoryReplicator;

import com.link_intersystems.maven.plugin.system.ContextLog;
import com.link_intersystems.maven.plugin.system.Goal;
import com.link_intersystems.maven.plugin.system.GoalExecutionException;
import com.link_intersystems.maven.plugin.system.MavenContext;

public class SvnRepositoryCopyExecutor implements Goal<SvnRepositoryCopyParams> {

	public void execute(MavenContext mavenContext,
			SvnRepositoryCopyParams executionParams) {
		RichSvnRepository sourceRepository = executionParams
				.getSourceRepository();
		RichSvnRepository targetRepository = executionParams
				.getTargetRepository();

		try {
			SVNRepository targetSvnRepository = targetRepository
					.getSVNRepository();
			SVNRepository sourceSvnRepository = sourceRepository
					.getSVNRepository();

			targetRepository.checkAvailability();
			sourceRepository.checkAvailability();

			SVNRepositoryReplicator replicator = SVNRepositoryReplicator
					.newInstance();
			ISVNReplicationHandler isvnReplicationHandler = new SvnReplicationMavenLogger(
					mavenContext);
			replicator.setReplicationHandler(isvnReplicationHandler);
			replicator.replicateRepository(sourceSvnRepository,
					targetSvnRepository, true);
		} catch (SVNException e) {
			throw new GoalExecutionException("Unable to replicate repository",
					e);
		}
	}

	private static class SvnReplicationMavenLogger implements
			ISVNReplicationHandler {

		private ContextLog svnLog;

		public SvnReplicationMavenLogger(MavenContext mavenContext) {
			svnLog = mavenContext.getContextLog("SVN COPY");
		}

		public void revisionReplicating(SVNRepositoryReplicator source,
				SVNLogEntry logEntry) throws SVNException {
			svnLog.info("Start replicating revision " + logEntry.getRevision());
		}

		public void revisionReplicated(SVNRepositoryReplicator source,
				SVNCommitInfo commitInfo) throws SVNException {
			svnLog.info("Finished replicating revision as "
					+ commitInfo.getNewRevision());
		}

		public void checkCancelled() throws SVNCancelException {
		}

	}
}
