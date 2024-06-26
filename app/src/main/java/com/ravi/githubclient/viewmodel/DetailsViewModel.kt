package com.ravi.githubclient.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.githubclient.utils.RetrofitInstance
import com.ravi.githubclient.model.Contributor
import com.ravi.githubclient.model.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val _repository = MutableStateFlow<Repository?>(null)
    val repository: StateFlow<Repository?> get() = _repository

    private val _contributors = MutableStateFlow<List<Contributor>>(emptyList())
    val contributors: StateFlow<List<Contributor>> get() = _contributors

    fun getRepositoryDetails(owner: String, repoName: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getRepositoryDetails(owner, repoName)
            if (response.isSuccessful) {
                _repository.value = response.body()
                _repository.value?.contributors_url?.let { fetchContributors(it) }
            }
        }
    }

    private fun fetchContributors(contributorsUrl: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getContributors(contributorsUrl)
            if (response.isSuccessful) {
                _contributors.value = response.body() ?: emptyList()
            }
        }
    }
}


