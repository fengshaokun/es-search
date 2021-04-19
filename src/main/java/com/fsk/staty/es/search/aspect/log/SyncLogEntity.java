package com.fsk.staty.es.search.aspect.log;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-19 17:41
 */


public class SyncLogEntity {
    private static final String STR_BLANK = "";
    private String moduleName = "gameSearch";
    private String classMethod = "";
    private String productId = "game-search";
    private String requestParam;
    private String timeCost = "";
    private String resultNum;
    private String result = "";

    public SyncLogEntity() {
    }

    public String getProductId() {
        return this.productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getClassMethod() {
        return this.classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public String getRequestParam() {
        return this.requestParam;
    }

    public void setRequestParam(String requestParam) {
        this.requestParam = requestParam;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getTimeCost() {
        return this.timeCost;
    }

    public void setTimeCost(String timeCost) {
        this.timeCost = timeCost;
    }

    public String getResultNum() {
        return this.resultNum;
    }

    public void setResultNum(String resultNum) {
        this.resultNum = resultNum;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String toString() {
        return "LogEntity{productId='" + this.productId + '\'' + ", classMethod='" + this.classMethod + '\'' + ", requestParam='" + this.requestParam + '\'' + ", timeCost=" + this.timeCost + ", resultNum=" + this.resultNum + '}';
    }
}
