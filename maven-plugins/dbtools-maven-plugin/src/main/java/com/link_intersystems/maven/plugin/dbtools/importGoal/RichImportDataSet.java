package com.link_intersystems.maven.plugin.dbtools.importGoal;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.operation.CompositeOperation;
import org.dbunit.operation.DatabaseOperation;

import com.link_intersystems.maven.plugin.dbtools.DataSet.Format;
import com.link_intersystems.maven.plugin.system.GoalParameterException;

public class RichImportDataSet {

	public enum DatabaseOps {
		INSERT(DatabaseOperation.INSERT), CLEAN_INSERT(
				DatabaseOperation.CLEAN_INSERT), TRUNCATE_INSERT(
				new CompositeOperation(DatabaseOperation.TRUNCATE_TABLE,
						DatabaseOperation.INSERT));

		private DatabaseOperation databaseOperation;

		private DatabaseOps(DatabaseOperation databaseOperation) {
			this.databaseOperation = databaseOperation;
		}

		public DatabaseOperation getDatabaseOperation() {
			return databaseOperation;
		}
	}

	private ImportDataSet importDataSet;

	public RichImportDataSet(ImportDataSet importDataSet) {
		this.importDataSet = importDataSet;
	}

	public IDataSet getDataSet() throws GoalParameterException {
		File file = importDataSet.getFile();
		Format format = getFormat();
		IDataSet dataSet = null;
		try {
			switch (format) {
			case XLS:
				dataSet = new XlsDataSet(file);
				break;
			case XML:
				dataSet = new XmlDataSet(new FileInputStream(file));
				break;
			case FLAT:
				FlatXmlDataSetBuilder flatXmlDataSetBuilder = new FlatXmlDataSetBuilder();
				dataSet = flatXmlDataSetBuilder.build(file);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			throw new GoalParameterException("Unable to create data set.", e);
		}
		if (dataSet == null) {
			throw new GoalParameterException("Dataset support for format "
					+ format + " is not implemented");
		}

		return dataSet;
	}

	public Format getFormat() throws GoalParameterException {
		String formatString = importDataSet.getFormat();
		Format format;
		if (StringUtils.isBlank(formatString)) {
			format = detectFormatUsingFileExtension();
		} else {
			format = Format.valueOf(formatString);
		}
		return format;
	}

	private Format detectFormatUsingFileExtension()
			throws GoalParameterException {
		File file = importDataSet.getFile();
		if (file == null) {
			throw new GoalParameterException(
					"Unable to detect data set format through file extension, because no file is configured. "
							+ "Please set a dataset file through command line or maven config");
		}
		String fileString = file.toString();
		String fileExtension = StringUtils.substringAfterLast(fileString, ".");
		Format[] values = Format.values();
		List<Format> formatCandidates = new ArrayList<Format>();
		for (Format currentFormat : values) {
			if (currentFormat.getFileExtension().equals(fileExtension)) {
				formatCandidates.add(currentFormat);
			}
		}

		if (formatCandidates.isEmpty()) {
			throw new GoalParameterException(
					"Unable to detect data set format for file extension '"
							+ fileExtension + "'. Please configure a format.");
		}
		if (formatCandidates.size() > 1) {
			throw new GoalParameterException(
					"Unable to detect data set format through file extension. Multiple formats match the file extension "
							+ fileExtension
							+ ". Matching formats are "
							+ formatCandidates + ". Please configure a format.");
		}
		Format format = formatCandidates.get(0);
		return format;
	}

	public DatabaseOperation getImportOperation() {
		DatabaseOps databaseOps = getImportDatabaseOps();
		DatabaseOperation selectedDatabaseOperation = databaseOps
				.getDatabaseOperation();
		return selectedDatabaseOperation;
	}

	public String getImportOperationName() {
		return getImportDatabaseOps().name();
	}

	private DatabaseOps getImportDatabaseOps() {
		String databaseOperation = importDataSet.getDatabaseOperation();
		DatabaseOps databaseOps = null;
		if (databaseOperation == null) {
			databaseOps = DatabaseOps.CLEAN_INSERT;
		} else {
			databaseOps = DatabaseOps.valueOf(databaseOperation);
		}
		return databaseOps;
	}

	@Override
	public String toString() {
		try {
			return "DataSet[format=" + getFormat() + ", file="
					+ importDataSet.getFile() + "]";
		} catch (GoalParameterException e) {
			return "DataSet[file=" + importDataSet.getFile() + "]";
		}
	}

	public String getDataSetFile() {
		return importDataSet.getFile().toString();
	}

	public ImportDataSet getImportDataSet() {
		return importDataSet;
	}
}
