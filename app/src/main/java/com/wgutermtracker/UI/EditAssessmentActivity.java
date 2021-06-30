package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Assessment;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class EditAssessmentActivity extends AppCompatActivity {

    WGU_DB db;
    Intent intent;
    TextView assessmentNameText, courseTextView, startDateTextView, endDateTextView, typeTextView;
    Assessment selectedAssessment, newAssessment;
    int assessmentID, courseID, termID;
    boolean assessmentStartNotify, assessmentEndNotify;
    Course selectedCourse;
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
    Calendar assessmentStartDateCal = Calendar.getInstance(), assessmentEndDateCal = Calendar.getInstance() ;
    Date assessmentStartDate, assessmentEndDate, courseStart, courseEnd;
    DatePickerDialog.OnDateSetListener startDatePicker, endDatePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assessment);
        db = WGU_DB.getInstance(getApplicationContext());
        intent = getIntent();
        assessmentID = intent.getIntExtra("assessmentID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        termID = intent.getIntExtra("termID", -1);
        selectedCourse = db.courseDao().getCourse(courseID);

        //if adding a course from scratch, it's ID is -1, so it new assessment is created, inserted in the db, and its assessmentID is retrieved.
        if (assessmentID < 0) {
            Date date = Calendar.getInstance().getTime();
            Assessment newAssessment = new Assessment(selectedCourse.getCourseID(), "Assessment Name", date, false, date, false, "PA");
            db.assessmentDao().insertAssessment(newAssessment);
            assessmentID = db.assessmentDao().getAssessment("Assessment Name").getAssessmentID();
        }

        selectedAssessment = db.assessmentDao().getAssessment(assessmentID);
        assessmentStartNotify = selectedAssessment.isAssessmentStartNotify();
        assessmentEndNotify = selectedAssessment.isAssessmentEndNotify();

        assessmentNameText = findViewById(R.id.assessmentNameText);
        courseTextView = findViewById(R.id.courseTextView);
        startDateTextView = findViewById(R.id.startDateTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        typeTextView = findViewById(R.id.typeTextView);

        startDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                assessmentStartDateCal.set(Calendar.YEAR, year);
                assessmentStartDateCal.set(Calendar.MONTH, month);
                assessmentStartDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                assessmentStartDate = assessmentStartDateCal.getTime();
                courseStart = selectedCourse.getCourseStartDate();
                courseEnd = selectedCourse.getCourseEndDate();

                if(assessmentStartDate.after(courseStart) && assessmentStartDate.before(courseEnd)){
                    startDateTextView.setText(format.format(assessmentStartDateCal.getTime()));
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Assessment dates must be within start and end of course", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };

        startDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAssessmentActivity.this, startDatePicker, assessmentStartDateCal.get(Calendar.YEAR), assessmentStartDateCal.get(Calendar.MONTH), assessmentStartDateCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                assessmentEndDateCal.set(Calendar.YEAR, year);
                assessmentEndDateCal.set(Calendar.MONTH, month);
                assessmentEndDateCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                assessmentEndDate = assessmentEndDateCal.getTime();
                courseStart = selectedCourse.getCourseStartDate();
                courseEnd = selectedCourse.getCourseEndDate();

                System.out.println("termStart" + courseStart);
                System.out.println("courseStartDate" + assessmentEndDate);
                System.out.println("termEnd" + courseEnd);

                if(assessmentEndDate.after(courseStart) && assessmentEndDate.before(courseEnd)){
                    endDateTextView.setText(format.format(assessmentEndDateCal.getTime()));
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Assessment dates must be within start and end of course", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };

        endDateTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditAssessmentActivity.this, endDatePicker, assessmentEndDateCal.get(Calendar.YEAR), assessmentEndDateCal.get(Calendar.MONTH), assessmentEndDateCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        populateTextViews();

    }


    private void populateTextViews() {
        if(selectedAssessment != null){
            assessmentNameText.setText(selectedAssessment.getAssessmentTitle());
            startDateTextView.setText(format.format(selectedAssessment.getAssessmentStartDate()));
            endDateTextView.setText(format.format(selectedAssessment.getAssessmentEndDate()));
            typeTextView.setText(selectedAssessment.getAssessmentType());
        }
        courseTextView.setText(db.courseDao().getCourse(courseID).getCourseName());
    }

    public void saveAssessment(View view) throws ParseException {
        String assessmentName = assessmentNameText.getText().toString();
        Date assessmentStartDate = format.parse(startDateTextView.getText().toString());
        Date assessmentEndDate = format.parse(endDateTextView.getText().toString());
        String assessmentType = typeTextView.getText().toString();

        newAssessment = new Assessment(assessmentID, courseID, assessmentName, assessmentStartDate, assessmentStartNotify, assessmentEndDate, assessmentEndNotify, assessmentType);

        db.assessmentDao().updateAssessment(newAssessment);

        Intent intent = new Intent(EditAssessmentActivity.this, AssessmentDetailActivity.class);
        intent.putExtra("assessmentID", assessmentID);
        intent.putExtra("courseID", courseID);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }

    //OBSOLETE NOTIFICATION FUNCTION
    /*
    public void notificationSetter(Date startDate, Date endDate){
        if(startDateSwitch.isChecked()){
            String courseName = db.courseDao().getCourse(courseID).getCourseName();
            String noteText = courseName + " assessment. Start Date: " + format.format(startDate);

            Intent notifyIntent = new Intent(EditAssessmentActivity.this, ObsoleteReceiver.class);
            notifyIntent.putExtra("note", noteText);
            PendingIntent sender = PendingIntent.getBroadcast(EditAssessmentActivity.this, ++assessmentStartAlertID, notifyIntent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            assessmentStartDateInMillisSec = startDate.getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, assessmentStartDateInMillisSec, sender);
        }

        if(endDateSwitch.isChecked()){
            String courseName = db.courseDao().getCourse(courseID).getCourseName();
            String noteText = courseName + " assessment. End Date: " + format.format(endDate);

            Intent notifyIntent = new Intent(EditAssessmentActivity.this, ObsoleteReceiver.class);
            notifyIntent.putExtra("note", noteText);
            PendingIntent sender = PendingIntent.getBroadcast(EditAssessmentActivity.this, ++assessmentEndAlertID, notifyIntent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            assessmentEndDateInMillisSec = endDate.getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, assessmentEndDateInMillisSec, sender);
        }
    }
    */

    public void backToEditCourse(View view) {
        Intent intent = new Intent(EditAssessmentActivity.this, EditCourseActivity.class);
        intent.putExtra("courseID", courseID);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }

    public void deleteAssessment(View view) {
        if(assessmentID > -1){
            db.assessmentDao().deleteAssessment(selectedAssessment);
        }
        Intent intent = new Intent(EditAssessmentActivity.this, EditCourseActivity.class);
        intent.putExtra("courseID", courseID);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }
}