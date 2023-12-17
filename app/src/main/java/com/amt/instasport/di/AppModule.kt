//package com.amt.instasport.di
//
//import android.content.Context
//import com.google.firebase.analytics.FirebaseAnalytics
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    @Provides
//    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()
//
//    @Provides
//    @Singleton
//    fun provideFirebaseAnalytics(@ApplicationContext context: Context): FirebaseAnalytics {
//        return FirebaseAnalytics.getInstance(context)
//    }
//
//    @Provides
//    @Singleton
//    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()
//
//}
