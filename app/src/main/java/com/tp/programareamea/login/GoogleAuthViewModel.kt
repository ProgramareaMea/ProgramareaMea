package com.tp.programareamea.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import com.tp.programareamea.firebase.User


class GoogleAuthViewModel(application: Application?) : AndroidViewModel(application!!){
    private val authRepository: AuthRepository = AuthRepository()
    var authenticatedUserLiveData: LiveData<User>? = null


    fun signInWithGoogle(googleAuthCredential: AuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential)
    }

}