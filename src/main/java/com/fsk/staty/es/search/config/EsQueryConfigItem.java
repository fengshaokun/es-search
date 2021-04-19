package com.fsk.staty.es.search.config;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-17 21:43
 */
@Data
@ToString
public class EsQueryConfigItem {

    private String id;
    private String index;
    private Map<String,Float> top;
    private Map<String,Float> term;
    private Map<String,Float> match;
    private Map<String,Float> matchPhrase;
    private Map<String,Float> synonymTerms;
    private String synonymQuery;
    private String filter;
    private String bf;
    private String sort;
    private String fields;
    private String mm;


}
