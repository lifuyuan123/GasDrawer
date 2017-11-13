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
import android.view.View;
import android.widget.Toast;

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

@Route(path = "/main/act/InstallRepairPicture")
public class InstallRepairPictureActivity extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener, RepairPictureModule.RepairPictureModuleCallback {
    @BindView(R.id.repair_picGridView_gas)
    RecyclerView repairPicGridViewGas;
    @BindView(R.id.repair_picGridView_hot)
    RecyclerView repairPicGridViewHot;
    @BindView(R.id.repair_picGridView_hot_two)
    RecyclerView repairPicGridViewHotTwo;
    @BindView(R.id.repair_picGridView_hot_two_dry)
    RecyclerView repairPicGridViewHotTwoDry;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private List<Bitmap> data = new ArrayList<>();
    private List<Bitmap> data1 = new ArrayList<>();
    private List<Bitmap> data2 = new ArrayList<>();
    private List<Bitmap> data3 = new ArrayList<>();
    private RepairPictureAdapter adapter;
    private RepairPictureAdapter adapter1;
    private RepairPictureAdapter adapter2;
    private RepairPictureAdapter adapter3;
    private List<String> picId= new ArrayList<>();
    private List<String> picId1= new ArrayList<>();
    private List<String> picId2= new ArrayList<>();
    private List<String> picId3= new ArrayList<>();
    private Map<String ,Object> map=new HashMap<>();
    private Bitmap bitmap = null;
    private File file = null;
    private RepairPictureModule module;
    private int type=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        module = new RepairPictureModule(this);
        initData();
        setAdapter();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_install_repair_picture;
    }

    @Override
    public void reloadData() {

    }


    private void setAdapter() {
        //燃气 type=0
        adapter = new RepairPictureAdapter(this, data);
        repairPicGridViewGas.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        repairPicGridViewGas.setAdapter(adapter);
        adapter.setAddCallBack(new RepairPictureAdapter.AddPictureCallBack() {
            @Override
            public void addPicture() {
                type=0;
                createPop();

            }

        });
        adapter.setLongclick(new RepairPictureAdapter.Longclick() {
            @Override
            public void movePicture(int position) {
                data.remove(data.get(position));
                picId.remove(picId.get(position));
                Toast.makeText(InstallRepairPictureActivity.this, "删除成功。", Toast.LENGTH_SHORT).show();
            }
        });

        //干衣机  type=1
        adapter1 = new RepairPictureAdapter(this, data1);
        repairPicGridViewHotTwoDry.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        repairPicGridViewHotTwoDry.setAdapter(adapter1);
        adapter1.setAddCallBack(new RepairPictureAdapter.AddPictureCallBack() {
            @Override
            public void addPicture() {
                type=1;
                createPop();
            }

        });
        adapter1.setLongclick(new RepairPictureAdapter.Longclick() {
            @Override
            public void movePicture(int position) {
                data1.remove(data1.get(position));
                picId1.remove(picId1.get(position));
                Toast.makeText(InstallRepairPictureActivity.this, "删除成功。", Toast.LENGTH_SHORT).show();
            }
        });
        //热水器  type=2
        adapter2 = new RepairPictureAdapter(this, data2);
        repairPicGridViewHot.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        repairPicGridViewHot.setAdapter(adapter2);
        adapter2.setAddCallBack(new RepairPictureAdapter.AddPictureCallBack() {
            @Override
            public void addPicture() {
                type=2;
                createPop();
            }

        });
        adapter2.setLongclick(new RepairPictureAdapter.Longclick() {
            @Override
            public void movePicture(int position) {
                data2.remove(data2.get(position));
                picId2.remove(picId2.get(position));
                Toast.makeText(InstallRepairPictureActivity.this, "删除成功。", Toast.LENGTH_SHORT).show();
            }
        });

        //两用灶台  type=3
        adapter3 = new RepairPictureAdapter(this, data3);
        repairPicGridViewHotTwo.setLayoutManager(new FullyGridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        repairPicGridViewHotTwo.setAdapter(adapter3);
        adapter3.setAddCallBack(new RepairPictureAdapter.AddPictureCallBack() {
            @Override
            public void addPicture() {
                type=3;
                createPop();
            }

        });
        adapter3.setLongclick(new RepairPictureAdapter.Longclick() {
            @Override
            public void movePicture(int position) {
                data3.remove(data3.get(position));
                picId3.remove(picId3.get(position));
                Toast.makeText(InstallRepairPictureActivity.this, "删除成功。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //初始化数据
    private void initData() {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_addpicture);
        data.add(mBitmap);
        data1.add(mBitmap);
        data2.add(mBitmap);
        data3.add(mBitmap);

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
        if (picId.size()==0&&picId1.size()==0&&picId2.size()==0&&picId3.size()==0) {
            Toast.makeText(this, "请添加照片后再确认。", Toast.LENGTH_SHORT).show();
        }else {
            if(picId.size()>0){
                map.put("picId",picId);
            }
            if(picId1.size()>0){
                map.put("picId1",picId1);
            }
            if(picId2.size()>0){
                map.put("picId2",picId2);
            }
            if(picId3.size()>0){
                map.put("picId3",picId3);
            }

            //Todo  将有的图片id集合添加到容器并返回上传
            Intent intent = new Intent();
            intent.putExtra("key", (Serializable) map);
            this.setResult(104, intent);
            finish();
        }
    }

    public void createPop() {
        List<String> data = new ArrayList<>();
        data.add("拍照");
        data.add("手机相册");
        final PopupDialog popDialog = new PopupDialog(InstallRepairPictureActivity.this, data);
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

    }

    @Override
    public void takeCancel() {

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
            case 0:
                picId.add(imgId);
                data.add(0, bitmap);
                adapter.notifyDataSetChanged();
                break;
            case 1:
                picId1.add(imgId);
                data1.add(0, bitmap);
                adapter1.notifyDataSetChanged();
                break;
            case 2:
                picId2.add(imgId);
                data2.add(0, bitmap);
                adapter2.notifyDataSetChanged();
                break;
            case 3:
                picId3.add(imgId);
                data3.add(0, bitmap);
                adapter3.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void Defeat() {
        Toast.makeText(this, "上传图片失败请重试！", Toast.LENGTH_SHORT).show();
    }

}
