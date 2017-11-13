package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.HttpTask.XCacheCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.GroupBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.presenter.adapter.MaterialUseAdapter;
import com.sctjsj.gasdrawer.util.CacheUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

//材料使用清单
@Route(path = "/main/act/MaterialUselist")
public class MaterialUselistActivity extends BaseAppcompatActivity {
    @BindView(R.id.uselist_Listview)
    ExpandableListView uselistListview;
    private MaterialUseAdapter adapter;
    private List<GroupBean> list=new ArrayList<>();
    private List<List<ChildBean>> data=new ArrayList<>();

    private List<ChildBean> lists = new ArrayList<>();
    private List<ChildBean> Historylists;
    private List<ChildBean> searchlist = new ArrayList<>();

    private int distype=2;
    private int materialType =-1;  //1.安装材料   2.维修材料

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        materialType=getIntent().getBundleExtra("key").getInt("type");
        LogUtil.e("materialType=",materialType+"");
        getdata();
        init();

    }

    //获取数据
    private void getdata() {
        HttpServiceImpl server = new HttpServiceImpl();
        server.setUseCacheFlag(false);
        Map<String, String> pParameters = new HashMap<>();
        pParameters.put("ctype", "itemClass");
        pParameters.put("jf", "materials|serveritems");
        server.doCommonPostWithCache(new HashMap<String, String>(), NetAddress.CLASS_IFICATION, pParameters, 10, new XCacheCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("list_onSuccess",result.toString());
                if (result != null && result.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("resultList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GroupBean bean = new GroupBean();
                            List<ChildBean> childBeanList = new ArrayList<>();
                            List<ChildBean> childBeanListtype2 = new ArrayList<>();
                            List<ServerChildBean> serverlist = new ArrayList<>();
                            bean.setClassName(object.getString("className"));
                            bean.setId(object.getInt("id"));
                            bean.setType(object.getInt("type"));
                            bean.setTime(object.getString("insertTime"));
                            JSONArray jsonArray1 = object.getJSONArray("materials");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject object1 = jsonArray1.getJSONObject(j);
                                ChildBean bean1 = new ChildBean();
                                if(object1.getInt("type")==materialType){
                                    if(object1.has("materialNo")){
                                        bean1.setMaterialNo(object1.getString("materialNo"));
                                    }
                                    bean1.setMaterialName(object1.getString("materialName"));
                                    bean1.setId(object1.getInt("id"));
                                    bean1.setModel(object1.getString("model"));
                                    bean1.setPurchasePrice(object1.getInt("purchasePrice"));
                                    bean1.setRemarks(object1.getString("remarks"));
                                    bean1.setSpecInfo(object1.getString("specInfo"));
                                    bean1.setSellingPrice(object1.getInt("sellingPrice"));
                                    bean1.setUnit(object1.getString("unit"));
                                    childBeanList.add(bean1);
                                }else {
                                    if(object1.has("materialNo")){
                                        bean1.setMaterialNo(object1.getString("materialNo"));
                                    }
                                    bean1.setMaterialName(object1.getString("materialName"));
                                    bean1.setId(object1.getInt("id"));
                                    bean1.setModel(object1.getString("model"));
                                    bean1.setPurchasePrice(object1.getInt("purchasePrice"));
                                    bean1.setRemarks(object1.getString("remarks"));
                                    bean1.setSpecInfo(object1.getString("specInfo"));
                                    bean1.setSellingPrice(object1.getInt("sellingPrice"));
                                    bean1.setUnit(object1.getString("unit"));
                                    childBeanListtype2.add(bean1);
                                }
                            }
                            //服务子列表数据
                            JSONArray jsonArray2 = object.getJSONArray("serveritems");
                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject o = jsonArray2.getJSONObject(k);
                                ServerChildBean b = new ServerChildBean();
                                b.setId(o.getInt("id"));
                                b.setPrice(o.getInt("price"));
                                b.setProName(o.getString("proName"));
                                b.setRemarks(o.getString("remarks"));
                                b.setUnit(o.getString("unit"));
                                serverlist.add(b);
                                Log.e("cccccobject", o.toString());
                            }
                            if(materialType==1){
                                data.add(childBeanList);
                            }else {
                                data.add(childBeanListtype2);
                            }
                            list.add(bean);
                            LogUtil.e("list____",list.toString());
                            if(adapter!=null)
                            adapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("list____JSONException",e.toString());
                    }
                } else {
                    Log.e("aaaaa", "null");
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
            }

            @Override
            public boolean onCache(Object result) {
                return false;
            }

            @Override
            public void onStart() {
            showLoading(true,"加载中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }



    private void init() {
//        Intent intent = getIntent();
//        Bundle bun = intent.getBundleExtra("key");
//        list = (List<GroupBean>) bun.getSerializable("list");
//        data = (List<List<ChildBean>>) bun.getSerializable("data");
        adapter = new MaterialUseAdapter(list, data, MaterialUselistActivity.this);
        uselistListview.setAdapter(adapter);
        //滑动隐藏软键盘
        uselistListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:    //当停止滚动时
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    //滚动时
                        //隐藏软键盘
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MaterialUselistActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:   //手指抬起，但是屏幕还在滚动状态

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });


    }

    @Override
    public int initLayout() {
        return R.layout.activity_material_uselist;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.uselist_iv_back, R.id.uselist_tv_btn, R.id.backlog_ViewPager_Edt,R.id.ral_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.uselist_iv_back:
                finish();
                break;
            case R.id.uselist_tv_btn:
                Set<ChildBean> child = adapter.getChild();
                Log.e("111112", child.toString());
                Iterator<ChildBean> iterator = child.iterator();
                for (int i = 0; i < child.size(); i++) {
                    if (iterator.hasNext()) {
                        lists.add(iterator.next());
                    }
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) lists);
                intent.putExtra("data", bundle);
                intent.putExtra("distype",distype);
                if (lists.size() > 0) {
                    boolean istrue = false;
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getAll() == 0) {
                            istrue = true;
                        }
                    }
                    if (!istrue) {
                        saveData(lists);
                        setResult(103, intent);
                        finish();
                    } else {
                        Toast.makeText(this, "请选择数量。", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "至少选择一项。", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.backlog_ViewPager_Edt:
                startActivityForResult(new Intent(this, SearchMaterialActivity.class).putExtra("data", (Serializable) data), 100);
                break;
            //是否优惠
            case R.id.ral_choose:
                startActivityForResult(new Intent(this,ChooseDiscountActivity.class).putExtra("data",1),100);
                break;
        }
    }

    //保存数据
    private void saveData(List<ChildBean> lists) {
        CacheUtils.putCache("MaterialUselist",lists);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 103) {
            Bundle data1 = data.getBundleExtra("data");
            searchlist = (List<ChildBean>) data1.getSerializable("data");
            lists.addAll(searchlist);
        }
        if(requestCode==100&&resultCode==201){
            distype=data.getIntExtra("key",2);
            Log.e("distypeM",distype+"");
        }
    }


}
