package com.link_intersystems.maven.plugin.tools.svn;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.settings.Server;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.core.wc.admin.SVNAdminClient;

import com.link_intersystems.maven.plugin.system.MavenContext;

public class RichSvnRepository {

	private static final String SVN_REPO_SPEC_PART_DIVIDER = "::";
	private SvnRepository svnRepository;
	private MavenContext mavenContext;
	private SVNAdminClient adminClient;
	private SVNRepository svnKitRepository;

	RichSvnRepository(MavenContext mavenContext, SvnRepository svnRepository) {
		this.mavenContext = mavenContext;
		this.svnRepository = svnRepository;
	}

	public SVNAdminClient getAdminClient() {
		if (adminClient == null) {
			String authenticationId = svnRepository.getAuthenticationId();
			if (!StringUtils.isBlank(authenticationId)) {

			}
			Server serverSettings = mavenContext
					.getServerSettings(authenticationId);
			String username = serverSettings.getUsername();
			String password = serverSettings.getPassword();

			DefaultSVNOptions defaultSVNOptions = new DefaultSVNOptions();
			SVNClientManager clientManager = SVNClientManager.newInstance(
					defaultSVNOptions, username, password);

			adminClient = clientManager.getAdminClient();
		}
		return adminClient;
	}

	public SVNRepository getSVNRepository() throws SVNException {
		if (svnKitRepository == null) {
			SVNURL url = SVNURL.parseURIEncoded(svnRepository.getUrl());
			SVNRepository svnKitRepository = SVNRepositoryFactory.create(url,
					null);

			applyAuthenticationIfAny(svnKitRepository);

			this.svnKitRepository = svnKitRepository;
		}
		return svnKitRepository;

	}

	private void applyAuthenticationIfAny(SVNRepository svnKitRepository) {
		String authenticationId = svnRepository.getAuthenticationId();
		ISVNAuthenticationManager authManager = null;
		if (StringUtils.isNotBlank(authenticationId)) {
			Server serverSettings = mavenContext
					.getServerSettings(authenticationId);
			String username = serverSettings.getUsername();
			String password = serverSettings.getPassword();
			authManager = SVNWCUtil.createDefaultAuthenticationManager(
					username, password);
			svnKitRepository.setAuthenticationManager(authManager);
		}
	}

	public void checkAvailability() throws MojoExecutionException {
		try {
			SVNRepository svnRepository = getSVNRepository();
			svnRepository.testConnection();
		} catch (SVNException e) {
			String url = svnRepository.getUrl();
			if (StringUtils.isBlank(url)) {
				throw new MojoExecutionException(
						"Repository definitions must have a valid url: " + url,
						e);
			}
			throw new MojoExecutionException(
					"Repository "
							+ toString()
							+ " can not be accessed. Does it exist? In case "
							+ "of an file url you should consider to create the repository "
							+ "via the svnadmin tool: svnadmin create <REPOSITORY_NAME>",
					e);
		}

	}

	@Override
	public String toString() {
		StringBuilder toString = new StringBuilder();
		toString.append(svnRepository.getAuthenticationId());
		toString.append(SVN_REPO_SPEC_PART_DIVIDER);
		toString.append(svnRepository.getUrl());
		return toString.toString();
	}
}
