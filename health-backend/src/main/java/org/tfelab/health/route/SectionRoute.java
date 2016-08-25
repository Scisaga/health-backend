package org.tfelab.health.route;

import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.get;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.OrmLiteDaoManager;
import org.tfelab.common.json.JSON;
import org.tfelab.health.ServiceWrapper;
import org.tfelab.health.model.DoctorService;
import org.tfelab.health.model.Section;
import org.tfelab.health.model.User;
import org.tfelab.io.Msg;

import com.j256.ormlite.dao.Dao;

public class SectionRoute {
	
	public static final Logger logger = LogManager.getLogger(SectionRoute.class.getName());
	
	public SectionRoute() {
		
		
		/**
		 * 
		 */
		get("/sections", (request, response) -> {

			try {
				
				Dao<Section, String> dao = OrmLiteDaoManager.getDao(Section.class);
				
				List<Section> sections = dao.queryForAll();
				
				Map result = new TreeMap<Integer, String>();
				
				for(Section section : sections) {
					result.put(section.id, section.name);
				}
				
				return new Msg<Map>(Msg.SUCCESS, result);
					
			} catch (Exception e) {
				logger.error("Error get sections.", e);
				return new Msg<>(Msg.FAILURE);
			}
		} , new MsgTransformer());
		
		get("/services", (request, response) -> {

			try {
				
				Dao<DoctorService, String> dao = OrmLiteDaoManager.getDao(DoctorService.class);
				
				List<DoctorService> services = dao.queryBuilder()
					    .distinct().selectColumns("name").query();
				
				List result = new ArrayList<String>();
				
				for(DoctorService service : services) {
					result.add(service.name);
				}
				
				return new Msg<List>(Msg.SUCCESS, result);
					
			} catch (Exception e) {
				logger.error("Error get sections.", e);
				return new Msg<>(Msg.FAILURE);
			}
		} , new MsgTransformer());
		
	}
}
