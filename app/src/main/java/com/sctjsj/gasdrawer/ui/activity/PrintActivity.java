package com.sctjsj.gasdrawer.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.application.MyApp;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.CustomerBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.util.BluetoothService;
import com.sctjsj.gasdrawer.util.JumpCode;
import com.sctjsj.gasdrawer.util.PicFromPrintUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

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

//打印页面
@Route(path = "/main/act/PrintActivity")
public class PrintActivity extends BaseAppcompatActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.bt_print)
    Button btPrint;
    private MyApp app;
    private Bitmap bm;
    private BitmapFactory.Options options;
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                btPrint.setVisibility(View.VISIBLE);
            }
        }
    };
    //连接打印机相关-------------------------------------------------
    private ProgressDialog connectProgress;
    private BluetoothService service;
    private BluetoothAdapter bluetoothAdapter;
    private String mConnectedDeviceName;
    //-------------------------------------------------


    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "qm.png";
    //      打印纸一行最大的字节
    private static final int LINE_BYTE_SIZE = 32;
    //      打印三列时，中间一列的中心线距离打印纸左侧的距离
    private static final int LEFT_LENGTH = 16;
    //     打印三列时，中间一列的中心线距离打印纸右侧的距离
    private static final int RIGHT_LENGTH = 16;
    //   打印三列时，第一列汉字最多显示几个文字
    private static final int LEFT_TEXT_MAX_LENGTH = 8;
    private int printType;
    private int payment;
    private int ispay;

    private CustomerBean Cusbean;
    private List<ServerChildBean> list1;
    private List<ChildBean> childset;

    private WorkMessageBean bean;
    private HttpServiceImpl services;
    private int meter=0;
//    private BluetoothService service;

    private int materialExPrice, materialPrice, serverExPrice, serverPrice,expensesPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApp) getApplicationContext();
