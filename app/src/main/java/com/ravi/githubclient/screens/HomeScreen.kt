package com.ravi.githubclient.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
    val repositories by viewModel.repositories.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                label = { Text("Search Repositories") },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { viewModel.searchRepositories(searchQuery.value) }
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }

        LazyColumn(state = listState) {
            itemsIndexed(repositories) { index, repo ->
                if (index >= repositories.size - 1 && repositories.isNotEmpty()) {
                    // Load next page when the last item is reached
                    LaunchedEffect(Unit) {
                        viewModel.fetchNextPage()
                    }
                }
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
