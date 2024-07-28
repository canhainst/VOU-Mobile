package com.example.vou_mobile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vou_mobile.fragment.UnusedVouchers
import com.example.vou_mobile.fragment.UsedVouchers

class TabHostVouchersAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    private val fragmentList = listOf(
        UnusedVouchers(),
        UsedVouchers()
    )

    private val fragmentTitleList = listOf(
        "Vouchers",
        "Used"
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]

    fun getPageTitle(position: Int): CharSequence = fragmentTitleList[position]
}