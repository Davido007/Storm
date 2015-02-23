package com.rulefinancial;

import java.sql.Date;
import java.sql.Time;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This class represent object from DB
 * @author dkmiecik
 * @version 1.0
 */
@SuppressWarnings("serial")
@Entity
//@Table(name="table0")
public class Feeds implements java.io.Serializable{
	
	@Id
	@GeneratedValue
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
	
	/**
	 * This constructor create empty object
	 */
	public Feeds(){
		
	}
	
	/**
	 * This constructor create object with all info
	 * @param symbol
	 * @param date
	 * @param stockOpen
	 * @param stockHigh
	 * @param stockLow
	 * @param stockClose
	 * @param stockVolume
	 * @param stockOpenInt
	 * @param time
	 */
	public Feeds(String symbol, Date date, 
			double stockOpen, double stockHigh, double stockLow,
			double stockClose, double stockVolume, double stockOpenInt, Time time) {
		super();
		this.symbol = symbol;
		this.date = date;
		this.time = time;
		this.stockOpen = stockOpen;
		this.stockHigh = stockHigh;
		this.stockLow = stockLow;
		this.stockClose = stockClose;
		this.stockVolume = stockVolume;
		this.stockOpenInt = stockOpenInt;
	}



	public int getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(int keyColumn) {
		this.keyColumn = keyColumn;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public double getStockOpen() {
		return stockOpen;
	}


	public void setStockOpen(double stockOpen) {
		this.stockOpen = stockOpen;
	}


	public double getStockHigh() {
		return stockHigh;
	}


	public void setStockHigh(double stockHigh) {
		this.stockHigh = stockHigh;
	}


	public double getStockLow() {
		return stockLow;
	}


	public void setStockLow(double stockLow) {
		this.stockLow = stockLow;
	}


	public double getStockClose() {
		return stockClose;
	}


	public void setStockClose(double stockClose) {
		this.stockClose = stockClose;
	}


	public double getStockVolume() {
		return stockVolume;
	}


	public void setStockVolume(double stockVolume) {
		this.stockVolume = stockVolume;
	}


	public double getStockOpenInt() {
		return stockOpenInt;
	}


	public void setStockOpenInt(double stockOpenInt) {
		this.stockOpenInt = stockOpenInt;
	}


	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	



	
	

}
