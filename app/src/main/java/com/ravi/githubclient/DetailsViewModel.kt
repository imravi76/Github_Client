package com.ravi.githubclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsViewModel : ViewModel() {
    private val _repository = MutableStateFlow<Repository?>(null)
    private val _contributors = MutableStateFlow<List<Contributor>>(emptyList())
    val contributors: StateFlow<List<Contributor>> get() = _contributors

    fun getRepositoryDetails(owner: String, repoName: String): StateFlow<Repository?> {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getRepositoryDetails(owner, repoName)
            if (response.isSuccessful) {
                _repository.value = response.body()
                _repository.value?.contributorsUrl?.let { fetchContributors(it) }
            }
        }
        return _repository
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

