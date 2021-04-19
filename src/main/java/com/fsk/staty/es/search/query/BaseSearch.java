package com.fsk.staty.es.search.query;


import com.fsk.staty.es.search.config.EsQueryConfigItem;
import com.yk.bean.EsSearchRequest;
import com.yk.bean.SearchDataResp;
import com.yk.client.RestHighLevelClientService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-17 22:25
 */
public abstract class BaseSearch<T, V> {
    protected abstract RestHighLevelClientService getEsClient();

    protected abstract EsQueryConfigItem getQueryConfigItem();

    protected abstract String getQueryWord();

    protected abstract V afterQuery(SearchDataResp<T> res, EsSearchRequest req);

    /**
     * 查询之前还可以调整查询条件,如添加动态过滤条件等等.
     *
     * @param req
     * @param config
     * @param queryWord
     */
    protected void beforeQuery(EsSearchRequest req, EsQueryConfigItem config, String queryWord) {

    }

    /**
     * 需要置顶功能添加此方法
     *
     * @return
     */
    protected String getTopIds() {
        return null;
    }

    /**
     * 需要同义词功能实现此方法
     *
     * @return
     */
    protected List<String> getSynonyms() {
        return null;
    }

    /**
     * 需要对搜索词进行预处理实现此方法,如去掉特殊字符.
     *
     * @param queryWord
     * @return
     */
    protected String preProcessQueryWord(String queryWord) {
        return queryWord;
    }

    public V search(Class<T> clazz) throws IOException {
        String q = preProcessQueryWord(this.getQueryWord());

        EsSearchRequest req = this.initRequest();

        EsQueryConfigItem config = this.getQueryConfigItem();

        req.setIncludeFields(config.getFields().replaceAll("\\s+", "").split(","));

        if (StringUtils.isNotBlank(config.getBf())) {
            req.setBf(config.getBf());
        }

        if (StringUtils.isNotBlank(config.getSort())) {
            req.setSortFields(config.getSort());
        }

        if (StringUtils.isNotBlank(config.getFilter())) {
            this.appendFilter(req, QueryBuilders.queryStringQuery(config.getFilter()));
        }

        if (config.getTop() != null && StringUtils.isNotBlank(this.getTopIds())) {
            this.addTop(req, config, this.getTopIds());
        }

        if (config.getTerm() != null) {
            config.getTerm().entrySet().forEach(
                    e -> {
                        appendShouldQueryBuilders(req, QueryBuilders.termQuery(e.getKey(), q).boost(e.getValue()));
                    }
            );
        }

        if (config.getMatch() != null) {
            config.getMatch().entrySet().forEach(
                    e -> {
                        MatchQueryBuilder qb = QueryBuilders.matchQuery(e.getKey(), q).boost(e.getValue());
                        if (StringUtils.isNotBlank(config.getMm())) ;
                        {
                            qb.minimumShouldMatch(config.getMm());
                        }
                        appendShouldQueryBuilders(req, qb);
                    }
            );
        }

        if (config.getMatchPhrase() != null) {
            config.getMatchPhrase().entrySet().forEach(e -> {
                appendShouldQueryBuilders(req, QueryBuilders.matchPhraseQuery(e.getKey(), q).boost(e.getValue()));
            });
        }

        if (config.getSynonymTerms() != null && CollectionUtils.isNotEmpty(this.getSynonyms())) {
            //支持termQuery同义词
            config.getSynonymTerms().entrySet().forEach(e -> {
                appendShouldQueryBuilders(req,
                        QueryBuilders.termsQuery(e.getKey(), this.getSynonyms()).boost(e.getValue()));
            });
        }

        if (StringUtils.isNotBlank(config.getSynonymQuery()) && CollectionUtils.isNotEmpty(this.getSynonyms())) {
            //支持termQuery同义词
            this.getSynonyms().forEach(e -> {
                appendShouldQueryBuilders(req,
                        QueryBuilders.queryStringQuery(String.format(config.getSynonymQuery(), e)));
            });
        }

        beforeQuery(req, config, q);
        SearchDataResp<T> res = getEsClient().search(req, clazz);
        V result = afterQuery(res, req);
        return result;


    }

    protected void appendFilter(EsSearchRequest req, QueryBuilder... q) {
        List<QueryBuilder> filters = req.getFilterBuilders();
        if (filters == null) {
            filters = new LinkedList<>();
            req.setFilterBuilders(filters);
        }
        for (QueryBuilder qb : q) {
            filters.add(qb);
        }
    }

    protected void appendShouldQueryBuilders(EsSearchRequest req, QueryBuilder... q) {
        List<QueryBuilder> shoulds = req.getShouldQueryBuilders();
        if (shoulds == null) {
            shoulds = new LinkedList<>();
            req.setShouldQueryBuilders(shoulds);
        }
        for (QueryBuilder qb : q) {
            shoulds.add(qb);
        }
    }

    protected void addTop(EsSearchRequest req, EsQueryConfigItem config, String sIds) {
        if (StringUtils.isNotBlank(sIds) && config.getTop() != null &&
                config.getTop().size() > 0) {
            Map.Entry<String, Float> topConfig = config.getTop().entrySet().stream().findFirst().get();
            String[] sIdArr = sIds.split(",");
            for (int i = sIdArr.length - 1; i >= 0; i--) {
                if (StringUtils.isNotBlank(sIdArr[i])) {
                    appendShouldQueryBuilders(req, QueryBuilders.termQuery(topConfig.getKey(), sIdArr[i].trim())
                            .boost(topConfig.getValue() * (1 + i / 10f)));
                }
            }
        }
    }


    protected EsSearchRequest initRequest() {
        EsSearchRequest req = new EsSearchRequest();
        req.setIndexName(this.getQueryConfigItem().getIndex());
        req.setPageIndex(this.getPageIndex());
        req.setSize(this.getPageSize());
        return req;
    }

    abstract protected int getPageSize();

    abstract protected int getPageIndex();
}
