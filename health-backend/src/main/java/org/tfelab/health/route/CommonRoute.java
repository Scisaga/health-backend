package org.tfelab.health.route;

import static spark.Spark.after;
import static spark.Spark.before;
import static spark.Spark.get;

import org.tfelab.io.server.RestfulRequestValidator;

/**
 * 
 * @author: karajan@tfelab.org
 * @Date: 2016-03-31
 */
public class CommonRoute {

	public CommonRoute() {
		
		/**
		 * 请求前验证
		 */
		before((request, response) -> {
			
		});
		
		after((request, response) -> {
		    
			response.header("Access-Control-Allow-Origin", "*");
			response.header("Access-Control-Allow-Methods", "POST, OPTIONS");
			response.header("Access-Control-Allow-Headers", "X-Custom-Header");
			response.header("Access-Control-Max-Age", "1000");
			
		});
	}
}
