package com.enesb.notebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity2 extends AppCompatActivity {

    EditText titleText, noteText;
    Button button;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        titleText = findViewById(R.id.title_text);
        noteText = findViewById(R.id.note_text);
        button = findViewById(R.id.button);
        database = this.openOrCreateDatabase("Notes", MODE_PRIVATE, null);

        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.matches("new")) {
            titleText.setText("");
            noteText.setText("");
            button.setVisibility(View.VISIBLE);
        } else {
            int noteId = intent.getIntExtra("noteId", 1);
            button.setVisibility(View.VISIBLE);

            try {
                Cursor cursor = database.rawQuery("SELECT * FROM notes WHERE id = ?", new String[] {String.valueOf(noteId)});

                int titleIx = cursor.getColumnIndex("notetitle");
                int noteIx = cursor.getColumnIndex("note");

                while (cursor.moveToNext()) {
                    titleText.setText(cursor.getString(titleIx));
                    noteText.setText(cursor.getString(noteIx));
                }

            }
            catch (Exception e) {

            }
        }
    }

    public void save(View view) {
        String title = titleText.getText().toString();
        String note = noteText.getText().toString();

        try {
            database = this.openOrCreateDatabase("Notes",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS notes (id INTEGER PRIMARY KEY, notetitle VARCHAR, note VARCHAR)");

            String sqlString = "INSERT INTO notes (notetitle, note) VALUES (?, ?)";
            SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
            sqLiteStatement.bindString(1, title);
            sqLiteStatement.bindString(2, note);
            sqLiteStatement.execute();

        } catch (Exception e) {

        }
        // finish();

        Intent intent = new Intent(MainActivity2.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Daha önceki bütün aktiviteleri kapatir.
        startActivity(intent);

    }
}