package com.sctjsj.gasdrawer.act;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.gasdrawer.R;
import com.sctjsj.gasdrawer.colorpicker.ColorPickerDialog;
import com.sctjsj.gasdrawer.event.ChooseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rhcad.touchvg.IGraphView;
import rhcad.touchvg.IViewHelper;
import rhcad.touchvg.ViewFactory;

/**
 * Created by mayikang on 17/4/12.
 */

@Route(path = "/draw/act/drawtest")
public class DrawTestActivity extends BaseAppcompatActivity implements IGraphView.OnSelectionChangedListener, IGraphView.OnFirstRegenListener {
    @BindView(R.id.radiobt_common)
    RadioButton radiobtCommon;
    @BindView(R.id.radiobt_system)
    RadioButton radiobtSystem;
    @BindView(R.id.radiobt_plane)
    RadioButton radiobtPlane;
    @BindView(R.id.radiobt_label)
    RadioButton radiobtLabel;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.hsv_common)
    ScrollView hsvCommon;
    @BindView(R.id.hsv_label)
    ScrollView hsvLabel;
    @BindView(R.id.hsv_plane)
    ScrollView hsvPlane;
    @BindView(R.id.hsv_system)
    ScrollView hsvSystem;
    @BindView(R.id.lin_seek)
    LinearLayout linSeek;
    private IViewHelper mHelper;
    private static final String PATH = "mnt/sdcard/Drawing1/";
    private SeekBar mLineWidthBar;
    //    private VerticalSeekBar seekBar;
    private int progre = 0;


    @Override
    public int initLayout() {
        return R.layout.activity_draw_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        if (mHelper == null) {
            mHelper = ViewFactory.createHelper();
        }
        //创建 GraphView

        //画板
        final ViewGroup layout = (ViewGroup) this.findViewById(R.id.container);
        mHelper.createSurfaceView(this, layout, savedInstanceState);
        // mHelper.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.background_repeat));
        if (mHelper.getGraphView() != null) {

            if (savedInstanceState == null) {
                mHelper.getGraphView().setOnFirstRegenListener(this);
            }
        }

        initButtons();

        initUndo();

        updateButtons();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    //常用
                    case R.id.radiobt_common:
                        hideAll();
                        hsvCommon.setVisibility(View.VISIBLE);
                        linSeek.setVisibility(View.VISIBLE);
                        radiobtCommon.setBackground(getResources().getDrawable(R.color.blue));
                        break;
                    //平面
                    case R.id.radiobt_plane:
                        hideAll();
                        hsvPlane.setVisibility(View.VISIBLE);
                        radiobtPlane.setBackground(getResources().getDrawable(R.color.blue));
                        break;
                    //系统
                    case R.id.radiobt_system:
                        hideAll();
                        hsvSystem.setVisibility(View.VISIBLE);
                        radiobtSystem.setBackground(getResources().getDrawable(R.color.blue));
                        break;
                    //标注
                    case R.id.radiobt_label:
                        hideAll();
                        hsvLabel.setVisibility(View.VISIBLE);
                        radiobtLabel.setBackground(getResources().getDrawable(R.color.blue));
                        break;
                }
            }
        });
    }

    //隐藏所有
    private void hideAll() {
        hsvCommon.setVisibility(View.GONE);
        hsvLabel.setVisibility(View.GONE);
        hsvPlane.setVisibility(View.GONE);
        hsvSystem.setVisibility(View.GONE);
        linSeek.setVisibility(View.GONE);
        radiobtCommon.setBackground(getResources().getDrawable(R.color.white));
        radiobtLabel.setBackground(getResources().getDrawable(R.color.white));
        radiobtPlane.setBackground(getResources().getDrawable(R.color.white));
        radiobtSystem.setBackground(getResources().getDrawable(R.color.white));
    }

    @Override
    public void reloadData() {

    }

    private void initButtons() {


        //厨房
        findViewById(R.id.btn_cu_fang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_cu_fang);
            }
        });
        //卫生间
        findViewById(R.id.btn_wei_sheng_jian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_wei_sheng_jian);
            }
        });
        //阳台
        findViewById(R.id.btn_yang_tai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_yang_tai);
            }
        });
        //客厅
        findViewById(R.id.btn_ke_ting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_ke_ting);
            }
        });

        //干衣机
        findViewById(R.id.btn_gan_yi_ji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_gan_yi_ji);
            }
        });
        //热水器
        findViewById(R.id.btn_re_shui_qi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_re_shui_qi);
            }
        });
        //燃气灶
        findViewById(R.id.btn_ran_qi_zao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_ran_qi_zao);
            }
        });
        //丝扣球阀
        findViewById(R.id.btn_si_kou_qiu_fa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_si_kou_qiu_fa);
            }
        });


        //穿墙
        findViewById(R.id.btn_chuan_qiang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_chuan_qiang);
            }
        });

        //燃气表
        findViewById(R.id.btn_ran_qi_biao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_ran_qi_biao);
            }
        });

        //z
        findViewById(R.id.btn_z).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_z);
            }
        });

        //门
        findViewById(R.id.btn_men).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.insertBitmapFromResource(R.drawable.icon_men);
            }
        });

        findViewById(R.id.lines).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("lines");
            }
        });


