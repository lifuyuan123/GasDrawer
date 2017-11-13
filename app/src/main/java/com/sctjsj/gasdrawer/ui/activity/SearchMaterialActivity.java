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
import com.sctjsj.gasdrawer.presenter.adapter.SearchMaterialAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchMaterialActivity extends BaseAppcompatActivity {

    @BindView(R.id.backlog_ViewPager_Edt)
    EditText backlogViewPagerEdt;
    @BindView(R.id.lv_search)
    ListView lvSearch;
    private SearchMaterialAdapter adapter;
    private List<List<ChildBean>> list = new ArrayList<>();
    private List<ChildBean> childBeanList = new ArrayList<>();
    private List<ChildBean> beanList = new ArrayList<>();
    private List<ChildBean> list1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    adapter = new SearchMaterialAdapter(beanList, SearchMaterialActivity.this);
                    lvSearch.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list = (List<List<ChildBean>>) getIntent().getSerializableExtra("data");
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
                        if (childBeanList.get(i).getMaterialName().contains(s.toString())) {
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
        return R.layout.activity_search_material;
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
                Set<ChildBean> child = adapter.getChild();
                Iterator<ChildBean> iterator = child.iterator();
                for (int i = 0; i < child.size(); i++) {
                    if(iterator.hasNext()){
                        list1.add(iterator.next());
                    }
                }

                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putSerializable("data", (Serializable) list1);
                intent.putExtra("data",bundle);
                if(list1.size()>0){
                    boolean istrue=false;
                    for (int i = 0; i < list1.size(); i++) {
                        if(list1.get(i).getAll()==0){
                            istrue=true;
                        }
                    }
                    if(!istrue){
                        setResult(103,intent);
                        finish();
                    }else {
                        Toast.makeText(this, "请选择数量。", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "至少选择一项。", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }





}
