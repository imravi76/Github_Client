package com.ravi.githubclient

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items") val items: List<Repository>
)

data class Repository(
    val name: String,
    val owner: Owner,
    val description: String?,
    val htmlUrl: String,
    val contributorsUrl: String
)

data class Owner(
    val login: String
)

data class Contributor(
    val login: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val contributions: Int
)
