package org.tfelab.health.route;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.ResponseTransformer;

/**
 * 
 * @author: karajan@tfelab.org
 * @Date: 2016-03-31
 */
public class MsgTransformer implements ResponseTransformer {
			
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public String render(Object model) throws JsonProcessingException {

		return mapper.writeValueAsString(model);
	}
}
