package com.example.contentproviders

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
  context, DATABASE_NAME, null, DATABASE_VERSION
) {

  companion object {
    const val DATABASE_NAME = "mydatabase"
    const val DATABASE_VERSION = 1
    const val TABLE_NAME = "contacts"
    const val TABLE_CREATE_QUERY = "create table $TABLE_NAME" +
      "(_id INTEGER PRIMARY KEY NOT NULL," +
      "name TEXT," +
      "phone TEXT)"
  }

  override fun onCreate(db: SQLiteDatabase?) {
    db?.execSQL(TABLE_CREATE_QUERY)
  }

  override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
    db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
  }
}
