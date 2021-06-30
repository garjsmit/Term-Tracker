package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Instructor;
import com.wgutermtracker.Entity.Term;
import com.wgutermtracker.R;
import com.wgutermtracker.Util.WGUConst;

import java.util.List;

public class InstructorListActivity extends AppCompatActivity {


    WGU_DB db;
    ListView instructorListView;
    int instructorID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_list);
        db = WGU_DB.getInstance(getApplicationContext());
        instructorListView = findViewById(R.id.instructorListView);

        instructorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditInstructorActivity.class);
                List<Instructor> instructorList = db.instructorDao().getAllInstructors();
                instructorID = instructorList.get(position).getInstructorID();
                intent.putExtra("instructorID", instructorID);
                startActivity(intent);
            }
        });
        populateInstructorList();
    }

    private void populateInstructorList() {
        List<Instructor> allInstructors = db.instructorDao().getAllInstructors();
        String[] instructors = new String[allInstructors.size()];

        if(!allInstructors.isEmpty()){
            for(int i = 0; i < allInstructors.size(); i++){
                instructors[i] = allInstructors.get(i).getInstructorName();
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, instructors);
        instructorListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void editInstructorDetailActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), EditInstructorActivity.class);
        intent.putExtra("instructorID", -1);
        intent.putExtra("courseID", -1);
        startActivity(intent);
    }

}