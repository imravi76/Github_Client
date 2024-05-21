package com.ravi.githubclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _repositories = MutableStateFlow<List<Repository>>(emptyList())
    val repositories: StateFlow<List<Repository>> get() = _repositories

    fun searchRepositories(query: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.searchRepositories(query)
            if (response.isSuccessful) {
                _repositories.value = response.body()?.items ?: emptyList()
            }
        }
    }
}
