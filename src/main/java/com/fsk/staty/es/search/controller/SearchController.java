package com.fsk.staty.es.search.controller;

import com.alibaba.fastjson.JSONObject;
import com.fsk.staty.es.search.bean.SearchBean;
import com.fsk.staty.es.search.bean.SearchResultBean;
import com.fsk.staty.es.search.query.SearchTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 15:18
 */
@Controller
@RequestMapping
public class SearchController {

    @Autowired
    private SearchTaskService searchTaskService;


    @ResponseBody
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public SearchResultBean search(@RequestBody SearchBean searchBean){
        SearchResultBean searchResultBean = new SearchResultBean();
        try {
            JSONObject jsonObject = searchTaskService.allSearch(searchBean);
            searchResultBean.setResult(jsonObject);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      return searchResultBean;
    }
}
