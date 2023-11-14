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
    }

    fun signOut() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut()

        // Sign out from Google Sign-In
        googleSignInClient.signOut().addOnCompleteListener(activity) {
            Log.v("Authentication", "Signed out of Google")
            // Handle UI update or redirection after sign-out
        }
    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null && account.idToken != null) {
                firebaseAuthWithGoogle(account.idToken!!)

            } else {
                Log.v("Authentication", "Google sign-in failed: Account or ID token is null")
            }
        } catch (e: ApiException) {
            Log.v("Authentication", "Google sign-in failed with exception: ${e.statusCode}")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Log.v("Authentication", "Firebase Auth with Google succeeded")
                // Notify MainActivity or update UI accordingly
            } else {
                Log.v("Authentication", "Firebase Auth with Google failed: ${task.exception}")
            }
        }
    }

    companion object {
        const val RC_SIGN_IN = 9001
    }
}
