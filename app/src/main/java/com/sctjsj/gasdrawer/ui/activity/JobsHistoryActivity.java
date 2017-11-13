package com.sctjsj.gasdrawer.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.presenter.adapter.BackLogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.qrefreshlayout.QRefreshLayout;
import q.rorbin.qrefreshlayout.RefreshHandler;

/***
 * 个人中心-任务历史
 */
@Route(path = "/main/act/JobsHistoryActivity")
public class JobsHistoryActivity extends BaseAppcompatActivity {

    @BindView(R.id.job_history_back)
    ImageView jobHistoryBack;
    @BindView(R.id.job_history_li)
    ListView jobHistoryLi;
    @BindView(R.id.backlog_ViewPager_Edt)
    EditText backlogViewPagerEdt;
    @BindView(R.id.activity_jobs_history)
    LinearLayout activityJobsHistory;
    @BindView(R.id.refresh)
    QRefreshLayout refresh;
    private int pageIndex=1;
    private HttpServiceImpl service = new HttpServiceImpl();
    private List<WorkMessageBean> list = new ArrayList<>();
    private BackLogAdapter adapter;
    int USERTYE;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //加载所有的历史记录
                    adapter = new BackLogAdapter(JobsHistoryActivity.this, list);
                    jobHistoryLi.setAdapter(adapter);
                    jobHistoryLi.setEnabled(true);
                    jobHistoryLi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            WorkMessageBean bean = list.get(position);
                            ARouter.getInstance().build("/main/act/TaskActivity").withObject("data", bean).navigation();

                        }
                    });
                    break;

                case 1:
                    //模糊查询历史记录
                    String s = (String) msg.obj;
                    List<WorkMessageBean> lists = new ArrayList<>();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if ((list.get(i).getClientName()).indexOf(s) != -1) {
                                lists.add(list.get(i));
                            }
                        }
                        adapter = new BackLogAdapter(JobsHistoryActivity.this, lists);
                        jobHistoryLi.setAdapter(adapter);
                    }
                    break;
                case 3:
                    jobHistoryLi.setEnabled(true);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //获取数据
        showLoading(true, "加载中...");
        getdata();
        refresh.setLoadMoreEnable(true);
        refresh.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                jobHistoryLi.setEnabled(false);
                getdata();
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
                jobHistoryLi.setEnabled(false);
                getdataMore();
            }
        });

        //edit监听
        backlogViewPagerEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") && s != null) {
                    Message message = new Message();
                    message.what = 1;
                    message.obj = s.toString();
                    handler.sendMessage(message);
                } else if (s.toString().equals("")) {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = s.toString();
                    handler.sendMessage(message);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_jobs_history;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.job_history_back)
    public void onViewClicked() {
        finish();
    }


    private void getdata() {
        list.clear();
        pageIndex=1;
        Map<String, String> pParameters = new HashMap<>();
        pParameters.put("ctype", "workorder");
        int id = (int) SPFUtil.get(Tag.TAG_ID, 0);
//        USERTYE =(int)SPFUtil.get(Tag.TAG_TYPE,0);

        USERTYE = (int) SPFUtil.get(Tag.TAG_USERTYPE, 0);
        int num = -1;
        if (USERTYE == 1 || USERTYE == 3) {
            num = 8;
        } else if (USERTYE == 2) {
            num = 5;
        }
//        int num=-1;
//        if(USERTYE==2){
//            num=5;
//        }else if(USERTYE==3){
//            num=6;
//        }else {
//            num=7;
//        }
        pParameters.put("cond", "{operator:{id:" + id + "},orderStatus:" + num + ",isDelete:1}");
        pParameters.put("orderby", "install_time desc");
        pParameters.put("pageIndex",pageIndex+"");
        Log.e("history", pParameters.toString());
        service.doCommonPost(new HashMap<String, String>(), NetAddress.HISTORICAL, pParameters, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("history",result.toString());
                if (result != null && result.length() > 0) {
                    try {
                        pageIndex++;
                        JSONObject obj = new JSONObject(result);
                        JSONArray arr = obj.getJSONArray("resultList");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject objs = arr.getJSONObject(i);
                            WorkMessageBean bean = new WorkMessageBean();
                            bean.setAcceptedNumber(objs.getString("accNumber"));
                            bean.setAddress(objs.getString("address"));
                            bean.setCAENNumber(objs.getString("ceaNo"));
                            bean.setClientName(objs.getString("clientName"));
                            if(objs.has("departName"))
                            bean.setDepartName(objs.getString("departName"));
                            bean.setHouseId(objs.getString("houseId"));
                            bean.setHouseName(objs.getString("houseName"));
                            bean.setId(objs.getInt("id"));
                            bean.setInsertTime(objs.getString("insertTime"));
                            bean.setInstallTime(objs.getString("installTime"));
                            bean.setIsDelete(objs.getInt("isDelete"));
                            bean.setOrderNo(objs.getString("orderNo"));
                            bean.setOrderStatus(objs.getInt("orderStatus"));
                            bean.setOrdertype(objs.getInt("orderType"));
                            if(objs.has("payTime"))
                            bean.setPayTime(objs.getString("payTime"));
                            bean.setTel(objs.getString("tel"));
                            bean.setTotalprice(objs.getInt("totalprice"));
                            list.add(bean);
                        }
                        Log.e("list", list.toString());
                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("history_JSONException",e.toString());
                    }


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
                dismissLoading();
                refresh.refreshComplete();
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


    private void getdataMore() {
        Map<String, String> pParameters = new HashMap<>();
        pParameters.put("ctype", "workorder");
        int id = (int) SPFUtil.get(Tag.TAG_ID, 0);
//        USERTYE =(int)SPFUtil.get(Tag.TAG_TYPE,0);

        USERTYE = (int) SPFUtil.get(Tag.TAG_USERTYPE, 0);
        int num = -1;
        if (USERTYE == 1 || USERTYE == 3) {
            num = 8;
        } else if (USERTYE == 2) {
            num = 5;
        }
//        int num=-1;
//        if(USERTYE==2){
//            num=5;
//        }else if(USERTYE==3){
//            num=6;
//        }else {
//            num=7;
//        }
        pParameters.put("cond", "{operator:{id:" + id + "},orderStatus:" + num + ",isDelete:1}");
        pParameters.put("orderby", "install_time desc");
        pParameters.put("pageIndex",pageIndex+"");
        Log.e("history", pParameters.toString());
        service.doCommonPost(new HashMap<String, String>(), NetAddress.HISTORICAL, pParameters, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (result != null && result.length() > 0) {
                    try {
                        pageIndex++;
                        JSONObject obj = new JSONObject(result);
                        JSONArray arr = obj.getJSONArray("resultList");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject objs = arr.getJSONObject(i);
                            WorkMessageBean bean = new WorkMessageBean();
                            bean.setAcceptedNumber(objs.getString("accNumber"));
                            bean.setAddress(objs.getString("address"));
                            bean.setCAENNumber(objs.getString("ceaNo"));
                            bean.setClientName(objs.getString("clientName"));
                            if(objs.has("departName"))
                            bean.setDepartName(objs.getString("departName"));
                            bean.setHouseId(objs.getString("houseId"));
                            bean.setHouseName(objs.getString("houseName"));
                            bean.setId(objs.getInt("id"));
                            bean.setInsertTime(objs.getString("insertTime"));
                            bean.setInstallTime(objs.getString("installTime"));
                            bean.setIsDelete(objs.getInt("isDelete"));
                            bean.setOrderNo(objs.getString("orderNo"));
                            bean.setOrderStatus(objs.getInt("orderStatus"));
                            bean.setOrdertype(objs.getInt("orderType"));
                            if(objs.has("payTime"))
                            bean.setPayTime(objs.getString("payTime"));
                            bean.setTel(objs.getString("tel"));
                            bean.setTotalprice(objs.getInt("totalprice"));
                            list.add(bean);
                        }
                        Log.e("list", list.toString());
                        Message message = new Message();
                        message.what = 3;
                        handler.sendMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
                dismissLoading();
                refresh.LoadMoreComplete();
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
}
