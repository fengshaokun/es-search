package com.fsk.staty.es.search.aspect.log;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 17:40
 */
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SyncLogAspectHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncLogAspectHelper.class);
    private ThreadLocal<Long> startTime = new ThreadLocal();
    private ThreadLocal<SyncLogEntity> logLocal = new ThreadLocal();
    private SyncLogAdapter adapter = new SyncLogAdapter();

    public SyncLogAspectHelper() {
    }

    public void doBeforeHelper(JoinPoint joinPoint) {
        this.startTime.set(System.currentTimeMillis());
        SyncLogEntity logEntity = new SyncLogEntity();
        logEntity.setClassMethod(joinPoint.getSignature().getName());
        Object params = this.adapter.getParams4PostJson(joinPoint);
        logEntity.setRequestParam(JSON.toJSONString(params));
        this.logLocal.set(logEntity);
    }

    public void doAfterReturningHelper(Object response) {
        SyncLogEntity logEntity = (SyncLogEntity)this.logLocal.get();
        if (logEntity != null) {
            logEntity.setTimeCost(String.valueOf(System.currentTimeMillis() - (Long)this.startTime.get()));
            JSONObject resp = this.adapter.getResponse(response);
            JSONObject result = new JSONObject();
            result.put("resultCode", resp.get("code"));
            result.put("resultMsg", resp.get("message"));
            logEntity.setResult(result.toJSONString());
            LOGGER.info(JSON.toJSONString(logEntity));
        }
    }
}
