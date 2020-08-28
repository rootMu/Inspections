package com.matthew.inspections.dagger.modules

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.matthew.inspections.BuildConfig.BASE_URL
import com.matthew.inspections.network.InspectionsService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Network Specific Dependencies
 */
@InstallIn(ApplicationComponent::class)
@Module
object NetworkModule {

    /**
     * Provides the InspectionsService implementation
     * @param retrofit the Retrofit object used to instantiate the service
     * @return the Inspections Service implementation
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun providesInspectionsService(retrofit: Retrofit): InspectionsService {
        return retrofit.create(InspectionsService::class.java)
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    /**
     * Provides the Retrofit object.
     * @param okHttpClient the OkHttpClient object
     * @return the Retrofit object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideRetrofitInterface(okHttpClient: OkHttpClient, gson: Gson) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    /**
     * Provides the OkHttpClient object.
     * @param interceptor the HttpLoggingInterceptor object
     * @return the OkHttpClient object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()


    /**
     * Provides the HttpLoggingInterceptor object.
     * @return the HttpLoggingInterceptor object
     */
    @Provides
    @Reusable
    @JvmStatic
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Reusable
    @JvmStatic
    internal fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}