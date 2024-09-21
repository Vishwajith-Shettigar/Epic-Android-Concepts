package com.example.epic_android_concepts.contentprovider

import android.content.ContentUris
import android.content.ContentValues
import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contentproviders.Contact
import com.example.contentproviders.MyContentProvider.Companion.CONTENT_URI
import com.example.contentproviders.MyContentProvider.Companion.MYTABLE
import com.example.contentproviders.MyContentProvider.Companion.MYTABLE_ID
import com.example.contentproviders.MyContentProvider.Companion.uriMatcher
import com.example.contentproviders.adapter.BaseAdapter
import com.example.epic_android_concepts.databinding.ActivityMainBinding
import com.example.epic_android_concepts.databinding.ContactViewBinding

class MainActivity : AppCompatActivity() {
  private lateinit var binding: ActivityMainBinding
  private lateinit var contactsAdapter: BaseAdapter<Contact>

  private lateinit var contentObserver: ContentObserver

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    contactsAdapter = BaseAdapter()

    binding.rvContacts.apply {
      adapter = contactsAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    contactsAdapter.expressionGetViewType = { it ->
      BaseAdapter.ViewType.CONTACTS
    }

    contactsAdapter.expressionOnCreateViewHolder = { viewGroup, viewType ->
      ContactViewBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
    }

    contactsAdapter.expressionViewHolderBinding = { data, viewtype, viewBinding ->
      val itemBinding = viewBinding as ContactViewBinding
      itemBinding.name.text = data.name
      itemBinding.phoneNumber.text = data.phoneNumber
      itemBinding.updateBtn.setOnClickListener {
        updateContact(data.id)
      }
      itemBinding.deleteBtn.setOnClickListener {
        deleteContact(data.id)
      }
    }

    binding.insertBtn.setOnClickListener {
      val name = binding.name.text.toString()
      val phoneNumber = binding.phoneNumber.text.toString()
      insertNewContact(name, phoneNumber)
    }

    binding.queryBtn.setOnClickListener {
      getAllContacts()
    }

    // Register a ContentObserver to listen for changes
    contentObserver = object : ContentObserver(Handler()) {

      override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (uri != null) {
          Log.e("#", "Data change detected for URI: $uri")
          when (uriMatcher.match(uri)) {
            MYTABLE -> {
              getAllContacts()
            }
            MYTABLE_ID -> {
              getChangedContact(ContentUris.parseId(uri).toString())
            }
          }
        }
      }
    }

    contentResolver.registerContentObserver(
      CONTENT_URI,
      true,
      contentObserver
    )
  }

  private fun deleteContact(id: Int) {
    val selection = "_id = ?"
    val selectionArgs = arrayOf(id.toString())
    contentResolver.delete(CONTENT_URI, selection, selectionArgs)
  }

  private fun getChangedContact(id: String) {
    val selection = "_id = ?"
    val selectionArgs = arrayOf(id)
    val cursor = contentResolver.query(CONTENT_URI, null, selection, selectionArgs, null)
    contactsAdapter.itemList.clear()
    if (cursor != null) {
      while (cursor.moveToNext()) {
        val id = cursor.getInt(cursor.getColumnIndex("_id"))
        val name = cursor.getString(cursor.getColumnIndex("name"))
        val phone = cursor.getString(cursor.getColumnIndex("phone"))
        contactsAdapter.addItem(
          Contact(id, name, phone)
        )
      }
      cursor.close()
    }
  }

  private fun getAllContacts() {
    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
    contactsAdapter.itemList.clear()
    if (cursor != null) {
      while (cursor.moveToNext()) {
        val id = cursor.getInt(cursor.getColumnIndex("_id"))
        val name = cursor.getString(cursor.getColumnIndex("name"))
        val phone = cursor.getString(cursor.getColumnIndex("phone"))
        Log.e("#", id.toString())
        contactsAdapter.addItem(
          Contact(id, name, phone)
        )
      }
      cursor.close()
    }
  }

  private fun updateContact(id: Int) {
    val recordId = id
    val uri = Uri.parse("$CONTENT_URI/$recordId")
    val updateValues = ContentValues().apply {
      put("name", binding.name.text.toString())
      put("phone", binding.phoneNumber.text.toString())
    }

    val selection = "_id = ?"
    val selectionArgs = arrayOf(recordId.toString())
    val updateCount = contentResolver.update(uri!!, updateValues, selection, selectionArgs)

  }

  private fun insertNewContact(name: String, phoneNumber: String) {
    val contentValues = ContentValues().apply {
      put("name", name)
      put("phone", phoneNumber)
    }
    val newUri = contentResolver.insert(CONTENT_URI, contentValues)
  }

  override fun onDestroy() {
    super.onDestroy()
    // Unregister the ContentObserver when the activity is destroyed
    contentResolver.unregisterContentObserver(contentObserver)
  }
}
