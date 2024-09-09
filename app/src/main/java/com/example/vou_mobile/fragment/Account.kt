package com.example.vou_mobile.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.vou_mobile.R
import com.example.vou_mobile.activity.ChangePassword
import com.example.vou_mobile.activity.HomePageActivity
import com.example.vou_mobile.activity.SignInActivity
import com.example.vou_mobile.activity.UpdateAccount
import com.example.vou_mobile.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Account.newInstance] factory method to
 * create an instance of this fragment.
 */
class Account : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var uuid: String? = null
    private lateinit var currentUser: User
    private val gson = Gson()

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
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)

        val userJson = sharedPreferences.getString("currentUser", null)
        if (userJson != null) {
            val userType = object : TypeToken<User>() {}.type
            currentUser = gson.fromJson(userJson, userType)
        }
        Picasso.get()
            .load(currentUser.avatar)
            .into(view.findViewById<ImageView>(R.id.userAvatar))

        view.findViewById<TextView>(R.id.fullName).text = currentUser.full_name
        view.findViewById<TextView>(R.id.username).text = currentUser.user_name

        val mainActivity = requireActivity() as HomePageActivity
        view.findViewById<TextView>(R.id.itemWarehouse).setOnClickListener {
            mainActivity.replaceFragment(ItemWarehouse())
        }

        view.findViewById<TextView>(R.id.myVoucher).setOnClickListener {
            mainActivity.replaceFragment(MyVoucher())
        }

        view.findViewById<TextView>(R.id.giftHistory).setOnClickListener {
            mainActivity.replaceFragment(GiftHistory())
        }

        view.findViewById<TextView>(R.id.account).setOnClickListener {
            val options = arrayOf("Edit Profile", "Change Password")

            val builder = AlertDialog.Builder(requireContext())

            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val updateAcc = Intent(requireContext(), UpdateAccount::class.java)
                        startActivity(updateAcc)
                    }
                    1 -> {
                        val changePwd = Intent(requireContext(), ChangePassword::class.java)
                        startActivity(changePwd)
                    }
                }
            }

            builder.show()
        }

        // Initialize the logout button
        val logoutButton: Button = view.findViewById(R.id.logout)
        logoutButton.setOnClickListener {
            // Handle logout logic
            FirebaseAuth.getInstance().signOut()
            navigateToLoginScreen()
        }

        return view
    }
    private fun navigateToLoginScreen() {
        // Navigate to the login activity
        val loginIntent = Intent(activity, SignInActivity::class.java)
        startActivity(loginIntent)
        activity?.finish()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Account.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Account().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}