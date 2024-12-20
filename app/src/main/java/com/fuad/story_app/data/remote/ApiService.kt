package com.fuad.story_app.data.remote

import com.fuad.story_app.data.remote.response.DetailStoryResponse
import com.fuad.story_app.data.remote.response.GetAllStoryResponse
import com.fuad.story_app.data.remote.response.LoginResponse
import com.fuad.story_app.data.remote.response.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null,
        @retrofit2.http.Header("Authorization") token: String
    ): Response

    @GET("stories")
    suspend fun getAllStories(
        @retrofit2.http.Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int
    ): GetAllStoryResponse

    @GET("stories")
    suspend fun getStoriesLocation(
        @retrofit2.http.Header("Authorization") token: String,
        @Query("location") location: Int = 1,
        @Query("size") size: Int = 50
    ): GetAllStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String,
        @retrofit2.http.Header("Authorization") token: String
    ): DetailStoryResponse
}