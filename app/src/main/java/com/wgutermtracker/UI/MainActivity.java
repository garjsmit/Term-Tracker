package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wgutermtracker.Database.DegreePath;
import com.wgutermtracker.R;

public class MainActivity extends AppCompatActivity {

    private DegreePath degreePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        degreePath = new  DegreePath(getApplication());
        degreePath.getAllTerms();

    }

    public void viewTermListActivity(View view) {
        Intent intent = new Intent(MainActivity.this, TermListActivity.class);
        startActivity(intent);
    }

    public void viewInstructorList(View view) {
        Intent intent = new Intent(MainActivity.this, InstructorListActivity.class);
        startActivity(intent);
    }
}