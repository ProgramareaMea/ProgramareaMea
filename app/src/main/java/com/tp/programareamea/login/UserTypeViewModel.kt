package com.tp.programareamea.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import com.tp.programareamea.firebase.User


class UserTypeViewModel(application: Application?) : AndroidViewModel(application!!){
    private val authRepository: AuthRepository = AuthRepository()
    var createdUserLiveData: LiveData<User>? = null


    fun createUser(authenticatedUser: User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser)
    }
}