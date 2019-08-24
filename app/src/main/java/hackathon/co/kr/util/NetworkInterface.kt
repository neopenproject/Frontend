package hackathon.co.kr.util

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkInterface {
    @GET
    fun getABCD(
            @Query("abcd") num: Int
    ): Call<String>
}