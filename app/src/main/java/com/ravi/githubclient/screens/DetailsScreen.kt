package com.ravi.githubclient.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape


import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ravi.githubclient.Contributor
import com.ravi.githubclient.DetailsViewModel
import coil.compose.rememberAsyncImagePainter

@Composable
fun DetailsScreen(owner: String, repoName: String, navController: NavController) {
    val viewModel: DetailsViewModel = viewModel()
    val repository = viewModel.getRepositoryDetails(owner, repoName).collectAsState()
    val contributors by viewModel.contributors.collectAsState()

    repository.value?.let { repo ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = repo.name, style = MaterialTheme.typography.bodySmall)
            Text(text = repo.description ?: "No description", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Project Link",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val encodedUrl = Uri.encode(repo.htmlUrl)
                    Log.d("DetailsScreen", "Navigating to: $encodedUrl") // Log URL
                    navController.navigate("webview/$encodedUrl")
                }
            )
            Text(text = "Contributors:", style = MaterialTheme.typography.bodySmall)
            contributors.forEach { contributor ->
                ContributorCard(contributor)
            }
        }
    }
}

@Composable
fun ContributorCard(contributor: Contributor) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(contributor.avatarUrl),
                contentDescription = null,
                modifier = Modifier.size(50.dp).clip(CircleShape)
            )

            Column {
                Text(text = contributor.login, style = MaterialTheme.typography.bodySmall)
                Text(text = "Contributions: ${contributor.contributions}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
