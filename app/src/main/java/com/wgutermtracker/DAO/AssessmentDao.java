package com.wgutermtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wgutermtracker.Entity.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM assessments")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE assessmentID = :assessmentID")
    Assessment getAssessment(int assessmentID);

    @Query("SELECT * FROM assessments WHERE assessmentName = :assessmentName")
    Assessment getAssessment(String assessmentName);

    @Query("SELECT * FROM assessments WHERE courseID_FK = :courseID_FK ORDER BY assessmentID")
    List<Assessment> getAssessmentByCourse(int courseID_FK);

    @Insert
    void insertAssessment(Assessment assessment);

    @Insert
    void insertAllAssessments(Assessment... assessments);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Query("DELETE FROM assessments")
    void deleteAllAssessments();
}
