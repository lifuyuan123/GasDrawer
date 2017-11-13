package com.sctjsj.gasdrawer.util;

import android.graphics.Bitmap;
import android.util.Log;

public class TestM8Driver {

    protected  static long mDwPos = 0;

    private  static Bitmap mBitmap = null;

    /**
     * LQDriver驱动的解析位图生成可打印数据
     * @param lx 打印位图的横坐标（位图最左边，单位0.1mm）
     * @param ly 打印位图的纵坐标（位图最上边，单位0.1mm）
     * @param bitmap 传入需要打印的位图（此位图目前需要ARGB 32位格式）
     * @return 返回可打印数据
     */
    public static byte[] RemixBmpInLQ(long lx, long ly, Bitmap bitmap){
        Log.d("StartDebug", "进入RemixBmpInLQ");
        byte[] printData = null;

        mBitmap = bitmap;

        /** 分析位图数据，并将其转换为黑白图片 */
//        analysisBitmap(bitmap);

        /** 申请缓存存储可打印数据，通常来说，可打印数据不会超过位图本身分辨率的两倍 */
        printData = new byte[bitmap.getWidth() * bitmap.getHeight() * 2];

        /** 生成可打印数 */
        if (InnerRemixBmpInLQ(lx, ly, printData) == false){
            printData = null;
        }

        Log.d("StartDebug", "退出RemixBmpInLQ");
        return printData;
    }


    /**
     * @brief 生成可打印数据
     * @param lX 打印位图的横坐标 （单位0.1MM）
     * @param lY 打印位图的纵坐标（位图最上边，单位0.1mm）
     * @param szOutputData 可打印数据缓存对了
     * @return 返回true表示成功解析，返回false表示解析失败
     */
    protected  static boolean InnerRemixBmpInLQ(long lX, long lY, byte[] szOutputData){
        Log.d("StartDebug", "进入InnerRemixBmpInLQ");
        boolean bRet = false;

        mDwPos = 0;


        try {
            for (int j = 0; j < mBitmap.getHeight() / 24f; j++) {

                //打印图片的指令
                szOutputData[(int) mDwPos++] = 0x1B;
                szOutputData[(int) mDwPos++] = 0x2A;
                szOutputData[(int) mDwPos++] = 33;

                szOutputData[(int) mDwPos++] = (byte) (mBitmap.getWidth() % 256); //nL
                szOutputData[(int) mDwPos++] = (byte) (mBitmap.getWidth() / 256); //nH
                //对于每一行，逐列打印
                for (int i = 0; i < mBitmap.getWidth(); i++) {
                    //每一列24个像素点，分为3个字节存储
                    for (int m = 0; m < 3; m++) {
                        //每个字节表示8个像素点，0表示白色，1表示黑色
                        for (int n = 0; n < 8; n++) {
                            byte b = px2Byte(i, j * 24 + m * 8 + n, mBitmap);
                            szOutputData[(int) mDwPos] += szOutputData[(int) mDwPos] + b;
                        }
                        mDwPos++;
                    }
                }
                //设置行距为0的指令
                szOutputData[(int) mDwPos++] = 0x1B;
                szOutputData[(int) mDwPos++] = 0x33;
                szOutputData[(int) mDwPos++] = 0x00;
                szOutputData[(int) mDwPos++] = 10;//换行

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        catch (Throwable t){
            t.printStackTrace();
        }

        bRet = true;
        Log.d("StartDebug", "退出InnerRemixBmpInLQ");
        return bRet;
    }

    public static byte px2Byte(int x, int y, Bitmap bit) {
        byte b;
        int pixel = bit.getPixel(x, y);
        int red = (pixel & 0x00ff0000) >> 16; // 取高两位
        int green = (pixel & 0x0000ff00) >> 8; // 取中两位
        int blue = pixel & 0x000000ff; // 取低两位
        int gray = (int)(red * 0.3 + green * 0.59 + blue * 0.11);;
        if ( gray < 128 ){
            b = 1;
        } else {
            b = 0;
        }
        return b;
    }

    /**
     * @brief 得到缓冲区的字节长度
     * @return 返会缓冲区的字节长度
     */
    public long getDwPos() {
        return mDwPos;
    }


}