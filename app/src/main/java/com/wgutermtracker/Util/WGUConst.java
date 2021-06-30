package com.wgutermtracker.Util;

import androidx.room.ColumnInfo;

import java.util.Date;

public class WGUConst {

    public static final String DB_NAME = "wgu_db.db";
    public static final int NUMBER_OF_THREADS = 4;

    public static final String TABLENAME_TERMS= "terms";
    public static final String TABLE_TERM_COL_ID = "termID";
    public static final String TABLE_TERM_COL_NAME = "termName";
    public static final String TABLE_TERM_COL_STARTDATE = "startDate";
    public static final String TABLE_TERM_COL_ENDDATE = "endDate";

    public static final String TABLENAME_COURSES = "courses";
    public static final String TABLE_COURSE_COL_ID = "courseID";
    public static final String TABLE_COURSE_COL_TERMID = "termID_FK";
    public static final String TABLE_COURSE_COL_NAME = "courseName";
    public static final String TABLE_COURSE_COL_STARTDATE = "courseStartDate";
    public static final String TABLE_COURSE_COL_START_NOTIFY = "courseStartNotify";
    public static final String TABLE_COURSE_COL_ENDDATE = "courseEndDate";
    public static final String TABLE_COURSE_COL_END_NOTIFY = "courseEndNotify";
    public static final String TABLE_COURSE_COL_STATUS= "courseStatus";
    public static final String TABLE_COURSE_COL_INSTRUCTORID= "instructorID";
    public static final String TABLE_COURSE_COL_NOTES = "courseNotes";

    public static final String TABLENAME_ASSESSMENTS = "assessments";
    public static final String TABLE_ASSESS_COL_ID = "assessmentID";
    public static final String TABLE_ASSESS_COL_COURSEID = "courseID_FK";
    public static final String TABLE_ASSESS_COL_NAME = "assessmentName";
    public static final String TABLE_ASSESS_COL_START_DATE = "assessmentStartDate";
    public static final String TABLE_ASSESS_COL_START_NOTIFY = "assessmentStartNotify";
    public static final String TABLE_ASSESS_COL_END_DATE = "assessmentEndDate";
    public static final String TABLE_ASSESS_COL_END_NOTIFY = "assessmentEndNotify";
    public static final String TABLE_ASSESS_COL_TYPE = "assessmentType";

    public static final String TABLENAME_INSTRUCTOR = "instructors";
    public static final String TABLE_INSTRUCT_COL_ID = "instructorID";
    public static final String TABLE_INSTRUCT_COL_COURESID = "courseID_FK";
    public static final String TABLE_INSTRUCT_COL_NAME = "instructorName";
    public static final String TABLE_INSTRUCT_COL_EMAIL = "instructorEmail";

}
