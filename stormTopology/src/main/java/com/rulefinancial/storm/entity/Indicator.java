package com.rulefinancial.storm.entity;

import java.sql.Date;
import java.sql.Time;


/**
 * Entity represents an indicator
 * 
 * Properties:
 *  - Instrument symbol
 *  - Indicator value
 *  - Type of indicator
 *  - Date and time when an indicator was calculated 
 *  
 * @author Piotr Gabara
 *
 */
public class Indicator {
	
	private String symbol;
	private double value;
	private IndicatorType indicatorType;
	private Date published;
	private Time time;
	
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public IndicatorType getIndicatorType() {
		return indicatorType;
	}
	
	public void setIndicatorType(IndicatorType indicatorType) {
		this.indicatorType = indicatorType;
	}

	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
}
