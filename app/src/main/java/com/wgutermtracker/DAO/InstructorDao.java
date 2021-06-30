package com.wgutermtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.wgutermtracker.Entity.Instructor;
import java.util.List;

@Dao
public interface InstructorDao {

    @Query("SELECT * FROM instructors ORDER BY instructorID")
    List<Instructor> getAllInstructors();

    @Query("SELECT * FROM instructors WHERE instructorID = :instructorID")
    Instructor getInstructorByInstructorID(int instructorID);

    @Query("SELECT * FROM instructors WHERE instructorName = :instructorName")
    Instructor getInstructorByInstructorName(String instructorName);

    @Insert
    void insertInstructor(Instructor instructor);

    @Insert
    void insertAllInstructors(Instructor... instructor);

    @Update
    void updateInstructor(Instructor instructor);

    @Delete
    void deleteInstructor(Instructor instructor);

    @Query("DELETE FROM instructors")
    void deleteAllInstructors();
}
