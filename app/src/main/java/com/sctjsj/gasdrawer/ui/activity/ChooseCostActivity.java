package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.HttpTask.XCacheCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.GroupBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.presenter.adapter.ChooseCostAdapter;
import com.sctjsj.gasdrawer.ui.CustomView.RemarkDialog;
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

/**
 * 选择服务费页面
 */
@Route(path = "/main/act/ChooseCostActivity")
public class ChooseCostActivity extends BaseAppcompatActivity {
    @BindView(R.id.Choose_Cost_back)
    ImageView ChooseCostBack;
    @BindView(R.id.Choose_Cost_Listview)
    ExpandableListView ChooseCostListview;
    @BindView(R.id.activity_choose_fee)
    LinearLayout activityChooseFee;
    @BindView(R.id.edt_material)
    EditText edtMaterial;
    private List<ServerChildBean> serverlist = new ArrayList<>();
    private List<GroupBean> list = new ArrayList<>();
    private List<List<ServerChildBean>> serverChild = new ArrayList<>();
    private ChooseCostAdapter adapter;
    private RemarkDialog mRemarkDialog;
    private int distype = 2;
    private int material;
    private List<ServerChildBean> Historylists;
    private String meter="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (result != null && result.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("resultList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            GroupBean bean = new GroupBean();
                            List<ChildBean> childBeanList = new ArrayList<>();
                            List<ServerChildBean> serverlist = new ArrayList<>();
                            bean.setClassName(object.getString("className"));
                            bean.setId(object.getInt("id"));
                            bean.setType(object.getInt("type"));
                            bean.setTime(object.getString("insertTime"));
                            JSONArray jsonArray1 = object.getJSONArray("materials");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject object1 = jsonArray1.getJSONObject(j);
                                ChildBean bean1 = new ChildBean();
                                bean1.setMaterialName(object1.getString("materialName"));
                                bean1.setId(object1.getInt("id"));
                                bean1.setModel(object1.getString("model"));
                                bean1.setPurchasePrice(object1.getInt("purchasePrice"));
                                bean1.setRemarks(object1.getString("remarks"));
                                bean1.setSpecInfo(object1.getString("specInfo"));
                                bean1.setSellingPrice(object1.getInt("sellingPrice"));
                                bean1.setUnit(object1.getString("unit"));
                                childBeanList.add(bean1);
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

                            serverChild.add(serverlist);
//                            data.add(childBeanList);
                            list.add(bean);
                            if(adapter!=null)
                                adapter.notifyDataSetChanged();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
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
//        serverChild = (List<List<ServerChildBean>>) bun.getSerializable("data");
        adapter = new ChooseCostAdapter(list, serverChild, ChooseCostActivity.this);
        ChooseCostListview.setAdapter(adapter);
        //滑动隐藏软键盘
        ChooseCostListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:    //当停止滚动时
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    //滚动时
                        //隐藏软键盘
                        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ChooseCostActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        return R.layout.activity_choose_cost;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.Choose_Cost_back, R.id.Choose_Cost_btn, R.id.backlog_ViewPager_Edt, R.id.ral_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Choose_Cost_back:
                finish();
                break;
            case R.id.Choose_Cost_btn:
                Set<ServerChildBean> serverChildBeen = adapter.getServerChildBeen();
                Iterator<ServerChildBean> iterator = serverChildBeen.iterator();
                for (int i = 0; i < serverChildBeen.size(); i++) {
                    if (iterator.hasNext()) {
                        serverlist.add(iterator.next());
                    }
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Log.e("serverlist",serverlist.toString());
                bundle.putSerializable("data", (Serializable) serverlist);
                intent.putExtra("data", bundle);
                if (serverlist.size() > 0&&!TextUtils.isEmpty(edtMaterial.getText().toString())) {
                    boolean istrue = false;
                    for (int i = 0; i < serverlist.size(); i++) {
                        if (TextUtils.isEmpty(serverlist.get(i).getEditext())) {
                            istrue = true;
                        }
                    }
                    intent.putExtra("distype", distype);
                    meter=edtMaterial.getText().toString();
                    intent.putExtra("materialall",Integer.parseInt(meter));
                    if (!istrue) {
                        saveData(serverlist,meter);
                        setResult(101, intent);
                        finish();
                    } else {
                        Toast.makeText(this, "请填写总价。", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if(serverlist.size()==0){
                        Toast.makeText(this, "至少选择一项。", Toast.LENGTH_SHORT).show();
                    }
                    if(TextUtils.isEmpty(edtMaterial.getText().toString())){
                        Toast.makeText(this, "请填写材料总长度。", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.backlog_ViewPager_Edt:
                startActivityForResult(new Intent(this, SearchServerActivity.class).putExtra("data", (Serializable) serverChild), 100);
                break;
            //选择优惠
            case R.id.ral_choose:
                startActivityForResult(new Intent(this, ChooseDiscountActivity.class).putExtra("data", 2), 100);
                break;
        }
    }


    //保存数据
    private void saveData(List<ServerChildBean> lists,String meter) {
        CacheUtils.putCache("ChooseCost",lists);
        CacheUtils.putCache("meter",meter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 103) {
            Bundle data1 = data.getBundleExtra("data");
            serverlist.addAll((List<ServerChildBean>) data1.getSerializable("data"));
        }
        if (requestCode == 100 && resultCode == 202) {
            distype = data.getIntExtra("key", 2);
        }
    }

}
