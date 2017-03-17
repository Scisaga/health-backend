package org.tfelab.health.route;

import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tfelab.common.json.JSON;
import org.tfelab.health.ServiceWrapper;
import org.tfelab.health.model.User;
import org.tfelab.io.Msg;

public class UserRoute {
	
	public static final Logger logger = LogManager.getLogger(UserRoute.class.getName());
	
	public UserRoute() {
		
		/**
		 * 添加一个用户
		 * 场景：用户注册
		 */
		post("/users", (request, response) -> {

			try {
				
				User user = JSON.fromJSON(request.queryParams("_q"), User.class);
				
				if(user.name == null || user.name.length() == 0){
					return new Msg<>(Msg.INSERT_FAILURE);
				}
				
				if(user.password == null || user.password.length() == 0){
					return new Msg<>(Msg.INSERT_FAILURE);
				}
				
				if (user.insert()) {
					return new Msg<Integer>(Msg.INSERT_SUCCESS, user.id);
				} else {
					return new Msg<>(Msg.INSERT_FAILURE);
				}
					
			} catch (Exception e) {
				logger.error("Error create user.", e);
				return new Msg<>(Msg.INSERT_FAILURE);
			}
			
		} , new MsgTransformer());
		
		/**
		 * 获取一个用户的信息
		 */
		get("/users/:id", (request, response) -> {

			try {
				
				int id = Integer.valueOf(request.params(":id"));
				
				User user = User.getUserById(id);
				
				if(user != null) {
					return new Msg<User>(Msg.SUCCESS, user);
				}
				else {
					return new Msg<>(Msg.OBJECT_NOT_FOUND);
				}
			} catch (Exception e) {
				logger.error("Error create user.", e);
				return new Msg<>(Msg.FAILURE);
			}
		} , new MsgTransformer());
		
		/**
		 * 更新用户
		 * 场景：用户编辑自己信息
		 */
		put("/users/:id", (request, response) -> {

			try {
				
				User user = JSON.fromJSON(request.body(), User.class);
				
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
				
				//System.err.println(JSON.toJson(user));
				
				User user_db = User.getUserByName(user.name);
				
				if (user_db != null && user_db.password.equals(user.password)) {
					return new Msg<Integer>(Msg.SUCCESS, user_db.id);
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
