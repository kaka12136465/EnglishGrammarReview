package com.example.englishgrammerreview;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private GrammarQuestionDatabaseHelper dbHelper;
    private TextInputEditText inputQuestionField;
    private TextView outputTextArea;

    private static final String[] OPTIONS = {"登録", "復習"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new GrammarQuestionDatabaseHelper(this);
        inputQuestionField = findViewById(R.id.input_question_field);
        outputTextArea = findViewById(R.id.output_text_area);

        //モードを選択するメニューを設定する。復習or登録
        setSelectModeMenu();

        //保存ボタンの処理を設定
        setSaveButtonAction();

        //すべて表示ボタンの処理を設定
        setShowAllButtonAction();
    }

    private void setShowAllButtonAction(){
        Button showAllButton = findViewById(R.id.show_all_button);
        showAllButton.setOnClickListener(e->{
            Cursor cursor = dbHelper.getAllQuestion();
            outputTextArea.setText(getCursorData(cursor));
        });
    }

    private void setSaveButtonAction(){
        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(e->{
            dbHelper.resisterQuestion(0, inputQuestionField.getText().toString(), null, null, null);
            inputQuestionField.setText("");
        });
    }

    public String getCursorData(Cursor cursor) {
        StringBuilder data = new StringBuilder();

        // カーソルが最初の行にあるか確認
        if (cursor != null && cursor.moveToFirst()) {
            // カーソルの各行をループ
            do {
                // ここで各カラムのデータを取得します
                int columnCount = cursor.getColumnCount(); // カラム数を取得
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i); // カラム名
                    String columnValue = cursor.getString(i);  // カラムの値（Stringに変換）

                    // 文字列として追加（カラム名と値）
                    data.append(columnName).append(": ").append(columnValue).append("\n");
                }
                data.append("\n"); // 各行の後に空行を追加
            } while (cursor.moveToNext()); // 次の行に移動
        }

        // 最終的に文字列として返す
        return data.toString();
    }


    private void setSelectModeMenu(){
        Spinner selectModeMenu = findViewById(R.id.select_mode_menu);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, OPTIONS);
        selectModeMenu.setAdapter(adapter);

        selectModeMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "選択された項目: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 何も選択されなかったときの処理（省略可）
            }
        });
    }
}