package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import java.io.Closeable;
import java.lang.reflect.ParameterizedType;

import org.dbunit.dataset.IDataSet;

public abstract class TypedDataSetWriter<WRITE_TARGET_TYPE> implements
		DataSetWriter {

	@SuppressWarnings({ "unchecked" })
	@Override
	public final void write(IDataSet dataSet, WriteTarget writeTarget)
			throws Exception {
		Class<?> currentClass = getClass();
		ParameterizedType genericSuperclass = (ParameterizedType) currentClass
				.getGenericSuperclass();
		Class<?> writeTargetType = (Class<?>) genericSuperclass
				.getActualTypeArguments()[0];
		WRITE_TARGET_TYPE writeTargetAdapter = (WRITE_TARGET_TYPE) writeTarget
				.getAdapter(writeTargetType);
		try {
			write(dataSet, writeTargetAdapter);
		} finally {
			if (Closeable.class.isInstance(writeTargetAdapter)) {
				Closeable closeable = Closeable.class.cast(writeTargetAdapter);
				closeable.close();
			}
		}

	}

	protected abstract void write(IDataSet dataSet,
			WRITE_TARGET_TYPE writeTarget) throws Exception;

}
