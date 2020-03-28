package com.tp.programareamea.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.tp.programareamea.R
import com.tp.programareamea.databinding.ActivityMainBinding
import com.tp.programareamea.firebase.User
import com.tp.programareamea.utils.Constants.Companion.USER


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val user: User = getUserFromIntent()
        initGoogleSignInClient()
        setMessageToMessageTextView(user)
    }

    private fun getUserFromIntent(): User {
        return intent.getSerializableExtra(USER) as User
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun setMessageToMessageTextView(user: User) {
        val message = "You are logged in as: " + user.name
        binding.mainTextView.text = message
    }
}
