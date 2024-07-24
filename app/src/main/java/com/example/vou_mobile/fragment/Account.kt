package com.example.vou_mobile.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.vou_mobile.R
import com.example.vou_mobile.activity.ChangePassword
import com.example.vou_mobile.activity.ResetPasswordActivity
import com.example.vou_mobile.activity.SignInActivity
import com.google.firebase.auth.FirebaseAuth
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
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        val username = "Nguyen Thanh"
        val userPictureUrl = "https://scontent.fsgn2-8.fna.fbcdn.net/v/t39.30808-6/446651424_1681220119288938_4828402852445544478_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=MHCPT2zDHoEQ7kNvgG2yjD-&_nc_ht=scontent.fsgn2-8.fna&oh=00_AYCz7QsbOTWFy-oLUAowm7ba85crAps7UHZfvK4xn-ewPA&oe=66A4CB11"

        Picasso.get()
            .load(userPictureUrl)
            .into(view.findViewById<ImageView>(R.id.userAvatar))
        view.findViewById<TextView>(R.id.username).text = username

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null){
            currentUserID = currentUser.uid
            view.findViewById<TextView>(R.id.userID).text = currentUserID
        }

        view.findViewById<TextView>(R.id.itemWarehouse).setOnClickListener {
            replaceFragment(ItemWarehouse())
        }

        view.findViewById<TextView>(R.id.myVoucher).setOnClickListener {
            replaceFragment(MyVoucher())
        }

        view.findViewById<TextView>(R.id.giftHistory).setOnClickListener {
            replaceFragment(GiftHistory())
        }

        view.findViewById<TextView>(R.id.account).setOnClickListener {
            val options = arrayOf("Change Password", "Reset Password")

            val builder = AlertDialog.Builder(requireContext())

            builder.setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        val changePwd = Intent(requireContext(), ChangePassword::class.java)
                        startActivity(changePwd)
                    }
                    1 -> {
                        val rsPwd = Intent(requireContext(), ResetPasswordActivity::class.java)
                        startActivity(rsPwd)
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

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
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