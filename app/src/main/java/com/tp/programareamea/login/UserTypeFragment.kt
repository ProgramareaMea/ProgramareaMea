package com.tp.programareamea.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tp.programareamea.R
import com.tp.programareamea.firebase.User
import com.tp.programareamea.mainActivity.MainActivity
import com.tp.programareamea.utils.Constants.Companion.USER

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.fragment_google_auth.view.admin_type_button
import kotlinx.android.synthetic.main.fragment_user_type.view.*

class UserTypeFragment : Fragment() {

    private lateinit var binding: View
    private var googleAuthViewModel: UserTypeViewModel? = null
    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var googleSignInOptions: GoogleSignInOptions


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        //    Get the passed arg User by the navigator
        val authenticatedUser: User = arguments!!.getSerializable("authenticatedUser") as User
        binding = inflater.inflate(R.layout.fragment_user_type, container, false)

        initUserTypeViewModel()
        initAdminTypeButton(authenticatedUser);
        initClientTypeButton(authenticatedUser);
        return binding.rootView

    }

    private fun initUserTypeViewModel() {
        googleAuthViewModel = ViewModelProvider(this).get(UserTypeViewModel::class.java)
    }

    private fun initAdminTypeButton(authenticatedUser: User) {
        binding.admin_type_button.setOnClickListener { _ -> setUserType(authenticatedUser, true) }
    }

    private fun initClientTypeButton(authenticatedUser: User) {
        binding.client_type_button.setOnClickListener { _ -> setUserType(authenticatedUser, false) }
    }

    private fun setUserType(authenticatedUser: User, isAdmin: Boolean ){
//        Sets the isAdmin field and then send the data to createNewUser
//        for adding is to firebase and redirecting to main

        authenticatedUser.isAdmin = isAdmin;
        createNewUser(authenticatedUser);

    }
    //Create User
    private fun createNewUser(authenticatedUser: User) {
        googleAuthViewModel!!.createUser(authenticatedUser)
        googleAuthViewModel!!.createdUserLiveData!!.observe(
            viewLifecycleOwner,
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
