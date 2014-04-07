package com.link_intersystems.maven.plugin.tools.svn;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.link_intersystems.maven.plugin.system.AbstractMavenContextMojo;

@Mojo(name = "repo-copy", requiresDependencyCollection = ResolutionScope.COMPILE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class SvnRepoCopyMojo extends
		AbstractMavenContextMojo<SvnCopyExecutor, SvnCopyParams> implements
		SvnCopyParams {
	@Parameter
	private SvnRepository sourceRepository;
	@Parameter
	private SvnRepository targetRepository;

	public RichSvnRepository getSourceRepository() {
		return new RichSvnRepository(getMavenContext(), sourceRepository);
	}

	public RichSvnRepository getTargetRepository() {
		return new RichSvnRepository(getMavenContext(), targetRepository);
	}

}
