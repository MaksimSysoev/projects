package com.mycervello.file_extractor.utils.db;

import java.util.Collection;

import org.springframework.transaction.TransactionStatus;


public interface JdbcHelper {
	
	TransactionStatus createTrx(String name);
	
	void executeInTrx(String transactionName, ITrxAction action);
	void executeInTrx_Unsafe(String transactionName, IUnsafeTrxAction action) throws Exception;
	void executeInTrxAndRollback_Unsafe(String transactionName, IUnsafeTrxAction action)
		throws Exception;
	
	Object createSqlArray(Collection<?> collectionOfValues, SqlDataType itemDataType);
	Object createSqlStringArray(Collection<?> collectionOfValues);
	
	
	@FunctionalInterface
	interface ITrxAction {
		void execute();
	}
	
	@FunctionalInterface
	interface IUnsafeTrxAction {
		void execute() throws Exception;
	}
}