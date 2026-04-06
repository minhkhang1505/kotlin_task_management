package com.nguyenminhkhang.taskmanagement.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.nguyenminhkhang.taskmanagement.domain.model.User

fun FirebaseUser?.toDomain(): User? = this?.let { firebaseUser ->
    User(
        uid = firebaseUser.uid,
        email = firebaseUser.email,
        displayName = firebaseUser.displayName,
        photoUrl = firebaseUser.photoUrl?.toString(),
        isEmailVerified = firebaseUser.isEmailVerified,
        isAnonymous = firebaseUser.isAnonymous,
    )
}

