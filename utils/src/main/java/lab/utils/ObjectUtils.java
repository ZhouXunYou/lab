package lab.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class ObjectUtils {
	private static final ObjectMapper om;
	private static final Logger LOG = LoggerFactory.getLogger(ObjectUtils.class);
	static {
		om = new ObjectMapper();
		om.enable(SerializationFeature.INDENT_OUTPUT);
	}
	public static String printObject(Object object) {
		String json = null;
		try {
			json = om.writeValueAsString(object);
			LOG.debug("object to json format:\n{}",json);
			return json;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
