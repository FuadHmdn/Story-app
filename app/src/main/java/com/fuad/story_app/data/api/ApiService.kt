package com.fuad.story_app.data.api

import com.fuad.story_app.data.api.response.DetailStoryResponse
import com.fuad.story_app.data.api.response.GetAllStoryResponse
import com.fuad.story_app.data.api.response.LoginResponse
import com.fuad.story_app.data.api.response.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
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
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response

    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
        @Query("size") size: Int
    ): GetAllStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): DetailStoryResponse
}