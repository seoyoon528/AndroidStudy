package com.example.basic.web_browser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {

    private val goHomeButton: ImageButton by lazy {
        findViewById(R.id.goHomeButton)
    }

    private val goBackButton: ImageButton by lazy {
        findViewById(R.id.goBackButton)
    }

    private val goForwardButton: ImageButton by lazy {
        findViewById(R.id.goForwardButton)
    }

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

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
    private fun initViews() {
        webView.apply{
            webViewClient = WebViewClient()     // 앱 내 webView에서 URL을 load할 수 있게 함
            webView.settings.javaScriptEnabled = true       // 뷰 초기화 시 자바 스크립트를 사용하겠다는 것을 명시
            webView.loadUrl(DEFAULT_URL)
        }
    }

    private fun bindViews() {
        addressBar.setOnEditorActionListener{ v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                webView.loadUrl(v.text.toString())
            }
            return@setOnEditorActionListener false     // 주소 작성 후에 키보드를 내려야 하므로 false
        }

        goHomeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

        goBackButton.setOnClickListener {
            webView.goBack()
        }

        goForwardButton.setOnClickListener {
            webView.goForward()
        }
    }

    companion object {
        private const val DEFAULT_URL = "http://www.google.com"
    }
}