//         service = app.getserver();
        options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        printType = getIntent().getIntExtra("key", -1);
        services = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        switch (printType) {
            //打印安装订单
            case 1:
                meter=getIntent().getIntExtra("length",0);
                bm = BitmapFactory.decodeFile(path, options);
                Cusbean = (CustomerBean) getIntent().getSerializableExtra("Cusbean");
                list1 = (List<ServerChildBean>) getIntent().getSerializableExtra("list1");
                childset = (List<ChildBean>) getIntent().getSerializableExtra("childset");
                getdatainst();
                break;
            case 2:
                meter=getIntent().getIntExtra("length",0);
                payment = getIntent().getIntExtra("payment", 3);
                ispay = getIntent().getIntExtra("ispay", 2);
                bean = (WorkMessageBean) getIntent().getSerializableExtra("bean");
                list1 = (List<ServerChildBean>) getIntent().getSerializableExtra("list1");
                childset = (List<ChildBean>) getIntent().getSerializableExtra("childset");
                bm = BitmapFactory.decodeFile(path, options);
                getdatainst();
                break;
        }
    }

    //获取安装信息
    private void getdatainst() {
        Map<String, String> map = new HashMap<>();
        map.put("ctype", "workorder");
        if (printType == 1) {
            map.put("id", String.valueOf(Cusbean.getId()));
        } else if (printType == 2) {
            map.put("id", String.valueOf(bean.getId()));
        }
        services.doCommonPost(null, NetAddress.GAS_USERMSG, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    try {
                        Log.e("printA_onSuccess", result.toString());
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject o=object.getJSONObject("data");
                            materialExPrice = o.getInt("materialExPrice");
                            materialPrice = o.getInt("materialPrice");
                            serverExPrice = o.getInt("serverExPrice");
                            if(o.has("serverPrice")){
                            serverPrice = o.getInt("serverPrice");
                            }else {
                            serverPrice=0;
                            }
                            expensesPrice=o.getInt("expensesPrice");
                            Message m = Message.obtain();
                            m.what = 1;
                            hander.sendMessage(m);
                        } else {
                            Toast.makeText(PrintActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("printA_JSONException", e.toString());
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("printA_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("onFinishedA", "onFinished");
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


    @Override
    public int initLayout() {
        return R.layout.activity_print;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.repair_repairCheck_back, R.id.bt_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_repairCheck_back:
                finish();
                break;
            //打印
            case R.id.bt_print:
                autograph();
                break;
        }
    }


    //打印的方法
    private void autograph() {
        if(service.getState() != STATE_CONNECTED){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("打印机未连接")
                    .setMessage("是否连接打印机？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //跳转连接打印机
                            Intent intent1 = new Intent(PrintActivity.this, BTScanActivity.class);
                            intent1.putExtra("data", true);
                            startActivityForResult(intent1, JumpCode.JUMP_FROM_Index_TO_BTSCANActivity);
                        }
                    }).show();
        }else {
            switch (printType) {
                case 1:
                    //打印安装订单
                    printOrder1(1);
                    break;
                case 2:
                    //打印维修订单
                    printOrder(1);
                    break;
        }
        }
//        //默认打印2份
//        final int[] printCount = {-1};
//        //弹出选择框，让商家选择打印订单的分数
//        final AlertDialog.Builder builder = new AlertDialog.Builder(PrintActivity.this);
//
//        builder.setTitle("请选择打印份数");
//        String[] items = new String[]{"打印1份", "打印2份"};
//        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                //打印一份
//                if (0 == which) {
//                    printCount[0] = 1;
//                }
//                //打印两份
//                if (1 == which) {
//                    printCount[0] = 2;
//                }
//
//            }
//        });
//        builder.setPositiveButton("确认打印", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                if (printCount[0] < 1) {
//                    Toast.makeText(PrintActivity.this, "请选择打印份数", Toast.LENGTH_SHORT).show();
//                } else {
////                    判断是从哪个界面来的
//
//
//                }
//
//                printCount[0] = -1;
//            }
//        });
//        builder.show();
    }


    /**
     * 打印订单  维修打印
     */
    private void printOrder(int count) {
        if (bean != null && childset.size() > 0 && bm != null
                && payment != -1 && ispay != -1) {
            for (int x = 0; x < count; x++) {
                //title
                String start = "*** 维修作业单 ***\n\n";
                service.printCenter();
                service.printSize(0);
                sendPrintMessage(this, service, start);

//                //客户基本信息------------------------------
//                String cus = "*** 报修客户基本信息 ***\n";
//                service.printCenter();
//                service.printSize(0);
//                sendPrintMessage(this, service, cus);

                //报修人
                String job_number = "报 修 人：" + bean.getClientName() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, job_number);

                //受理编号
                String name = "受理编号：" + bean.getAcceptedNumber() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, name);

                //工单号
                String department = "工 单 号：" + bean.getOrderNo() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, department);

                //报修类别
                String s = "";
                if (bean.getOrderStatus() == 1) {
                    s = "分户安装";
                } else if (bean.getOrderStatus() == 2) {
                    s = "户内改造";
                } else if (bean.getOrderStatus() == 3) {
                    s = "燃气具维修";
                } else if (bean.getOrderStatus() == 4) {
                    s = "安检整改";
                } else if (bean.getOrderStatus() == 5) {
                    s = "其他";
                }
                String business_type = "报修类别：" + s + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, business_type);

                //报修时间
                String time = "报修时间：" + bean.getInsertTime() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, time);

                //联系电话
                String tel = "联系电话：" + bean.getTel() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, tel);

                //用户地址
                String adress = "用户地址：" + bean.getAddress() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, adress);
                //客户基本信息------------------------------

                //派工信息------------------------------------
                //维修人
                String weixiu = "维 修 人：" + (String) SPFUtil.get(Tag.TAG_NAME, "不详") + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, weixiu);
                //报修简诉
                String reson = "报修简诉：" + bean.getRepairReason() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, reson);

                //派工时间
                String times = "派工时间：" + bean.getInstallTime() + "\n\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, times);


                //派工信息------------------------------------


                //材料使用清单--------------------------------------------
//                String material = "*** 材料使用清单 ***\n";
//                service.printCenter();
//                service.printSize(0);
//                sendPrintMessage(this, service, material);

                //top
                String num = "货物编号";
                String mamess = "名称";
