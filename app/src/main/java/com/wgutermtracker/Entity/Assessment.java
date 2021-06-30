package com.wgutermtracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.wgutermtracker.Util.WGUConst;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = WGUConst.TABLENAME_ASSESSMENTS,
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = WGUConst.TABLE_COURSE_COL_ID,
                childColumns = WGUConst.TABLE_ASSESS_COL_COURSEID,
                onDelete = CASCADE
        )
)
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int assessmentID;

    @ColumnInfo(name = WGUConst.TABLE_ASSESS_COL_COURSEID)
    private int courseID_FK;

    @ColumnInfo(name = WGUConst.TABLE_ASSESS_COL_NAME)
    private String assessmentTitle;

    @ColumnInfo(name = WGUConst.TABLE_ASSESS_COL_START_DATE)
    private Date assessmentStartDate;

    @ColumnInfo(name = WGUConst.TABLE_ASSESS_COL_START_NOTIFY)
    private boolean assessmentStartNotify;

    @ColumnInfo(name = WGUConst.TABLE_ASSESS_COL_END_DATE)
    private Date assessmentEndDate;

    @ColumnInfo(name = WGUConst.TABLE_ASSESS_COL_END_NOTIFY)
    private boolean assessmentEndNotify;

    @ColumnInfo(name = WGUConst.TABLE_ASSESS_COL_TYPE)
    private String assessmentType;

    public Assessment() {
        this.assessmentID = -1;
        this.courseID_FK = -1;
        this.assessmentTitle = null;
        this.assessmentStartDate = null;
        this.assessmentStartNotify = false;
        this.assessmentEndDate = null;
        this.assessmentEndNotify = false;
        this.assessmentType = null;
    }

    public Assessment(int courseID_FK, String assessmentTitle, Date assessmentStartDate, boolean assessmentStartNotify, Date assessmentEndDate, boolean assessmentEndNotify, String asssessmentType) {
        this.courseID_FK = courseID_FK;
        this.assessmentTitle = assessmentTitle;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentStartNotify = assessmentStartNotify;
        this.assessmentEndDate = assessmentEndDate;
        this.assessmentEndNotify = assessmentEndNotify;
        this.assessmentType = asssessmentType;
    }

    public Assessment(int assessmentID, int courseID_FK, String assessmentTitle, Date assessmentStartDate, boolean assessmentStartNotify, Date assessmentEndDate,  boolean assessmentEndNotify, String asssessmentType) {
        this.assessmentID = assessmentID;
        this.courseID_FK = courseID_FK;
        this.assessmentTitle = assessmentTitle;
        this.assessmentStartDate = assessmentStartDate;
        this.assessmentStartNotify = assessmentStartNotify;
        this.assessmentEndDate = assessmentEndDate;
        this.assessmentEndNotify = assessmentEndNotify;
        this.assessmentType = asssessmentType;
    }


    public int getAssessmentID() { return assessmentID; }

    public int getCourseID_FK() { return courseID_FK; }

    public String getAssessmentTitle() { return assessmentTitle; }

    public Date getAssessmentStartDate() { return assessmentStartDate; }

    public boolean isAssessmentStartNotify() { return assessmentStartNotify;    }

    public Date getAssessmentEndDate() { return assessmentEndDate; }

    public boolean isAssessmentEndNotify() {  return assessmentEndNotify;    }

    public String getAssessmentType() { return assessmentType; }

    public void setAssessmentID(int assessmentID) { this.assessmentID = assessmentID; }

    public void setCourseID_FK(int courseID_FK) { this.courseID_FK = courseID_FK; }

    public void setAssessmentTitle(String assessmentTitle) { this.assessmentTitle = assessmentTitle; }

    public void setAssessmentStartDate(Date assessmentStartDate) { this.assessmentStartDate = assessmentStartDate; }

    public void setAssessmentStartNotify(boolean assessmentStartNotify) {  this.assessmentStartNotify = assessmentStartNotify;   }

    public void setAssessmentEndDate(Date assessmentEndDate) { this.assessmentEndDate = assessmentEndDate; }

    public void setAssessmentEndNotify(boolean assessmentEndNotify) {  this.assessmentEndNotify = assessmentEndNotify;    }

    public void setAssessmentType(String assessmentType) { this.assessmentType = assessmentType; }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentID=" + assessmentID +
                ", courseID_FK=" + courseID_FK +
                ", assessmentTitle='" + assessmentTitle + '\'' +
                ", assessmentStartDate=" + assessmentStartDate +
                ", assessmentStartNotify=" + assessmentStartNotify +
                ", assessmentEndDate=" + assessmentEndDate +
                ", assessmentEndDate=" + assessmentEndDate +
                ", assessmentType=" + assessmentType +
                '}';
    }
}
