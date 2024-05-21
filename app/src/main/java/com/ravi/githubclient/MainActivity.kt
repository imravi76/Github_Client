package com.ravi.githubclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ravi.githubclient.navigation.NavGraph
import com.ravi.githubclient.ui.theme.GithubClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubClientTheme {
                NavGraph()
            }
        }
    }
}
