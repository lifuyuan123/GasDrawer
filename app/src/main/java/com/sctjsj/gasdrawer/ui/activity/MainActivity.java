package com.sctjsj.gasdrawer.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jauker.widget.BadgeView;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.gasdrawer.MainLooper;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.application.MyApp;
import com.sctjsj.gasdrawer.event.GotoEvent;
import com.sctjsj.gasdrawer.event.MessageEvent;
import com.sctjsj.gasdrawer.presenter.IMainPresenter;
import com.sctjsj.gasdrawer.presenter.impl.MainPresenterImpl;
import com.sctjsj.gasdrawer.util.BluetoothService;
import com.sctjsj.gasdrawer.util.JumpCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

import static com.sctjsj.gasdrawer.util.OtherConstant.DEVICE_NAME;
import static com.sctjsj.gasdrawer.util.OtherConstant.MESSAGE_DEVICE_NAME;
import static com.sctjsj.gasdrawer.util.OtherConstant.MESSAGE_READ;
import static com.sctjsj.gasdrawer.util.OtherConstant.MESSAGE_STATE_CHANGE;
import static com.sctjsj.gasdrawer.util.OtherConstant.MESSAGE_WRITE;
import static com.sctjsj.gasdrawer.util.OtherConstant.REQUEST_ENABLE_BT;
import static com.sctjsj.gasdrawer.util.OtherConstant.STATE_CONNECTED;
import static com.sctjsj.gasdrawer.util.OtherConstant.STATE_CONNECTING;
import static com.sctjsj.gasdrawer.util.OtherConstant.STATE_LISTEN;
import static com.sctjsj.gasdrawer.util.OtherConstant.STATE_NONE;

@Route(path = "/user/act/index")
public class MainActivity extends BaseAppcompatActivity {

    @BindView(R.id.replaceId)
    FrameLayout replaceId;
    @BindView(R.id.main_Rbtn_Home)
    RadioButton mainRbtnHome;
    @BindView(R.id.main_Rbtn_backLog)
    RadioButton mainRbtnBackLog;
    @BindView(R.id.main_Rbtn_Msg)
    RadioButton mainRbtnMsg;
    @BindView(R.id.main_Rbtn_Mine)
    RadioButton mainRbtnMine;
    @BindView(R.id.main_RadioGrop)
    RadioGroup mainRadioGrop;
    @BindView(R.id.activity_main)
    LinearLayout activityMain;
    @BindView(R.id.tv)
    TextView tv;
    private IMainPresenter mIMainPresenter;
    private BadgeView badgeView;
    private boolean isout=false;

