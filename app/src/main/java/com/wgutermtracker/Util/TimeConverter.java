package com.wgutermtracker.Util;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class TimeConverter {
    @TypeConverter
    public static Date fromTimeStamp(Long timestamp) {return timestamp == null ? null : new Date(timestamp); }

    @TypeConverter
    public static Long dateToTimestamp(Date date) { return date == null ? null : date.getTime(); }

}
