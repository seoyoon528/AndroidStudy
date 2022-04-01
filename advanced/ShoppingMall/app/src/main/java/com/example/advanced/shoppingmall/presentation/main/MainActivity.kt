package com.example.advanced.shoppingmall.presentation.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.advanced.shoppingmall.R
import com.example.advanced.shoppingmall.databinding.ActivityMainBinding
import com.example.advanced.shoppingmall.presentation.BaseActivity
import com.example.advanced.shoppingmall.presentation.BaseFragment
import com.example.advanced.shoppingmall.presentation.list.ProductListFragment
import com.example.advanced.shoppingmall.presentation.profile.ProfileFragment
import org.koin.android.ext.android.inject

internal class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)            // ViewBinding

    override val viewModel: MainViewModel by inject<MainViewModel> ()          // koin 모듈로 주입

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        initNavigationBar()
        showFragment(ProductListFragment(), ProductListFragment.TAG)
    }

    private fun initNavigationBar() {
        binding.bottomNav.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.menu_products -> {
                        showFragment(ProductListFragment(), ProductListFragment.TAG)
                        true
                    }
                    R.id.menu_profile -> {
                        showFragment(ProfileFragment(), ProfileFragment.TAG)
                        true
                    }
                    else -> false
                }
            }
            selectedItemId = R.id.menu_products         // 초기값 세팅
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {             // 기존에 만들어져있는 Fragment가 있다면
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {               // 없다면
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()          // 화면 회전 시 발생하는 오류 커밋
        }
    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this) {
        when (it) {
            is MainState.RefreshOrderList -> {
                binding.bottomNav.selectedItemId = R.id.menu_profile

                val fragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)
                (fragment as? BaseFragment<*, *>)?.viewModel?.fetchData()           // BaseFragment Type Casting
            }
        }
    }

}