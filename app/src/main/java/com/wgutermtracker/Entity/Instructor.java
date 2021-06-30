package com.wgutermtracker.Entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.wgutermtracker.Util.WGUConst;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = WGUConst.TABLENAME_INSTRUCTOR)
public class Instructor {

    @PrimaryKey(autoGenerate = true)
    private int instructorID;

    @ColumnInfo(name = WGUConst.TABLE_INSTRUCT_COL_NAME)
    private String instructorName;

    @ColumnInfo(name = WGUConst.TABLE_INSTRUCT_COL_EMAIL)
    private String email;

    public Instructor() {
        this.instructorID = -1;
        this.instructorName = "";
        this.email = "";
    }

    public Instructor(String instructorName, String email) {
        this.instructorName = instructorName;
        this.email = email;
    }


    public Instructor(int instructorID, String instructorName, String email) {
        this.instructorID = instructorID;
        this.instructorName = instructorName;
        this.email = email;
    }

    public int getInstructorID() { return instructorID; }

    public String getInstructorName() { return instructorName; }

    public String getEmail() { return email; }

    public void setInstructorID(int instructorID) {
        this.instructorID = instructorID;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "instructorID=" + instructorID +
                ", instructorName='" + instructorName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
