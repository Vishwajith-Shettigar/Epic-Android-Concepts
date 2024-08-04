package com.example.contentproviders

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.contentproviders.DatabaseHelper.Companion.TABLE_NAME

class MyContentProvider : ContentProvider() {
  companion object {
    const val AUTHORITY = "com.example.mycontentprovider"
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    const val MYTABLE = 1
    const val MYTABLE_ID = 2

    val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
      addURI(AUTHORITY, TABLE_NAME, MYTABLE)
      addURI(AUTHORITY, "$TABLE_NAME/#", MYTABLE_ID)
    }
  }

  private lateinit var databaseHelper: DatabaseHelper

  override fun onCreate(): Boolean {
    databaseHelper = DatabaseHelper(context!!)
    return true
  }

  override fun query(
    uri: Uri,
    columns: Array<out String>?,
    selection: String?,
    selectionArgs: Array<out String>?,
    sortOrder: String?,
  ): Cursor? {
    val db = databaseHelper.readableDatabase

    // This match making doesnt needed, beacuse we are making when beforing calling query.
    when (uriMatcher.match(uri)) {
      MYTABLE -> {
        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder)
      }
      MYTABLE_ID -> {
        return db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder)
      }
    }
    return null
  }

  override fun getType(uri: Uri): String {
    return when (uriMatcher.match(uri)) {
      MYTABLE -> "vnd.android.cursor.dir/vnd.com.example.$TABLE_NAME"
      MYTABLE_ID -> "vnd.android.cursor.item/vnd.com.example.$TABLE_NAME"
      else -> throw IllegalArgumentException("Unsupported URI: $uri")
    }
  }

  override fun insert(uri: Uri, values: ContentValues?): Uri {
    val db = databaseHelper.writableDatabase
    val id = db.insert(TABLE_NAME, null, values)
    context?.contentResolver?.notifyChange(uri, null)
    val resultUri = ContentUris.withAppendedId(CONTENT_URI, id)
    context?.contentResolver?.notifyChange(resultUri, null, true)
    return resultUri
  }

  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
    val db = databaseHelper.writableDatabase
    val count = db.delete(TABLE_NAME, selection, selectionArgs)
    context?.contentResolver?.notifyChange(uri, null)
    return count
  }

  override fun update(
    uri: Uri, values: ContentValues?, selection: String?,
    selectionArgs: Array<String>?,
  ): Int {
    val db = databaseHelper.writableDatabase
    val count = db.update(TABLE_NAME, values, selection, selectionArgs)
    context?.contentResolver?.notifyChange(uri, null)
    return count
  }
}
