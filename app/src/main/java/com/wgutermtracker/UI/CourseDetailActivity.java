package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Assessment;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.Entity.Instructor;
import com.wgutermtracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CourseDetailActivity extends AppCompatActivity {

    ListView assessmentListView;
    TextView courseTitleText, statusText, startDateView, endDateView, instructorNameTextView, courseNotesText;
    WGU_DB db;
    Intent intent;
    int courseID, termID, notificationID;
    String instructorName;
    Course selectedCourse;
    Switch startDateSwitch, endDateSwitch;
    SimpleDateFormat format;
    Long startDateInMilliSec, endDateInMilliSec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        db = WGU_DB.getInstance(getApplicationContext());
        intent = getIntent();
        termID = intent.getIntExtra("termID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        selectedCourse = db.courseDao().getCourse(courseID);
        format = new SimpleDateFormat("MM/dd/yy");

        courseTitleText = findViewById(R.id.courseTitleText);
        statusText = findViewById(R.id.statusText);
        startDateView = findViewById(R.id.startDateView);
        endDateView = findViewById(R.id.endDateView);
        startDateSwitch = findViewById(R.id.startDateSwitch);
        endDateSwitch = findViewById(R.id.endDateSwitch);
        instructorNameTextView = findViewById(R.id.instructorNameTextView);
        courseNotesText = findViewById(R.id.courseNotesText);
        assessmentListView = findViewById(R.id.assessmentListView);

        Date startDate = selectedCourse.getCourseStartDate();
        Date endDate = selectedCourse.getCourseEndDate();
        int instructorID = selectedCourse.getInstructorID_FK();
        String courseName = selectedCourse.getCourseName();
        String notes = selectedCourse.getNote();
        String status = selectedCourse.getStatus();

        startDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean notifyStart = startDateSwitch.isChecked();
                    boolean notifyEnd = endDateSwitch.isChecked();

                    db.courseDao().updateCourse(new Course(courseID, termID, courseName, startDate, notifyStart, endDate, notifyEnd, status, instructorID, notes));

                    Intent notifyStartIntent = new Intent(CourseDetailActivity.this, CourseNotificationReceiver.class);
                    String startNotificationText = courseName + " has begun, " + format.format(startDate);
                    notifyStartIntent.putExtra("note", startNotificationText);
                    notifyStartIntent.putExtra("title", "A course has started");
                    PendingIntent sender = PendingIntent.getBroadcast(CourseDetailActivity.this, ++notificationID, notifyStartIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    startDateInMilliSec = startDate.getTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, startDateInMilliSec, sender);
                }
            }
        });

        endDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean notifyStart = startDateSwitch.isChecked();
                    boolean notifyEnd = endDateSwitch.isChecked();

                    db.courseDao().updateCourse(new Course(courseID, termID, courseName, startDate, notifyStart, endDate, notifyEnd, status, instructorID, notes));
                    Intent notifyEndIntent = new Intent(CourseDetailActivity.this, CourseNotificationReceiver.class);
                    String endNotificationText = courseName + " has ended, " + format.format(endDate);
                    notifyEndIntent.putExtra("note", endNotificationText);
                    notifyEndIntent.putExtra("title", "A course has ended");
                    PendingIntent sender = PendingIntent.getBroadcast(CourseDetailActivity.this, ++notificationID, notifyEndIntent, 0);
                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    endDateInMilliSec = endDate.getTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, endDateInMilliSec, sender);
                }
            }
        });


        //populates Assessment ListView & creates click listener & intent for assessment detail activity
        populateAssessmentList();
        assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AssessmentDetailActivity.class);
                int assessmentID;
                List<Assessment> courseAssessments = db.assessmentDao().getAssessmentByCourse(courseID);
                assessmentID = courseAssessments.get(position).getAssessmentID();

                intent.putExtra("assessmentID", assessmentID);
                intent.putExtra("courseID", courseID);
                intent.putExtra("termID", termID);
                startActivity(intent);
            }
        });
        populateTextViews();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.shareCourseNotes){
            String courseName = db.courseDao().getCourse(courseID).getCourseName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, selectedCourse.getNote());
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Course Notes for " + courseName);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Course Notes for " + courseName);
            startActivity(shareIntent);
        }
        return true;
    }

    private void populateAssessmentList() {
        List<Assessment> assessmentList = db.assessmentDao().getAssessmentByCourse(courseID);
        String[] assessments = new String[assessmentList.size()];

        if(!assessmentList.isEmpty()){
            for(int i =0; i<assessmentList.size(); i++){
                assessments[i] = assessmentList.get(i).getAssessmentTitle();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assessments);
        assessmentListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void populateTextViews() {
        if(selectedCourse != null){
            instructorName = db.instructorDao().getInstructorByInstructorID(selectedCourse.getInstructorID_FK()).getInstructorName();
            courseTitleText.setText(selectedCourse.getCourseName());
            statusText.setText(selectedCourse.getStatus());
            startDateView.setText(format.format(selectedCourse.getCourseStartDate()));
            endDateView.setText(format.format(selectedCourse.getCourseEndDate()));
            startDateSwitch.setChecked(selectedCourse.isCourseStartNotify());
            endDateSwitch.setChecked(selectedCourse.isCourseEndNotify());
            instructorNameTextView.setText(instructorName);
            courseNotesText.setText(selectedCourse.getNote());
        }
    }

    public void editCourse(View view) {
        Intent intent = new Intent(CourseDetailActivity.this, EditCourseActivity.class);
        intent.putExtra("termID", termID);
        intent.putExtra("courseID", courseID);
        startActivity(intent);
    }
}