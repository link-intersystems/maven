package com.link_intersystems.maven.plugin.dbtools.exportGoal;

/**
 * Decouples the target of a {@link DataSetWriter}, because some writers write
 * to files, others to directories and others to databases.
 *
 *
 */
public interface WriteTarget {

	<T> T getAdapter(Class<T> adapterClass);

}