//                String UnitPrice="  单价";
//                String dan = "免费用量";
                String geshu = "应收量"+"\n";
//                String all = "应收金额" + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, printThreeData(num, mamess,geshu));
//                sendPrintMessage(this, service, printThreeData(UnitPrice,dan+"        "+geshu, all));

                //遍历列表
                for (int i = 0; i < childset.size(); i++) {
                    ChildBean childBean = childset.get(i);
                    if (i == childset.size()) {
                        service.printLeft();
                        service.printSize(0);
                        sendPrintMessage(this, service, printThreeData(childBean.getId() + "", childBean.getMaterialName(), childBean.getCount())+"\n\n");
//                        sendPrintMessage(this, service, printThreeData("    " + childBean.getSellingPrice() + "", childBean.getFreeCount()+"              "+childBean.getCount(), childBean.getAll() + "\n\n"));

                    }
                    service.printLeft();
                    service.printSize(0);
                    sendPrintMessage(this, service, printThreeData(childBean.getId() + "", childBean.getMaterialName(), childBean.getCount())+"\n\n");
//                    sendPrintMessage(this, service, printThreeData("    " + childBean.getSellingPrice() + "", childBean.getFreeCount()+"        "+childBean.getCount(), childBean.getAll() + "\n\n"));
                }
                //材料使用清单--------------------------------------------


                if(list1.size()>0) {
//                    //服务项目费用----------------------------------------------
//                    String sever = "*** 服务项目费用 ***\n";
//                    service.printCenter();
//                    service.printSize(0);
//                    sendPrintMessage(this, service, sever);

                    //top
                    //top
                    String id = "服务费编码";
                    String servername = "名称";
//                String money = "单价";
//                String number = "数量(个)";
                    String allmoney = "应收金额" + "\n";
                    service.printLeft();
                    service.printSize(0);
                    sendPrintMessage(this, service, printThreeData(id, servername, allmoney));
//                sendPrintMessage(this, service, printThreeData(money, number, allmoney));

                    //遍历列表
                    for (int i = 0; i < list1.size(); i++) {
                        ServerChildBean serverChildBean = list1.get(i);
                        if (i == list1.size()) {
                            service.printLeft();
                            service.printSize(0);
                            sendPrintMessage(this, service, printThreeData(serverChildBean.getId() + "", serverChildBean.getProName(), serverChildBean.getEditext() + "元\n\n"));
//                        sendPrintMessage(this, service, printThreeData("    " + serverChildBean.getPrice() + "", serverChildBean.getUnit(), serverChildBean.getEditext() + "\n\n"));

                        }
                        service.printLeft();
                        service.printSize(0);
                        sendPrintMessage(this, service, printThreeData(serverChildBean.getId() + "", serverChildBean.getProName(), serverChildBean.getEditext() + "\n\n"));
//                    sendPrintMessage(this, service, printThreeData("    " + serverChildBean.getPrice() + "", serverChildBean.getUnit(), serverChildBean.getEditext() + "\n\n"));
                    }
                }

                //安装工程量
                String InstallationCount = "安装工程量：" + meter+ "米\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, InstallationCount);


                //应收实收明细------------------------------------
                String receivable = "应收材料费：" + materialPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, receivable);

                String netreceipts = "实收材料费：" + materialExPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, netreceipts);

                String receivableserver = "应收服务费：" + serverPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, receivableserver);

                String netreceiptsserver = "实收服务费：" + serverExPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, netreceiptsserver);

                String netreceiptsall = "实收总金额：" + expensesPrice+ "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, netreceiptsall);
                //应收实收明细------------------------------------


                //支付方式-----------------------------------
                String s2 = "";
                switch (payment) {
                    case 1:
                        s2 = "扫码支付";
                        break;
                    case 2:
                        s2 = "维修人员代收";
                        break;
                    case 3:
                        s2 = "营业厅缴费";
                        break;
                }
                String payments = "收费  方式：" + s2 + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, payments);

                //是否收费
                String s3 = "";
                switch (ispay) {
                    case 1:
                        s3 = "已收费";
                        break;
                    case 2:
                        s3 = "未收费";
                        break;
                }
                String ispays = "是否已收费：" + s3 + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, ispays);
                //支付方式-----------------------------------

                //签名----------------------------------------------
                String names = "*** 签名 ***\n\n";
                service.printCenter();
                service.printSize(0);
                sendPrintMessage(this, service, names);

