package com.sctjsj.gasdrawer.presenter.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.RepairGasBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by liuha on 2017/5/5.
 */

public class RepairGasAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<RepairGasBean> data;

    public List<RepairGasBean> getData() {
        return data;
    }

    public void setData(List<RepairGasBean> data) {
        this.data = data;
    }

    public RepairGasAdapter(Context mContext, List<RepairGasBean> data) {
        this.mContext = mContext;
        this.data = data;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RepairGasAdapterHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.repair_gas_item, null);
            holder = new RepairGasAdapterHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RepairGasAdapterHolder) convertView.getTag();
        }

        //设置型号输入框的监听
        holder.gasType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.get(position).setNo(s.toString());
            }
        });

        //设置品牌的监听
        holder.gasBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.get(position).setBrand(s.toString());
            }
        });

        holder.gasNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                data.get(position).setSerNumber(s.toString());
            }
        });

        holder.gasDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                data.get(position).setInstallTime(s.toString());
            }
        });

        holder.gasGzRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        data.get(position).setIsRepair(1);
                    } else {
                        data.get(position).setIsRepair(2);
                    }

                }
            }
        });

        if(data.get(position).getIsRepair()!=null)
        switch (data.get(position).getIsRepair()){
            case 1:
                holder.gasGzRgTure.setChecked(true);
                break;
            case 2:
                holder.gasGzRgFalse.setChecked(true);
                break;
        }

        holder.gasByRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        data.get(position).setIsMaintain(1);
                    } else {
                        data.get(position).setIsMaintain(2);
                    }

                }
            }
        });
        if(data.get(position).getIsMaintain()!=null)
        switch (data.get(position).getIsMaintain()){
            case 1:
                holder.gasByRgTure.setChecked(true);
                break;
            case 2:
                holder.gasByRgFalse.setChecked(true);
                break;
        }

        holder.gasGhGr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        data.get(position).setIsChange(1);
                    } else {
                        data.get(position).setIsChange(2);
                    }
                }
            }
        });

        if(data.get(position).getIsChange()!=null)
        switch (data.get(position).getIsChange()){
            case 1:
                holder.gasGhGrTure.setChecked(true);
                break;
            case 2:
                holder.gasGhGrFalse.setChecked(true);
                break;
        }
        holder.gasBnwGr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton button = (RadioButton) group.getChildAt(i);
                    if (button.isChecked()) {
                        data.get(position).setRepairWay(1);
                    } else {
                        data.get(position).setRepairWay(2);
                    }
                }
            }
        });
        if(data.get(position).getRepairWay()!=null)
        switch (data.get(position).getRepairWay()){
            case 1:
                holder.gasBnwGrTure.setChecked(true);
                break;
            case 2:
                holder.radioButton.setChecked(true);
                break;
        }

        holder.gasType.setText(data.get(position).getNo());
        holder.gasBrand.setText(data.get(position).getBrand());
        holder.gasNumber.setText(data.get(position).getSerNumber());
        holder.gasDate.setText(data.get(position).getInstallTime());

        for (int i = 0; i < holder.gasGzRg.getChildCount(); i++) {
            RadioButton button = (RadioButton) holder.gasGzRg.getChildAt(i);
            if (button.getText().toString().equals(data.get(position).getIsRepair())) {
                button.isChecked();
            }
        }

        for (int i = 0; i < holder.gasByRg.getChildCount(); i++) {
            RadioButton button = (RadioButton) holder.gasByRg.getChildAt(i);
            if (button.getText().toString().equals(data.get(position).getIsMaintain())) {
                button.isChecked();
            }
        }

        for (int i = 0; i < holder.gasGhGr.getChildCount(); i++) {
            RadioButton button = (RadioButton) holder.gasGhGr.getChildAt(i);
            if (button.getText().toString().equals(data.get(position).getIsChange())) {
                button.isChecked();
            }
        }
        for (int i = 0; i < holder.gasBnwGr.getChildCount(); i++) {
            RadioButton button = (RadioButton) holder.gasBnwGr.getChildAt(i);
            if (button.getText().toString().equals(data.get(position).getRepairWay())) {
                button.isChecked();
            }
        }
        return convertView;
    }

    static class RepairGasAdapterHolder {
        @BindView(R.id.gas_type)
        EditText gasType;
        @BindView(R.id.gas_Brand)
        EditText gasBrand;
        @BindView(R.id.gas_number)
        EditText gasNumber;
        @BindView(R.id.gas_date)
        EditText gasDate;
        @BindView(R.id.gas_gz_rg)
        RadioGroup gasGzRg;
        @BindView(R.id.gas_by_rg)
        RadioGroup gasByRg;
        @BindView(R.id.gas_gh_gr)
        RadioGroup gasGhGr;
        @BindView(R.id.gas_bnw_gr)
        RadioGroup gasBnwGr;
        @BindView(R.id.gas_gz_rg_ture)
        RadioButton gasGzRgTure;
        @BindView(R.id.gas_gz_rg_false)
        RadioButton gasGzRgFalse;
        @BindView(R.id.gas_by_rg_ture)
        RadioButton gasByRgTure;
        @BindView(R.id.gas_by_rg_false)
        RadioButton gasByRgFalse;
        @BindView(R.id.gas_gh_gr_ture)
        RadioButton gasGhGrTure;
        @BindView(R.id.gas_gh_gr_false)
        RadioButton gasGhGrFalse;
        @BindView(R.id.gas_bnw_gr_ture)
        RadioButton gasBnwGrTure;
        @BindView(R.id.radioButton)
        RadioButton radioButton;

        RepairGasAdapterHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
