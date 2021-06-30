package com.wgutermtracker.Database;

import android.app.Application;

import com.wgutermtracker.DAO.AssessmentDao;
import com.wgutermtracker.DAO.CourseDao;
import com.wgutermtracker.DAO.InstructorDao;
import com.wgutermtracker.DAO.TermDao;
import com.wgutermtracker.Entity.Assessment;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.Entity.Instructor;
import com.wgutermtracker.Entity.Term;

import java.util.List;

public class DegreePath {

    private TermDao termDao;
    private CourseDao courseDao;
    private AssessmentDao assessmentDao;
    private InstructorDao instructorDao;
    private List<Term> allTerms;
    private List<Course> allCourses;
    private List<Assessment> allAssessments;
    private List<Instructor> allInstructors;

    public DegreePath(Application application){
        WGU_DB db = WGU_DB.getInstance(application);
        termDao = db.termDao();
        courseDao = db.courseDao();
        assessmentDao = db.assessmentDao();
        instructorDao = db.instructorDao();

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public List<Term> getAllTerms(){
        WGU_DB.databaseWriteExecutor.execute(()->{
                allTerms=termDao.getAllTerms();
        });
        return allTerms;
    }

}