//                //签名图片打印
                Bitmap bitmap = PicFromPrintUtils.compressPic240(bm);
                byte[] bytes = PicFromPrintUtils.draw2PxPoint240(bitmap);

//                PrintPic instance = PrintPic.getInstance();
//                instance.init(bm);
//                byte[] bytes1 = instance.printDraw();
                sendPrintImag(this, service, bytes);

                //签名----------------------------------------------

                //尾部---------------------------------------------
                //地址
                String ads = "\n\n" + "蓬溪港华燃气有限公司" + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, ads);

                //电话
                String serverph = "服务电话：5428456" + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, serverph);

                //提示
                String tip = "凭本小票到蓬溪港华燃气有限公司(南门口)客户中心缴费" + "\n\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, tip);

                //尾部---------------------------------------------

                String end = "\n\n*** END ***\n\n\n\n\n\n";
                service.printCenter();
                service.printSize(0);
                sendPrintMessage(this, service, end);

            }

        } else {
            Toast.makeText(this, "打印信息不完整", Toast.LENGTH_SHORT).show();
        }

    }


    //打印安装订单

    /**
     * 打印订单
     */
    private void printOrder1(int count) {
        if (Cusbean != null && list1 != null && childset != null && bm != null) {
            for (int x = 0; x < count; x++) {
                //title
                String start = "*** 材料及工程量清单 ***\n\n";
                service.printCenter();
                service.printSize(0);
                sendPrintMessage(this, service, start);

//                //客户基本信息------------------------------
//                String cus = "*** 客户基本信息 ***\n";
//                service.printCenter();
//                service.printSize(0);
//                sendPrintMessage(this, service, cus);

                //业务类型
                String s = "";
                if (Cusbean.getOrderStatus() == 1) {
                    s = "分户安装";
                } else if (Cusbean.getOrderStatus() == 2) {
                    s = "户内改造";
                } else if (Cusbean.getOrderStatus() == 3) {
                    s = "燃气具维修";
                } else if (Cusbean.getOrderStatus() == 4) {
                    s = "安检整改";
                }
                String business_type = "业务类型：" + s + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, business_type);

                //工作单号
                String job_number = "工作单号：" + Cusbean.getOrderNo() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, job_number);

                //客户名称
                String name = "客户名称：" + Cusbean.getClientName() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, name);

                //部门
                String department = "部    门：" + Cusbean.getDepartName() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, department);

                //日期
                String data = "日    期：" + Cusbean.getInstallTime() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, data);

                //CEA 号
                String CEA = "CEA   号：" + Cusbean.getCAENo() + "\n";
                service.printLeft();
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, CEA);

                //电话
                String phone = "电    话：" + Cusbean.getTel() + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, phone);

                //地址
                String adress = "地    址：" + Cusbean.getAddress() + "\n\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, adress);
                //客户基本信息------------------------------


//                //材料使用清单--------------------------------------------
//                String material = "*** 材料使用清单 ***\n";
//                service.printCenter();
//                service.printSize(0);
//                sendPrintMessage(this, service, material);

                //top
                String num = "货物编号";
                String mamess = "名称";
//                String UnitPrice="  单价";
//                String dan = "免费用量";
                String geshu = "应收量" + "\n";
