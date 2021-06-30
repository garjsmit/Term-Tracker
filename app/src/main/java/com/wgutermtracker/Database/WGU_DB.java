package com.wgutermtracker.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.wgutermtracker.DAO.AssessmentDao;
import com.wgutermtracker.DAO.CourseDao;
import com.wgutermtracker.DAO.InstructorDao;
import com.wgutermtracker.DAO.TermDao;
import com.wgutermtracker.Entity.Assessment;
import com.wgutermtracker.Entity.Course;
import com.wgutermtracker.Entity.Instructor;
import com.wgutermtracker.Entity.Term;
import com.wgutermtracker.Util.TimeConverter;
import com.wgutermtracker.Util.WGUConst;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Term.class, Course.class, Assessment.class, Instructor.class}, exportSchema = false, version = 8)
@TypeConverters({TimeConverter.class})
public abstract class WGU_DB extends RoomDatabase {

    private static volatile WGU_DB instance;

    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();
    public abstract InstructorDao instructorDao();

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(WGUConst.NUMBER_OF_THREADS);

    public static  WGU_DB getInstance(Context context){
        if(instance == null) {
            synchronized (WGU_DB.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), WGU_DB.class, WGUConst.DB_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> {

                //This is only for the purpose of entering sample data?
                TermDao termDao = instance.termDao();
                CourseDao courseDao = instance.courseDao();
                AssessmentDao assessmentDao = instance.assessmentDao();
                InstructorDao instructorDao = instance.instructorDao();

                termDao.deleteAllTerms();
                courseDao.deleteAllCourses();
                assessmentDao.deleteAllAssessments();
                instructorDao.deleteAllInstructors();


                Term term1 = new Term("Fall 2019", new Date(2019, 06, 01), new Date(2019, 11, 30));
                Term term2 = new Term("Spring 2020", new Date(2020, 00, 01), new Date(2020,05,31));
                Term term3 = new Term("Fall 2020", new Date(2021,06,01), new Date(2021,11,30));
                Term term4 = new Term("Spring 2021", new Date(2021-1900, 00, 01), new Date(2021-1900, 05, 30));

                termDao.insertAllTerms(term1,term2,term3,term4);
                List<Term> termList = termDao.getAllTerms();

                Instructor instructor1 = new Instructor("Mr. Dorsey", "jack@twitter.com");
                Instructor instructor2 = new Instructor("Mr. Zuckerberg", "mark@facebook.com");
                Instructor instructor3 = new Instructor("Carolyn Sher-DeCusatis","carolyn.sher@wgu.edu");
                Instructor instructor4 = new Instructor("Alvaro Escobar","alvaro.escobar@wgu.edu");
                Instructor instructor5 = new Instructor("Mr. Anderson", "neo@zion.org");
                instructorDao.insertAllInstructors(instructor1, instructor2, instructor3, instructor4, instructor5);
                List<Instructor> instructorList = instructorDao.getAllInstructors();

                Course course1t1 = new Course(termList.get(0).getTermID(), "Network & Security Foundations", new Date( 2019-1900, 10, 01), false, new Date(2019-1900,10,15), false,"Completed", instructorList.get(0).getInstructorID(), "Notes");
                Course course2t1 = new Course(termList.get(0).getTermID(), "Ethics in Technology", new Date( 2019-1900, 10, 16), false, new Date(2019-1900,10,31), false,"Completed", instructorList.get(0).getInstructorID(), "Notes");
                Course course3t1 = new Course(termList.get(0).getTermID(), "Scrip & Prog Foundations", new Date( 2019-1900, 11, 01), false, new Date(2019-1900,11,15), false, "Completed", instructorList.get(0).getInstructorID(), "Notes");
                Course course4t1 = new Course(termList.get(0).getTermID(), "Web Dev Foundations", new Date( 2019-1900, 11, 16), false, new Date(2019-1900,11,31), false, "Completed", instructorList.get(0).getInstructorID(), "Notes");

                Course course1t2 = new Course(termList.get(1).getTermID(), "Web Dev Applications", new Date(2020-1900, 00,01), false, new Date(2020-1900, 00, 15), false, "Completed",  instructorList.get(1).getInstructorID(), "Notes");
                Course course2t2 = new Course(termList.get(1).getTermID(), "Script & Prog Applications", new Date(2020-1900, 00,16), false, new Date(2020-1900, 00, 31),false, "Completed",  instructorList.get(1).getInstructorID(), "Notes");
                Course course3t2 = new Course(termList.get(1).getTermID(), "UI Design", new Date(2020-1900, 01,01), false,new Date(2020-1900, 01, 14),false,"Completed",  instructorList.get(1).getInstructorID(), "Notes");
                Course course4t2 = new Course(termList.get(1).getTermID(), "Tech Comm", new Date(2020-1900, 01,15), false,new Date(2020-1900, 01, 29),false,"Completed",  instructorList.get(1).getInstructorID(), "Notes");

                Course course1t3 = new Course(termList.get(2).getTermID(), "Software I", new Date(2020-1900, 06,01), false,new Date(2020-1900, 06, 31),false,"Completed",  instructorList.get(2).getInstructorID(), "Notes");
                Course course2t3 = new Course(termList.get(2).getTermID(), "Data Management Applications", new Date(2020-1900, 07,01), false,new Date(2020-1900, 07, 31),false,"Completed",  instructorList.get(2).getInstructorID(), "Notes");
                Course course3t3 = new Course(termList.get(2).getTermID(), "Software II", new Date(2020-1900, 10,01), false,new Date(2020-1900, 10, 31),false,"Completed",  instructorList.get(2).getInstructorID(), "Notes");
                Course course4t3 = new Course(termList.get(2).getTermID(), "UX Design", new Date(2020-1900, 11,15), false,new Date(2020-1900, 11, 31),false,"Completed",  instructorList.get(2).getInstructorID(), "Notes");

                Course course1t4 = new Course(termList.get(3).getTermID(), "Mobile App Dev", new Date(2021-1900, 00,15),false, new Date(2021-1900, 00, 31),false,"Completed",  instructorList.get(3).getInstructorID(), "Notes");
                Course course2t4 = new Course(termList.get(3).getTermID(), "Adv. Data Management", new Date(2021-1900, 01,01), false,new Date(2021-1900, 01, 28),false,"Completed",  instructorList.get(3).getInstructorID(), "Notes");
                Course course3t4 = new Course(termList.get(3).getTermID(), "Data Structures & Alg", new Date(2021-1900, 02,01), false,new Date(2021-1900, 02, 15),false,"Completed",  instructorList.get(3).getInstructorID(), "Notes");
                Course course4t4 = new Course(termList.get(3).getTermID(), "Software Dev Capstone", new Date(2021-1900, 03, 01), false,new Date(2021-1900, 03, 15),false,"In progress", instructorList.get(3).getInstructorID(), "Notes");
                courseDao.insertAllCourses(course1t1, course2t1, course3t1, course4t1, course1t2, course2t2, course3t2, course4t2, course1t3, course2t3, course3t3, course4t3, course1t4, course2t4, course3t4, course4t4);
                List<Course> courseList = courseDao.getAllCourses();

                Assessment assess1 = new Assessment(courseList.get(0).getCourseID(), "Network & Security Foundations", new Date( 2019-1900, 10, 01), false, new Date(2019-1900, 10,15),  false, "OA");
                Assessment assess2 = new Assessment(courseList.get(1).getCourseID(), "Ethics in Technology", new Date( 2019-1900, 10, 16), false,  new Date(2019-1900,10,31),  false, "OA");
                Assessment assess3 = new Assessment(courseList.get(2).getCourseID(), "Scrip & Prog Foundations", new Date( 2019-1900, 11, 01),   false, new Date(2019-1900,11,15),  false, "OA");
                Assessment assess4 = new Assessment(courseList.get(3).getCourseID(), "Web Dev Foundations", new Date( 2019-1900, 11, 16),  false, new Date(2019-1900,11,31),  false, "OA");
                Assessment assess5 = new Assessment(courseList.get(4).getCourseID(), "Web Dev Applications", new Date(2020-1900, 00,01),  false, new Date(2020-1900, 00, 15),  false, "OA");
                Assessment assess6 = new Assessment(courseList.get(5).getCourseID(), "Script & Prog Applications", new Date(2020-1900, 00,16),  false, new Date(2020-1900, 00, 31),  false, "PA");
                Assessment assess7 = new Assessment(courseList.get(6).getCourseID(), "UI Design", new Date(2020-1900, 01,01), false,  new Date(2020-1900, 01, 14),  false, "OA");
                Assessment assess8 = new Assessment(courseList.get(7).getCourseID(), "Tech Comm", new Date(2020-1900, 01,15),  false, new Date(2020-1900, 01, 29),  false, "PA");
                Assessment assess9 = new Assessment(courseList.get(8).getCourseID(), "Software I", new Date(2020-1900, 06,01), false,  new Date(2020-1900, 06, 31),  false, "PA");
                Assessment assess10 = new Assessment(courseList.get(9).getCourseID(), "Data Management Applications", new Date(2020, 07,01),  false,  new Date(2020, 07, 31),  false, "OA");
                Assessment assess11 = new Assessment(courseList.get(10).getCourseID(), "Software II", new Date(2020-1900, 10,01),  false, new Date(2020-1900, 10, 31),  false, "PA");
                Assessment assess12 = new Assessment(courseList.get(11).getCourseID(), "UX Design", new Date(2020-1900, 11,15),  false, new Date(2020-1900, 11, 31),  false, "PA");
                Assessment assess13 = new Assessment(courseList.get(12).getCourseID(), "Mobile App Assessment", new Date(2021-1900, 00,15),  false, new Date(2021-1900, 00, 31),  false, "PA");
                Assessment assess14 = new Assessment(courseList.get(13).getCourseID(), "Adv. Data Management Assessment", new Date(2021-1900, 01,01),  false, new Date(2021-1900, 01, 28), false, "OA");
                Assessment assess15 = new Assessment(courseList.get(14).getCourseID(), "Data Structures & Alg Assessment", new Date(2021-1900, 02,01),  false, new Date(2021-1900, 02, 15), false,  "OA");
                Assessment assess16 = new Assessment(courseList.get(15).getCourseID(), "Software Dev Capstone Assessment", new Date(2021-1900, 03, 01),  false, new Date(2021-1900,03,15),  false, "PA");

                assessmentDao.insertAllAssessments(assess1, assess2, assess3, assess4, assess5, assess6, assess7, assess8, assess9, assess10, assess11, assess12, assess13, assess14, assess15, assess16);
                //List<Assessment> assessmentList = assessmentDao.getAllAssessments();
            });
        }

    };

}
