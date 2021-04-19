package com.fsk.staty.es.search.query;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fsk.staty.es.search.bean.SearchBean;
import com.fsk.staty.es.search.config.EsQueryConfig;
import com.fsk.staty.es.search.config.EsQueryConfigItem;
import com.fsk.staty.es.search.utils.SpringUtil;
import com.yk.bean.EsSearchRequest;
import com.yk.bean.SearchDataResp;
import com.yk.client.RestHighLevelClientService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 14:22
 */
public abstract class EsBaseSearch extends BaseSearch<JSONObject, JSONObject>
        implements Callable<JSONObject> {

    private static Logger logger = LoggerFactory.getLogger(EsBaseSearch.class);

    protected SearchBean searchBean;

    public EsBaseSearch(SearchBean searchBean) {
        this.searchBean = searchBean;
    }


    @Override
    public JSONObject call() throws Exception {
        return this.search(JSONObject.class);
    }

    @Override
    protected RestHighLevelClientService getEsClient() {
        return SpringUtil.getBean(RestHighLevelClientService.class);
    }

    @Override
    protected EsQueryConfigItem getQueryConfigItem() {
        EsQueryConfig cfg = SpringUtil.getBean(EsQueryConfig.class);
        return cfg.getConfigById(this.getQueryConfigId());
    }

    @Override
    protected String getQueryWord() {
        if (StringUtils.isNotBlank(searchBean.getNewQuery())) {
            return searchBean.getNewQuery();
        }
        return searchBean.getQuery();
    }

    @Override
    protected JSONObject afterQuery(SearchDataResp<JSONObject> res, EsSearchRequest req) {
        JSONObject result = new JSONObject();
        result.put("counts", res.getResultNum());
        if (res.getResultNum() == 0) {
            result.put("resultList", Collections.emptyList());
        } else {
            result.put("resultList", processScore(res.getResultData()));
        }
        return result;
    }

    @Override
    protected int getPageSize() {
        return searchBean.getRows();
    }

    @Override
    protected int getPageIndex() {
        return searchBean.getPage();
    }

    protected abstract String getQueryConfigId();

    private List<JSONObject> processScore(List<JSONObject> list) {
        List<JSONObject> resultList = list;
        if (!CollectionUtils.isEmpty(resultList)) {
            resultList.forEach(e ->
            {
                if (e!=null && e.containsKey("_score")){
                    Object score = e.get("_score");
                    e.remove("_score");
                    e.put("score",score);
                }
            });
        }
        return resultList;
    }

}
