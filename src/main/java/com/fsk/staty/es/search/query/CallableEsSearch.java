package com.fsk.staty.es.search.query;

import com.fsk.staty.es.search.bean.SearchBean;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 15:00
 */
public class CallableEsSearch extends EsBaseSearch {
    public CallableEsSearch(SearchBean searchBean) {
        super(searchBean);
    }

    @Override
    protected String getQueryConfigId() {
        return "user_test";
    }
}
