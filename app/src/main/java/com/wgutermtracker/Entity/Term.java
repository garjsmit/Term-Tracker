package com.wgutermtracker.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.wgutermtracker.Util.WGUConst;
import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = WGUConst.TABLENAME_TERMS)
public class Term {

    @PrimaryKey(autoGenerate = true)
    private int termID;

    @ColumnInfo(name = WGUConst.TABLE_TERM_COL_NAME)
    private String  termName;

    @ColumnInfo(name = WGUConst.TABLE_TERM_COL_STARTDATE)
    private Date startDate;

    @ColumnInfo(name = WGUConst.TABLE_TERM_COL_ENDDATE)
    private Date endDate;

    public Term() {
        this.termName = null;
        this.startDate = null;
        this.endDate = null;
    }

    public Term(String termName, Date startDate, Date endDate) {
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Term(int termId, String termName, Date startDate, Date endDate) {
        this.termID = termId;
        this.termName = termName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getTermID() { return termID; }

    public String getTermName() {  return termName;  }

    public Date getStartDate() {  return startDate;  }

    public Date getEndDate() {  return endDate;   }

    public void setTermID(int termID) { this.termID = termID;    }

    public void setTermName(String termName) { this.termName = termName;  }

    public void setStartDate(Date startDate) {  this.startDate = startDate;  }

    public void setEndDate(Date endDate) {  this.endDate = endDate; }

    @Override
    public String toString() {
        return "Term{" +
                "termID=" + termID +
                ", termName='" + termName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}