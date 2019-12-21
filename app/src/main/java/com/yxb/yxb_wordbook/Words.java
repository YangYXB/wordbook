package com.yxb.yxb_wordbook;

public class Words {
    private String word ;
    private String meanings ;
    private String exS ;

    public Words(){
    }

    public Words(String word,String meanings,String exS){
        this.word= word;
        this.meanings=meanings;
        this.exS=exS;
    }

    public String getExS() {
        return exS;
    }

    public String getMeanings() {
        return meanings;
    }

    public String getWord() {
        return word;
    }

    public void setExS(String exS) {
        this.exS=exS;
    }

    public void setMeanings(String meanings) {
        this.meanings = meanings;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void getAllWords(){
        /*
        private MyDH dbHelper = new MyDH(MainActivity., "Ven.db", null, 1);;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String ins =
                "select * from WordsBook";
        Cursor cursor = db.rawQuery(ins, null);
        if (cursor.moveToFirst()) {
            do {

                String word = cursor.getString(cursor.getColumnIndex("word"));
                String meanings = cursor.getString(cursor.getColumnIndex("meanings"));
                String exS = cursor.getString(cursor.getColumnIndex("exampleSentence"));
                show1.append(word + " ");
                show1.append(meanings + " ");
                show1.append(exS + " ");
                show1.append("\n");
            } while (cursor.moveToNext());
        }
        cursor.close();
        */
    }
}


