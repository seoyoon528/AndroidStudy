package com.example.intermediate.quote_of_the_today

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.setConfigSettingsAsync(         // remote Config 설정
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0       // 원래 Fetch 주기가 12시간이지만 개발용이므로 주기를 없앰
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if(it.isSuccessful) {       // Fetch & Activate 작업이 성공했다면
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))        // quotes & isNameRevealed flag 가져와서
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")

                displayQuotesPager(quotes, isNameRevealed)          // quotes pager 보여줌
            }
        }
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)     // JSON 값  Parsing
        var jsonList = emptyList<JSONObject>()

        for(index in 0 until  jsonArray.length()) {      // jsonArray가 가지고 있는 값까지 for 문을 돌며
            val jsonObject = jsonArray.getJSONObject(index)     // JSONObject를 하나씩 가져옴

            jsonObject?.let {       // jsonObject가 null이 아니라면
                jsonList = jsonList + it    // jsonList에 새로 추가된 jsonObject 추가
            }
        }
        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name")
            )
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {
        viewPager.adapter = QuotesPagerAdapter(
            quotes,
            isNameRevealed
        )
    }
}