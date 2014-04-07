package com.link_intersystems.maven.plugin.system;

import org.apache.maven.plugin.logging.Log;

class ContextLogImpl implements ContextLog {

	private Log log;
	private String context;

	ContextLogImpl(Log log) {
		this.log = log;
	}

	ContextLogImpl(Log log, String context) {
		this.log = log;
		this.context = context;
	}

	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	public void debug(CharSequence content) {
		log.debug(applyContext(content));
	}

	public void debug(CharSequence content, Throwable error) {
		log.debug(applyContext(content), error);
	}

	public void debug(Throwable error) {
		log.debug(applyContext(""), error);
	}

	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	public void info(CharSequence content) {
		log.info(applyContext(content));
	}

	private String applyContext(CharSequence content) {
		if (context == null) {
			return content.toString();
		} else {
			String contextLog = "[" + context + "] ";
			return contextLog + content.toString();
		}
	}

	public void info(CharSequence content, Throwable error) {
		log.info(applyContext(content), error);
	}

	public void info(Throwable error) {
		log.info(applyContext(""), error);
	}

	public boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	public void warn(CharSequence content) {
		log.warn(applyContext(content));
	}

	public void warn(CharSequence content, Throwable error) {
		log.warn(applyContext(content), error);
	}

	public void warn(Throwable error) {
		log.warn(applyContext(""), error);
	}

	public boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	public void error(CharSequence content) {
		log.error(applyContext(content));
	}

	public void error(CharSequence content, Throwable error) {
		log.error(applyContext(content), error);
	}

	public void error(Throwable error) {
		log.error(applyContext(""), error);
	}

	public ContextLog getContextLog(String context) {
		return new ContextLogImpl(this, context);
	}

}
