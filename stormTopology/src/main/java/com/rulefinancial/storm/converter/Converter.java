package com.rulefinancial.storm.converter;

/**
 * Interface converts json type to a T type object
 * 
 * @author Piotr Gabara
 *
 * @param <T>	Object type that You want to fetch from jeson
 */
public abstract interface Converter<T> {
  
	/**
	 * Method converts json to a T type object
	 * @param json JSON value
	 * @return	T object, which represent specific json
	 */
	T convertJsonTo(String json);
	
}
