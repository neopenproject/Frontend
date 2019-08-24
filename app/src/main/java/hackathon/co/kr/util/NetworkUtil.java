package hackathon.co.kr.util;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class NetworkUtil {
    private static final String BASE_URL = "http://ec2-15-164-171-69.ap-northeast-2.compute.amazonaws.com/";
    private static NetworkInterface instance = null;

    public static synchronized NetworkInterface getInstance() {
        if (instance == null) {
            instance = new NetworkUtil().getService();
        }
        return instance;
    }

    private NetworkUtil() {

    }

    private NetworkInterface getService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder() // 네트워크 타임아웃 시간 설정
                .addInterceptor(logging)
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(NetworkInterface.class);
    }

}
