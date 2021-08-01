package com.dr.predulive.daos

import com.dr.predulive.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {
    val db = FirebaseFirestore.getInstance()
    val usersCollection = db.collection("users")

    fun addUser(user: User?) {
        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                usersCollection.document(user.uid).set(it)
            }
        }
    }

    fun getUserById(uid: String): Task<DocumentSnapshot> {
        return usersCollection.document(uid).get()
    }

    fun setUserProperty(uid: String, propertyName: String, newPropertyValue: String) {
        usersCollection.document(uid).update(propertyName, newPropertyValue)
    }


    fun updateUser(user: User) {
        GlobalScope.launch(Dispatchers.IO) {

            usersCollection.document(user.uid).update(
                "imageUrl", user.imageUrl,
                "displayName", user.displayName,
                "details", user.details,
                "instituteName", user.instituteName,
                "resume", user.resume
            )
        }
    }


}