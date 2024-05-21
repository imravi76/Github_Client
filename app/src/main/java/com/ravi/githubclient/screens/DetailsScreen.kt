package com.ravi.githubclient.screens

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import coil.compose.rememberAsyncImagePainter
import com.ravi.githubclient.Contributor
import com.ravi.githubclient.DetailsViewModel

@Composable
fun DetailsScreen(owner: String, repoName: String, navController: NavController) {
    val viewModel: DetailsViewModel = viewModel()
    viewModel.getRepositoryDetails(owner, repoName)
    val repository by viewModel.repository.collectAsState()
    val contributors by viewModel.contributors.collectAsState()

    repository?.let { repo ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = repo.name, style = MaterialTheme.typography.bodySmall)
            Text(text = repo.description ?: "No description", style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Project Link",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable {
                    val encodedUrl = Uri.encode(repo.html_url)
                    Log.d("DetailsScreen", "Navigating to: $encodedUrl")
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
                painter = rememberAsyncImagePainter(contributor.avatar_url),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(text = contributor.login, style = MaterialTheme.typography.bodySmall)
                Text(text = "Contributions: ${contributor.contributions}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