//                String all = "应收金额" + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, printThreeData(num, mamess,geshu));
//                sendPrintMessage(this, service, printThreeData(UnitPrice,dan+"        "+geshu,all));

                //遍历列表
                for (int i = 0; i < childset.size(); i++) {
                    ChildBean childBean = childset.get(i);
                    if (i == childset.size()) {
                        service.printLeft();
                        service.printSize(0);
                        sendPrintMessage(this, service, printThreeData(childBean.getId()+"", childBean.getMaterialName(),childBean.getCount()+"\n\n"));
//                        sendPrintMessage(this, service, printThreeData("    " + childBean.getSellingPrice() + "", childBean.getFreeCount()+"            "+childBean.getCount(), childBean.getAll() + "\n\n"));

                    }
                    service.printLeft();
                    service.printSize(0);
                    sendPrintMessage(this, service, printThreeData(childBean.getId()+"", childBean.getMaterialName(),childBean.getCount()+"\n\n"));
//                    sendPrintMessage(this, service, printThreeData("    " + childBean.getSellingPrice() + "", childBean.getFreeCount()+"            "+childBean.getCount(), childBean.getAll() + "\n\n"));
                }
                //材料使用清单--------------------------------------------


//                //服务项目费用----------------------------------------------
//                String sever = "*** 服务项目费用 ***\n";
//                service.printCenter();
//                service.printSize(0);
//                sendPrintMessage(this, service, sever);

                //top
                String id = "服务费编码";
                String servername = "名称";
//                String money = "单价";
//                String number = "数量(个)";
                String allmoney = "应收金额" + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, printThreeData(id,servername,allmoney));
//                sendPrintMessage(this, service, printThreeData(money, number, allmoney));

                //遍历列表
                for (int i = 0; i < list1.size(); i++) {
                    ServerChildBean serverChildBean = list1.get(i);
                    if (i == list1.size()) {
                        service.printLeft();
                        service.printSize(0);
                        sendPrintMessage(this, service, printThreeData(serverChildBean.getId()+"", serverChildBean.getProName(),serverChildBean.getEditext() + "\n\n"));
//                        sendPrintMessage(this, service, printThreeData("    " + serverChildBean.getPrice() + "", serverChildBean.getUnit(), serverChildBean.getEditext() + "\n\n"));

                    }
                    service.printLeft();
                    service.printSize(0);
                    sendPrintMessage(this, service, printThreeData(serverChildBean.getId()+"", serverChildBean.getProName(),serverChildBean.getEditext() + "\n\n"));
//                    sendPrintMessage(this, service, printThreeData("    " + serverChildBean.getPrice() + "", serverChildBean.getUnit(), serverChildBean.getEditext() + "\n\n"));
                }


                //安装工程量
                String InstallationCount = "安装工程量：" + meter+ "米\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, InstallationCount);

                //应收实收明细------------------------------------
                String receivable = "应收材料费：" + materialPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, receivable);

                String netreceipts = "实收材料费：" + materialExPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, netreceipts);

                String receivableserver = "应收服务费：" + serverPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, receivableserver);

                String netreceiptsserver = "实收服务费：" + serverExPrice + "元\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, netreceiptsserver);

                String netreceiptsall = "实收总金额：" + expensesPrice+ "元\n\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, netreceiptsall);
                //应收实收明细------------------------------------

                //签名----------------------------------------------


                String names = "*** 签名 ***\n\n";
                service.printCenter();
                service.printSize(0);
                sendPrintMessage(this, service, names);