//        //表具
//        findViewById(R.id.btn_biao_ju).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_biao_ju);
//            }
//        });
//        //测压阀
//        findViewById(R.id.btn_ce_ya_fa).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_ce_ya_fa);
//            }
//        });
//        //出入墙套盒
//        findViewById(R.id.btn_chu_ru_qiang_tao_he).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_chu_ru_qiang_tao_he);
//            }
//        });
//        //穿墙
//        findViewById(R.id.btn_chuan_qiang).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_chuan_qiang);
//            }
//        });
//        //电磁阀
//        findViewById(R.id.btn_dian_ci_fa).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_dian_ci_fa);
//            }
//        });
//        //管道延伸
//        findViewById(R.id.btn_guan_dao_yan_shen).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_guan_dao_yan_shen);
//            }
//        });
//        //气嘴
//        findViewById(R.id.btn_qi_zui).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_qi_zui);
//            }
//        });
//
//        //球阀
//        findViewById(R.id.btn_qiu_fa).setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                mHelper.insertBitmapFromResource(R.mipmap.icon_qiu_fa);
//            }
//        });

        //画直线
        findViewById(R.id.line_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("line");
            }
        });
        //画矩形
        findViewById(R.id.rect_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("rect");
            }
        });
        //画三角形
        findViewById(R.id.triangle_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("triangle");
            }
        });
        //自由画
        findViewById(R.id.splines_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("splines");
            }
        });

        //选择控件
        findViewById(R.id.select_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("select");
            }
        });
        //删除
        findViewById(R.id.erase_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.setCommand("erase");
            }
        });

//        //截屏保存
//        findViewById(R.id.snapshot_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bitmap bitmap = mHelper.extentSnapshot(200, true);
//                Bitmap bitmap1 = null;
//                if (bitmap != null) {
//                    //绘制白色背景
//                    bitmap1 = drawBg4Bitmap(Color.WHITE, bitmap);
//                    boolean b = mHelper.savePNG(bitmap1, PATH +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"snapshot.png");
//                    LogUtil.e("path",PATH +new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"snapshot.png");
//                    if (b) {
//                        Toast.makeText(DrawTestActivity.this, "保存成功。", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(DrawTestActivity.this, "保存失败。", Toast.LENGTH_SHORT).show();
//                    }
//                }else {
//                    Toast.makeText(DrawTestActivity.this, "请作图后再保存。", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        mLineWidthBar = (SeekBar) findViewById(R.id.lineWidthBar);
        mLineWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //设置线条粗细
                mHelper.setStrokeWidth(progress);
                progre = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHelper.setContextEditing(true);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHelper.setContextEditing(false);
            }
        });
//        mLineWidthBar = (SeekBar) findViewById(R.id.lineWidthBar);

