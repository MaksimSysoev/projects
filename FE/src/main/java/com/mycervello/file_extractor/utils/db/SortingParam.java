package com.mycervello.file_extractor.utils.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycervello.file_extractor.utils.datatype.StringUtils;


public class SortingParam {
	
	//
	//Constructors
	//
	public SortingParam() {}
	
	public SortingParam(String field) {
		this.field = field;
		this.orderCode = ORDER_ASC;
	}
	
	public SortingParam(String field, int orderCode) {
		this.field = field;
		this.orderCode = orderCode;
	}
	//
	
	//
	//Constants
	//
	public static final int ORDER_ASC = 0;
	public static final int ORDER_DESC = 1;
	//
	
	//
	//Variables
	//
	private String field;
	private Integer orderCode;
	//
	
	//
	//Properties
	//
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Integer getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(Integer orderCode) {
		this.orderCode = orderCode;
	}
	//
	
	//
	//Public methods
	//
	@JsonIgnore
	public boolean isCorrect() {
		return (StringUtils.isNotEmpty(this.field));
	}
	//
}