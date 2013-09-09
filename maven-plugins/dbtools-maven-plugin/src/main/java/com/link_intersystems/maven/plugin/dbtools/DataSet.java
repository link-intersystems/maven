package com.link_intersystems.maven.plugin.dbtools;

public class DataSet {

	public enum Format {
		XLS("xls"), XML("xml"), FLAT("xml"), CSV("");

		private String fileExtension;

		private Format(String fileExtension) {
			this.fileExtension = fileExtension;
		}

		public String getFileExtension() {
			return fileExtension;
		}
	}

	private String format;
	private String[] excludeTables;

	public DataSet() {
		super();
	}

	public String[] getExcludeTables() {
		return excludeTables;
	}

	public void setExcludeTables(String[] excludeTables) {
		this.excludeTables = excludeTables;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}