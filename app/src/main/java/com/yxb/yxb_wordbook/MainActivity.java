package com.yxb.yxb_wordbook;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView tv1;
    private MyDH dbHelper = new MyDH(this, "Ven.db", null, 1);//连接数据库
    private int flag = 0;//横竖屏flag，用于列表显示

    private List<Words> wordsList = new ArrayList<>();//单词列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //进入软件及显示所有单词
            flag = 0;
            queryALL();
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            flag = 1;
            queryALL();
            /*
            tvM=(TextView)findViewById(R.id.meaningsName);
            tvE=findViewById(R.id.exSName);
            tvE.setVisibility(View.GONE);
            tvM.setVisibility(View.GONE);
            */
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.men, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //选项菜单
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                //新增单词
                addWord();
                return true;
            case R.id.del:
                //删除单词
                delWord();
                return true;
            case R.id.upd:
                //修改单词
                updateWord();
                return true;
            case R.id.que:
                //查询单词
                queWord();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //显示所有单词
    public void queryALL() {
        initWords();//初始化单词列表
        //在listview里面显示单词列表
        WordsAdapter adapter = new WordsAdapter(MainActivity.this, R.layout.words_item, wordsList);
        ListView lv = findViewById(R.id.listWords);
        lv.setAdapter(adapter);

        //点击单词，右边编辑框出现释义及例句
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (flag == 1) {//横屏才操作，显示释义及例句
                    //String word=wordsList.get(i).getWord();
                    String meanings = wordsList.get(i).getMeanings();
                    String exS = wordsList.get(i).getExS();
                    tv1 = (TextView) findViewById(R.id.textView1);
                    tv1.setText("");
                    tv1.append("释义：" + meanings);
                    tv1.append("\n");
                    tv1.append("例句：" + exS);
                    //Toast.makeText(MainActivity.this,word,Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //初始化单词列表
    public void initWords() {
        wordsList.clear();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //查询语句
        String ins = "select * from WordsBook";
        //将返回的结果放到cursor里
        Cursor cursor = db.rawQuery(ins, null);
        if (cursor.moveToFirst()) {
            do {
                //取得列值
                String word = cursor.getString(cursor.getColumnIndex("word"));
                String meanings = cursor.getString(cursor.getColumnIndex("meanings"));
                String exS = cursor.getString(cursor.getColumnIndex("exampleSentence"));
                //创建一个单词，并初始化
                Words w1 = new Words(word, meanings, exS);
                //w1 = new Words(word," "," ");
                //将单词加入单词列表
                wordsList.add(w1);
            } while (cursor.moveToNext());
        }
        cursor.close();//关闭cursor
    }


    //添加单词
    public void addWord() {
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        //加载事先编好的界面dialog.xml
        final View textEntryView = factory.inflate(R.layout.dialog, null);
        //三个输入框
        final EditText editTextWord = (EditText) textEntryView.findViewById(R.id.editTextWord);
        final EditText editTextMeanings = (EditText) textEntryView.findViewById(R.id.editTextMeanings);
        final EditText editTextExS = (EditText) textEntryView.findViewById(R.id.editTextExS);
        //设置对话框
        AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
        ad1.setTitle("添加新单词:");
        ad1.setView(textEntryView);
        ad1.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                try {
                    //获取三个编辑框的内容
                    String s1 = editTextWord.getText().toString();
                    String s2 = editTextMeanings.getText().toString();
                    String s3 = editTextExS.getText().toString();
                    //连接数据库
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    //插入语句
                    String ins = "insert into WordsBook values (?,?,?)";
                    db.execSQL(ins, new String[]{s1, s2, s3});
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_LONG).show();
                    //添加成功后重新显示所有单词
                    queryALL();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_LONG).show();
                }
            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();
    }

    //删除单词
    public void delWord() {
        final EditText et = new EditText(this);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
        ad1.setTitle("请输入需要删除的单词:");
        ad1.setView(et);
        ad1.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                try {

                    String s1 = et.getText().toString();

                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String ins = "DELETE FROM WordsBook WHERE word = ?";
                    //先查询是否有此单词
                    String ins1 = "select * from WordsBook WHERE word = ?";

                    Cursor cursor = db.rawQuery(ins1, new String[]{s1});
                    if (cursor.moveToFirst()) {
                        db.execSQL(ins, new String[]{s1});
                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "不存在此单词", Toast.LENGTH_LONG).show();
                    }
                    cursor.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_LONG).show();
                }
                queryALL();
            }

        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad1.show();
    }

    //修改单词
    public void updateWord() {
        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View textEntryView = factory.inflate(R.layout.dialog, null);
        final EditText editTextWord = (EditText) textEntryView.findViewById(R.id.editTextWord);
        final EditText editTextMeanings = (EditText) textEntryView.findViewById(R.id.editTextMeanings);
        final EditText editTextExS = (EditText) textEntryView.findViewById(R.id.editTextExS);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
        ad1.setTitle("修改单词:");
        ad1.setView(textEntryView);
        ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                try {
                    String s1 = editTextWord.getText().toString();
                    String s2 = editTextMeanings.getText().toString();
                    String s3 = editTextExS.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    //先查询是否有此单词
                    String ins2 = "select * from WordsBook WHERE word = ?";
                    Cursor cursor = db.rawQuery(ins2, new String[]{s1});
                    if (cursor.moveToFirst()) {//如果存在
                        //先将此单词删除
                        String ins1 = "DELETE FROM WordsBook WHERE word = ?";
                        db.execSQL(ins1, new String[]{s1});

                        //然后将修改后的内容添加
                        String ins = "insert into WordsBook values (?,?,?)";
                        db.execSQL(ins, new String[]{s1, s2, s3});
                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "不存在此单词", Toast.LENGTH_LONG).show();
                    }
                    cursor.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_LONG).show();
                }
                queryALL();
            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();
    }

    //查询单词
    public void queWord() {
        wordsList.clear();
        final EditText et = new EditText(this);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
        ad1.setTitle("请输入需要查询的单词:");
        ad1.setView(et);
        ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                try {
                    String s1 = et.getText().toString();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    String ins = "select * from WordsBook where word like ? ";
                    Cursor cursor = db.rawQuery(ins, new String[]{"%" + s1 + "%"});
                    if (cursor.moveToFirst()) {
                        do {
                            String word = cursor.getString(cursor.getColumnIndex("word"));
                            String meanings = cursor.getString(cursor.getColumnIndex("meanings"));
                            String exS = cursor.getString(cursor.getColumnIndex("exampleSentence"));
                            Words w1 = new Words(word, meanings, exS);
                            wordsList.add(w1);
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    WordsAdapter adapter = new WordsAdapter(MainActivity.this, R.layout.words_item, wordsList);
                    ListView lv = (ListView) findViewById(R.id.listWords);
                    lv.setAdapter(adapter);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_LONG).show();
                }
            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
            }
        });
        ad1.show();
    }

}

