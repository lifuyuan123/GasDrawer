package com.sctjsj.gasdrawer.ui.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.util.BTStateChangeEvent;
import com.sctjsj.gasdrawer.util.BluetoothReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 蓝牙扫描连接页面
 */
public class BTScanActivity extends AppCompatActivity {
    @BindView(R.id.lv_linked_device_list)
    ListView mLVLinked;
    @BindView(R.id.lv_extra_device_list)
    ListView mLVExtra;
    /**
     * 常量
     */
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    //扫描进度
    private AlertDialog.Builder builder;
//    private SweetAlertDialog scanProgress;
//    private SweetAlertDialog bindProgress;

    private ProgressDialog bindProgress;
    private ProgressDialog scanProgress;
    private BluetoothReceiver bluetoothReceiver;
    private BluetoothAdapter bluetoothAdapter;

    //数据
    private List<BluetoothDevice> extraDevice = new ArrayList<>();
    private List<BluetoothDevice> linkedDevice = new ArrayList<>();

    //Filter
    private IntentFilter stateFilter;
    private IntentFilter foundFilter;
    private IntentFilter bindFilter;
    private IntentFilter startDiscoveryFilter;
    private IntentFilter finishDiscoveryFilter;

    //适配器
    private LinkedAdapter linkedAdapter;
    private ExtraAdapter extraAdapter;
    private IntentFilter intentFilter;
    private boolean data;

//    //Listview
//    @Bind(R.id.lv_extra_device_list)ListView mLVExtra;
//    @Bind(R.id.lv_linked_device_list)ListView mLVLinked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btscan);
        ButterKnife.bind(this);
        //注册事件总线
        EventBus.getDefault().register(this);
        /**
         * 注册蓝牙适配器状态变化监听器
         */

        //main跳转过来的
        data = getIntent().getBooleanExtra("data", false);

        //蓝牙配对广播
        bindFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        //状态改变广播
        stateFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        //发现设备广播
        foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //开始扫描广播
        startDiscoveryFilter = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_STARTED");
        //结束扫描广播
        finishDiscoveryFilter = new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED");

        bluetoothReceiver = new BluetoothReceiver();
//        registerReceiver(bluetoothReceiver, bindFilter);
//        registerReceiver(bluetoothReceiver, stateFilter);
//        registerReceiver(bluetoothReceiver, foundFilter);
//        registerReceiver(bluetoothReceiver, startDiscoveryFilter);
//        registerReceiver(bluetoothReceiver, finishDiscoveryFilter);
        intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_STARTED");
        intentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
        registerReceiver(bluetoothReceiver,intentFilter);

        linkedAdapter = new LinkedAdapter();
        mLVLinked.setAdapter(linkedAdapter);

        //配对设备去连接
        mLVLinked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //address
                String address = linkedDevice.get(position).getAddress();
                //已连接
                Intent intent = new Intent(BTScanActivity.this, InstalActivity.class);
                intent.putExtra("device", address);
                setResult(RESULT_OK, intent);
                if(data){
                    finish();
                }else {
                    startActivity(intent);
                    finish();
                }

            }
        });

        //长按已配对的设备删除配对
