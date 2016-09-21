package org.tfelab.health.route;

import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.get;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.stream.events.Comment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.OrmLiteDaoManager;
import org.tfelab.common.json.JSON;
import org.tfelab.common.json.JSONable;
import org.tfelab.health.ServiceWrapper;
import org.tfelab.health.model.Doctor;
import org.tfelab.health.model.DoctorComment;
import org.tfelab.health.model.DoctorService;
import org.tfelab.health.model.Hospital;
import org.tfelab.health.model.Section;
import org.tfelab.health.model.User;
import org.tfelab.io.Msg;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

public class CommentRoute {
	
	public static final Logger logger = LogManager.getLogger(CommentRoute.class.getName());

    public CommentRoute() {
	   
	   post("/comments", (request, response) -> {

			try {
				
				DoctorComment comment = JSON.fromJSON(request.queryParams("_q"), DoctorComment.class);
				
				if (comment.insert()) {
					return new Msg<Integer>(Msg.INSERT_SUCCESS, comment.id);
				} else {
					return new Msg<>(Msg.INSERT_FAILURE);
				}
				
			} catch (Exception e) {
				logger.error("Error create user.", e);
				return new Msg<>(Msg.FAILURE);
			}
			
			
		}, new MsgTransformer());
	   
	   get("/comments", (request, response) -> {

			try {
				
				Dao<DoctorComment, String> dao = OrmLiteDaoManager.getDao(DoctorComment.class);
					
			    List<DoctorComment> comments = dao.queryForAll();
				
			    List result = new ArrayList<String>();
				
				for(DoctorComment comment : comments) {
					result.add(comment.content);
					result.add(comment.rating);
					result.add(comment.disease);
				}
				
				return new Msg<List>(Msg.SUCCESS, result);
					
			} catch (Exception e) {
				logger.error("Error get comments.", e);
				return new Msg<>(Msg.FAILURE);
			}
		} , new MsgTransformer());
		
		
		
		
	}
   static List<DoctorService> getDoctorsDetail(Query query) throws Exception{
	  
	   Dao<Doctor, String> doctorDao = OrmLiteDaoManager.getDao(Doctor.class);
	   
	   QueryBuilder<Doctor, String> qb = doctorDao.queryBuilder();
	  
	   Where<Doctor, String> where = qb.where();
	   
	   
	   //return result;
   }
   
   public static void main(String[] args){
	   
	   
	   
   }
   class Query implements JSONable<DoctorService> {
		
		public Object doctor_id;
		int user_id;
        
		long offset = 0;
		long limit = 1;
		
		@Override
		public String toJSON() {
			
			return JSON.toJSON(this);
		}
	}

}
