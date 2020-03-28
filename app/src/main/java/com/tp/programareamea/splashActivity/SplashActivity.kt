package com.tp.programareamea.splashActivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tp.programareamea.R
import com.tp.programareamea.databinding.ActivitySplashBinding
import com.tp.programareamea.firebase.User
import com.tp.programareamea.login.AuthActivity
import com.tp.programareamea.mainActivity.MainActivity
import com.tp.programareamea.utils.Constants.Companion.USER


class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    var splashViewModel: SplashViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_splash)
        initSplashViewModel()
        checkIfUserIsAuthenticated()
    }

    private fun initSplashViewModel() {
        splashViewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    private fun checkIfUserIsAuthenticated() {
        splashViewModel!!.checkIfUserIsAuthenticated()
        splashViewModel!!.isUserAuthenticatedLiveData!!.observe(this,
            Observer { user: User ->
                if (!user.isAuthenticated) {
                    goToAuthInActivity()
                    finish()
                } else {
                    getUserFromDatabase(user.uid)
                }
            }
        )
    }

    private fun goToAuthInActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
    }

    private fun getUserFromDatabase(uid: String) {
        splashViewModel!!.setUid(uid)
        splashViewModel!!.userLiveData!!.observe(
            this,
            Observer { user: User ->
                goToMainActivity(user)
                finish()
            }
        )
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
    }
}
