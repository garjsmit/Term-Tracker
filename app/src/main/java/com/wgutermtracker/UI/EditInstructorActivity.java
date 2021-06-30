package com.wgutermtracker.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wgutermtracker.Database.WGU_DB;
import com.wgutermtracker.Entity.Instructor;
import com.wgutermtracker.R;

public class EditInstructorActivity extends AppCompatActivity {

    WGU_DB db;
    Intent intent;
    EditText instructorNameTextView, instructorEmailText;
    Instructor newInstructor, selectedInstructor;
    int instructorID;
    String instructorName, instructorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_instructor);
        db = WGU_DB.getInstance(getApplicationContext());
        intent = getIntent();
        instructorID = intent.getIntExtra("instructorID", -1);

        if(instructorID < 0){
            Instructor newInstructor = new Instructor("Instructor Name", "email@altavista.com");
            db.instructorDao().insertInstructor(newInstructor);
            instructorID = db.instructorDao().getInstructorByInstructorName("Instructor Name").getInstructorID();
        }
        selectedInstructor = db.instructorDao().getInstructorByInstructorID(instructorID);
        instructorNameTextView = findViewById(R.id.instructorNameTextView);
        instructorEmailText = findViewById(R.id.instructorEmailText);

        populateTextViews();

    }

    private void populateTextViews() {
        if(selectedInstructor != null){
            instructorNameTextView.setText(selectedInstructor.getInstructorName());
            instructorEmailText.setText(selectedInstructor.getEmail());
        }
    }

    public void saveInstructor(View view) {
        instructorName = instructorNameTextView.getText().toString();
        instructorEmail = instructorEmailText.getText().toString();

        System.out.println("instructorID" + instructorID);
        System.out.println("instructorName" + instructorName);
        System.out.println("instructorEmail" + instructorEmail);

        newInstructor = new Instructor(instructorID, instructorName, instructorEmail);
        db.instructorDao().updateInstructor(newInstructor);

        intent = new Intent(EditInstructorActivity.this, InstructorListActivity.class);
        intent.putExtra("instructorID", instructorID);
        startActivity(intent);

    }

    public void deleteInstructor(View view) {
        db.instructorDao().deleteInstructor(selectedInstructor);
        Intent intent = new Intent(EditInstructorActivity.this, InstructorListActivity.class);
        startActivity(intent);
    }
}