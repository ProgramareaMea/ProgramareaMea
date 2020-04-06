package com.tp.programareamea.firebase

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class User(
    var uid: String="",
    var name: String="",
    var email: String="",
    var userType: String="",
    @get:Exclude var isNew: Boolean = true,
    @get:Exclude var isCreated: Boolean = true,
    @get:Exclude var isAuthenticated:Boolean = false
) : Serializable