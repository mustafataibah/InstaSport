//package com.amt.instasport.di
//
//import com.amt.instasport.auth.AuthenticationManager
//import com.amt.instasport.auth.UserDatabaseManager
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.FirebaseDatabase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ViewModelComponent
//
//@Module
//@InstallIn(ViewModelComponent::class)
//object AuthModule {
//    @Provides
//    fun provideAuthenticationManager(firebaseAuth: FirebaseAuth): AuthenticationManager {
//        return AuthenticationManager(firebaseAuth)
//    }
//
//    @Provides
//    fun provideUserDatabaseManager(firebaseDatabase: FirebaseDatabase): UserDatabaseManager {
//        return UserDatabaseManager(firebaseDatabase)
//    }
//}
//
