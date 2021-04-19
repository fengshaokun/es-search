package com.fsk.staty.es.search.aspect.log;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 17:36
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;

public class SyncLogAdapter {
    public SyncLogAdapter() {
    }

    public JSONObject getResponse(Object response) {
        return (JSONObject)JSON.toJSON(response);
    }

    public Object getParams4PostJson(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object parameters = null;
        if (null != args) {
            parameters = args[0];
        }

        return parameters;
    }
}
