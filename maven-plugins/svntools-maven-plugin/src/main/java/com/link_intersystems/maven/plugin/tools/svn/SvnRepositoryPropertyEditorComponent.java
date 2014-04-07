package com.link_intersystems.maven.plugin.tools.svn;

import com.link_intersystems.maven.plugin.system.PropertyEditorComponent;
import com.link_intersystems.maven.plugin.system.PropertyParseException;

public class SvnRepositoryPropertyEditorComponent implements
		PropertyEditorComponent<SvnRepository> {

	@Override
	public SvnRepository parseProperty(String property)
			throws PropertyParseException {
		String[] svnRepositoryParts = property.split("::");
		if(svnRepositoryParts.length != 2){
			throw new PropertyParseException(property, "expected format is AUTHENTICATION_ID::SVN_URL");
		}
		String authenticationId = svnRepositoryParts[0];
		String svnUrl = svnRepositoryParts[1];
		SvnRepository svnRepository = new SvnRepository();
		svnRepository.setAuthenticationId(authenticationId);
		svnRepository.setUrl(svnUrl);
		return svnRepository;
	}

}
