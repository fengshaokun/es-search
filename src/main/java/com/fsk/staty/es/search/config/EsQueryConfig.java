package com.fsk.staty.es.search.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yk
 * @version 1.0
 * @date 2021-04-17 21:39
 */
@Component
@ConfigurationProperties(prefix = "es.query")
@Data
public class EsQueryConfig {
    private List<EsQueryConfigItem> config;
    private Map<String,EsQueryConfigItem> configMap;

    public synchronized  EsQueryConfigItem getConfigById(String id){
        if (configMap ==null){
            configMap = new HashMap<String,EsQueryConfigItem>();
            if (config!=null){
                config.forEach(e->{
                    configMap.put(e.getId(),e);
                });
            }
        }
        return configMap.get(id);
    }

    @Override
    public String toString() {
        return "EsQueryConfig{" +
                "config=" + config +
                '}';
    }
}
