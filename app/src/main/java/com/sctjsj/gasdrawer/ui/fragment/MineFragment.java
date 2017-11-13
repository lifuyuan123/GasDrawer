package com.sctjsj.gasdrawer.ui.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.sctjsj.basemodule.base.ui.frg.BaseFragment;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.gasdrawer.MainLooper;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.entity.javaBean.WorkerBean;
import com.sctjsj.gasdrawer.model.MineModule;
import com.sctjsj.gasdrawer.ui.CustomView.PopupDialog;
import com.sctjsj.gasdrawer.ui.activity.BTScanActivity;
import com.sctjsj.gasdrawer.util.JumpCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by liuha on 2017/4/21.
 * 我的fragment
 */

public class MineFragment extends BaseFragment implements MineModule.MineModuleCallBack, TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.mine_Img)
    CircleImageView mineImg;
    @BindView(R.id.mine_userName_txt)
    TextView mineUserNameTxt;
    @BindView(R.id.mine_userInfo_layout)
    LinearLayout mineUserInfoLayout;
    @BindView(R.id.mine_history_layout)
    LinearLayout mineHistoryLayout;
    @BindView(R.id.mine_paw_layout)
    LinearLayout minePawLayout;
    @BindView(R.id.mine_btn)
    Button mineBtn;
    Unbinder unbinder;
    @BindView(R.id.bt)
    LinearLayout bt;
    private WorkerBean bean;
    private MineModule module;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private Bitmap bitmap;


    @Override
    protected int setLayoutResouceId() {
        return R.layout.layout_mine;
    }

    @Override
    protected void initData(Bundle arguments) {
        module = new MineModule(this, getActivity());
        module.getWorkerMsg();

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void onLazyLoad() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        //1.加载布局 XML 文件
        mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        //2.接收从别的地方传过来的参数，如 Intent 跳转
        unbinder = ButterKnife.bind(this, mRootView);
        initData(getArguments());
        //3.绑定 Fragment中的控件
        initView();

        //4.设置碎片已加载完
        mIsPrepare = true;
        //5.懒加载数据
        onLazyLoad();
        //6.设置监听
        setListener();
        return mRootView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }


    @OnClick({R.id.mine_userInfo_layout, R.id.mine_history_layout, R.id.mine_paw_layout, R.id.mine_btn, R.id.bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_userInfo_layout:
                ARouter.getInstance().build("/main/act/MineMsgActivity").withObject("worker", bean).navigation();
                break;
            case R.id.mine_history_layout:
                ARouter.getInstance().build("/main/act/JobsHistoryActivity").navigation();
                break;
            case R.id.mine_paw_layout:
                ARouter.getInstance().build("/main/act/ChangPswActivity").navigation();
                break;
            case R.id.mine_btn:
                //退出登陆
                goOut();
                break;
            case R.id.bt:
                Intent intent1 = new Intent(getActivity(), BTScanActivity.class);
                intent1.putExtra("data", true);
                getActivity().startActivityForResult(intent1, JumpCode.JUMP_FROM_Index_TO_BTSCANActivity);
        }
    }

    @Subscribe
    public void onEventMainThread(String s) {
        if (s.equals("out")) {
            module.LogOut(getActivity(),2);
        }
    }

    //退出
    private void goOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("退出登录")
                .setMessage("确定退出登录吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //调用退出接口
                     module.LogOut(getActivity(),1);
                    }
                }).show();
    }

    @Override
    public void getMineMsg(WorkerBean bean) {
        this.bean = bean;
        if (null != bean.getSmallUrl() && !TextUtils.isEmpty(bean.getSmallUrl())) {
            PicassoUtil.getPicassoObject().load(bean.getSmallUrl()).error(R.drawable.ic_icon).into(mineImg);
            Log.e("-------",bean.toString());
        }
        if(!TextUtils.isEmpty(bean.getUsername())){
            mineUserNameTxt.setText(bean.getUsername());
        }
    }


    //修改头像成功的回调
    @Override
    public void changIconSucceed() {
        Log.e("changIconSucceed", "changIconSucceed");
        MainLooper.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mineImg.setImageBitmap(bitmap);
            }
        });

    }

    @OnClick(R.id.mine_Img)
    public void onViewClicked() {
        createPop();
    }


    public void createPop() {
        List<String> data = new ArrayList<>();
        data.add("拍照");
        data.add("手机相册");
        final PopupDialog popDialog = new PopupDialog(getActivity(), data);
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
                        //getTakePhoto().onPickMultipleWithCrop(1, cropOptions1);
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
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
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
        File file = new File(result.getImage().getPath());
        module.UpLoadedIcon(file);
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
        File file = new File(Environment.getExternalStorageDirectory(), "/Gas/images/" + System.currentTimeMillis() + ".jpg");
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        PermissionManager.handlePermissionsResult(getActivity(), type, invokeParam, this);
    }
}