//        mLineWidthBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                //设置线条粗细
//                mHelper.setStrokeWidth(progress);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                mHelper.setContextEditing(true);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mHelper.setContextEditing(false);
//            }
//        });

        //设置画板监听
        mHelper.getGraphView().setOnSelectionChangedListener(this);

        //颜色选择器
        findViewById(R.id.colorpicker_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog(DrawTestActivity.this, mHelper.getLineColor(), new ColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int color) {
                        mHelper.setLineColor(color);
                    }
                }).show();
            }
        });
    }

    //设置背景颜色
    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        int with = 0;
        if (orginBitmap.getHeight() >= orginBitmap.getWidth()) {
            with = orginBitmap.getHeight();
        } else {
            with = orginBitmap.getWidth();
        }
        Bitmap bitmap = Bitmap.createBitmap(with, with, orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, with, with, paint);
        if (orginBitmap.getHeight() >= orginBitmap.getWidth()) {
            canvas.drawBitmap(orginBitmap, (with - orginBitmap.getWidth()) / 2, 0, paint);
        } else {
            canvas.drawBitmap(orginBitmap, 0, (with - orginBitmap.getHeight()) / 2, paint);
        }

        return bitmap;
    }

    private void initUndo() {
        findViewById(R.id.undo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mHelper.undo();
            }
        });
        findViewById(R.id.redo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mHelper.redo();
            }
        });

        mHelper.getGraphView().setOnContentChangedListener(new IGraphView.OnContentChangedListener() {
            @Override
            public void onContentChanged(IGraphView view) {
                LogUtil.e("zzzzz","有反应");
                updateButtons();
            }
        });
        //回退
        mHelper.startUndoRecord(PATH + "undo");
    }

    private void updateButtons() {
        if (mHelper.canUndo()){
            findViewById(R.id.iv_vgundo).setBackground(getResources().getDrawable(R.drawable.vgundo));
        }else {
            findViewById(R.id.iv_vgundo).setBackground(getResources().getDrawable(R.drawable.vgundo_no));
        }

        if(mHelper.canRedo()){
            findViewById(R.id.iv_vgredo).setBackground(getResources().getDrawable(R.drawable.vgredo));
        }else {
            findViewById(R.id.iv_vgredo).setBackground(getResources().getDrawable(R.drawable.vgredo_no));
        }

        findViewById(R.id.undo_btn).setEnabled(mHelper.canUndo());
        findViewById(R.id.redo_btn).setEnabled(mHelper.canRedo());
        mLineWidthBar.setProgress(mHelper.getStrokeWidth());

    }

    @Override
    public void onSelectionChanged(IGraphView view) {
        LogUtil.e("zzzzz1","有反应");
        updateButtons();
    }

    @Override
    public void onDestroy() {
        mHelper.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        mHelper.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mHelper.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mHelper.onSaveInstanceState(outState, PATH);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mHelper.onRestoreInstanceState(savedInstanceState);
    }

    @Subscribe
    public void onEventMainThread(ChooseEvent event) {
        if (event != null) {
            Toast.makeText(this, "" + event.getResId(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFirstRegen(IGraphView iGraphView) {
        mHelper.startUndoRecord(PATH + "undo");
    }

    @OnClick({R.id.lin_enlarge, R.id.lin_narrow, R.id.Instalact_Img_back, R.id.tv_save, R.id.lin_yxglq, R.id.lin_bwbcq, R.id.cq2, R.id.lin_dqz, R.id.lin_dcf, R.id.lin_fl, R.id.lin_flg, R.id.lin_flqf, R.id.lin_fxbcq, R.id.lin_gszh, R.id.lin_gm, R.id.lin_hj, R.id.lin_lsglq, R.id.lin_lljkzq, R.id.lin_mdqf, R.id.lin_mb, R.id.lin_sd, R.id.lin_skqf2, R.id.lin_tyxg, R.id.lin_txyjg, R.id.lin_xjdymqg, R.id.lin_xjgymdqg, R.id.lin_xjjkgd, R.id.lin_xjzymdgd, R.id.lin_ylb, R.id.lin_yjdymdqgd, R.id.lin_yjgymdqgd, R.id.lin_yjjkgd, R.id.lin_yjzymdqgd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Instalact_Img_back:
                finish();
                break;
            case R.id.tv_save:
                Bitmap bitmap = mHelper.extentSnapshot(200, true);
                Bitmap bitmap1 = null;
                if (bitmap != null) {
                    //绘制白色背景
                    bitmap1 = drawBg4Bitmap(Color.WHITE, bitmap);
                    boolean b = mHelper.savePNG(bitmap1, PATH + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "snapshot.png");
                    if (b) {
                        Toast.makeText(DrawTestActivity.this, "保存成功。", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DrawTestActivity.this, "保存失败。", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请作图后再保存。", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lin_yxglq:
                mHelper.insertBitmapFromResource(R.drawable.icon_yxglz);
                break;
            case R.id.lin_bwbcq:
                mHelper.insertBitmapFromResource(R.drawable.icon_bwbcq);
                break;
            case R.id.cq2:
                mHelper.insertBitmapFromResource(R.drawable.icon_cq2);
                break;
            case R.id.lin_dqz:
                mHelper.insertBitmapFromResource(R.drawable.icon_dqz);
                break;
            case R.id.lin_dcf:
                mHelper.insertBitmapFromResource(R.drawable.icon_dcf);
                break;
            case R.id.lin_fl:
                mHelper.insertBitmapFromResource(R.drawable.icon_fl);
                break;
            case R.id.lin_flg:
                mHelper.insertBitmapFromResource(R.drawable.icon_flg);
                break;
            case R.id.lin_flqf:
                mHelper.insertBitmapFromResource(R.drawable.icon_flqf);
                break;
            case R.id.lin_fxbcq:
                mHelper.insertBitmapFromResource(R.drawable.icon_fxbcq);
                break;
            case R.id.lin_gszh:
                mHelper.insertBitmapFromResource(R.drawable.icon_slzh);
                break;
            case R.id.lin_gm:
                mHelper.insertBitmapFromResource(R.drawable.icon_gm);
                break;
            case R.id.lin_hj:
                mHelper.insertBitmapFromResource(R.drawable.icon_hj);
                break;
            case R.id.lin_lsglq:
                mHelper.insertBitmapFromResource(R.drawable.icon_lsglq);
                break;
            case R.id.lin_lljkzq:
                mHelper.insertBitmapFromResource(R.drawable.icon_llqkzq);
                break;
            case R.id.lin_mdqf:
                mHelper.insertBitmapFromResource(R.drawable.icon_mdqf);
                break;
            case R.id.lin_mb:
                mHelper.insertBitmapFromResource(R.drawable.icon_mb);
                break;
            case R.id.lin_sd:
                mHelper.insertBitmapFromResource(R.drawable.icon_sd);
                break;
            case R.id.lin_skqf2:
                mHelper.insertBitmapFromResource(R.drawable.icon_skqf2);
                break;
            case R.id.lin_tyxg:
                mHelper.insertBitmapFromResource(R.drawable.icon_tzxg);
                break;
            case R.id.lin_txyjg:
                mHelper.insertBitmapFromResource(R.drawable.icon_txyjg);
                break;
            case R.id.lin_xjdymqg:
                mHelper.insertBitmapFromResource(R.drawable.icon_xjdymqgd);
                break;
            case R.id.lin_xjgymdqg:
                mHelper.insertBitmapFromResource(R.drawable.icon_xjgymqgd);
                break;
            case R.id.lin_xjjkgd:
                mHelper.insertBitmapFromResource(R.drawable.icon_xjjkgd);
                break;
            case R.id.lin_xjzymdgd:
                mHelper.insertBitmapFromResource(R.drawable.icon_xjzzymqgd);
                break;
            case R.id.lin_ylb:
                mHelper.insertBitmapFromResource(R.drawable.icon_yqb);
                break;
            case R.id.lin_yjdymdqgd:
                mHelper.insertBitmapFromResource(R.drawable.icon_yjdymqgd);
                break;
            case R.id.lin_yjgymdqgd:
                mHelper.insertBitmapFromResource(R.drawable.icon_yjgymdgd);
                break;
            case R.id.lin_yjjkgd:
                mHelper.insertBitmapFromResource(R.drawable.icon_yjjkgd);
                break;
            case R.id.lin_yjzymdqgd:
                mHelper.insertBitmapFromResource(R.drawable.icon_yjzymdgd);
                break;
            //放大
            case R.id.lin_enlarge:
                mHelper.setStrokeWidth(progre + 1);
                mLineWidthBar.setProgress(progre + 1);
                break;
            //缩小
            case R.id.lin_narrow:
                mHelper.setStrokeWidth(progre - 1);
                mLineWidthBar.setProgress(progre - 1);
                break;
        }
    }

}
