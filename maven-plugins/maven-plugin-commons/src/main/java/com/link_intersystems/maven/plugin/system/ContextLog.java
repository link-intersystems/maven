package com.link_intersystems.maven.plugin.system;

import org.apache.maven.plugin.logging.Log;

public interface ContextLog extends Log {

	public ContextLog getContextLog(String context);
}
