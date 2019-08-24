package hackathon.co.kr.util.network

import hackathon.co.kr.util.network.model.ProblemResponse
import hackathon.co.kr.util.network.model.StringResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkInterface {
    @FormUrlEncoded
    @POST("/api/v1/account/cus")
    fun postAccountCus(
            @Field("email") email: String,
            @Field("pwd") pwd: String
    ): Call<StringResponse>

    @FormUrlEncoded
    @POST("/api/v1/account/cus/login")
    fun postAccountCusLogin(
            @Field("email") email: String,
            @Field("pwd") pwd: String
    ): Call<StringResponse>

    @GET("/api/v1/problem/post")
    fun getAnswerPost(
            @Query("order") order: String?
    ): Call<ProblemResponse>

//    @FormUrlEncoded
//    @POST("/api/v1/problem/post")
//    fun postAnswerPost(
//    ): Call<>
//
//    @GET("/api/v1/answer/post")
//    fun getAnswerPost(
//            @Query("user_type") user_type: String?,
//            @Query("is_teacher") is_teacher: Boolean?,
//            @Query("is_grade") is_grade: Boolean?,
//            @Query("is_grade_view") is_grade_view: Boolean?
//    ): Call<>
//
//    @Multipart
//    @POST("/api/v1/answer/post")
//    fun postAnswerPost(
//            @Part image: MultipartBody.Part,
//            @Part("problem_post") problem_post: RequestBody,
//            @Part("is_over") is_over: RequestBody
//    ): Call<>
}