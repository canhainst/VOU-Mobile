package com.example.vou_mobile.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vou_mobile.adapter.FavoriteEventAdapter
import com.example.vou_mobile.databinding.FragmentFavoriteEventBinding
import com.example.vou_mobile.viewModel.GameViewModel
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteEvent.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteEvent : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel = EventViewModelProviderSingleton.getEventViewModel()
    private val gameViewModel = GameViewModel()
    private lateinit var binding: FragmentFavoriteEventBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var uuid: String

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
        binding = FragmentFavoriteEventBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)!!
        initFavoriteEvents(uuid)

        return binding.root
    }

    private fun initFavoriteEvents(uuid: String) {
        viewModel.loadFavoriteEvents(uuid, requireContext())

        binding.rcvEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val adapter = FavoriteEventAdapter(viewModel.favoriteEvents.value!!, gameViewModel) // Khởi tạo adapter với danh sách trống
        binding.rcvEvents.adapter = adapter

        viewModel.favoriteEvents.observe(viewLifecycleOwner, Observer { items ->
            adapter.updateEvents(items)
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Event.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteEvent().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}