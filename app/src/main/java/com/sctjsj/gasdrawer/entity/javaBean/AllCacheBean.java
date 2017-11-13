package com.sctjsj.gasdrawer.entity.javaBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by haohaoliu on 2017/7/3.
 * explain:保存缓存
 */

public class AllCacheBean implements Serializable {
    private HashMap<String,Object> cache=new HashMap<>();
    private  static AllCacheBean bean=new AllCacheBean();
    private AllCacheBean(){

    }
    public static AllCacheBean getCacheBean(){
        return bean;
    }


    public HashMap<String, Object> getCache() {
        return cache;
    }

    public void setCache(HashMap<String, Object> cache) {
        this.cache = cache;
    }

    public void cleanAll(){
        cache.clear();
    }
}
