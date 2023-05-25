package kg.surfit.testweekapp3.utils.retrofit

import kg.surfit.testweekapp3.models.GitRepo
import kg.surfit.testweekapp3.models.GitUser
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RestApiInterface {

    @GET("/users/{username}")
    suspend fun getGitUser(@Path("username") userName: String): GitUser

    @GET("/users/{username}/repos")
    suspend fun getUserRepositories(@Path("username") userName: String): List<GitRepo>



    companion object {
        private const val BASE_URL = "https://api.github.com"

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        operator fun invoke(): RestApiInterface {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestApiInterface::class.java)
        }
    }
}