    private ProgressDialog connectProgress;
    private BluetoothService bluetoothService;
    private BluetoothAdapter bluetoothAdapter;
    private String mConnectedDeviceName;
    private MyApp app;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isout=false;
                    break;
            }
        }
    };

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};//需要用的的权限数组
    List<String> permissionlis = new ArrayList<>();//添加未被授权的权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLogin();

        Log.e("tolen",SPFUtil.get(Tag.TAG_TOKEN,"none-token").toString());
        app = (MyApp) getApplication();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        EventBus.getDefault().register(this);
        init();
    }

    //判断是否已经
    private void isLogin() {
        String data = (String) SPFUtil.get(Tag.TAG_TOKEN, "none");
        if (data.equals("none")) {
            //没有登陆过的
            ARouter.getInstance().build("/main/act/login").navigation();
        }
    }

    //初始化
    private void init() {
        //申请写出权限,用于签名
        if (Build.VERSION.SDK_INT >= 23) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionlis.add(permissions[i]);
                }
            }
            if (!permissionlis.isEmpty()) {
                String[] permission = permissionlis.toArray(new String[permissionlis.size()]);//将未被授权的权限转为String数组
                ActivityCompat.requestPermissions(this, permission, 100);
            } else {
                //todo
            }
        }

        setDrawable(mainRbtnHome, R.drawable.ic_home_selector);
        setDrawable(mainRbtnBackLog, R.drawable.ic_backlog_selector);
        setDrawable(mainRbtnMsg, R.drawable.ic_message_selector);
        setDrawable(mainRbtnMine, R.drawable.ic_mine_selsctor);
        mIMainPresenter = new MainPresenterImpl(this, getSupportFragmentManager());
        mIMainPresenter.addFragment();

        badgeView = new BadgeView(MainActivity.this);
        badgeView.setTargetView(tv);
        badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.TOP);
    }

    //给单选按钮设置图片
    private void setDrawable(RadioButton mRadioButton, int id) {
        Drawable mDrawable = getResources().getDrawable(id);
        mDrawable.setBounds(0, 0, 50, 50);
        mRadioButton.setCompoundDrawables(null, mDrawable, null, null);
    }


    @Override
    public int initLayout() {

        return R.layout.activity_main;
    }

    @Override
    public void reloadData() {

    }

    @OnCheckedChanged({R.id.main_Rbtn_Home, R.id.main_Rbtn_backLog, R.id.main_Rbtn_Msg, R.id.main_Rbtn_Mine})
    public void myOnCheckedChanged(CompoundButton CompoundButton, boolean flag) {
        switch (CompoundButton.getId()) {
            case R.id.main_Rbtn_Home:
                if (flag) {
                    mIMainPresenter.replaceFragment("home");
                }
                break;
            case R.id.main_Rbtn_backLog:
                if (flag) {
                    mIMainPresenter.replaceFragment("backlog");
                }
                break;
//            case R.id.main_Rbtn_Msg:
//                if (flag) {
//                    mIMainPresenter.replaceFragment("message");
//                    if(badgeView!=null){
//                        badgeView.setBadgeCount(0);
//                        app.setNum(0);
//                    }
//
//                }
//                break;
            case R.id.main_Rbtn_Mine:
                if (flag) {
                    mIMainPresenter.replaceFragment("mine");
                }
                break;
        }


    }

    //申请权限的返回结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {//遍历
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "不成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "取消", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //蓝牙
        switch (requestCode) {
            case JumpCode.JUMP_FROM_Index_TO_BTSCANActivity:
                if (RESULT_OK == resultCode) {
                    //获取
                    String address = data.getStringExtra("device");

                    if (bluetoothAdapter == null) {
                        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    }

                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                    bluetoothService.connect(device);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Handler mxHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:

                    switch (msg.arg1) {
                        //连接成功
                        case STATE_CONNECTED:
                            //关闭连接进度
                            if (connectProgress != null) {
                                connectProgress.dismiss();
                            }
                            Toast.makeText(MainActivity.this, "蓝牙连接成功", Toast.LENGTH_SHORT).show();

                            break;
                        //正在连接
                        case STATE_CONNECTING:
                            if (connectProgress == null) {
                                connectProgress = new ProgressDialog(MainActivity.this);
                            }
                            connectProgress.setTitle("");
                            connectProgress.setCancelable(false);
                            connectProgress.setButton(DialogInterface.BUTTON_POSITIVE, "取消连接", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    connectProgress.dismiss();
                                    bluetoothService.stop();
                                }
                            });
                            connectProgress.setMessage("正在连接设备");
                            connectProgress.show();
                            break;

                        //连接失败
                        case STATE_LISTEN:
                            if (connectProgress != null) {
                                connectProgress.dismiss();
                                Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case STATE_NONE:
                            Toast.makeText(MainActivity.this, "断开连接", Toast.LENGTH_SHORT).show();
                            if (connectProgress != null) {
                                connectProgress.dismiss();
                            }

                            break;
                    }
                    break;

                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    break;

                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // 获取连接设备的名字
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(MainActivity.this, "连接至"
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        if (bluetoothService == null) {
            bluetoothService = app.getblue(mxHandler);
        }
    }


    //订阅消息
    @Subscribe
    public void OnEventMainThread(GotoEvent event) {
        if (null != event) {
            mainRbtnBackLog.setChecked(true);
            mIMainPresenter.ClickImg();

        }
    }

    //订阅消息
    @Subscribe
    public void OnEventMainThread(final MessageEvent event) {


        Log.e("msg_event", event.toString());
        if (null != event) {

            MainLooper.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mainRbtnMsg.getVisibility()!= View.GONE){
                    if(mainRbtnMsg!=null&&mainRbtnMsg.isChecked()){
                       app.setNum(0);
                    }else {
                        badgeView.setBadgeCount(app.getNum());
                    }
                    }
                        mainRbtnBackLog.setChecked(true);
                        mIMainPresenter.ClickImg();
                }
            });

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (!isout) {
                isout = true;
                Toast.makeText(this, "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                // 利用handler延迟发送更改状态信息
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessageDelayed(msg, 2000);

            } else {
                finish();
                System.exit(0);
            }

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
