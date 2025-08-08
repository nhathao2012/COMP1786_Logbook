package com.example.todolisttask3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "TaskDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String CREATE_TABLE_TASK = "CREATE TABLE Task (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "deadline TEXT, " +
                    "duration INTEGER, " +
                    "description TEXT," +
                    "isTaskDone INTEGER DEFAULT 0)";
            db.execSQL(CREATE_TABLE_TASK);
            Log.w("DatabaseHelper", "Table Task created successfully.");
        } catch (Exception e) {
            Log.w("DatabaseHelper", "Error creating table Task", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Task");
        onCreate(db);
    }

    public Cursor readAllTask() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Task",
                new String[]{"_id", "name", "deadline", "duration", "description","isTaskDone"},
                null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public long createNewTask(String name, String deadline, int duration, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        ContentValues rowValues = new ContentValues();
        rowValues.put("name", name);
        rowValues.put("deadline", deadline);
        rowValues.put("duration", duration);
        rowValues.put("description", description);
        try {
            result = db.insertOrThrow("Task", null, rowValues);
        } finally {
            db.close();
        }
        return result;
    }

    public void updateTask(long id, String name, String deadline, int duration, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues rowValues = new ContentValues();
        rowValues.put("name", name);
        rowValues.put("deadline", deadline);
        rowValues.put("duration", duration);
        rowValues.put("description", description);
        db.update("Task", rowValues, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public Cursor getTaskById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Task",
                new String[]{"_id", "name", "deadline", "duration", "description","isTaskDone"},
                "_id = ?",
                new String[]{String.valueOf(id)},
                null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public void deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Task", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void updateTaskStatus(long taskId, boolean isDone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isTaskDone", isDone ? 1 : 0);

        String selection = "_id = ?";
        String[] selectionArgs = { String.valueOf(taskId) };

        db.update("Task", values, selection, selectionArgs);
        db.close();
    }

    public void clearDoneTask() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Task", "isTaskDone = ?", new String[]{"1"});
        db.close();
    }

}
