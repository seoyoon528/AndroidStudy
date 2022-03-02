package com.example.airbnb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.houseViewPager)
    }

    // viewPager 클릭 시 외부에 공유하기 기능
    private val viewPagerAdapter = HouseViewPagerAdapter(itemClicked = {
        val intent = Intent()
            .apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "[지금 이 가격에 예약하세요!] ${it.title} ${it.price} 사진보기 : ${it.imgUrl}")
                type = "text/plain"
            }

        startActivity(Intent.createChooser(intent, null))
    })
    private val recyclerAdapter = HouseListAdapter()

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val recyclerView: RecyclerView by lazy {
        findViewById(R.id.recyclerView)
    }

    private val currentLocationButton: LocationButtonView by lazy {
        findViewById(R.id.currentLocationButton)
    }

    private val bottomSheetTitleTextView: TextView by lazy {
        findViewById(R.id.bottomSheetTitleTextView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mapView.onCreate(savedInstanceState)

        // naver Map 객체 가져오기
        mapView.getMapAsync (this)      //  callback method :: OnMapReadyCallback

        viewPager.adapter = viewPagerAdapter
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // ViewPager 전환 시 카메라 위치 이동동
       viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val selectedHouseModel = viewPagerAdapter.currentList[position]
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(selectedHouseModel.lat, selectedHouseModel.lng))
                    .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)
            }
        })
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
        uiSetting.isLocationButtonEnabled = false
        currentLocationButton.map = naverMap       //  현위치 버튼 위치 옮겨 활성화

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

        // API로 데이터 불러오기
        retrofit.create(HouseService::class.java).also {
            it.getHouseList()
                .enqueue(object : Callback<HouseDto> {
                    override fun onResponse(call: Call<HouseDto>, response: Response<HouseDto>) {
                        if (response.isSuccessful.not()) {
                            // 실패 처리
                            return
                        }
                        response.body()?.let { dto ->
                            updateMarker(dto.items)     //  마커 꽂기 메소드
                            viewPagerAdapter.submitList(dto.items)      // 뷰페이저에 데이터 리스트 집어 넣기
                            recyclerAdapter.submitList(dto.items)       //  리사이클러뷰에 데이터 리스트 집어 넣기

                            bottomSheetTitleTextView.text = "${dto.items.size}개의 숙소"
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
                marker.onClickListener  = this@MainActivity      //  this :: Overlay.OnClickListener
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

    // 핀(마커) 클릭 시 뷰페이저에서 해당 숙소 정보로 이동
    override fun onClick(overlay: Overlay): Boolean {
        overlay.tag

        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overlay.tag
        }

        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }

        return true
    }
}