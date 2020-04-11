package com.tp.programareamea.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.tp.programareamea.firebase.User
import com.tp.programareamea.mainActivity.MainActivity
import com.tp.programareamea.utils.Constants.Companion.USER
import com.tp.programareamea.login.GoogleAuthViewModel

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_google_auth.view.*

class GoogleAuthFragment : Fragment() {

    private lateinit var binding: View
    private var googleAuthViewModel: GoogleAuthViewModel? = null
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = inflater.inflate(R.layout.fragment_google_auth, container, false)
        initAuthViewModel()
        initSignInButton()
        initGoogleSignInClient()


        return binding.rootView

    }

    private fun initSignInButton() {
        binding.google_sign_in_button.setOnClickListener { _ -> signIn() }
    }

    private fun initAuthViewModel() {
        googleAuthViewModel = ViewModelProvider(this).get(GoogleAuthViewModel::class.java)
    }

    private fun initGoogleSignInClient() {
        googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity!!.applicationContext, googleSignInOptions)

        val account =
            GoogleSignIn.getLastSignedInAccount(context)// If account is not null, the user is already SIGNED IN

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

    //Sign in using Google Credentials using
    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        googleAuthViewModel!!.signInWithGoogle(googleAuthCredential)
        googleAuthViewModel!!.authenticatedUserLiveData!!.observe(this,
            Observer { authenticatedUser: User ->
                if (authenticatedUser.isNew) {
                    createNewUser(authenticatedUser)
                } else {
                    goToMainActivity(authenticatedUser)
                }
            }
        )
    }

    //Create User
    private fun createNewUser(authenticatedUser: User) {
        googleAuthViewModel!!.createUser(authenticatedUser)
        googleAuthViewModel!!.createdUserLiveData!!.observe(
            this,
            Observer { user: User ->
                if (user.isCreated) {
                    Toast.makeText(
                        activity,
                        "Hi" + user.name + "\n Your account was successfully created",
                        Toast.LENGTH_LONG
                    ).show()
                }
                goToMainActivity(user)
            }
        )
    }

    //Changes to main activity after login
    private fun goToMainActivity(user: User) {
        val intent = Intent(activity, MainActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        activity!!.finish()
    }


}