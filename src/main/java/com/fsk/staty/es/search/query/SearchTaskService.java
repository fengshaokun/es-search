package com.fsk.staty.es.search.query;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fsk.staty.es.search.bean.SearchBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 15:03
 */

@Service(value = "searchTaskService")
public class SearchTaskService {

    private ThreadPoolExecutor executor = null;

    public static final String THREAD_TYPE = "ES_SEARCH";

    synchronized ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            RejectedExecutionHandler retryHandler = new RetryRejectedExecutionHandler();
            executor.setThreadFactory(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, THREAD_TYPE);
                }
            });
            executor.setMaximumPoolSize(200);
            executor.setCorePoolSize(20);
            executor.setKeepAliveTime(60, TimeUnit.SECONDS);
            executor.setRejectedExecutionHandler(retryHandler);
        }
        return executor;
    }

    public JSONObject allSearch(SearchBean searchBean) throws ExecutionException, InterruptedException {
        JSONObject reJson = new JSONObject();
        List<Future<JSONObject>> results = new ArrayList<>();
        Callable<JSONObject> callable = SearchCallableFactory.getCallable(searchBean);
        results.add(getExecutor().submit(callable));
        for (int i = 0; i < results.size(); i++) {
            JSONObject result = results.get(i).get();
            if (null != result && result.size() > 0) {
                reJson.putAll(result);
            }
        }
        return reJson;
    }

    class RetryRejectedExecutionHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executor.execute(r);
        }
    }
}
