//package com.amt.instasport
//
//import android.content.Intent
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import com.amt.instasport.ui.viewmodel.AuthViewModel
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.common.api.ApiException
//
//fun googleSignInLauncher(authViewModel: AuthViewModel): ActivityResultLauncher<Intent> {
//    return rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartActivityForResult()
//    ) { result ->
//        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
//        try {
//            val account = task.getResult(ApiException::class.java)
//            authViewModel.firebaseAuthWithGoogle(account.idToken!!)
//        } catch (e: ApiException) {
//            // Handle exceptions
//        }
//    }
//}