//                //签名图片打印
                Bitmap bitmap = PicFromPrintUtils.compressPic240(bm);
                byte[] bytes = PicFromPrintUtils.draw2PxPoint240(bitmap);
                sendPrintImag(this, service, bytes);

                //签名----------------------------------------------


                //尾部---------------------------------------------
                //地址
                String ads = "\n\n"+ "蓬溪港华燃气有限公司" + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, ads);

                //电话
                String serverph = "服务电话：5428456" + "\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, serverph);

                //提示
                String tip = "凭本小票到蓬溪港华燃气有限公司(南门口)客户中心缴费"+ "\n\n";
                service.printLeft();
                service.printSize(0);
                sendPrintMessage(this, service, tip);

                //尾部---------------------------------------------

                String end = "\n\n*** END ***\n\n\n\n";
                service.printCenter();
                service.printSize(0);
                sendPrintMessage(this, service, end);

            }

        } else {
            Toast.makeText(this, "打印信息有误", Toast.LENGTH_SHORT).show();
        }

    }


    //打印相关方法-------------------------------------------------
    //     获取数据长度
    @SuppressLint("NewApi")
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 打印两列
     *
     * @param leftText  左侧文字
     * @param rightText 右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printTwoData(String leftText, String rightText) {
        StringBuilder sb = new StringBuilder();
        int leftTextLength = getBytesLength(leftText);
        int rightTextLength = getBytesLength(rightText);
        sb.append(leftText);

        // 计算两侧文字中间的空格
        int marginBetweenMiddleAndRight = LINE_BYTE_SIZE - leftTextLength - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }
        sb.append(rightText);
        return sb.toString();
    }

    /**
     * 打印三列
     *
     * @param leftText   左侧文字
     * @param middleText 中间文字
     * @param rightText  右侧文字
     * @return
     */
    @SuppressLint("NewApi")
    public static String printThreeData(String leftText, String middleText, String rightText) {
        StringBuilder sb = new StringBuilder();
        // 左边最多显示 LEFT_TEXT_MAX_LENGTH 个汉字 + 两个点
        if (leftText.length() > LEFT_TEXT_MAX_LENGTH) {
            leftText = leftText.substring(0, LEFT_TEXT_MAX_LENGTH) + "..";
        }
        int leftTextLength = getBytesLength(leftText);
        int middleTextLength = getBytesLength(middleText);
        int rightTextLength = getBytesLength(rightText);

        sb.append(leftText);
        // 计算左侧文字和中间文字的空格长度
        int marginBetweenLeftAndMiddle = LEFT_LENGTH - leftTextLength - middleTextLength / 2;

        for (int i = 0; i < marginBetweenLeftAndMiddle; i++) {
            sb.append(" ");
        }
        sb.append(middleText);

        // 计算右侧文字和中间文字的空格长度
        int marginBetweenMiddleAndRight = RIGHT_LENGTH - middleTextLength / 2 - rightTextLength;

        for (int i = 0; i < marginBetweenMiddleAndRight; i++) {
            sb.append(" ");
        }

        // 打印的时候发现，最右边的文字总是偏右一个字符，所以需要删除一个空格
        sb.delete(sb.length() - 1, sb.length()).append(rightText);
        return sb.toString();
    }

    /**
     * 打印
     *
     * @param message
     */
    public void sendPrintMessage(Context context, BluetoothService bluetoothService, String message) {

        if (bluetoothService == null) {
            return;
        }

        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != STATE_CONNECTED) {
            Toast.makeText(context, "打印机未连接", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send;
            try {
                send = message.getBytes("GB2312");
            } catch (UnsupportedEncodingException e) {
                send = message.getBytes();
            }

            bluetoothService.write(send);
        }
    }

    public void sendPrintImag(Context context, BluetoothService bluetoothService, byte[] data) {

        if (bluetoothService == null) {
            return;
        }

        // Check that we're actually connected before trying anything
        if (bluetoothService.getState() != STATE_CONNECTED) {
            Toast.makeText(context, "打印机未连接", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (data.length > 0) {
            bluetoothService.write(data);
        }
    }
    //打印相关方法-------------------------------------------------


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
                    service.connect(device);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!bluetoothAdapter.isEnabled()) {
            //打开蓝牙
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        if (service == null) {
            service = app.getblue(mxHandler);
        }
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
                            Toast.makeText(PrintActivity.this, "蓝牙连接成功", Toast.LENGTH_SHORT).show();

                            break;
                        //正在连接
                        case STATE_CONNECTING:
                            if (connectProgress == null) {
                                connectProgress = new ProgressDialog(PrintActivity.this);
                            }
                            connectProgress.setTitle("");
                            connectProgress.setCancelable(false);
                            connectProgress.setButton(DialogInterface.BUTTON_POSITIVE, "取消连接", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    connectProgress.dismiss();
                                    service.stop();
                                }
                            });
                            connectProgress.setMessage("正在连接设备");
                            connectProgress.show();
                            break;

                        //连接失败
                        case STATE_LISTEN:
                            if (connectProgress != null) {
                                connectProgress.dismiss();
                                Toast.makeText(PrintActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case STATE_NONE:
                            Toast.makeText(PrintActivity.this, "断开连接", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(PrintActivity.this, "连接至"
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
