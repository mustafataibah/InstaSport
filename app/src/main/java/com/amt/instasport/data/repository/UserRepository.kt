//package com.amt.instasport.data.repository
//
//import com.amt.instasport.data.model.User
//import com.google.firebase.Firebase
//import com.google.firebase.database.DataSnapshot
//import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.ValueEventListener
//import com.google.firebase.database.database
//
//class UserRepository {
//
//    private val database = Firebase.database
//    private val userRef = database.getReference("users")
//
//    fun writeUser(user: User) {
//        userRef.child(user.uid).setValue(user)
//    }
//
//    fun readUser(userId: String, onSuccess: (User?) -> Unit, onFailure: (Exception?) -> Unit) {
//        userRef.child(userId).get().addOnSuccessListener { dataSnapshot ->
//            val user = dataSnapshot.getValue(User::class.java)
//            onSuccess(user)
//        }.addOnFailureListener { exception ->
//            onFailure(exception)
//        }
//    }
//
//    fun doesUserExist(userId: String, callback: (Boolean) -> Unit) {
//        userRef.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                callback(snapshot.exists())
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//        })
//    }
//}
