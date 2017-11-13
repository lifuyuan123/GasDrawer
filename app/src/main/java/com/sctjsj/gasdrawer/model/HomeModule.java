package com.sctjsj.gasdrawer.model;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.NoticeBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuha on 2017/5/23.
 * 主页的module
 */

public class HomeModule {
    private List<NoticeBean> data;
    private IHttpService service= (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private Map<String,String> body;
    private HomeCallBack call;
    private List<String> bannerData;

    public HomeModule(HomeCallBack call) {
        this.call = call;
    }

    //  请求主页的公告通知
    public void getNoticeMsg(){
        body=new HashMap<>();
        body.put("ctype","notice");
        body.put("cond","{type:1,isPublish:1}");
        body.put("orderby","publish_time desc");
        service.doCommonPost(null, NetAddress.GAS_HOME_NOTICE, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                pullNoticeMsg(result);


            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }




    //获取首页banner页面的网址
    public void getBannerData(){
        body=new HashMap<>();
        body.put("ctype","homeSlider");
        body.put("cond","{deleteStatus:0}");
        body.put("jf","accessory");
        service.doCommonPost(null, NetAddress.GAS_BANNER, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {//获取banner 图的数据
                Log.e("---onSuccess",result.toString());
                try {
                    bannerData=new ArrayList<String>();
                    JSONObject object=new JSONObject(result);
                    if(object.getBoolean("result")){
                        JSONArray array=object.getJSONArray("resultList");
                        for (int i = 0; i <array.length() ; i++) {
                            JSONObject listObj=array.getJSONObject(i);
                               JSONObject banner=listObj.getJSONObject("accessory");
                                bannerData.add(banner.getString("url"));
                        }
                    }
                    call.bannerSucced(bannerData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }


    //解析JSON
    private void pullNoticeMsg(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            if(obj.getBoolean("result")){
               //请求成功
                data=new ArrayList<>();
                JSONArray arr=obj.getJSONArray("resultList");
                for (int i = 0; i <arr.length() ; i++) {
                    JSONObject jsbean=arr.getJSONObject(i);
                    NoticeBean bean=new NoticeBean();
                    bean.setId(jsbean.getInt("id"));
                    bean.setTitle(jsbean.getString("title"));
                    bean.setContent(jsbean.getString("content"));
                    bean.setPublishTime(jsbean.getString("publishTime"));
                    data.add(bean);
                }
                call.Succeed(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public interface HomeCallBack{
        public  void  Succeed(List data);
        public void  bannerSucced(List data);
    }

}
