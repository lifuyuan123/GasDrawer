package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.presenter.adapter.SearchMaterialAdapter;
import com.sctjsj.gasdrawer.presenter.adapter.SearchServerAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchServerActivity extends BaseAppcompatActivity {

    @BindView(R.id.backlog_ViewPager_Edt)
    EditText backlogViewPagerEdt;
    @BindView(R.id.lv_search)
    ListView lvSearch;

    private List<List<ServerChildBean>> list = new ArrayList<>();
    private List<ServerChildBean> childBeanList = new ArrayList<>();
    private List<ServerChildBean> beanList = new ArrayList<>();
    private List<ServerChildBean> list1;
    private SearchServerAdapter adapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter = new SearchServerAdapter(beanList, SearchServerActivity.this);
                    lvSearch.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list= (List<List<ServerChildBean>>) getIntent().getSerializableExtra("data");
        getdata();

        backlogViewPagerEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                beanList.clear();
                if(!TextUtils.isEmpty(s.toString())){
                    for (int i = 0; i < childBeanList.size(); i++) {
                        if (childBeanList.get(i).getProName().contains(s.toString())) {
                            beanList.add(childBeanList.get(i));
                        }
                    }
                }
                Message message = new Message();
                message.what = 0;
                handler.sendMessage(message);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getdata() {
        for (int i = 0; i < list.size(); i++) {
            childBeanList.addAll(list.get(i));
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_search_server;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.iv_back, R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_ok:
                list1=new ArrayList<>();
                Set<ServerChildBean> child = adapter.getServerChildBeen();
                Iterator<ServerChildBean> iterator = child.iterator();
                for (int i = 0; i < child.size(); i++) {
                    if(iterator.hasNext()){
                        list1.add(iterator.next());
                    }
                }

                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("data", (Serializable) list1);
                intent.putExtra("data",bundle);
                if (list1.size() > 0) {
                    boolean istrue = false;
                    for (int i = 0; i < list1.size(); i++) {
                        if (TextUtils.isEmpty(list1.get(i).getEditext())) {
                            istrue = true;
                        }
                    }
                    if (!istrue) {
                        setResult(103, intent);
                        finish();
                    } else {
                        Toast.makeText(this, "请填写总价。", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
