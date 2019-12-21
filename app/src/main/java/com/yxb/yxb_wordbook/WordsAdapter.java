package com.yxb.yxb_wordbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class WordsAdapter extends ArrayAdapter<Words> {
    private int resourceId;
    public WordsAdapter(Context context,int textViewResourceId,List<Words> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Words words =getItem(position);

        View view= LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        //取得 三个textview，
        TextView wordName = (TextView) view.findViewById(R.id.wordName);
        TextView meaningsName = (TextView) view.findViewById(R.id.meaningsName);
        TextView exSName=(TextView) view.findViewById(R.id.exSName);
        //将word的三个属性，word、meanings、exs放到三个textview
        wordName.setText(words.getWord());
        meaningsName.append(words.getMeanings());
        exSName.append(words.getExS());

        return view;
    }
}


