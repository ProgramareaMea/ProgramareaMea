package com.tp.programareamea.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.tp.programareamea.R
import com.tp.programareamea.databinding.ActivityAuthBinding
import com.tp.programareamea.firebase.User
import com.tp.programareamea.utils.Constants.Companion.USER


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private var authViewModel: AuthViewModel? = null
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        initAuthViewModel()
        initSignInButton()
        initGoogleSignInClient()

    }

    private fun initSignInButton() {
        binding.googleSignInButton.setOnClickListener { _ -> signIn() }
    }

    private fun initAuthViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    private fun initGoogleSignInClient() {
        gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account =
            GoogleSignIn.getLastSignedInAccount(this)// If account is not null, the user is already SIGNED IN

    }

    //opens the “Choose an account” dialog
    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    //When an account is selected, the function is triggered:
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount =
                    task.getResult(ApiException::class.java)
                googleSignInAccount?.let { getGoogleAuthCredential(it) }
            } catch (e: ApiException) {
                Log.d("Login error", e.message ?: "error getting message")
            }
        }
    }

    //get Google credentials
    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential: AuthCredential =
            GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        authViewModel!!.signInWithGoogle(googleAuthCredential)
        authViewModel!!.authenticatedUserLiveData!!.observe(this,
            Observer { authenticatedUser: User ->
                if (authenticatedUser.isNew) {
                    createNewUser(authenticatedUser)
                } else {
                    goToMainActivity(authenticatedUser)
                }
            }
        )
    }

    private fun createNewUser(authenticatedUser: User) {
        authViewModel!!.createUser(authenticatedUser)
        authViewModel!!.createdUserLiveData!!.observe(
            this,
            Observer { user: User ->
                if (user.isCreated) {
                    Toast.makeText(
                        this,
                        "Hi" + user.name + "\n Your account was successfully created",
                        Toast.LENGTH_LONG
                    ).show()
                }
                goToMainActivity(user)
            }
        )
    }

    private fun goToMainActivity(user: User) {
        val intent = Intent(this@AuthActivity, AuthActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }


}
