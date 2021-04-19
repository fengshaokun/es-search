package com.fsk.staty.es.search.query;

import com.alibaba.fastjson.JSONObject;
import com.fsk.staty.es.search.bean.SearchBean;

import java.util.concurrent.Callable;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 15:00
 */
public class SearchCallableFactory {

    public static Callable<JSONObject> getCallable(SearchBean searchBean){
        return new CallableEsSearch(searchBean);
    }
}
