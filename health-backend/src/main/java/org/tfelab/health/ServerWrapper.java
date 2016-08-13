package org.tfelab.health;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.Refacter;
import org.tfelab.health.route.UserRoute;

import static spark.Spark.port;
import static spark.Spark.staticFileLocation;

public class ServerWrapper {
	
	public static final Logger logger = LogManager.getLogger(ServerWrapper.class.getName());
	
	public ServerWrapper() {
		
	}
	
	public void run() {

		port(10010);

		staticFileLocation("/www");
		
		new UserRoute();
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
		new ServerWrapper().run();
	}

}
