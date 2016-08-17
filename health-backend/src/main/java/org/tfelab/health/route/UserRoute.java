package org.tfelab.health.route;

import static spark.Spark.post;
import static spark.Spark.put;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.json.JSON;
import org.tfelab.health.ServerWrapper;
import org.tfelab.health.model.User;
import org.tfelab.io.Msg;

public class UserRoute {
	
	public static final Logger logger = LogManager.getLogger(UserRoute.class.getName());
	
	public UserRoute() {
		
		/**
		 * 添加一个用户
		 * 场景：用户注册
		 */
		post("/user", (request, response) -> {

			try {
				
				User user = JSON.fromJSON(request.queryParams("_q"), User.class);
				
				if(user.name == null || user.name.length() == 0){
					return new Msg<>(Msg.INSERT_FAILURE);
				}
				
				if(user.password == null || user.password.length() == 0){
					return new Msg<>(Msg.INSERT_FAILURE);
				}
				
				if (user.insert()) {
					return new Msg<>(Msg.INSERT_SUCCESS);
				} else {
					return new Msg<>(Msg.INSERT_FAILURE);
				}
					
			} catch (Exception e) {
				logger.error("Error create user.", e);
				return new Msg<>(Msg.INSERT_FAILURE);
			}
		} , new MsgTransformer());
		
		/**
		 * 更新用户
		 * 场景：用户编辑自己信息
		 */
		put("/user", (request, response) -> {

			try {
				
				User user = JSON.fromJSON(request.queryParams("_q"), User.class);
				
				if(user.name == null || user.name.length() == 0){
					return new Msg<>(Msg.UPDATE_FAILURE);
				}
				
				if(user.password == null || user.password.length() == 0){
					return new Msg<>(Msg.UPDATE_FAILURE);
				}
				
				if (user.update()) {
					return new Msg<>(Msg.UPDATE_SUCCESS);
				} else {
					return new Msg<>(Msg.UPDATE_FAILURE);
				}
					
			} catch (Exception e) {
				logger.error("Error update user.", e);
				return new Msg<>(Msg.UPDATE_FAILURE);
			}
		}, new MsgTransformer());
		
		/**
		 * 用户登录
		 * 场景：
		 */
		post("/login", (request, response) -> {

			try {
				
				User user = JSON.fromJSON(request.queryParams("_q"), User.class);
				
				if(user.name == null || user.name.length() == 0){
					return new Msg<>(Msg.FAILURE);
				}
				
				if(user.password == null || user.password.length() == 0){
					return new Msg<>(Msg.FAILURE);
				}
				
				User user_db = User.getUserById(user.id);
				
				if (user_db != null && user_db.password == user.password) {
					return new Msg<>(Msg.SUCCESS);
				} else {
					return new Msg<>(Msg.FAILURE);
				}
					
			} catch (Exception e) {
				logger.error("Error login user.", e);
				return new Msg<>(Msg.FAILURE);
			}
			
		}, new MsgTransformer());
		
	}
}
