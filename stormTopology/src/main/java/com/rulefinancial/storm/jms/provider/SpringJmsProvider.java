package com.rulefinancial.storm.jms.provider;

import backtype.storm.contrib.jms.JmsProvider;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Class configures jms connection factory and destinantion
 * 
 * @author Piotr Gabara
 *
 */
public class SpringJmsProvider implements JmsProvider {
	
	private static final long serialVersionUID = -6683072987096352129L;
  
	private ConnectionFactory connectionFactory;
	private Destination destination;
  
	/**
	 * Constructor injects instances of ConnectionFactory and Destination from Spring application context
	 * @param springApplicationConextPath	path to Spring Xml application context
	 * @param connectionFactoryBean	the name of bean that represent ConnectionFactory instance
	 * @param destinationBean	the name of bean that represent Destination instance
	 */
	public SpringJmsProvider(String springApplicationConextPath, String connectionFactoryBean, String destinationBean) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext(springApplicationConextPath);
    
		this.connectionFactory = (ConnectionFactory) context.getBean(connectionFactoryBean);
		this.destination = (Destination) context.getBean(destinationBean);
    
		((ConfigurableApplicationContext)context).close();
		
	}
  
	/**
	 * Method returns ConnectionFactory instance
	 */
	public ConnectionFactory connectionFactory() throws Exception {
		return this.connectionFactory;
	}
  
	/**
	 * Method returns Destination instance
	 */
	public Destination destination() throws Exception {
		return this.destination;
	}
	
}
