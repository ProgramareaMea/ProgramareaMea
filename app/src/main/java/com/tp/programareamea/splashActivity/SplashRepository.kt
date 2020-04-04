package com.tp.programareamea.splashActivity

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.tp.programareamea.firebase.User
import com.tp.programareamea.utils.Constants.Companion.USERS


class SplashRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val user = User()
    private val usersRef = rootRef.collection(USERS)

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<User> {
        val isUserAuthenticateInFirebaseMutableLiveData: MutableLiveData<User> =
            MutableLiveData<User>()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            user.isAuthenticated = false
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        } else {
            user.uid = firebaseUser.uid
            user.isAuthenticated = true
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user)
        }
        return isUserAuthenticateInFirebaseMutableLiveData
    }

    //Gets user data from Firestore, creates User class, and stores in LiveData
    fun addUserToLiveData(uid: String?): MutableLiveData<User> {
        val userMutableLiveData: MutableLiveData<User> = MutableLiveData<User>()
        usersRef.document(uid!!).get()
            .addOnCompleteListener { userTask: Task<DocumentSnapshot?> ->
                if (userTask.isSuccessful) {
                    val document = userTask.result
                    if (document!!.exists()) {
                        val user: User? = document.toObject<User>(User::class.java)
                        userMutableLiveData.value = user
                    }
                } else {
                    Log.d("UserLiveDataError",userTask.exception!!.message ?: "could not get exception message")
                }
            }
        return userMutableLiveData
    }
}