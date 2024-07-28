package com.example.vou_mobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.TabHostVouchersAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyVoucher.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyVoucher : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_voucher, container, false)

        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)

        val adapter = TabHostVouchersAdapter(requireActivity())
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setCustomView(createTabView(position))
        }.attach()

        // Set listener to update icon visibility on tab selection
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateTab(tab, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                updateTab(tab, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Optional: Handle tab reselection if needed
            }
        })

        // Initialize the first tab as selected
        updateTab(tabLayout.getTabAt(tabLayout.selectedTabPosition)!!, true)

        return view
    }
    private fun createTabView(position: Int): View {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tabhost, null)
        val tabText = view.findViewById<TextView>(R.id.tabText)
        tabText.text = when (position) {
            0 -> "Vouchers"
            else -> "Used"
        }
        return view
    }

    private fun updateTab(tab: TabLayout.Tab, isSelected: Boolean) {
        val tabView = tab.customView
        val tabIcon = tabView?.findViewById<ImageView>(R.id.tabIcon)
        tabIcon?.visibility = if (isSelected) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyVoucher.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyVoucher().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}