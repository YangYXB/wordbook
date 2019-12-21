package com.yxb.yxb_wordbook;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDH extends SQLiteOpenHelper {
    //创建单词表
    public static final String CREAT_BOOK =
            "create table WordsBook ("
                    + "word varchar(50) primary key,"
                    + "meanings text,"
                    + "exampleSentence text)";

    private Context mContext;

    public MyDH (Context context, String name, SQLiteDatabase.CursorFactory factory ,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREAT_BOOK);
        Toast.makeText(mContext,"Create succeeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
