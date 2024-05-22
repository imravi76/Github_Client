package com.ravi.githubclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> get() = _repositories

    private var currentQuery = ""
    private var currentPage = 1
    private val pageSize = 10
    private var isLoading = false

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
            }
            isLoading = false
        }
    }
}
