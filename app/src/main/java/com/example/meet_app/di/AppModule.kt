package com.example.meet_app.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.meet_app.api.user.UserApi
import com.example.meet_app.api.user.UserRepository
import com.example.meet_app.auth.AuthApi
import com.example.meet_app.auth.AuthRepository
import com.example.meet_app.auth.AuthRepositoryImpl
import com.example.meet_app.roomDb.AppDatabase
import com.example.meet_app.roomDb.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // api method
    @Provides
    @Singleton
    fun provideApi(prefs: SharedPreferences): Retrofit {
        // Create an OkHttpClient instance with the Bearer token
        val token = prefs.getString("jwt", null)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
//            .baseUrl("http://192.168.153.117:8080/")
            .baseUrl("http://192.168.132.164:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    }

    // roomdb method
    @Provides
    @Singleton
    fun provideRoomDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "meetapp_db"
        ).build()
    }

    // auth related methods
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences {
        return app.getSharedPreferences("prefs", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        userApi: UserApi,
        userDao: UserDao,
        prefs: SharedPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(api, userApi, prefs, userDao)
    }

    // user related methods
    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao, api: UserApi): UserRepository {
        return UserRepository(userDao, api)
    }

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }


}

