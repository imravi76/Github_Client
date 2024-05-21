package com.ravi.githubclient.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ravi.githubclient.HomeViewModel
import com.ravi.githubclient.Repository

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val searchQuery = remember { mutableStateOf("") }
    val repositories = viewModel.repositories.collectAsState()

    Column {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            label = { Text("Search Repositories") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.clickable {
                        viewModel.searchRepositories(searchQuery.value)
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        LazyColumn {
            items(repositories.value) { repo ->
                RepositoryCard(repo) {
                    navController.navigate("details/${repo.owner.login}/${repo.name}")
                }
            }
        }
    }
}

@Composable
fun RepositoryCard(repo: Repository, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = repo.name, style = MaterialTheme.typography.bodySmall)
            Text(text = repo.description ?: "No description", style = MaterialTheme.typography.bodySmall)
        }
    }
}
