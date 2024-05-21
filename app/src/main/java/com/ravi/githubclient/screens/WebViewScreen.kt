package com.ravi.githubclient.screens

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(url: String) {
    val decodedUrl = Uri.decode(url)
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true // Enable JavaScript if needed
                loadUrl(decodedUrl)
            }
        },
        update = { webView ->
            webView.loadUrl(decodedUrl)
        }
    )
}

