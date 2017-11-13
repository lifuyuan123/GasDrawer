package com.sctjsj.gasdrawer.util;

import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.AllCacheBean;

import java.util.HashMap;

/**
 * Created by haohaoliu on 2017/7/3.
 * explain: 管路缓存的utils
 */

public class CacheUtils  {

    private static AllCacheBean cacheBean=AllCacheBean.getCacheBean();
    //存入缓存
    public  static  void  putCache(String key,Object data){
        HashMap<String,Object> cache=cacheBean.getCache();
        cache.put(key,data);
        cacheBean.setCache(cache);
    }

    //取缓存
    public  static Object getCache(String key){
       HashMap<String,Object> cache= cacheBean.getCache();
        if(cache.containsKey(key)){
            return cache.get(key);
        }else {
            return null;
        }
    }

    public static void cleanAll(){
        cacheBean.cleanAll();
    }
}
