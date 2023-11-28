package com.example.myapplication.repositories

import android.app.Activity
import android.util.Log
import com.example.myapplication.modelviews.MyViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthenticationManager(private val activity: Activity) {
    interface SignInResult {
        fun onSignInResult(success: Boolean, userId: String?)
    }
    lateinit private var viewModel: MyViewModel
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient
    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("322774038654-lj24soegu1v7np2penvo302r3qt63v22.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signIn(viewModel: MyViewModel) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
        this.viewModel = viewModel
    }

    fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(acct?.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.v("Authentication", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        viewModel.setUser(user)
                    } else {
                        TODO("Implement error handling")
                        println("error")
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Authentication", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun signOut() {
        // Sign out from Firebase
        firebaseAuth.signOut()

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