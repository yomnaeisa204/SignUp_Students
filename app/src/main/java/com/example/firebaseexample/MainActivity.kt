package com.example.firebaseexample

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.firebaseexample.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
 binding= ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Initialize Firebase Auth
        // Initialize Firebase Auth
        auth = Firebase.auth
        binding.already.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.signupBtn.setOnClickListener {
            val email =binding.email.text.toString()
            val password = binding.passEt.text.toString()
            val conpass=binding.confirmpassEt.text.toString()
            if(email.isBlank()||password.isBlank()||conpass.isBlank())
                Toast.makeText(this, "Missing Field/s", Toast.LENGTH_SHORT).show()
            else if (password.length<6)
                Toast.makeText(this, "Short Password", Toast.LENGTH_SHORT).show()
            else if (password !=conpass)
                Toast.makeText(this, "Passwords Don’t Match", Toast.LENGTH_SHORT).show()
            else{
                binding.loadingProgress.isVisible=true
                //addUser()
                signUp(email,password)
            }
        }
    }

    private fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                  verifyEmail()
                 else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    binding.loadingProgress.isVisible=false

                }
            }
    }

    private fun verifyEmail() {
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Check your email!", Toast.LENGTH_SHORT).show()
                    binding.loadingProgress.isVisible=false
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
    }


}