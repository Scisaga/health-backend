package org.tfelab.health;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.tfelab.common.config.Configs;
import org.tfelab.common.db.Refacter;
import org.tfelab.health.model.DoctorComment;
import org.tfelab.health.model.DoctorService;
import org.tfelab.health.route.QueryRoute;
import org.tfelab.health.route.SectionRoute;
import org.tfelab.health.route.UserRoute;
import org.tfelab.health.route.CommentRoute;//new
import org.tfelab.health.route.DoctorRoute;

import static spark.Spark.port;

import java.util.Set;

import static spark.Spark.*;

public class ServiceWrapper {
	
	public static final Logger logger = LogManager.getLogger(ServiceWrapper.class.getName());
	
	public int port = 8010;
	
	/**
	 * 
	 */
	public ServiceWrapper() {
		
		port = Configs.dev.getInt("port");
		staticFiles.externalLocation("www");
		port(port);
		
		/**
		 * Using Reflection load routes
		 */
		Reflections reflections = new Reflections("org.tfelab.health.route", new SubTypesScanner(false));
		Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
		
		for(Class<? extends Object> clazz : allClasses) {
			if(clazz.getSimpleName().matches(".+?Route")) {
				logger.info("Add {}.", clazz.getSimpleName());
				try {
					clazz.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					logger.error(e);
					System.exit(1);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	public static void initdb(){
		
		logger.info("Creating tables...");
		
		try {
			
			//Refacter.dropTables("org.tfelab.health.model");
			Refacter.createTables("org.tfelab.health.model");
	
			logger.info("Create tables done.");
			
		} catch (Exception e) {
			logger.error("Error create tables.", e);
		}	
	}
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//initdb();
		new ServiceWrapper();
	}

}
