package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.sctjsj.basemodule.base.HttpTask.XCacheCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.ChildBean;
import com.sctjsj.gasdrawer.entity.GroupBean;
import com.sctjsj.gasdrawer.entity.javaBean.CustomerBean;
import com.sctjsj.gasdrawer.entity.javaBean.NetAddress;
import com.sctjsj.gasdrawer.entity.javaBean.ServerChildBean;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.model.InstalModule;
import com.sctjsj.gasdrawer.model.RepairPictureModule;
import com.sctjsj.gasdrawer.presenter.adapter.SelectMaterialAdapter;
import com.sctjsj.gasdrawer.presenter.adapter.ServerAdapter;
import com.sctjsj.gasdrawer.ui.CustomView.PopupDialog;
import com.sctjsj.gasdrawer.util.BluetoothService;
import com.sctjsj.gasdrawer.util.CacheUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/user/main/act/instalActivity")
public class InstalActivity extends BaseAppcompatActivity implements InstalModule.InstalModuleCallBack, TakePhoto.TakeResultListener, InvokeListener, RepairPictureModule.RepairPictureModuleCallback {
    @BindView(R.id.Instalact_Img_back)
    ImageView InstalactImgBack;
    @BindView(R.id.Instalact_Txt_YWtype)
    TextView InstalactTxtYWtype;
    @BindView(R.id.Instalact_Txt_WorkNumber)
    TextView InstalactTxtWorkNumber;
    @BindView(R.id.Instalact_Txt_UName)
    TextView InstalactTxtUName;
    @BindView(R.id.Instalact_Txt_dep)
    TextView InstalactTxtDep;
    @BindView(R.id.Instalact_Txt_date)
    TextView InstalactTxtDate;
    @BindView(R.id.Instalact_Txt_CEA)
    TextView InstalactTxtCEA;
    @BindView(R.id.Instalact_Txt_Phone)
    TextView InstalactTxtPhone;
    @BindView(R.id.Instalact_Txt_Loc)
    TextView InstalactTxtLoc;
    @BindView(R.id.Instalact_Img_CLAdd)
    ImageView InstalactImgCLAdd;
    @BindView(R.id.Instalact_LtV_CL)
    ListView InstalactLtVCL;
    @BindView(R.id.Instalact_Img_SerAdd)
    ImageView InstalactImgSerAdd;
    @BindView(R.id.Instalact_LtV_Ser)
    ListView InstalactLtVSer;
    @BindView(R.id.Instalact_Btn_sig)
    Button InstalactBtnSig;
    @BindView(R.id.activity_instal)
    LinearLayout activityInstal;
    @BindView(R.id.Instalact_Img_Sign)
    ImageView InstalactImgSign;
    @BindView(R.id.Instalact_Img_autograph)
    ImageView InstalactImgAutograph;

    @Autowired(name = "data")
    WorkMessageBean bean;
    @BindView(R.id.instal_up)
    TextView instalUp;
    @BindView(R.id.Instalact_linear)
    LinearLayout InstalactLinear;
    @BindView(R.id.Instalact_Iv_add)
    ImageView InstalactIvAdd;
    @BindView(R.id.Instalact_Img_plan)
    ImageView InstalactImgPlan;
    @BindView(R.id.Instalact_Iv_add2)
    ImageView InstalactIvAdd2;
    @BindView(R.id.Instalact_Img_plan2)
    ImageView InstalactImgPlan2;
    @BindView(R.id.Instalact_linear_plan)
    LinearLayout InstalactLinearPlan;
    private InvokeParam invokeParam;
    private Bitmap bitmap = null;
    private TakePhoto takePhoto;
    private File file = null;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_server_change)
    TextView tvServerChange;
    private InstalModule module = new InstalModule(this, this);
    private RepairPictureModule moduless = new RepairPictureModule(this);
    private CustomerBean Cusbean;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "qm.png";

    private List<GroupBean> list = new ArrayList<>();
    private List<List<ChildBean>> childlist = new ArrayList<>();
    private List<List<ServerChildBean>> serverChildlist = new ArrayList<>();
    private boolean flag = false;
    private List<ChildBean> childset;
    private List<ServerChildBean> list1;
    private RepairPictureModule modules;
    private Bitmap bm;
    private String imgId, imgIdPlan,imagIdPlan2;
    private int distype, distypeSer;

    private int ispay, payment;

    private int length;
    private BluetoothService service;
    //上传图片类型  1.签名   2.规划图
    private int type = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getCusData();
        modules = new RepairPictureModule(this);
