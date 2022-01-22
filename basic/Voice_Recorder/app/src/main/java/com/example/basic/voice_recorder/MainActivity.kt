package com.example.basic.voice_recorder

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private val recordButton: RecordButton by lazy {
        findViewById(R.id.recordButton)
    }

    // TODO Manifest.permission 오류 고치기
//    private val requiredPermissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    private var state = State.BEFORE_RECORDING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestAudioPermission()
        initViews()
    }

    override fun onRequestPermissionsResult(        // 권한이 부여되어있는지 확인
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val audioRecordPermissionGranted =
            requestCode == REQUEST_RECORD_AUDIO_PERMISSION &&
            grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED

        if (!audioRecordPermissionGranted) {        // 권한이 부여되지 않았다면
            finish()        // 종료
        }
    }

    private fun requestAudioPermission() {
//        requestPermissions(requiredPermissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun initViews() {
        recordButton.updateIconWithState(state)
    }

    companion object {      // request code
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 201
    }
}