package com.thehedgelog.smspush.firebase.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object Firestore {

    val bucket = FirebaseFirestore.getInstance().collection("${FirebaseAuth.getInstance().currentUser!!.uid}/messages/list").orderBy("timestamp", Query.Direction.DESCENDING)

    val bucketForUser = { user: FirebaseUser -> FirebaseFirestore.getInstance().collection("${user.uid}/messages/list")}

}