//        获取服务和材料清单数据
        getServerAndMaterialData();

    }

    //获取用户数据
    private void getCusData() {
        module.getCustomerMsg(bean.getId());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_instal;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.Instalact_Img_back, R.id.Instalact_Img_CLAdd, R.id.Instalact_Img_SerAdd, R.id.Instalact_linear,
            R.id.tv_change, R.id.tv_server_change, R.id.Instalact_linear_plan, R.id.instal_up,R.id.Instalact_linear_plan2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Instalact_Img_back:
                finish();
                break;
            //材料使用清单
            case R.id.Instalact_Img_CLAdd:
                Intent mIntent = new Intent(this, MaterialUselistActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("list", (Serializable) list);
                mBundle.putSerializable("data", (Serializable) childlist);
                mBundle.putSerializable("type", 1);
                mIntent.putExtra("key", mBundle);
                startActivityForResult(mIntent, 100);
                break;
            //服务项目费用
            case R.id.Instalact_Img_SerAdd:
                Intent intent = new Intent(this, ChooseCostActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);
                bundle.putSerializable("data", (Serializable) serverChildlist);
                intent.putExtra("key", bundle);
                startActivityForResult(intent, 100);
                break;
            //签名
            case R.id.Instalact_linear:
                startActivityForResult(new Intent(InstalActivity.this, AutographActivity.class), 100);
                break;
            //材料修改
            case R.id.tv_change:
                Intent intent1 = new Intent(this, MaterialUselistActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("list", (Serializable) list);
                bundle1.putSerializable("data", (Serializable) childlist);
                intent1.putExtra("key", bundle1);
                startActivityForResult(intent1, 100);
                break;
            //服务修改
            case R.id.tv_server_change:
                Intent intent2 = new Intent(this, ChooseCostActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("list", (Serializable) list);
                bundle2.putSerializable("data", (Serializable) serverChildlist);
                intent2.putExtra("key", bundle2);
                startActivityForResult(intent2, 100);
                break;
            //获取规划图片
            case R.id.Instalact_linear_plan:
                createPop();
                type=2;
                break;
            //上传
            case R.id.instal_up:
                upLoadData();
                break;
            case R.id.Instalact_linear_plan2:
                createPop();
                type=3;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            switch (resultCode) {
                case 102:
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    bm = BitmapFactory.decodeFile(path, options);
                    //上传签名图片
                    File file = new File(path);
                    type = 1;
                    modules.UpImg(file);
                    break;
                //材料
                case 103:
                    Bundle data1 = data.getBundleExtra("data");
                    childset = (List<ChildBean>) data1.getSerializable("data");
                    distype = data.getIntExtra("distype", 2);
                    SelectMaterialAdapter adapter = new SelectMaterialAdapter(childset, this);
                    InstalactLtVCL.setAdapter(adapter);
                    ListViewUtil.setListViewHeightBasedOnChildren(InstalactLtVCL);
                    InstalactLtVCL.setVisibility(View.VISIBLE);
                    InstalactImgCLAdd.setVisibility(View.GONE);
                    tvChange.setVisibility(View.VISIBLE);
                    Log.e("diatype", distype + "");
                    break;
                //服务
                case 101:
                    Bundle data2 = data.getBundleExtra("data");
                    list1 = (List<ServerChildBean>) data2.getSerializable("data");
                    distypeSer = data.getIntExtra("distype", 2);
                    length = data.getIntExtra("materialall", 0);
                    ServerAdapter adapter1 = new ServerAdapter(list1, this);
                    InstalactLtVSer.setAdapter(adapter1);
                    ListViewUtil.setListViewHeightBasedOnChildren(InstalactLtVSer);
                    InstalactLtVSer.setVisibility(View.VISIBLE);
                    InstalactImgSerAdd.setVisibility(View.GONE);
                    tvServerChange.setVisibility(View.VISIBLE);
                    Log.e("diatype", distypeSer + "  length" + length);
                    break;
            }
        }
    }

    //获取到用户数据的回调
    @Override
    public void getData(CustomerBean data) {
        this.Cusbean = data;
        if (Cusbean.getOrderStatus() == 1) {
            InstalactTxtYWtype.setText("分户安装");
        } else if (Cusbean.getOrderStatus() == 2) {
            InstalactTxtYWtype.setText("户内改造");
        } else if (Cusbean.getOrderStatus() == 3) {
            InstalactTxtYWtype.setText("燃气具维修");
        } else if (Cusbean.getOrderStatus() == 4) {
            InstalactTxtYWtype.setText("安检整改");
        }
        InstalactTxtWorkNumber.setText(Cusbean.getOrderNo());
        InstalactTxtUName.setText(Cusbean.getDepartName());
        InstalactTxtDep.setText(Cusbean.getInsertTime());
        InstalactTxtDate.setText(Cusbean.getInstallTime());
        InstalactTxtCEA.setText(Cusbean.getCAENo());
        InstalactTxtPhone.setText(Cusbean.getTel());
        InstalactTxtLoc.setText(Cusbean.getAddress());
    }

    //获取服务和材料清单数据
    private void getServerAndMaterialData() {
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
                            serverChildlist.add(serverlist);
                            childlist.add(childBeanList);
                            list.add(bean);
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

            }

            @Override
            public boolean onCache(Object result) {
                return false;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }

    //上传数据
    private void upLoadData() {
        if (flag && childset.size() > 0 && list1.size() > 0 && !TextUtils.isEmpty(imgId) && !TextUtils.isEmpty(imgIdPlan)&& !TextUtils.isEmpty(imagIdPlan2)) {
            //转换数据
            String materialcarts = getmaterialCarts();
            String serverCarts = getserverCarts();
            String imagPlan=getImagPlan();
            LogUtil.e("imagPlan",imagPlan.toString());
            showLoading(true, "上传中...");
            module.upOrderMsg(materialcarts, serverCarts, bean.getId() + "", imgId, length, distype, distypeSer, imagPlan);
        } else {
            Toast.makeText(this, "请检查数据是否正确！", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagPlan() {
//        平面图5    系统图6
        StringBuffer sb=new StringBuffer();
        sb.append("[{"+"id"+":"+imgIdPlan+","+"type"+":"+5+"},"+"{"+"id"+":"+imagIdPlan2+","+"type"+":"+6+"}]");
        return sb.toString();
    }

    //拼接参数
    private String getserverCarts() {
        StringBuffer materialCarts = new StringBuffer();
        materialCarts.append("[");
        for (int i = 0; i < list1.size(); i++) {
            ServerChildBean bean = list1.get(i);
            materialCarts.append("{" + "count:" + "1" + "," + "price:" + bean.getPrice() + "," + "totalPrice:" + bean.getEditext() + "," + "serveritem:" + "{" + "id:" + bean.getId() + "}");
            if (i == list1.size() - 1 || list1.size() == 1) {
                materialCarts.append("}]");
            } else {
                materialCarts.append("},");
            }
        }
        String data = materialCarts.toString();
        Log.e("222222server", data.toString());
        return data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtils.cleanAll();
    }

    //拼接参数
    private String getmaterialCarts() {
        StringBuffer materialCarts = new StringBuffer();
        materialCarts.append("[");
        for (int i = 0; i < childset.size(); i++) {
            ChildBean bean = childset.get(i);
            materialCarts.append("{" + "count:" + bean.getCount() + "," + "price:" + bean.getSellingPrice() + "," + "totalPrice:" + bean.getAll() + "," + "bnMaterialTbl:" + "{" + "id:" + bean.getId() + "}");
            if (i == childset.size() - 1 || childset.size() == 1) {
                materialCarts.append("}]");
            } else {
                materialCarts.append("},");
            }
        }
        String data = materialCarts.toString();
        Log.e("222222material", data.toString());
        return data;
    }

    @Override
    public void Succeed(String imgId) {
        flag = true;
        //上传图片成功
        if (type == 1) {
            InstalactImgSign.setVisibility(View.GONE);
            InstalactImgAutograph.setImageBitmap(bm);
            InstalactImgAutograph.setVisibility(View.VISIBLE);
            this.imgId = imgId;
        } else if (type == 2) {
            InstalactIvAdd.setVisibility(View.GONE);
            InstalactImgPlan.setImageBitmap(bitmap);
            InstalactImgPlan.setVisibility(View.VISIBLE);
            imgIdPlan = imgId;
        }else if(type==3){
            InstalactIvAdd2.setVisibility(View.GONE);
            InstalactImgPlan2.setImageBitmap(bitmap);
            InstalactImgPlan2.setVisibility(View.VISIBLE);
            imagIdPlan2=imgId;
        }
    }

    @Override
    public void Defeat() {
        Toast.makeText(this, "上传签名失败", Toast.LENGTH_SHORT).show();
    }

    //网络请求上传成功
    @Override
    public void upSucceed() {
        dismissLoading();
        finish();
    }

    @Override
    public void upFalse() {
        dismissLoading();
    }

    //选择图片
    public void createPop() {
        List<String> data = new ArrayList<>();
        data.add("拍照");
        data.add("手机相册");
        final PopupDialog popDialog = new PopupDialog(InstalActivity.this, data);
        popDialog.setOnItemClickListener(new PopupDialog.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    //拍照
                    case 0:
                        //裁剪参数
                        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                        getTakePhoto().onPickFromCaptureWithCrop(getUri(), cropOptions);
                        break;
                    //手機相冊
                    case 1:

                        //裁剪参数
                        CropOptions cropOptions1 = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
                        //裁剪后的输出
                        //系统ui
                        getTakePhoto().onPickFromGalleryWithCrop(getUri(), cropOptions1);
                        //自定义ui
                        // getTakePhoto().onPickMultipleWithCrop(1, cropOptions1);
                        break;
                    //取消
                    case 2:
                        popDialog.dismiss();
                        break;
                }
            }
        });
        popDialog.show();
    }

    //获取TakePhoto实例
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    //图片保存路径
    private Uri getUri() {
        file = new File(Environment.getExternalStorageDirectory(), "/Gas/images/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    @Override
    public void takeSuccess(TResult result) {
        bitmap = BitmapFactory.decodeFile(result.getImage().getPath());
        if (null != file) {
//            type = 2;
            moduless.UpImg(file);
        } else {
            Toast.makeText(this, "保存图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //创建TakePhoto实例
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

}
