package com.fsk.staty.es.search.bean;

import lombok.Data;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-17 21:28
 */
@Data
public class SearchBean extends BasicParams{

    private int sort ;
    private Integer page = CodeConsts.PAGE_DEFAULT_INDEX;
    private Integer rows = CodeConsts.PAGE_DEFAULT_ROWS;
    private String newQuery;


}
