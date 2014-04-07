package com.link_intersystems.maven.plugin.tools.svn;

import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.link_intersystems.maven.plugin.system.AbstractMavenContextMojo;
import com.link_intersystems.maven.plugin.system.PropertyEditor;

@Mojo(name = "repo-copy", requiresDependencyCollection = ResolutionScope.COMPILE, requiresDependencyResolution = ResolutionScope.RUNTIME, configurator = "editorSupport")
public class SvnRepoCopyMojo extends
		AbstractMavenContextMojo<SvnRepositoryCopyExecutor, SvnRepositoryCopyParams> implements
		SvnRepositoryCopyParams {

	@Parameter
	@PropertyEditor(propertyEditorClass = SvnRepositoryPropertyEditorComponent.class)
	private SvnRepository sourceRepository;

	@Parameter
	@PropertyEditor(propertyEditorClass = SvnRepositoryPropertyEditorComponent.class)
	private SvnRepository targetRepository;

	public RichSvnRepository getSourceRepository() {
		return new RichSvnRepository(getMavenContext(), sourceRepository);
	}

	public RichSvnRepository getTargetRepository() {
		return new RichSvnRepository(getMavenContext(), targetRepository);
	}

}
