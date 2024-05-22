package com.ravi.githubclient.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ravi.githubclient.model.Owner
import com.ravi.githubclient.model.Repository

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "repositories.db"
        private const val TABLE_REPOSITORIES = "repositories"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_OWNER = "owner"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_HTML_URL = "html_url"
        private const val COLUMN_CONTRIBUTORS_URL = "contributors_url"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_REPOSITORIES ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_OWNER TEXT,"
                + "$COLUMN_DESCRIPTION TEXT,"
                + "$COLUMN_HTML_URL TEXT,"
                + "$COLUMN_CONTRIBUTORS_URL TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REPOSITORIES")
        onCreate(db)
    }

    fun addRepositories(repositories: List<Repository>) {
        val db = this.writableDatabase
        repositories.forEach { repo ->
            val values = ContentValues().apply {
                put(COLUMN_NAME, repo.name)
                put(COLUMN_OWNER, repo.owner.login)
                put(COLUMN_DESCRIPTION, repo.description)
                put(COLUMN_HTML_URL, repo.html_url)
                put(COLUMN_CONTRIBUTORS_URL, repo.contributors_url)
            }
            db.insert(TABLE_REPOSITORIES, null, values)
        }
        db.close()
    }

    fun getRepositories(): List<Repository> {
        val repositoryList = mutableListOf<Repository>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_REPOSITORIES"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val repository = Repository(
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    owner = Owner(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_OWNER))),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    html_url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HTML_URL)),
                    contributors_url = cursor.getString(cursor.getColumnIndexOrThrow(
                        COLUMN_CONTRIBUTORS_URL
                    ))
                )
                repositoryList.add(repository)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return repositoryList
    }
}
