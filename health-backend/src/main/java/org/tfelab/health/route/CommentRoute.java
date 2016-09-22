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
import org.tfelab.health.model.DoctorComment;
import org.tfelab.health.model.DoctorService;
import org.tfelab.io.Msg;

public class CommentRoute {

	public static final Logger logger = LogManager.getLogger(CommentRoute.class.getName());

	public CommentRoute() {

		post("/comments", (request, response) -> {

			try {

				DoctorComment comment = JSON.fromJSON(request.queryParams("_q"), DoctorComment.class);

				if (comment.insert()) {
					
					Connection conn = PooledDataSource.getDataSource("health").getConnection();
					Statement stat = conn.createStatement();
					String sql = "SELECT avg(rating) AS r FROM doctor_comments WHERE doctor_id = " + comment.doctor.id;
					ResultSet rs = stat.executeQuery(sql);
					
					float r = 0;
					if(rs.next()) {
						r = rs.getFloat(1);
					}
					
					comment.doctor.rating = r;
					comment.doctor.update();
					rs.close();
					stat.close();
					conn.close();
					
					
					return new Msg<Integer>(Msg.INSERT_SUCCESS, comment.id);
				} else {
					return new Msg<>(Msg.INSERT_FAILURE);
				}

			} catch (Exception e) {
				logger.error("Error create user.", e);
				return new Msg<>(Msg.FAILURE);
			}

		} , new MsgTransformer());

		get("/comments", (request, response) -> {

			try {
				
				Query q = JSON.fromJSON(request.queryParams("_q"), Query.class);
				
				List<DoctorComment> comments = DoctorComment.getCommentsByDoctorId(q.doctor_id, q.limit, q.offset);

				return new Msg<List<DoctorComment>>(Msg.SUCCESS, comments);

			} catch (Exception e) {
				logger.error("Error get comments.", e);
				return new Msg<>(Msg.FAILURE);
			}
		} , new MsgTransformer());

	}

	class Query implements JSONable<DoctorService> {

		public int doctor_id;

		long offset = 0;
		long limit = 10;
		
		public Query(){}

		@Override
		public String toJSON() {

			return JSON.toJSON(this);
		}
	}

	public static void main(String[] args) {

	}

}
