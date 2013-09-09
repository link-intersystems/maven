package com.link_intersystems.maven.plugin.dbtools.exportGoal;

import static org.apache.commons.lang3.Validate.notNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class FileWriteTarget implements WriteTarget {

	private File file;

	public FileWriteTarget(File file) {
		notNull(file, "file must not be null");
		this.file = file;
	}

	@Override
	public <T> T getAdapter(Class<T> adapterClass) {
		if (File.class.equals(adapterClass)) {
			return adapterClass.cast(file);
		} else if (OutputStream.class.equals(adapterClass)) {
			ensureFileNotADirecotry(adapterClass);
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				return adapterClass.cast(fileOutputStream);
			} catch (FileNotFoundException e) {
				throw new IllegalStateException("Can not adapt file " + file
						+ " to " + adapterClass, e);
			}
		} else if (Writer.class.isAssignableFrom(adapterClass)) {
			OutputStream outputStream = getAdapter(OutputStream.class);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
					outputStream);
			return adapterClass.cast(outputStreamWriter);
		}
		throw new UnsupportedOperationException("WriteTarget "
				+ getClass().getSimpleName() + " does not support adaption to "
				+ adapterClass);
	}

	private <T> void ensureFileNotADirecotry(Class<T> adapterClass) {
		if (file.isDirectory()) {
			throw new UnsupportedOperationException("Can not adapt a "
					+ getClass().getSimpleName() + " to " + adapterClass
					+ ", because file " + file + " is a directory.");
		}
	}

	@Override
	public String toString() {
		return file.toString();
	}
}
