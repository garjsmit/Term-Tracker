package com.wgutermtracker.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import com.wgutermtracker.Util.WGUConst;
import java.util.Date;
import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = WGUConst.TABLENAME_COURSES,
        foreignKeys = {
            @ForeignKey(
                entity = Term.class,
                parentColumns = WGUConst.TABLE_TERM_COL_ID,
                childColumns = WGUConst.TABLE_COURSE_COL_TERMID,
                onDelete =  CASCADE
            ),
            @ForeignKey(
                entity = Instructor.class,
                parentColumns = WGUConst.TABLE_INSTRUCT_COL_ID,
                childColumns = WGUConst.TABLE_COURSE_COL_INSTRUCTORID
            )
        }
)
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int courseID;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_TERMID)
    private int termID_FK;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_NAME)
    private String courseName;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_STARTDATE)
    private Date courseStartDate;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_START_NOTIFY)
    private boolean courseStartNotify;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_ENDDATE)
    private Date courseEndDate;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_END_NOTIFY)
    private boolean courseEndNotify;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_STATUS)
    private String status;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_INSTRUCTORID)
    private int instructorID_FK;

    @ColumnInfo(name = WGUConst.TABLE_COURSE_COL_NOTES)
    private String note;

    public Course() {
        this.termID_FK = -1;
        this.courseName = "";
        this.courseStartDate = null;
        this.courseStartNotify = false;
        this.courseEndDate = null;
        this.courseEndNotify = false;
        this.status = null;
        this.instructorID_FK = -1;
        this.note = null;
    }

    public Course(int termID_fk, String courseName, Date courseStartDate, boolean courseStartNotify, Date courseEndDate, boolean courseEndNotify, String status, int instructorID_FK, String note) {
        this.termID_FK = termID_fk;
        this.courseName = courseName;
        this.courseStartDate = courseStartDate;
        this.courseStartNotify = courseStartNotify;
        this.courseEndDate = courseEndDate;
        this.courseEndNotify = courseEndNotify;
        this.status = status;
        this.instructorID_FK = instructorID_FK;
        this.note = note;
    }

    public Course(int courseID, int termID_fk, String courseName, Date courseStartDate, boolean courseStartNotify, Date courseEndDate, boolean courseEndNotify, String status, int instructorID_FK, String note) {
        this.courseID = courseID;
        this.termID_FK = termID_fk;
        this.courseName = courseName;
        this.courseStartDate = courseStartDate;
        this.courseStartNotify = courseStartNotify;
        this.courseEndDate = courseEndDate;
        this.courseEndNotify = courseEndNotify;
        this.status = status;
        this.instructorID_FK = instructorID_FK;
        this.note = note;
    }

    public int getCourseID() { return courseID; }

    public int getTermID_FK() { return termID_FK; }

    public String getCourseName() { return courseName; }

    public Date getCourseStartDate() { return courseStartDate; }

    public boolean isCourseStartNotify() { return courseStartNotify;    }

    public Date getCourseEndDate() { return courseEndDate; }

    public boolean isCourseEndNotify() { return courseEndNotify;    }

    public String getStatus() { return status; }

    public int getInstructorID_FK() { return instructorID_FK; }

    public String getNote() { return note; }

    public void setCourseID(int courseID) { this.courseID = courseID; }

    public void setTermID_FK(int termID_FK) { this.termID_FK = termID_FK; }

    public void setCourseName(String courseName) { this.courseName = courseName; }

    public void setCourseStartDate(Date courseStartDate) { this.courseStartDate = courseStartDate; }

    public void setCourseStartNotify(boolean courseStartNotify) { this.courseStartNotify = courseStartNotify;    }

    public void setCourseEndDate(Date courseEndDate) { this.courseEndDate = courseEndDate; }

    public void setCourseEndNotify(boolean courseEndNotify) { this.courseEndNotify = courseEndNotify;    }

    public void setStatus(String status) { this.status = status; }

    public void setInstructorID_FK(int instructorID_FK){ this.instructorID_FK = instructorID_FK; }

    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return "Course{" +
                "courseID=" + courseID +
                ", termID_FK=" + termID_FK +
                ", courseName='" + courseName + '\'' +
                ", courseStartDate=" + courseStartDate +
                ", courseStartNotify=" + courseStartNotify +
                ", courseEndDate=" + courseEndDate +
                ", courseEndNotify=" + courseEndNotify +
                ", status='" + status + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
