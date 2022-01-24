package com.example.basic.web_browser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private val addressBar: EditText by lazy {
        findViewById(R.id.addressBar)
    }

    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    private fun initViews() {
        webView.apply{
            webViewClient = WebViewClient()     // 앱 내 webView에서 URL을 load할 수 있게 함
            webView.settings.javaScriptEnabled = true       // 뷰 초기화 시 자바 스크립트를 사용하겠다는 것을 명시
            webView.loadUrl("http://www.google.com")
        }
    }

    private fun bindViews() {
        addressBar.setOnEditorActionListener{ v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                webView.loadUrl(v.text.toString())
            }

            return@setOnEditorActionListener false     // 주소 작성 후에 키보드를 내려야 하므로 false

        }
    }
}