package com.amt.instasport


//// Need to wrap this in a composable
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