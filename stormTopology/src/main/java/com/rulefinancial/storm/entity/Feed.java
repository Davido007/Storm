package com.rulefinancial.storm.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Feed implements Serializable {
	
	private static final long serialVersionUID = 4463886464638323452L;
	
	private int keyColumn;
	private String symbol;
	private Date date;
	private Time time;
	private double stockOpen;
	private double stockHigh;
	private double stockLow;
	private double stockClose;
	private double stockVolume;
	private double stockOpenInt;
	  
	public int getKeyColumn() {
		return this.keyColumn;
	}
  
	public void setKeyColumn(int keyColumn) {
		this.keyColumn = keyColumn;
	}
  
	public String getSymbol() {
		return this.symbol;
	}
  
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
  
	public Date getDate() {
		return this.date;
	}
  
	public void setDate(Date date) {
		this.date = date;
	}
  
	public Time getTime() {
		return this.time;
	}
  
	public void setTime(Time time) {
		this.time = time;
	}
  
	public double getStockOpen() {
		return this.stockOpen;
	}
  
	public void setStockOpen(double stockOpen) {
		this.stockOpen = stockOpen;
	}
  
	public double getStockHigh() {
		return this.stockHigh;
	}
  
	public void setStockHigh(double stockHigh) {
		this.stockHigh = stockHigh;
	}
  
	public double getStockLow() {
		return this.stockLow;
	}
  
	public void setStockLow(double stockLow) {
		this.stockLow = stockLow;
	}
  
	public double getStockClose() {
		return this.stockClose;
	}
  
	public void setStockClose(double stockClose) {
		this.stockClose = stockClose;
	}
  
	public double getStockVolume() {
		return this.stockVolume;
	}
  
	public void setStockVolume(double stockVolume) {
		this.stockVolume = stockVolume;
	}
  
	public double getStockOpenInt() {
		return this.stockOpenInt;
	}
  
	public void setStockOpenInt(double stockOpenInt) {
		this.stockOpenInt = stockOpenInt;
	}
  
	public String toString() {
		return "[FEED:" + this.keyColumn + "] SYMBOL: " + this.symbol;
	}
	
}
