package org.tfelab.health;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.Refacter;
import org.tfelab.health.route.QueryRoute;
import org.tfelab.health.route.SectionRoute;
import org.tfelab.health.route.UserRoute;

import static spark.Spark.port;
import static spark.Spark.*;

public class ServiceWrapper {
	
	public static final Logger logger = LogManager.getLogger(ServiceWrapper.class.getName());
	
	public ServiceWrapper() {
		
	}
	
	public void run() {
		
		externalStaticFileLocation("www");
		//staticFileLocation("/www");
		
		port(10000);
		
		new UserRoute();
		new SectionRoute();
		new QueryRoute();
	}
	
	public static void initdb(){
		
		logger.info("Creating tables...");
		
		try {
			
			Refacter.dropTables("org.tfelab.health.model");
			Refacter.createTables("org.tfelab.health.model");
	
			logger.info("Create tables done.");
			
		} catch (Exception e) {
			logger.error("Error create tables.", e);
		}	
	}
	

	public static void main(String[] args) {
		//initdb();
		new ServiceWrapper().run();
	}

}