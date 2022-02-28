package com.example.airbnb

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView.onCreate(savedInstanceState)

        // naver Map 객체 가져오기
        mapView.getMapAsync (this)      //  callback method :: OnMapReadyCallback
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        //  지정한 위도, 경도 위치로 Camera 위치 update
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(37.52862006299251, 126.9330290239728))
        naverMap.moveCamera(cameraUpdate)

        // 현위치 설정
        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = true        //  현위치 버튼 활성화

        // 현재 위치 권한 받기
        locationSource = FusedLocationSource(this@MainActivity, LOCATION_PERMISSION_REQUEST_CODE )
        naverMap.locationSource = locationSource

        getHouseListFromAPI()
    }

    private fun getHouseListFromAPI() {
        // Retrofit 객체
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(HouseService::class.java).also {
            it.getHouseList()
                .enqueue(object : Callback<HouseDto>{
                    override fun onResponse(call: Call<HouseDto>, response: Response<HouseDto>) {
                        if (response.isSuccessful.not()) {
                            // 실패 처리
                            return
                        }

                        response.body()?.let { dto ->
                            updateMarker(dto.items)     //  마커 꽂기 메소드
                        }
                    }

                    override fun onFailure(call: Call<HouseDto>, t: Throwable) {
                        // 실패 처리
                    }

                })
        }
    }

    // 지정한 위도, 경도 위치에 핀(마커) 꽂기
    private fun updateMarker(houses: List<HouseModel>) {
        houses.forEach { house ->
            naverMap.apply {
                val marker = Marker()
                marker.position = LatLng(house.lat, house.lng)
                marker.map = naverMap
                marker.tag = house.id
                marker.icon = MarkerIcons.BLACK
                marker.iconTintColor = Color.RED
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }

        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {      //  권한이 거부되었음을 네이버에 알림
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}