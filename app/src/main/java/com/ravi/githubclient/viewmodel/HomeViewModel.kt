package com.ravi.githubclient.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.githubclient.utils.DatabaseHelper
import com.ravi.githubclient.utils.RetrofitInstance
import com.ravi.githubclient.model.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> get() = _repositories

    private var currentQuery = ""
    private var currentPage = 1
    private val pageSize = 10
    private var isLoading = false

    private val dbHelper = DatabaseHelper(application)

    init {
        loadSavedRepositories()
    }

    private fun loadSavedRepositories() {
        viewModelScope.launch {
            val savedRepositories = dbHelper.getRepositories()
            if (savedRepositories.isNotEmpty()) {
                _repositories.value = savedRepositories
            }
        }
    }

    fun searchRepositories(query: String) {
        currentQuery = query
        currentPage = 1
        _repositories.value = emptyList()
        fetchRepositories()
    }

    fun fetchNextPage() {
        if (isLoading) return
        currentPage++
        fetchRepositories()
    }

    private fun fetchRepositories() {
        viewModelScope.launch {
            isLoading = true
            val response = RetrofitInstance.api.searchRepositories(currentQuery, currentPage, pageSize)
            if (response.isSuccessful) {
                val newItems = response.body()?.items ?: emptyList()
                _repositories.value = _repositories.value + newItems

                if (currentPage == 1) {
                    dbHelper.addRepositories(newItems.take(10))
                }
            }
            isLoading = false
        }
    }
}
