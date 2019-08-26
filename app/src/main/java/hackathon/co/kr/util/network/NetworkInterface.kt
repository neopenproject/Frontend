package hackathon.co.kr.util.network

import hackathon.co.kr.util.network.model.ProblemResponse
import hackathon.co.kr.util.network.model.ResponseVO
import hackathon.co.kr.util.network.model.StringResponse
import hackathon.co.kr.util.network.model2.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*
import kotlin.collections.HashMap

interface NetworkInterface {

    @Headers("Content-Type: application/json")
    @POST("/api/v1/account/cus")
    fun postAccountCus(
            @Body map : Map<String, String>
    ): Call<StringResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/v1/account/cus/login")
    fun postAccountCusLogin(
            @Body map : Map<String, String>
    ): Call<StringResponse>

    @GET("/api/v1/problem/post")
    fun getAnswerPost(): Call<ProblemResponse>

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

//    @FormUrlEncoded
//    @POST("/api/v1/answer/post")
//    fun postAnswerPost(
//            @Part image: MultipartBody.Part,
//            @Part("problem_post") problem_post: RequestBody,
//            @Part("is_over") is_over: RequestBody
//    ): Call<>

    @Multipart
    @POST("/api/v1/answer/post")
    fun postAnswerPost(
            @Header("Authorization") token : String,
            @Part image: MultipartBody.Part,
            @Part("problem_post") problem_post: RequestBody,
            @Part("is_over") is_over: RequestBody,
            @Part("time") time: RequestBody
    ): Call<BaseResponse>


}