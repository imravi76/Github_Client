package com.ravi.githubclient.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.ravi.githubclient.viewmodel.HomeViewModel
import com.ravi.githubclient.model.Repository

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = viewModel()
    val searchQuery = remember { mutableStateOf("") }
    val repositories by viewModel.repositories.collectAsState()
    val listState = rememberLazyListState()
    rememberCoroutineScope()

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
