package com.tp.programareamea.mainActivity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.tp.programareamea.R
import com.tp.programareamea.databinding.ActivityMainBinding
import com.tp.programareamea.firebase.User
import com.tp.programareamea.login.AuthActivity
import com.tp.programareamea.utils.Constants.Companion.USER


class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    private lateinit var binding: ActivityMainBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
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

        val account =
            GoogleSignIn.getLastSignedInAccount(this)// If account is not null, the user is already SIGNED IN
    }

    private fun setMessageToMessageTextView(user: User) {
        val message = "You are logged in as: " + user.name
        binding.mainTextView.text = message
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            goToAuthInActivity()
        }
    }

    private fun goToAuthInActivity() {
        val intent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(intent)
    }

    private fun signOut() {
        singOutFirebase()
        signOutGoogle()
    }

    private fun singOutFirebase() {
        firebaseAuth.signOut()
    }

    private fun signOutGoogle() {
        googleSignInClient!!.signOut()
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.sign_out_button -> {
                signOut()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }


}
