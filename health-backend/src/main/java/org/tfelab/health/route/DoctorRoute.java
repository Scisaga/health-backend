package org.tfelab.health.route;

import static spark.Spark.post;
import static spark.Spark.get;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.db.PooledDataSource;
import org.tfelab.common.json.JSON;
import org.tfelab.common.json.JSONable;
import org.tfelab.health.model.Doctor;
import org.tfelab.health.model.DoctorComment;
import org.tfelab.health.model.DoctorService;
import org.tfelab.health.model.User;
import org.tfelab.io.Msg;

public class DoctorRoute {

	public static final Logger logger = LogManager.getLogger(CommentRoute.class.getName());

	public DoctorRoute() {

		get("/doctors/:id", (request, response) -> {

			try {
				
				int id = Integer.valueOf(request.params(":id"));
				
				Doctor doctor = Doctor.getDoctorById(id);
				
				if(doctor != null) {
					return new Msg<Doctor>(Msg.SUCCESS, doctor);
				}
				else {
					return new Msg<>(Msg.OBJECT_NOT_FOUND);
				}
			} catch (Exception e) {
				logger.error("Error create user.", e);
				return new Msg<>(Msg.FAILURE);
			}
			
		} , new MsgTransformer());

	}
}
