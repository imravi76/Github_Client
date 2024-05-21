package com.ravi.githubclient

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items") val items: List<Repository>
)

data class Repository(
    val name: String,
    val owner: Owner,
    val description: String?,
    val html_url: String,
    val contributors_url: String
)

data class Owner(
    val login: String
)

data class Contributor(
    val login: String,
    val avatar_url: String,
    val html_url: String,
    val contributions: Int
)
