package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.Entity.Term;
import com.wgutermtracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TermDetailActivity extends AppCompatActivity {

    ListView courseListView;
    TextView courseTitleText, startDateTextView, endDateTextView;
    WGU_DB db;
    Intent intent;
    int termID, courseID;
    Term selectedTerm;
    SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        db = WGU_DB.getInstance(getApplicationContext());
        intent = getIntent();
        termID = intent.getIntExtra("termID", -1);
        selectedTerm = db.termDao().findTerm(termID);
        format = new SimpleDateFormat("MM/dd/yy");

        courseListView = findViewById(R.id.courseListView);
        courseTitleText = findViewById(R.id.courseTitleText);
        startDateTextView = findViewById(R.id.startDateView);
        endDateTextView = findViewById(R.id.endDateView);

        populateTextViews();

        courseListView = findViewById(R.id.courseListView);

        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CourseDetailActivity.class);
                courseID = db.courseDao().getAllCourses(termID).get(position).getCourseID();
                intent.putExtra("courseID", courseID);
                intent.putExtra("termID", termID);
                startActivity(intent);
            }
        });
        populateCourseList();

    }

    private void populateTextViews() {
        if(selectedTerm != null){
            startDateTextView.setText(format.format(selectedTerm.getStartDate()));
            endDateTextView.setText(format.format(selectedTerm.getEndDate()));
            courseTitleText.setText(selectedTerm.getTermName());
        }
    }


    private void populateCourseList() {
        List<Course> allCourses = db.courseDao().getAllCourses(termID);
        String[] courses = new String[allCourses.size()];

        if(!allCourses.isEmpty()){
            for(int i = 0; i < allCourses.size(); i++){
                courses[i] = allCourses.get(i).getCourseName();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        courseListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    public void editTerm(View view) {
        Intent editTermIntent = new Intent(TermDetailActivity.this, EditTermActivity.class);
        editTermIntent.putExtra("termID", termID);
        startActivity(editTermIntent);
    }


    @Override
    protected void onResume() {
        populateCourseList();
        populateTextViews();
        super.onResume();

    }

}