package com.sctjsj.gasdrawer.ui.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sctjsj.gasdrawer.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liuha on 2017/6/1.
 */

public class ReasonDialog extends Dialog {
    @BindView(R.id.reson_message)
    EditText resonMessage;
    @BindView(R.id.reson_button)
    Button resonButton;
    Context mContext;
    private ResonDialogCallBack callBack;

    public ReasonDialog(Context context) {
        super(context, R.style.alert_dialog);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reason_dialog);
    }

    @OnClick(R.id.reson_button)
    public void onViewClicked() {
        if(!TextUtils.isEmpty(resonMessage.getText().toString())){
            String data=resonMessage.getText().toString();
            callBack.onViewClicked(data);
        }else {
            Toast.makeText(mContext,"备注不能为空！",Toast.LENGTH_SHORT).show();
        }
    }

   public interface ResonDialogCallBack{
        public  void onViewClicked(String data);
    }

    public void  setListener(ResonDialogCallBack callBack){
        this.callBack=callBack;
    }
}
