package com.enesb.notebookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> titleArray;
    ArrayList<Integer> idArray;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        titleArray = new ArrayList<String>();
        idArray = new ArrayList<Integer>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, titleArray);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                intent.putExtra("noteId", idArray.get(position));
                intent.putExtra("info", "old");

                startActivity(intent);
            }
        });

        getData();
    }

    public void getData() {

        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Notes",MODE_PRIVATE,null); // Database yoksa olusturur, varsa acar.

            Cursor cursor = database.rawQuery("SELECT * FROM notes", null); // Imlec: sorgu yapabiliyoruz
            int nameIx = cursor.getColumnIndex("notetitle");
            int idIx = cursor.getColumnIndex("id");

            while (cursor.moveToNext()) {
                titleArray.add(cursor.getString(nameIx));
                idArray.add(cursor.getInt(idIx));

            }

            arrayAdapter.notifyDataSetChanged(); // Yeni veri ekledim bunu göster

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == R.id.add_item) {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("info" , "new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}