package com.tp.programareamea.firebase

import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class User(
    val uid: String,
    val name: String,
    val email: String,
    @get:Exclude var isNew: Boolean = true,
    @get:Exclude var isCreated: Boolean = false
) : Serializable