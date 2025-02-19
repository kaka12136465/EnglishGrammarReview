package com.example.englishgrammerreview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GrammarQuestionDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TOEIC_grammar.db"; // データベース名
    private static final int DATABASE_VERSION = 1; // バージョン

    // コンストラクタ
    public GrammarQuestionDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // データベース作成時に呼ばれる
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE questions (id INTEGER PRIMARY KEY AUTOINCREMENT, serialNum Integer, question TEXT, selections TEXT, answer TEXT, explanation TEXT)";
        db.execSQL(createTable);
    }

    // データベースのバージョンが更新されたときに呼ばれる
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS questions");
        onCreate(db);
    }

    public void resisterQuestion(int serialNum, String question, String selections, String answer, String explanation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("serialNum", serialNum);
        values.put("question", question);
        values.put("selections", selections);
        values.put("answer", answer);
        values.put("explanation", explanation);
        db.insert("questions", null, values);
        db.close();
    }

    public Cursor getAllQuestion(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("questions", null, null, null, null, null, null);
    }

    public void addExplanation(int serialNum, String additionalExplanation){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.query("questions", null, "serialNum = ?", new String[]{String.valueOf(serialNum)}, null, null, null);
        if(cursor == null){ return;}
        if(!cursor.moveToFirst()){return;}

        cursor.close();
    }

    public void deleteQuestion(int serialNum){
        SQLiteDatabase db = this.getWritableDatabase();

        String whereClause = "serialNum = ?";
        String[] whereArgs = {String.valueOf(serialNum)};

        int rowsDeleted = db.delete("questions", whereClause, whereArgs);

        if (rowsDeleted > 0) {
            Log.d("Database", "データが削除されました。削除された行数: " + rowsDeleted);
        } else {
            Log.d("Database", "指定された serialNum に一致する行はありませんでした。");
        }

        // データベースを閉じる（必要であれば）
        db.close();
    }
}
