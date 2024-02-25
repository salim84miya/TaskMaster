package com.example.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

private val DATABASE_NAME = "Note.db"
private val DATABASE_VERSION = 1

private val TABLE_NAME = "todo"
private val COLUMN_ID = "column_id"
private val TASK_NAME = "task_name"
private val TASK_STATUS ="task_status"


class DatabaseHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TASK_NAME + " TEXT,"+ TASK_STATUS+ " INTEGER DEFAULT 0);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(query)
        onCreate(db)

    }

    fun insertData(task: String,task_status:Int) {
        val db: SQLiteDatabase = writableDatabase
        val cv = ContentValues()

        cv.put(TASK_NAME, task)
        cv.put(TASK_STATUS,task_status)

        val result: Long = db.insert(TABLE_NAME, null, cv)


    }

    fun insertDataAgain(id:Int,task: String,task_status:Int) {
        val db: SQLiteDatabase = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_ID,id)
        cv.put(TASK_NAME, task)
        cv.put(TASK_STATUS,task_status)

        val result: Long = db.insert(TABLE_NAME, null, cv)


    }


    fun readData(): Cursor? {
        val query = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase
        var cursor: Cursor? =null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }
        return cursor
    }

    fun updateData(task: String, id: Int,status:Int) {
        val db: SQLiteDatabase = writableDatabase
        val cv = ContentValues()

        cv.put(TASK_NAME, task)
        cv.put(TASK_STATUS,status)


        val result: Int = db.update(TABLE_NAME, cv, "column_id=?", arrayOf(id.toString()))

    }

    fun deleteData(id: Int) {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "column_id=?", arrayOf(id.toString()))
        

    }
}