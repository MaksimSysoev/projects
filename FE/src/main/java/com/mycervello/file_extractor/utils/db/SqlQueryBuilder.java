package com.mycervello.file_extractor.utils.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.mycervello.file_extractor.utils.datatype.StringUtils;
import com.mycervello.file_extractor.utils.datatype.collection.CollectionUtils;
import com.mycervello.file_extractor.utils.datatype.collection.LinkedCaseInsensitiveSet;

public class SqlQueryBuilder extends AbstractQueryBuilder {
	
	//
	//Constructors
	//
	public SqlQueryBuilder() {
		super();
	}
	
	public SqlQueryBuilder(String schema, String objectName) {
		super(schema, objectName);
	}
	
	public SqlQueryBuilder(String objectName) {
		super(null, objectName);
	}
	//
	
	//
	//Constants
	//
	private static final String BASIC_SQL_QUERY = "SELECT %s FROM %s";
	private static final String ORDER_BY_ITEMS_SEPARATOR = ",";
	private static final String FIELDS_SEPARATOR = ",";
	
	//public static final SortingParam DEFAULT_SORTING_PARAM = new SortingParam("id", SortingParam.ORDER_DESC);
	//
	
	//
	//Variables
	//
	private LinkedCaseInsensitiveSet fields = new LinkedCaseInsensitiveSet();
	private List<SortingParam> sortingParams = new ArrayList<>();
	private List<String> groupByExps = new ArrayList<>();
	private int resultLimit = -1;
	private int offset;
	//
	
	//
	//Properties
	//
	public LinkedCaseInsensitiveSet getFields() {
		return fields;
	}
	public void setFields(Set<String> fields) {
		this.fields = new LinkedCaseInsensitiveSet(fields);
	}
	
	public List<SortingParam> getSortingParams() {
		return sortingParams;
	}
	
	public int getResultLimit() {
		return resultLimit;
	}
	public void setResultLimit(int resultLimit) {
		this.resultLimit = resultLimit;
	}
	
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	//
	
	//
	//Private methods
	//
	private String buildOrderByClause() {
		List<String> orderByClauseItems = new ArrayList<String>();
//		if (this.sortingParams == null) {
//			this.sortingParams = new ArrayList<SortingParam>();
//		}
//		
//		if (this.sortingParams.isEmpty()) {
//			this.sortingParams.add(DEFAULT_SORTING_PARAM);
//		}
		if (this.sortingParams != null) {
			for (SortingParam param : this.sortingParams) {
				String orderByClauseItem = buildOrderByClauseItem(param);
				if (orderByClauseItem != null) {
					orderByClauseItems.add(orderByClauseItem);
				}
			}
		}
		
		return joinSortingItems(orderByClauseItems);
	}
	//
	
	//
	//Private static methods
	//
	private static String buildOrderByClauseItem(SortingParam param) {
		if (param == null || !param.isCorrect()) {
			return null;
		}
		
		String directionString =
			(param.getOrderCode() == SortingParam.ORDER_DESC ? "DESC" : "ASC");
		return (param.getField() + " " + directionString);
	}
	//
	
	//
	//Public methods
	//
	public void addField(String newField) {
		if (StringUtils.isNotEmpty(newField)) {
			this.fields.add(newField);	
		}
	}
	
	public void addFields(String... newFields) {
		this.addFields(Arrays.asList(newFields));
	}
	
	public void addFields(Collection<String> fields) {
		if (fields != null) {	
			for (String field : fields) {
				this.addField(field);
			}
		}
	}
	/*
	public void addFields(String[] fields) {
		if (fields != null) {
			this.fields.addAll(Arrays.asList(fields));
		}
	}
	*/
	
	public void addGroupBy(String groupByExpression) {
		this.groupByExps.add(groupByExpression);
	}
	
	public void removeFields(Collection<String> fieldsToRemove) {
		if (this.fields != null && fieldsToRemove != null) {
			this.fields.removeAll(fieldsToRemove);
		}
	}
	
	
	public void addSortingParam(SortingParam newParam) {
		if (newParam != null) {
			this.sortingParams.add(newParam);
		}
	}
	
	public void addSortingParams(List<SortingParam> newParams) {
		if (newParams != null) {
			this.sortingParams.addAll(newParams);
		}
	}
	
	public String build() {
		
		//- check parameters
		if (StringUtils.isEmpty(super.objectName)) {
			return null;
		}
		
		//- build a basic query
		String fullObjectName = (StringUtils.isEmpty(super.schema) ? "" : (super.schema + "."))
			+ super.objectName;
		String query = String.format(BASIC_SQL_QUERY, joinFields(this.fields), fullObjectName);
		
		//- add WHERE clause
		String fullSqlCondition = super.joinConditions();
		if (StringUtils.isNotEmpty(fullSqlCondition)) {
			query += " WHERE " + fullSqlCondition;
		}
		
		//- add GROUP BY clause
		if (CollectionUtils.isNotEmpty(this.groupByExps)) {
			String groupByClause = String.join(FIELDS_SEPARATOR, this.groupByExps);
			query += " GROUP BY " + groupByClause;
		}
		
		//- add ORDER BY clause
		String orderByClause = this.buildOrderByClause();
		if (StringUtils.isNotEmpty(orderByClause)) {
			query += " ORDER BY " + orderByClause;
		}
		
		//- add LIMIT clause
		if (this.resultLimit >= 0) {
			query += " LIMIT " + this.resultLimit;
		}
		
		//- add OFFSET clause
		if (this.offset > 0) {
			query += " OFFSET " + this.offset;
		}
		
		return query;
	}
	//
	
	
	//
	//Public static methods
	//
	public static String joinFields(Collection<String> fields) {
		return (CollectionUtils.isEmpty(fields) ? "*" : String.join(FIELDS_SEPARATOR, fields));
	}
	
	public static String joinFields(String... fields) {
		return (fields.length == 0 ? "*" : String.join(FIELDS_SEPARATOR, fields));
	}
	
	public static String joinSortingItems(Collection<String> items) {
		return (CollectionUtils.isEmpty(items) ? "" : String.join(ORDER_BY_ITEMS_SEPARATOR, items));
	}
	//
}