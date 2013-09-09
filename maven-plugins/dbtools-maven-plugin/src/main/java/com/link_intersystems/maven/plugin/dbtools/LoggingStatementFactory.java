package com.link_intersystems.maven.plugin.dbtools;

import java.sql.SQLException;

import org.apache.maven.plugin.logging.Log;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IBatchStatement;
import org.dbunit.database.statement.IPreparedBatchStatement;
import org.dbunit.database.statement.IStatementFactory;

public class LoggingStatementFactory implements IStatementFactory{

	private IStatementFactory statementFactory;
	private Log log;

	public LoggingStatementFactory(IStatementFactory statementFactory, Log log) {
		this.statementFactory = statementFactory;
		this.log = log;
	}

	public IBatchStatement createBatchStatement(IDatabaseConnection connection)
			throws SQLException {
		return statementFactory.createBatchStatement(connection);
	}

	public IPreparedBatchStatement createPreparedBatchStatement(String sql,
			IDatabaseConnection connection) throws SQLException {
		if (log.isDebugEnabled()) {
			log.debug("Executing: " + sql);
		}
		return statementFactory.createPreparedBatchStatement(sql, connection);
	}

}
