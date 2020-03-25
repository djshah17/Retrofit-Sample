# Retrofit-Sample
This is a demo app for how to use Retrofit in kotlin.


## Add Internet Permission in the AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

## Add the below dependencies in your app level build.gradle file
```gradle
//Retrofit
implementation 'com.squareup.retrofit2:retrofit:2.5.0'
implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
//logging
implementation 'com.squareup.okhttp3:logging-interceptor:3.9.1'
```

## Create a ApiClient class
```kotlin
object ApiClient {

    private const val BASE_URL: String = "https://jsonplaceholder.typicode.com/"

    private val gson : Gson by lazy {
        GsonBuilder().setLenient().create()
    }

    private val interceptor : HttpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val httpClient : OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    private val retrofit : Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService :  ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }

}
```

## Create an interface for different API calls
```kotlin
interface ApiService {

    @GET("users")
    fun getUsers(): Call<MutableList<User>>

}
```

## Create a response class to parse json data
```kotlin
data class User(
    @SerializedName("id")
    val id: Int? = null,
    ) : Serializable
```

## Define a method to call API
```kotlin
if (isInternetAvailable()) {
    getUsersData()
}

private fun getUsersData() {

        showProgressBar()

        ApiClient.apiService.getUsers().enqueue(object : Callback<MutableList<User>> {
            override fun onFailure(call: Call<MutableList<User>>, t: Throwable) {
                hideProgressBar()
                Log.e("error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<MutableList<User>>,
                response: Response<MutableList<User>>
            ) {
                hideProgressBar()
                val usersResponse = response.body()
                listUsers.clear()
                usersResponse?.let { listUsers.addAll(it) }
                adapter?.notifyDataSetChanged()
            }

        })

    }
```
