package com.api.isswhu.demo.util;

import com.api.isswhu.demo.domain.other.RespCode;
import com.api.isswhu.demo.domain.other.ResponData;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回给前端的接口数据格式化处理工具类
 *
 * @author  xiaoyang, daisj
 * @version 1.0
 * @since 2016-12-18
 */
public class ResponseUtil {
	


	/*public static Map<String, Object> badResult(Object cause) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(ERROR_CODE, FAILURE);
		result.put(ERROR_MESSAGE, cause);
		return result;
	}
	
	public static Map<String, Object> ok() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(ERROR_CODE, SUCCESS);
		result.put(DATA, "success");
		return result;
	}
	
	public static Map<String, Object> ok(Object obj) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(ERROR_CODE, SUCCESS);
		result.put(DATA, obj);
		return result;
	}
	
	public static Map<String, Object> okWithCount(Object obj,Integer count) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(ERROR_CODE, SUCCESS);
        result.put(DATA, obj);
        result.put(COUNT, count);
        return result;
    }*/

	public static ResponData badResult(RespCode cause) {
		return new ResponData(cause);
	}
	public static ResponData error() {
		return new ResponData(RespCode.ERROR);
	}
	public static ResponData ok() {
		return new ResponData(RespCode.SUCCESS);
	}

	public static ResponData ok(Object obj) {
		
		return new ResponData(RespCode.SUCCESS,obj);
	}
	



}
