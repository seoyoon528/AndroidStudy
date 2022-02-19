package com.example.tinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tinder.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {       // 로그인 되지 않은 경우
            startActivity(Intent(this, LoginActivity::class.java))      //  LoginActivity 실행
        } else {        // 로그인 되어 있는 경우
            startActivity(Intent(this, LikeActivity::class.java))      //  LikeActivity 실행
            finish()
        }
    }
}