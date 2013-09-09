package com.link_intersystems.maven.plugin.system;

public interface PropertyEditorComponent<T> {

	public T parseProperty(String property) throws PropertyParseException;
}
