package com.example.firebaseexample

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.firebaseexample.databinding.ActivityLoginBinding
import com.example.firebaseexample.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = Firebase.auth
        binding.newUser.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.logBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.passEt.text.toString()
            if (email.isBlank() || password.isBlank())
                Toast.makeText(this, "Missing Field/s", Toast.LENGTH_SHORT).show()
            else {
                binding.loadingProgress.isVisible = true
                signIn(email, password)
            }
        }

    }
       private fun signIn(email: String, password: String) {
           auth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(this) { task ->
                   if (task.isSuccessful) {
                       if (auth.currentUser!!.isEmailVerified) {
                           binding.loadingProgress.isVisible = false
                           startActivity(Intent(this, HomeActivity::class.java))
                           finish()
                       } else
                           Toast.makeText(this, "Check your email!!", Toast.LENGTH_SHORT).show()

                   } else
                       Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()


               }

       }


       public override fun onStart() {
            super.onStart()
            // Check if user is signed in (non-null) and update UI accordingly.
            val currentUser = auth.currentUser
            if (currentUser != null && currentUser.isEmailVerified) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }


    
