package com.example.used_trading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.used_trading.chatlist.ChatListFragment
import com.example.used_trading.home.HomeFragment
import com.example.used_trading.mypage.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        replaceFragment(homeFragment)       // 초기 화면은 homeFragment로 set

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(homeFragment)

                R.id.chatList -> replaceFragment(chatListFragment)

                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {       // Fragment 전환 메소드
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}