package com.example.vou_mobile.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.VerticalItemWarehouseAdapter
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.GiftExchangesHistory
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.model.Items
import com.example.vou_mobile.model.ItemsOfEvent
import com.example.vou_mobile.model.VouchersList
import com.example.vou_mobile.model.Warehouse
import com.google.firebase.auth.FirebaseAuth

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ItemWarehouse.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemWarehouse : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var currentUserID: String

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
        val view = inflater.inflate(R.layout.fragment_item_warehouse, container, false)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null){
            currentUserID = currentUser.uid
        }

        val itemsTest1 = listOf(
            Items("0", 0),
            Items("1", 2),
            Items("2", 4),
            Items("3", 2),
            Items("4", 1)
        )
        val itemsTest2 = listOf(
            Items("5", 0),
            Items("6", 2),
            Items("7", 4),
            Items("8", 2),
            Items("9", 1)
        )
        val itemsTest3 = listOf(
            Items("10", 0),
            Items("11", 2),
            Items("12", 4),
            Items("13", 2),
        )
        val itemsOfEventTest = listOf(
            ItemsOfEvent("0", itemsTest1),
            ItemsOfEvent("1", itemsTest2),
            ItemsOfEvent("2", itemsTest3)
        )

        val allItemWarehouseRecyclerView = view.findViewById<RecyclerView>(R.id.itemWarehouse)
        allItemWarehouseRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        allItemWarehouseRecyclerView.adapter = VerticalItemWarehouseAdapter(itemsOfEventTest)

        return view
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItemWarehouse.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItemWarehouse().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}