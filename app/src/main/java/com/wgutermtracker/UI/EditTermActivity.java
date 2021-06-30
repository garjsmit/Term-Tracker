package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.wgutermtracker.DAO.TermDao;
import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.Entity.Term;
import com.wgutermtracker.R;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditTermActivity extends AppCompatActivity {

    WGU_DB db;
    Intent intent;
    ListView courseListView;
    EditText termTitleText, startDateView, endDateView;
    int termID, courseID;
    Term newTerm, selectedTerm;
    Course selectedCourse;
    List<Course> allCourses;
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
    Calendar startCalendar = Calendar.getInstance(), endCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startDatePicked, endDatePicked;
    Long startDate, endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_term);
        db = WGU_DB.getInstance(getApplicationContext());
        intent = getIntent();
        termID = intent.getIntExtra("termID", -1);

        if(termID < 0){
            newTerm = new Term("Edit Term Name", Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
            db.termDao().insertTerm(newTerm);
            termID = db.termDao().findTerm("Edit Term Name").getTermID();
        }

        selectedTerm = db.termDao().findTerm(termID);

        courseListView = findViewById(R.id.courseListView);
        termTitleText = findViewById(R.id.termTitleText);
        startDateView = findViewById(R.id.startDateView);
        endDateView = findViewById(R.id.endDateView);


        startDatePicked = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, month);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.add(Calendar.MONTH, 5);
                endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                startDateView.setText(format.format(startCalendar.getTime()));
                endDateView.setText(format.format(endCalendar.getTime()));
            }
        };

        startDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTermActivity.this, startDatePicked,
                        startCalendar.get(Calendar.YEAR),
                        startCalendar.get(Calendar.MONTH),
                        startCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        //No longer need the following code, because the endDate is determined by finding 6 months from the Start date of the Term.
/*

        endDatePicked = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, month);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                endDateView.setText(format.format(endCalendar.getTime()));
            }
        };

        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditTermActivity.this, endDatePicked,
                        endCalendar.get(Calendar.YEAR),
                        endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });
 */

        endDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "End dates are filled automatically. Six months from the beginning of the term", Toast.LENGTH_LONG);
                toast.show();
            }
        });

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

        populateTextView();
        populateCourseListView();
    }

    private void populateCourseListView() {
        allCourses = db.courseDao().getAllCourses(termID);
        String[] courses = new String[allCourses.size()];
        if(!allCourses.isEmpty()){
            for(int i=0; i < allCourses.size(); i++){
                courses[i] = allCourses.get(i).getCourseName();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courses);
        courseListView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void populateTextView() {
        if(selectedTerm != null){
            termTitleText.setText(selectedTerm.getTermName());
            startDateView.setText(format.format(selectedTerm.getStartDate()));
            endDateView.setText(format.format(selectedTerm.getEndDate()));
        }
    }

    public void saveTerm() throws  ParseException{
        String termName = String.valueOf(termTitleText.getText());
        Date termStartDate = format.parse(startDateView.getText().toString());
        Date termEndDate = format.parse(endDateView.getText().toString());
        newTerm = new Term(termID, termName, termStartDate, termEndDate);
        db.termDao().updateTerm(newTerm);
    }

    public void addCourse(View view) throws ParseException {
        saveTerm();
        Intent intent = new Intent(EditTermActivity.this, EditCourseActivity.class);
        intent.putExtra("courseID", -1);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTextView();
        populateCourseListView();
    }


    public void cancel(View view) {
        Intent intent = new Intent(EditTermActivity.this, TermDetailActivity.class);
        intent.putExtra("termID", termID);
        startActivity(intent);
    }

    public void deleteTerm(View view) {
        if(allCourses.isEmpty()) {
            db.termDao().deleteTerm(selectedTerm);
            Intent intent = new Intent(EditTermActivity.this, TermListActivity.class);
            startActivity(intent);
        }
        else{
            //display warning that terms cannot be deleted if they have courses.
            Toast.makeText(getApplicationContext(), "Terms cannot be deleted with scheduled courses.", Toast.LENGTH_LONG).show();
        }

    }

    public void saveTermAndReturnToTermList(View view) throws ParseException {
        saveTerm();
        Intent intent = new Intent(EditTermActivity.this, TermListActivity.class);
        startActivity(intent);
    }
}