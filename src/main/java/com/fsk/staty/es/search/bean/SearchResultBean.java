package com.fsk.staty.es.search.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-17 21:30
 */
@Data
public class SearchResultBean {
    private String code = "000000";
    private String message = "SUCCESS";
    private String status = "200";
    private String costTime;
    private String sid;
    private JSONObject result = new JSONObject();
}
