package com.tp.programareamea.splashActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tp.programareamea.firebase.User


class SplashViewModel(application: Application?) :
    AndroidViewModel(application!!) {
    private val splashRepository: SplashRepository = SplashRepository()
    var isUserAuthenticatedLiveData: LiveData<User>? = null
    var userLiveData: LiveData<User>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase()
    }

    fun setUid(uid: String?) {
        userLiveData = splashRepository.addUserToLiveData(uid)
    }

}