//        mLVLinked.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                final SweetAlertDialog dialog=new SweetAlertDialog(BTScanActivity.this,SweetAlertDialog.WARNING_TYPE);
//                dialog.setTitleText("");
//                dialog.setContentText("取消和该设备的配对？");
//                dialog.setConfirmText("确认");
//                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                    @Override
//                    public void onClick(SweetAlertDialog sweetAlertDialog) {
//                        String address=extraDevice.get(position).getAddress();
//                        if(bluetoothAdapter==null){
//                            bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
//                        }
//                        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
//                        if(device!=null){
//                            Log.e("gg","device!=null");
//                            boolean result=removeBond(device);
//                            if(result){
//                                Log.e("gg","device移除成功");
//                                gotoScan();
//                            }
//                        }else {
//                            Log.e("gg","device=null");
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//                return true;
//            }
//        });

        //未配对设备去配对
        mLVExtra.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                ProgressDialog dialog1=new ProgressDialog(BTScanActivity.this);
                dialog1.setTitle("");
                dialog1.setMessage("确认和该设备的配对？");
                dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String address = extraDevice.get(position).getAddress();
                        if (bluetoothAdapter == null) {
                            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        }
                        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                        boolean result = device.createBond();

                        Toast.makeText(BTScanActivity.this, "创建蓝牙配对" + (result == true ? "成功" : "失败"), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog1.show();
            }
        });

        extraAdapter = new ExtraAdapter();
        mLVExtra.setAdapter(extraAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gotoScan();
    }

    @Override
    protected void onDestroy() {
        //注销广播
//        this.unregisterReceiver(bluetoothReceiver);
        bluetoothAdapter.cancelDiscovery();
        super.onDestroy();
    }

    @OnClick({R.id.act_bt_scan_back, R.id.act_bt_scan_rv_scan_device})
    public void btScanClick(View v) {
        switch (v.getId()) {
            case R.id.act_bt_scan_back:
                finish();
                break;
            case R.id.act_bt_scan_rv_scan_device:
                registerReceiver(bluetoothReceiver,intentFilter);
                gotoScan();
                break;

        }
    }

    /**
     * 回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (RESULT_OK == resultCode) {
                    gotoScan();
                }
                break;
        }
    }

    /**
     * 扫描蓝牙设备
     */
    private void gotoScan() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //判断是否支持蓝牙
        if (bluetoothAdapter == null) {
            final ProgressDialog dialog1=new ProgressDialog(this);
            dialog1.setTitle("错误");
            dialog1.setMessage("当前设备不支持蓝牙功能");
            dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog1.dismiss();
                }
            });
            dialog1.show();
            return;
        }

        //判断蓝牙是否开启
        if (!bluetoothAdapter.isEnabled()) {
            //申请去开启蓝牙，回调处理
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 0);
            return;
        }

        //开启扫描设备
        bluetoothAdapter.startDiscovery();

    }

    /**
     * 利用反射取消蓝牙绑定
     *
     * @param device
     * @return
     */
    private boolean removeBond(BluetoothDevice device) {
        if (device == null) {
            return false;
        }
        Class btDeviceCls = BluetoothDevice.class;
        Method removeBond = null;
        try {

            removeBond = btDeviceCls.getMethod("removeBond");

            removeBond.setAccessible(true);
            boolean result = (boolean) removeBond.invoke(device);
            Log.e("gg", "调用结果" + result);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    @OnClick(R.id.act_bt_scan_back)
    public void onViewClicked() {
    }


    /**
     * 适配器
     */
    class LinkedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return linkedDevice.size();
        }

        @Override
        public Object getItem(int position) {
            return linkedDevice.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinkedHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(BTScanActivity.this).inflate(R.layout.item_bluetooth_device_list, null);
                holder = new LinkedHolder();

                holder.mTVName = (TextView) convertView.findViewById(R.id.item_bluetooth_device_list_tv_name);
                holder.mTVMac = (TextView) convertView.findViewById(R.id.item_bluetooth_device_list_tv_mac);
                holder.mTVState = (TextView) convertView.findViewById(R.id.item_bluetooth_device_list_tv_state);

                convertView.setTag(holder);
            } else {
                holder = (LinkedHolder) convertView.getTag();
            }

            holder.mTVName.setText("设备名：" + linkedDevice.get(position).getName());
            holder.mTVMac.setText("MAC：" + linkedDevice.get(position).getAddress());

            if (BluetoothDevice.BOND_BONDED == linkedDevice.get(position).getBondState()) {
                holder.mTVState.setText("已配对");
            }

            if (BluetoothDevice.BOND_NONE == linkedDevice.get(position).getBondState()) {
                holder.mTVState.setText("未配对");
            }

            return convertView;
        }

        class LinkedHolder {
            TextView mTVName, mTVMac, mTVState;
        }

    }

    class ExtraAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return extraDevice.size();
        }

        @Override
        public Object getItem(int position) {
            return extraDevice.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinkedHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(BTScanActivity.this).inflate(R.layout.item_bluetooth_device_list, null);
                holder = new LinkedHolder();

                holder.mTVName = (TextView) convertView.findViewById(R.id.item_bluetooth_device_list_tv_name);
                holder.mTVMac = (TextView) convertView.findViewById(R.id.item_bluetooth_device_list_tv_mac);
                holder.mTVState = (TextView) convertView.findViewById(R.id.item_bluetooth_device_list_tv_state);

                convertView.setTag(holder);
            } else {
                holder = (LinkedHolder) convertView.getTag();
            }

            holder.mTVName.setText("设备名：" + extraDevice.get(position).getName());
            holder.mTVMac.setText("MAC：" + extraDevice.get(position).getAddress());

            if (BluetoothDevice.BOND_BONDED == extraDevice.get(position).getBondState()) {
                holder.mTVState.setText("已配对");
            }

            if (BluetoothDevice.BOND_NONE == extraDevice.get(position).getBondState()) {
                holder.mTVState.setText("未配对");
            }

            return convertView;
        }

        class LinkedHolder {
            TextView mTVName, mTVMac, mTVState;
        }

    }


    /**
     * 订阅消息
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(BTStateChangeEvent event) {
        if (event != null) {
            Log.e("eeeee","eeeee");
            Intent intent = event.getIntent();

            String action = intent.getAction();
            Log.e("eeeeeaction",action);
            //扫描开始
            if ("android.bluetooth.adapter.action.DISCOVERY_STARTED".equals(action)) {
                Log.e("eeeeeaction","开始扫描");
                //清除原来的数据
                linkedDevice.clear();
                extraDevice.clear();
                //扫描进度

                if (scanProgress == null) {
                    Log.e("eeeeeaction","开始扫描空");
                    scanProgress=new ProgressDialog(this);
                }
                Log.e("eeeeeaction","开始扫描dilog");
                scanProgress.setTitle("正在扫描可用设备");
                scanProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanProgress.dismiss();
                    }
                });
                scanProgress.setMessage("正在扫描中");
                scanProgress.setCancelable(false);
                scanProgress.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                scanProgress.show();
            }
            //扫描结束
            if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                Log.e("eeeeeaction","结束扫描");
                if (scanProgress != null) {
                    scanProgress.dismiss();
                    bluetoothAdapter.cancelDiscovery();
                }

                linkedAdapter.notifyDataSetChanged();
                extraAdapter.notifyDataSetChanged();
                unregisterReceiver(bluetoothReceiver);

            }

            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.e("eeeeeaction","找到设备");
                //获取设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    int state = device.getBondState();
                    //已配对设备
                    if (12 == state) {
                        if (!linkedDevice.contains(device)) {
                            linkedDevice.add(device);
                            linkedAdapter.notifyDataSetChanged();
                        }
                    }
                    //未配对设备
                    if (10 == state) {
                        if (!extraDevice.contains(device)) {
                            extraDevice.add(device);
                            extraAdapter.notifyDataSetChanged();
                        }
                    }

                }
            }
            //绑定设备
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                Log.e("eeeeeaction","绑定设备");
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                if (bindProgress == null) {
                    bindProgress = new ProgressDialog(this);
                }
                switch (state) {
                    case BluetoothDevice.BOND_NONE:
                        bindProgress.dismiss();
                        Toast.makeText(this, "删除配对", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        bindProgress.setMessage("正在配对中");
                        bindProgress.setTitle("");
                        bindProgress.show();
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        bindProgress.dismiss();
                        Toast.makeText(this, "配对成功", Toast.LENGTH_SHORT).show();
                        //重新扫描设备
                        gotoScan();
                        break;

                }
            }
        }
    }

}

