package com.sctjsj.gasdrawer.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkMessageBean;
import com.sctjsj.gasdrawer.model.RepairPictureModule;
import com.sctjsj.gasdrawer.presenter.adapter.RepairPictureAdapter;
import com.sctjsj.gasdrawer.ui.CustomView.PopupDialog;
import com.sctjsj.gasdrawer.util.FullyGridLayoutManager;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 提交照片（子页）
 */
@Route(path = "/main/act/RepairPicture")
public class RepairPicture extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener, RepairPictureModule.RepairPictureModuleCallback {

    @BindView(R.id.repair_picture_back)
    ImageView repairPictureBack;
    @BindView(R.id.repair_picture_sure)
    TextView repairPictureSure;
    @BindView(R.id.repair_picGridView)
    RecyclerView repairPicGridView;
    @BindView(R.id.tv_prompt)
    TextView tvPrompt;
    @BindView(R.id.lin_top)
    LinearLayout linTop;
    @BindView(R.id.repair_after)
    RecyclerView repairAfter;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private List<Bitmap> data;
    private List<Bitmap> dataAfter;
    private RepairPictureAdapter adapter;
    private RepairPictureAdapter adapterAfter;
    private List<String> picId;
    private List<String> picIdAfter;
    private Bitmap bitmap = null;
    private File file = null;
    private RepairPictureModule module;
    @Autowired(name = "data")
    WorkMessageBean bean;
    private int type=-1;
    private Map<String,Object> map=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showlayout();
        module = new RepairPictureModule(this);
        picId = new ArrayList<>();
        data = new ArrayList<>();
        picIdAfter=new ArrayList<>();
        dataAfter=new ArrayList<>();
        initData();
        setAdapter();


    }

    private void showlayout() {
        if (!TextUtils.isEmpty(bean.getApplianceName()) && bean.getApplianceName() != null) {
            linTop.setVisibility(View.VISIBLE);
            tvPrompt.setText("请添加 " + bean.getApplianceName() + " 的照片。");
        } else {
            linTop.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        adapter = new RepairPictureAdapter(this, data);
        repairPicGridView.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        repairPicGridView.setAdapter(adapter);
        adapter.setAddCallBack(new RepairPictureAdapter.AddPictureCallBack() {
            @Override
            public void addPicture() {
                type=1;
                createPop();
            }

        });
        adapter.setLongclick(new RepairPictureAdapter.Longclick() {
            @Override
            public void movePicture(int position) {
                data.remove(data.get(position));
                Toast.makeText(RepairPicture.this, "删除成功。", Toast.LENGTH_SHORT).show();
            }
        });


        adapterAfter=new RepairPictureAdapter(this,dataAfter);
        repairAfter.setAdapter(adapterAfter);
        repairAfter.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        adapterAfter.setAddCallBack(new RepairPictureAdapter.AddPictureCallBack() {
            @Override
            public void addPicture() {
                type=2;
                createPop();
            }

        });
        adapterAfter.setLongclick(new RepairPictureAdapter.Longclick() {
            @Override
            public void movePicture(int position) {
                dataAfter.remove(dataAfter.get(position));
                Toast.makeText(RepairPicture.this, "删除成功。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化数据
    private void initData() {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpicture);
        data.add(mBitmap);
        dataAfter.add(mBitmap);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_repair_picture;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.repair_picture_back, R.id.repair_picture_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.repair_picture_back:
                finish();
                break;
            case R.id.repair_picture_sure:
                commit();
                break;
        }
    }

    //提交信息
    private void commit() {
        if (null != picId && picId.size() > 0&&picIdAfter!=null&&picIdAfter.size()>0) {
            map.put("picId",picId);
            map.put("picIdAfter",picIdAfter);
            Intent intent = new Intent();
            intent.putExtra("key", (Serializable) map);
            this.setResult(104, intent);
            finish();
        } else {
            Toast.makeText(this, "请添加照片后再确认。", Toast.LENGTH_SHORT).show();
        }
    }

    public void createPop() {
        List<String> data = new ArrayList<>();
        data.add("拍照");
        data.add("手机相册");
        final PopupDialog popDialog = new PopupDialog(RepairPicture.this, data);
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //创建TakePhoto实例
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /***
     * 图片选择成功
     *
     * @param result
     */
    @Override
    public void takeSuccess(TResult result) {
        bitmap = BitmapFactory.decodeFile(result.getImage().getPath());
        if (null != file) {
            module.UpImg(file);
        } else {
            Toast.makeText(this, "保存图片失败", Toast.LENGTH_SHORT).show();
            type=-1;
        }
    }

    /**
     * 图片选择失败
     *
     * @param result
     * @param msg
     */
    @Override
    public void takeFail(TResult result, String msg) {
    type=-1;
    }

    @Override
    public void takeCancel() {
    type=-1;
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 图片保存路径
     *
     * @return
     */
    private Uri getUri() {
        file = new File(Environment.getExternalStorageDirectory(), "/Gas/images/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    /**
     * 页面跳转回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //处理运行时权限
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public void Succeed(String imgId) {
        switch (type){
            case 1:
                picId.add(imgId);
                data.add(0, bitmap);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                picIdAfter.add(imgId);
                dataAfter.add(0, bitmap);
                adapterAfter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void Defeat() {
        type=-1;
        Toast.makeText(this, "上传图片失败请重试！", Toast.LENGTH_SHORT).show();
    }
}
