package com.medina.juanantonio.watcher.di

import android.content.Context
import androidx.room.Room
import com.medina.juanantonio.watcher.R
import com.medina.juanantonio.watcher.database.WatcherDb
import com.medina.juanantonio.watcher.network.ApiService
import com.medina.juanantonio.watcher.sources.content.ContentRemoteSource
import com.medina.juanantonio.watcher.sources.content.ContentRepository
import com.medina.juanantonio.watcher.sources.content.IContentRemoteSource
import com.medina.juanantonio.watcher.sources.content.IContentRepository
import com.medina.juanantonio.watcher.sources.media.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideApiService(
        @ApplicationContext context: Context
    ): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val requiredHeaderInterceptor = Interceptor { chain ->
            val requestBuilder =
                chain.request()
                    .newBuilder()
                    .addHeader("lang", "en")
                    .addHeader("versioncode", "11")
                    .addHeader("clienttype", "ios_jike_default")

            chain.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.MINUTES)
            .writeTimeout(20, TimeUnit.MINUTES)
            .readTimeout(20, TimeUnit.MINUTES)
            .addInterceptor(requiredHeaderInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(context.getString(R.string.loklok_api_base_url))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWatcherDb(@ApplicationContext context: Context): WatcherDb {
        return Room.databaseBuilder(context, WatcherDb::class.java, "watcher.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoDatabase(
        watcherDb: WatcherDb
    ): IVideoDatabase {
        return VideoDatabase(watcherDb)
    }

    @Provides
    @Singleton
    fun provideContentRemoteSource(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): IContentRemoteSource {
        return ContentRemoteSource(context, apiService)
    }

    @Provides
    @Singleton
    fun provideContentRepository(
        remoteSource: IContentRemoteSource,
        database: IVideoDatabase
    ): IContentRepository {
        return ContentRepository(remoteSource, database)
    }

    @Provides
    @Singleton
    fun provideMediaRemoteSource(
        @ApplicationContext context: Context,
        apiService: ApiService
    ): IMediaRemoteSource {
        return MediaRemoteSource(context, apiService)
    }

    @Provides
    @Singleton
    fun provideMediaRepository(
        remoteSource: IMediaRemoteSource,
        database: IVideoDatabase
    ): IMediaRepository {
        return MediaRepository(remoteSource, database)
    }
}