package com.sctjsj.gasdrawer.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lifuy on 2017/6/7.
 */

public class MyDbHelper {

    private SQLiteDatabase sq;
    private Context context;
    private String jason;

    public MyDbHelper(Context context) {
        this.context = context;
        MyDb myDb = new MyDb(context);
        sq=  myDb.getWritableDatabase();
    }

    public void saveJason(String jsaon) {//增加
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", jsaon);
        sq.insert("json", null, contentValues);
    }
    public void deleteJason() {//全部删除
        sq.delete("json", null, null);
    }

    public String getJson() {
        Cursor cursor = sq.rawQuery("select * from json ", null);
        while (cursor.moveToNext()) {
            jason = cursor.getString(cursor.getColumnIndex("name"));
        }
        return jason;
    }


}
