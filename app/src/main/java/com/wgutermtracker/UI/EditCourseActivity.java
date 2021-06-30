package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Assessment;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.Entity.Instructor;
import com.wgutermtracker.Entity.Term;
import com.wgutermtracker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditCourseActivity extends AppCompatActivity {

    WGU_DB db;
    Intent intent;
    int courseID, termID;
    boolean courseStartNotify, courseEndNotify;
    ListView assessmentListView;
    EditText courseNameText, startDateView, endDateView, courseNotesText;
    TextView instructorNameTextView;
    Spinner statusSpinner, instructorSpinner;
    Course newCourse, selectedCourse;
    List<Instructor> allInstructors;
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy");
    Calendar courseStartCal = Calendar.getInstance(), courseEndCal = Calendar.getInstance();
    Date courseStartDate, courseEndDate, termStart, termEnd;
    DatePickerDialog.OnDateSetListener startDatePicked, endDatePicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        db = WGU_DB.getInstance(getApplicationContext());
        intent = getIntent();
        termID = intent.getIntExtra("termID", -1);
        courseID = intent.getIntExtra("courseID", -1);
        Term selectedTerm  = db.termDao().findTerm(termID);

        //if adding a new course from the EditTermActivity, the courseID sent is -1.
        // This code creates a new Term, inserts in the database, then retrieves it to get its actual courseID.
        if(courseID < 0 ){
            allInstructors = db.instructorDao().getAllInstructors();
            Course newCourse = new Course(termID, "Edit Course Name", Calendar.getInstance().getTime(), false, Calendar.getInstance().getTime(), false, "In progress", allInstructors.get(0).getInstructorID(), "Notes");
            System.out.println("Instructor's ID: " + allInstructors.get(0).getInstructorID());
            db.courseDao().insertCourse(newCourse);
            courseID = db.courseDao().getCourse("Edit Course Name").getCourseID();
        }

        selectedCourse = db.courseDao().getCourse(courseID);
        courseStartNotify = selectedCourse.isCourseStartNotify();
        courseEndNotify = selectedCourse.isCourseEndNotify();

        assessmentListView = findViewById(R.id.assessmentListView);
        courseNameText = findViewById(R.id.courseNameText);
        startDateView = findViewById(R.id.startDateView);
        endDateView = findViewById(R.id.endDateView);
        courseNotesText = findViewById(R.id.courseNotesText);
        statusSpinner = findViewById(R.id.statusSpinner);
        instructorSpinner = findViewById(R.id.instructorSpinner);
        instructorNameTextView = findViewById(R.id.instructorNameTextView);

        startDatePicked = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                courseStartCal.set(Calendar.YEAR, year);
                courseStartCal.set(Calendar.MONTH, month);
                courseStartCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                courseStartDate = courseStartCal.getTime();
                termStart = selectedTerm.getStartDate();
                termEnd = selectedTerm.getEndDate();

                System.out.println("termStart" + termStart);
                System.out.println("courseStartDate" + courseStartDate);
                System.out.println("termEnd" + termEnd);


                if(courseStartDate.after(termStart) && courseStartDate.before(termEnd)) {
                    startDateView.setText(format.format(courseStartCal.getTime()));
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Course dates must be within Start and End of Term", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        };

        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditCourseActivity.this, startDatePicked,
                        courseStartCal.get(Calendar.YEAR),
                        courseStartCal.get(Calendar.MONTH),
                        courseStartCal.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        endDatePicked = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                courseEndCal.set(Calendar.YEAR, year);
                courseEndCal.set(Calendar.MONTH, month);
                courseEndCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                courseEndDate = courseEndCal.getTime();
                termStart = selectedTerm.getStartDate();
                termEnd = selectedTerm.getEndDate();

                System.out.println("termStart" + termStart);
                System.out.println("courseEndDate" + courseEndDate);
                System.out.println("termEnd" + termEnd);

                if(courseEndDate.after(termStart) && courseEndDate.before(termEnd)) {
                    endDateView.setText(format.format(courseEndCal.getTime()));
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Course dates must be within Start and End of Term", Toast.LENGTH_LONG);
                    toast.show();
                }
                endDateView.setText(format.format(courseEndCal.getTime()));

            }
        };

        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditCourseActivity.this, endDatePicked,
                        courseEndCal.get(Calendar.YEAR),
                        courseEndCal.get(Calendar.MONTH),
                        courseEndCal.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });


        assessmentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), EditAssessmentActivity.class);
                int assessmentID;
                List<Assessment> courseAssessments = db.assessmentDao().getAssessmentByCourse(courseID);
                assessmentID = courseAssessments.get(position).getAssessmentID();
                intent.putExtra("termID", termID);
                intent.putExtra("assessmentID", assessmentID);
                intent.putExtra("courseID", courseID);
                startActivity(intent);
            }
        });

        populateTextViews();
        populateAssessmentListView();
        populateStatusSpinner();
        populateInstructorSpinner();
    }

    private void populateInstructorSpinner() {
        allInstructors = db.instructorDao().getAllInstructors();
        String[] instructors = new String[allInstructors.size()];

        String instructorName = db.instructorDao().getInstructorByInstructorID(selectedCourse.getInstructorID_FK()).getInstructorName();
        int instructorIndex = allInstructors.indexOf(db.instructorDao().getInstructorByInstructorName(instructorName));
        instructorSpinner.setSelection(instructorIndex);

        if(!allInstructors.isEmpty()){
            for(int i = 0; i < allInstructors.size(); i++){
                instructors[i] = allInstructors.get(i).getInstructorName();
                if(instructors[i].equals(instructorName)) instructorIndex = i;
            }
        }

        ArrayAdapter<String> instructorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, instructors);
        instructorSpinner.setAdapter(instructorAdapter);

        instructorSpinner.setSelection(instructorIndex);

    }

    private void populateStatusSpinner() {
        String[] statuses = {"Plan to take", "In progress", "Completed", "Dropped"};

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statuses);
        statusSpinner.setAdapter(statusAdapter);

    }

    private void populateTextViews() {
        if(selectedCourse != null){
            courseNameText.setText(selectedCourse.getCourseName());
            startDateView.setText(format.format(selectedCourse.getCourseStartDate()));
            endDateView.setText(format.format(selectedCourse.getCourseEndDate()));
            courseNotesText.setText(selectedCourse.getNote());
        }
    }

    private void populateAssessmentListView() {
        List<Assessment> assessmentList = db.assessmentDao().getAssessmentByCourse(courseID);
        String[] assessments = new String[assessmentList.size()];

        if(!assessmentList.isEmpty()){
            for(int i = 0 ; i<assessmentList.size(); i++){
                assessments[i] = assessmentList.get(i).getAssessmentTitle();
            }
        }

        ArrayAdapter<String> assessmentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assessments);
        assessmentListView.setAdapter(assessmentAdapter);

        assessmentAdapter.notifyDataSetChanged();
    }

    /*
    public void notificationSetter(Date date, int alertID, String noteText){
            Intent notifyIntent = new Intent(EditCourseActivity.this, ObsoleteReceiver.class);
            notifyIntent.putExtra("note", noteText);
            PendingIntent sender = PendingIntent.getBroadcast(EditCourseActivity.this, ++alertID, notifyIntent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            dateInMilliSec = date.getTime();
            alarmManager.set(AlarmManager.RTC_WAKEUP, dateInMilliSec, sender);
    }
    */

    public void saveCourse(View view) throws ParseException {
        String courseName = String.valueOf(courseNameText.getText());
        Date startDate = format.parse(startDateView.getText().toString());
        Date endDate = format.parse(endDateView.getText().toString());
        String courseStatus = String.valueOf(statusSpinner.getSelectedItem());
        String courseNote = String.valueOf(courseNotesText.getText());
        String instructorName = String.valueOf(instructorSpinner.getSelectedItem());
        //String courseStartNote = courseName + " has started, " + format.format(startDate);
        //String courseEndNote = courseName + " has ended, " + format.format(endDate);
        int instructorID = db.instructorDao().getInstructorByInstructorName(instructorName).getInstructorID();

        newCourse = new Course(courseID, termID, courseName, startDate, courseStartNotify, endDate, courseEndNotify, courseStatus, instructorID, courseNote);

        //OBSOLETE NOTIFICATION
        /*
        if(courseStartNotify) {
            notificationSetter(startDate, courseStartAlertID, courseStartNote);
        }
        if(courseEndNotify) {
            notificationSetter(endDate, courseEndAlertID, courseEndNote);
        }
         */

        db.courseDao().updateCourse(newCourse);

        Intent intent = new Intent(EditCourseActivity.this, CourseDetailActivity.class);
        intent.putExtra("courseID", courseID);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }

    public void deleteCourse(View view) {
        db.courseDao().deleteCourse(selectedCourse);
        Intent intent = new Intent(EditCourseActivity.this, EditTermActivity.class);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }

    public void createNewAssessment(View view) {
        Intent intent = new Intent(EditCourseActivity.this, EditAssessmentActivity.class);
        intent.putExtra("termID", termID);
        intent.putExtra("courseID", courseID);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedCourse = db.courseDao().getCourse(courseID);
        populateAssessmentListView();
        populateStatusSpinner();
        populateTextViews();
    }
}