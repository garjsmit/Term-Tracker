package com.wgutermtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wgutermtracker.Entity.Course;

import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM courses")
    List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE termID_FK = :termID_FK")
    List<Course> getAllCourses(int termID_FK);

    @Query("SELECT * FROM courses WHERE courseID = :courseID ORDER BY courseID")
    Course getCourse(int courseID);

    @Query("SELECT * FROM courses WHERE courseName = :courseName ORDER BY courseID")
    Course getCourse(String courseName);

    @Query("SELECT * FROM courses WHERE termID_FK = :termID_FK AND courseID = :courseID")
    Course getCourse(int termID_FK, int courseID);

    @Insert
    void insertCourse(Course course);

    @Insert
    void insertAllCourses(Course... course);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);

    @Query("DELETE FROM courses")
    void deleteAllCourses();

}
