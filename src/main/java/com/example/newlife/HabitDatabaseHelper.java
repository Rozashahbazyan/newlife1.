package com.example.newlife;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//public class HabitDatabaseHelper extends SQLiteOpenHelper {
//
//    private static final String DB_NAME = "habits.db";
//    private static final int DB_VERSION = 1;
//
//    public HabitDatabaseHelper(Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//    }
//
//    @Override
////    public void onCreate(SQLiteDatabase db) {
////        // Create table to store habit completion records
////        db.execSQL("CREATE TABLE habit_completions (" +
////                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
////                "habit_id INTEGER," + // Foreign key to habit
////                "completion_date TEXT," + // Date in yyyy-MM-dd format
////                "is_completed INTEGER," + // 0 = not completed, 1 = completed
////                "UNIQUE(habit_id, completion_date))"); // Ensure unique entries per habit and date
////    }
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE IF NOT EXISTS habit_completions (" +
//                "habit_id INTEGER, " +
//                "is_completed INTEGER, " +
//                "completion_date TEXT, " +
//                "PRIMARY KEY (habit_id, completion_date))");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < newVersion) {
//            db.execSQL("DROP TABLE IF EXISTS habit_completions");
//            onCreate(db);
//        }
//    }
//
//    // Insert or update a habit's completion status for a specific date
//    public void insertOrUpdateHabitCompletion(long habitId, int isCompleted, String completionDate) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("INSERT OR REPLACE INTO habit_completions (habit_id, is_completed, completion_date) " +
//                "VALUES (?, ?, ?)", new Object[]{habitId, isCompleted, completionDate});
//    }
//
//    // Check if a habit is completed for a specific date
//    public boolean getHabitStatus(long habitId, String date) {
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.rawQuery(
//                "SELECT is_completed FROM habit_completions " +
//                        "WHERE habit_id = ? AND completion_date = ?",
//                new String[]{String.valueOf(habitId), date});
//
//        boolean isCompleted = false;
//        if (cursor != null) {
//            if (cursor.moveToFirst()) {
//                isCompleted = cursor.getInt(0) == 1; // 1 = completed
//            }
//            cursor.close();
//        }
//        return isCompleted;
//    }
//
//    // Retrieve completion data for the last 7 days
//    public Cursor getLastWeekData() {
//        return getReadableDatabase().rawQuery(
//                "SELECT habit_id, completion_date, is_completed FROM habit_completions " +
//                        "WHERE completion_date >= date('now', '-7 days') " +
//                        "ORDER BY completion_date DESC", null);
//    }
//
//    // Helper method to get the current date in yyyy-MM-dd format
//    public String getCurrentDate() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        return sdf.format(new Date());
//    }
//    public void saveHabitCompletionStatus(int habitId, boolean isCompleted, String date) {
//        SQLiteDatabase db = null;
//        try {
//            db = getWritableDatabase();
//            ContentValues values = new ContentValues();
//            values.put("habit_id", habitId);
//            values.put("is_completed", isCompleted ? 1 : 0);
//            values.put("completion_date", date);
//
//            db.insertWithOnConflict(
//                    "habit_completions",
//                    null,
//                    values,
//                    SQLiteDatabase.CONFLICT_REPLACE
//            );
//        } catch (Exception e) {
//            Log.e("HabitDatabaseHelper", "Error saving habit completion status", e);
//        } finally {
//            if (db != null) {
//                db.close();
//            }
//        }
//    }
//}
public class HabitDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "habits.db";
    private static final int DB_VERSION = 1;

    public HabitDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
//    public void onCreate(SQLiteDatabase db) {
//        // Create table to store habit completion records
//        db.execSQL("CREATE TABLE habit_completions (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "habit_id INTEGER," + // Foreign key to habit
//                "completion_date TEXT," + // Date in yyyy-MM-dd format
//                "is_completed INTEGER," + // 0 = not completed, 1 = completed
//                "UNIQUE(habit_id, completion_date))"); // Ensure unique entries per habit and date
//    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS habit_completions (" +
                "habit_id INTEGER, " +
                "is_completed INTEGER, " +
                "completion_date TEXT, " +
                "PRIMARY KEY (habit_id, completion_date))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS habit_completions");
            onCreate(db);
        }
    }

    // Insert or update a habit's completion status for a specific date
    public void insertOrUpdateHabitCompletion(long habitId, int isCompleted, String completionDate) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT OR REPLACE INTO habit_completions (habit_id, is_completed, completion_date) " +
                "VALUES (?, ?, ?)", new Object[]{habitId, isCompleted, completionDate});
    }

    // Check if a habit is completed for a specific date
    public boolean getHabitStatus(long habitId, String date) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT is_completed FROM habit_completions " +
                        "WHERE habit_id = ? AND completion_date = ?",
                new String[]{String.valueOf(habitId), date});

        boolean isCompleted = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                isCompleted = cursor.getInt(0) == 1; // 1 = completed
            }
            cursor.close();
        }
        return isCompleted;
    }

    // Retrieve completion data for the last 7 days
    public Cursor getLastWeekData() {
        return getReadableDatabase().rawQuery(
                "SELECT habit_id, completion_date, is_completed FROM habit_completions " +
                        "WHERE completion_date >= date('now', '-7 days') " +
                        "ORDER BY completion_date DESC", null);
    }

    // Helper method to get the current date in yyyy-MM-dd format
    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
    public void saveHabitCompletionStatus(long habitId, boolean isCompleted, String date) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("habit_id", habitId);
            values.put("is_completed", isCompleted ? 1 : 0);
            values.put("completion_date", date);

            db.insertWithOnConflict(
                    "habit_completions",
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE
            );
        } catch (Exception e) {
            Log.e("HabitDatabaseHelper", "Error saving habit completion status", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }


}