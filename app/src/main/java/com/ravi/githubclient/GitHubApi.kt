package com.ravi.githubclient

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface GitHubApi {
    @GET("search/repositories")
    suspend fun searchRepositories(@Query("q") query: String): Response<SearchResponse>

    @GET("repos/{owner}/{repoName}")
    suspend fun getRepositoryDetails(@Path("owner") owner: String, @Path("repoName") repoName: String): Response<Repository>

    @GET
    suspend fun getContributors(@Url url: String): Response<List<Contributor>>
}

object RetrofitInstance {
    val api: GitHubApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }
}
