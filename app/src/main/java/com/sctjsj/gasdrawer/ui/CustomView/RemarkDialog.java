package com.sctjsj.gasdrawer.ui.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sctjsj.gasdrawer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by liuha on 2017/4/27.
 */

public class RemarkDialog extends Dialog {
    @BindView(R.id.remark_dialog_title)
    TextView remarkDialogTitle;
    @BindView(R.id.remark_dialog_edt1)
    EditText remarkDialogEdt1;
    @BindView(R.id.remark_dialog_edt2)
    EditText remarkDialogEdt2;
    @BindView(R.id.remark_dialog_Btnsure)
    Button remarkDialogBtnsure;
    private Context mContext;
    private static RemarkDialog mlog;
    private SureCallBAck callback;
    private Unbinder unbinder;

    public String getTitle() {
        return title;
    }

    private String title;
    private String num2;
    private String num1;



    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }




    public RemarkDialog(Context context) {
        super(context,R.style.alert_dialog);
        this.mContext = context;
    }

    public RemarkDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remark_dialog);
        unbinder= ButterKnife.bind(this);
        if(remarkDialogTitle!=null){
            remarkDialogTitle.setText(title);
        }
        findViewById(R.id.remark_dialog_Btnsure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callback!=null){
                    if(!TextUtils.isEmpty(remarkDialogEdt1.getText().toString())&&!TextUtils.isEmpty(remarkDialogEdt2.getText().toString())){
                        callback.onViewClicked(remarkDialogEdt1.getText().toString().trim(),remarkDialogEdt2.getText().toString().trim());
                    }else {
                        Toast.makeText(mContext,"请将数据填完整之后再确认！",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }



   public interface SureCallBAck{
        public void onViewClicked(String edt1,String edt2);
    }

    public void setSureBtnOnclick(SureCallBAck mSureCallBAck){
        this.callback=mSureCallBAck;
    }

    public void setTitle(String title){
        this.title=title;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        unbinder.unbind();
    }
}
