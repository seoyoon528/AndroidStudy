package com.example.intermediate.quote_of_the_today

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews() {
        viewPager.setPageTransformer { page, position ->        // PageTransformer :: ViewPager의 Custom Swipe 효과
            when {
                position.absoluteValue >= 1.0F -> {         // position의 절댓값이 1 이상인 경우(= 현재 페이지의 앞, 뒤 페이지가 아닌 경우)
                    page.alpha = 0F     //  완전히 투명하게 함
                }
                position == 0F -> {
                    page.alpha = 1F     // 현재 페이지는 전부 보이게 함(=불투명)
                }
                else -> {
                    page.alpha = 1F - 2 * position.absoluteValue        // 현재 페이지에서 멀어질수록 투명하게 함 (기울기 값을 가파르게 하기 위해 x2)
                }
            }
        }
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig

        remoteConfig.setConfigSettingsAsync(         // remote Config 설정
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0       // 원래 Fetch 주기가 12시간이지만 개발용이므로 주기를 없앰
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            progressBar.visibility = View.GONE

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
        val adapter = QuotesPagerAdapter(
            quotes,
            isNameRevealed
        )

        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount / 2, false)      // 양쪽으로 무한 Swipe 할 수 있도록 전체 item size의 중간 position에 quote를 위치하게 함
    }
}