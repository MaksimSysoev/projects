package com.mycervello.file_extractor.utils.db;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mycervello.file_extractor.utils.exception.AppRuntimeException;


/**
 * This class provides utilities to work with JDBC-framework. It's designed as a service and
 * uses DB-resources.
 * 
 * @author Gennadiy Pervukhin
 */
@Service("jdbcHelper")
public class JdbcHelperImpl implements JdbcHelper
{
	//
	//Variables
	//
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	//
	
	//
	//Private methods
	//
	private Array createSqlArray(String sqlTypeName, Object[] values) {
		DataSource dataSource = null;
		Connection connection = null;
		try {
			dataSource = jdbcTemplate.getDataSource();
			connection = dataSource.getConnection();
			return connection.createArrayOf(sqlTypeName, values);
		}
		catch (SQLException e) {
			throw new AppRuntimeException(e);
		}
		finally {
			DataSourceUtils.releaseConnection(connection, dataSource);
		}
	}
	//
	
	//
	//Public methods
	//
	@Override
	public TransactionStatus createTrx(String name) {
		DefaultTransactionDefinition transactionInfo = new DefaultTransactionDefinition();
		transactionInfo.setName(name);
		return transactionManager.getTransaction(transactionInfo);
	}
	
	@Override
	public void executeInTrx(String transactionName, ITrxAction action) {
		try {
			executeInTrx_Unsafe(transactionName, action::execute);
		}
		catch (Exception error) {
			throw (error instanceof RuntimeException)
				? (RuntimeException) error : new AppRuntimeException(error);
		}
	}
	
	@Override
	public void executeInTrx_Unsafe(String transactionName, IUnsafeTrxAction action)
		throws Exception
	{
		TransactionStatus transaction = this.createTrx(transactionName);
		try {
			//- execute the action
			action.execute();
			//- commit the transaction if no exceptions have been thrown
			transactionManager.commit(transaction);
		}
		catch (Exception error) {
			//- roll back the transaction to close it
			transactionManager.rollback(transaction);
			//- re-throw the exception to let the calling code know what happened
			throw error;
		}
	}
	
	@Override
	public void executeInTrxAndRollback_Unsafe(String transactionName, IUnsafeTrxAction action)
		throws Exception
	{
		TransactionStatus transaction = this.createTrx(transactionName);
		try {
			//- execute the action
			action.execute();
		}
		finally {
			//- roll back the transaction to close it
			transactionManager.rollback(transaction);
		}
	}
	
	@Override
	public Object createSqlArray(Collection<?> collectionOfValues, SqlDataType itemDataType) {
		
		//- determine SQL-data type of items in the collection
		String sqlTypeName;
		if (itemDataType == SqlDataType.STRING) {
			sqlTypeName = "varchar";
		}
		else if (itemDataType == SqlDataType.INTEGER) {
			sqlTypeName = "integer";
		}
		else {
			throw new IllegalArgumentException("The app does not know how to work with "
				+ "collections of " + itemDataType + " data type.");
		}
		
		return createSqlArray(sqlTypeName, collectionOfValues.toArray());
	}
	
	@Override
	public Object createSqlStringArray(Collection<?> collectionOfValues) {
		return this.createSqlArray(collectionOfValues, SqlDataType.STRING);
	}
	//
}