package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Assessment;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AssessmentDetailActivity extends AppCompatActivity {

    TextView assessmentNameText, courseTextView, startDateTextView, endDateTextView, typeTextView;
    WGU_DB db;
    Intent intent;
    int assessmentID, courseID, termID, notificationID;
    Assessment selectedAssessment;
    Switch assessmentStartDateSwitch, assessmentEndDateSwitch;
    SimpleDateFormat format;
    Long startDateInMilliSec, endDateInMilliSec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        db = WGU_DB.getInstance(getApplicationContext());
        intent = getIntent();
        assessmentID = intent.getIntExtra("assessmentID", -1);
        termID = intent.getIntExtra("termID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        format = new SimpleDateFormat("MM/dd/yy");
        selectedAssessment = db.assessmentDao().getAssessment(assessmentID);

        assessmentNameText = findViewById(R.id.assessmentNameText);
        courseTextView = findViewById(R.id.courseTextView);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        typeTextView = findViewById(R.id.typeTextView);
        assessmentStartDateSwitch = findViewById(R.id.assessmentStartDateSwitch);
        assessmentEndDateSwitch = findViewById(R.id.assessmentEndDateSwitch);

        String assessmentTitle = selectedAssessment.getAssessmentTitle();
        Date startDate = selectedAssessment.getAssessmentStartDate();
        Date endDate = selectedAssessment.getAssessmentEndDate();
        String assessmentType = selectedAssessment.getAssessmentType();

        assessmentStartDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean notifyStart = assessmentStartDateSwitch.isChecked();
                    boolean notifyEnd = assessmentEndDateSwitch.isChecked();

                    db.assessmentDao().updateAssessment(new Assessment(assessmentID, courseID, assessmentTitle, startDate, notifyStart, endDate, notifyEnd, assessmentType));

                    Intent notifyStartIntent = new Intent(AssessmentDetailActivity.this, AssessmentNotificationReceiver.class);
                    String startNotificationText = assessmentTitle + " has begun, " + format.format(startDate);
                    notifyStartIntent.putExtra("note", startNotificationText);
                    notifyStartIntent.putExtra("title", "An assessment has started");
                    PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetailActivity.this, ++notificationID, notifyStartIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    startDateInMilliSec = startDate.getTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, startDateInMilliSec, sender);

                }
            }
        });

        assessmentEndDateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    boolean notifyStart = assessmentStartDateSwitch.isChecked();
                    boolean notifyEnd = assessmentEndDateSwitch.isChecked();

                    Assessment newAssessment = new Assessment(courseID, assessmentTitle, startDate, notifyStart, endDate, notifyEnd, assessmentType);

                    db.assessmentDao().updateAssessment(newAssessment);

                    Intent notifyEndIntent = new Intent(AssessmentDetailActivity.this, AssessmentNotificationReceiver.class);
                    String startNotificationText = assessmentTitle + " has ended, " + format.format(startDate);
                    notifyEndIntent.putExtra("note", startNotificationText);
                    notifyEndIntent.putExtra("title", "An assessment has ended");
                    PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetailActivity.this, ++notificationID, notifyEndIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    endDateInMilliSec = endDate.getTime();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, endDateInMilliSec, sender);
                }
            }
        });

        populateTextViews();
    }

    private void populateTextViews() {
        if(selectedAssessment != null){
            assessmentNameText.setText(selectedAssessment.getAssessmentTitle());
            courseTextView.setText(db.courseDao().getCourse(selectedAssessment.getCourseID_FK()).getCourseName());
            startDateTextView.setText(format.format(selectedAssessment.getAssessmentStartDate()));
            endDateTextView.setText(format.format(selectedAssessment.getAssessmentEndDate()));
            assessmentStartDateSwitch.setChecked(selectedAssessment.isAssessmentEndNotify());
            assessmentEndDateSwitch.setChecked(selectedAssessment.isAssessmentEndNotify());
            typeTextView.setText(selectedAssessment.getAssessmentType());
        }
    }


    public void backToCourseDetails(View view) {
        Intent intent = new Intent(AssessmentDetailActivity.this, CourseDetailActivity.class);
        intent.putExtra("courseID", courseID);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }
}