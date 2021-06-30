package com.wgutermtracker.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.wgutermtracker.Entity.Term;

import java.util.List;

@Dao
public interface TermDao {

    @Query("SELECT * FROM terms ORDER BY termID")
    List<Term> getAllTerms();

    @Query("SELECT * FROM terms WHERE termID = :termID ORDER BY termID")
    Term findTerm(int termID);

    @Query("SELECT * FROM terms WHERE termName = :termName ORDER BY termID")
    Term findTerm(String termName);

    @Insert
    void insertTerm(Term term);

    @Insert
    void insertAllTerms(Term... term);

    @Update
    void updateTerm(Term term);

    @Delete
    void deleteTerm(Term term);

    @Query("DELETE FROM terms")
    void deleteAllTerms();
}
