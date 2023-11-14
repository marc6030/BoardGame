package com.example.myapplication.repositories

import android.app.Activity
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthenticationManager(private val activity: Activity) {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient


    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        Log.v("Authentication", "Signed in")
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    Log.v("Authentication", "signInWithCredential:success")
                } else {
                    // If sign in fails
                    Log.w("Authentication", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun signOut() {
        // Sign out from Firebase
        auth.signOut()

        // Sign out from Google Sign-In
        googleSignInClient.signOut().addOnCompleteListener(activity) {
            Log.v("Authentication", "Signed out of Google")
            // Handle UI update or redirection after sign-out
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}
