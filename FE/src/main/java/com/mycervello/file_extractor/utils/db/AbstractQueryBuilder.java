package com.mycervello.file_extractor.utils.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mycervello.file_extractor.utils.datatype.StringUtils;
import com.mycervello.file_extractor.utils.datatype.collection.CollectionUtils;

public abstract class AbstractQueryBuilder {

	//
	//Constructors
	//
	public AbstractQueryBuilder() {
	}
	
	public AbstractQueryBuilder(String schema, String objectName) {
		this();
		this.schema = schema;
		this.objectName = objectName;
	}
	
	/*public AbstractQueryBuilder(SearchRequest request) {
		this(request.getSchemaName(), request.getObjectName());
	}*/
	//
	
	//
	//Constants
	//
	public static final int LOGIC_OP_AND = 0;
	public static final int LOGIC_OP_OR = 1;
	//
	
	//
	//Variables
	//
	protected String schema;
	protected String objectName;
	protected List<String> conditions = new ArrayList<String>();
	protected int logicOpForConditions = 0;
	//
	
	//
	//Properties
	//
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public int getLogicOpForConditions() {
		return logicOpForConditions;
	}
	public void setLogicOpForConditions(int logicOpForConditions) {
		this.logicOpForConditions = logicOpForConditions;
	}
	
	public List<String> getConditions() {
		return conditions;
	}
	//
	
	//
	//Private methods
	//
	private static String prepareSubCondition(String condition, int logicOperator) {
		if (logicOperator == LOGIC_OP_OR) {
			return condition;
		}
		
		//add parentheses around the sub-condition, if AND-operator is used for joining of
		//conditions and the passed sub-condition contains OR-operator
		String upperCaseCondition = condition.toUpperCase();
		if (upperCaseCondition.contains(" OR ") || upperCaseCondition.contains(")OR(")) {
			condition = "(" + condition + ")";
		}
		return condition;
	}
	//
	
	//
	//Protected methods
	//
	protected static String getLogicOperatorString(int logicOperator) {
		return (logicOperator == LOGIC_OP_OR ? " OR " : " AND ");
	}
	
	protected String joinConditions() {
		return joinConditions(this.conditions, this.logicOpForConditions);
	}
	//
	
	//
	//Public methods
	//
	public void addCondition(String newCondition) {
		if (!StringUtils.isEmpty(newCondition)) {
			this.conditions.add(newCondition);
		}
	}
	
	public void addConditions(List<String> newConditions) {
		if (newConditions != null) {
			this.conditions.addAll(newConditions);
		}
	}
	//
	
	//
	//Public static methods
	//
	public static String joinConditions(Collection<String> conditions, int logicOperator) {
		if (CollectionUtils.isEmpty(conditions)) {
			return null;
		}
		
		//- prepare sub-conditions
		List<String> preparedConditions = new ArrayList<String>();
		for (String condition : conditions) {
			if (StringUtils.isNotEmpty(condition)) {
				preparedConditions.add(prepareSubCondition(condition, logicOperator));
			}
		}
		
		//- join sub-conditions into a string
		return String.join(getLogicOperatorString(logicOperator), preparedConditions);
	}
	//
}