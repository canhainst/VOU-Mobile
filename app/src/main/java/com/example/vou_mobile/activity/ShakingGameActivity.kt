package com.example.vou_mobile.activity

import com.example.vou_mobile.utilities.TextToSpeechUtils
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityShakingGameBinding
import com.example.vou_mobile.databinding.DetailDialogBinding
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.games.ShakeDetector
import com.example.vou_mobile.viewModel.GameViewModel
import com.squareup.picasso.Picasso

class ShakingGameActivity : AppCompatActivity(), ShakeDetector.OnShakeListener {
    private lateinit var binding: ActivityShakingGameBinding
    private val gameViewModel = GameViewModel()
    private var rotate = false
    private val typeOfEvent = "Lắc xì"

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var shakeDetector: ShakeDetector

    private lateinit var ttsUtil: TextToSpeechUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShakingGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationGift.playAnimation()

        initShowout(binding.getVoucherLL)
        initShowout(binding.itemsLL)
        binding.menuFab.setOnClickListener {
            toggleFabMode(it)
        }

        binding.btnClose.setOnClickListener {
            gameViewModel.setGame(typeOfEvent, this)
            gameViewModel.stopGame()
        }

        binding.btnAddTurn.setOnClickListener {
            val intent = Intent(binding.root.context, RequestTurnAtivity::class.java)
            startActivity(intent)
        }

        binding.getVoucherFab.setOnClickListener {
            val intent = Intent(binding.root.context, GetVoucherActivity::class.java)
            startActivity(intent)
        }

        binding.itemsFab.setOnClickListener {
            val event = Event("", "0", "Shaking Game", "https://phuclong.com.vn/upload/files/a4%20htk-02.jpg", "18:00 26/07/2024", "18:00 30/08/2024", "", "Lắc xì", null)
            showItemsDialog(event)
        }

        //Cảm biến gia tốc
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        shakeDetector = ShakeDetector(this)

        ttsUtil = TextToSpeechUtils(this) {
            // Callback được gọi khi TTS đã sẵn sàng
            ttsUtil.speak(binding.tvGuidance.text.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showItemsDialog(event: Event) {
        val binding = DetailDialogBinding.inflate(LayoutInflater.from(binding.root.context))
        val dialogBuilder = AlertDialog.Builder(binding.root.context)
            .setView(binding.root)
            .create()

        binding.brandName.text = event.id_brand
        binding.script.text = event.name
        binding.script2.text = event.name
        Picasso.get()
            .load(event.image)
            .into(binding.picture)
        binding.Time.text = Helper.getTimeRangeString(event)

        binding.detail.text = getItemsListByEventID()
        binding.detail.textSize = 15f // thay đổi kích thước chữ
        binding.detail.setTextColor(ContextCompat.getColor(this, R.color.black)) // thay đổi màu chữ
        binding.detail.setLineSpacing(10f, 1.2f) // điều chỉnh khoảng cách giữa các dòng


        binding.btnDirection.text = "Send Item"
        binding.btnDirection.setOnClickListener {
            dialogBuilder.dismiss()
            val intent = Intent(this@ShakingGameActivity, HomePageActivity::class.java).apply {
                putExtra("sendItem", true)
                putExtra("fromShakingGame", true)
            }
            startActivity(intent)
        }
        binding.btnBack.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.show()
    }

    private fun getItemsListByEventID(): SpannableString {
       //lay danh sach item tu firebase
        val listTitle = "ITEMS LIST:\n" // Ghi hoa ITEMS LIST
        val listContent = "Mảnh Tranh: 0 pcs\nMảnh Gốm: 1 pcs\nMảnh Giáp: 2 pcs\n"
        val fullText = listTitle + listContent

        val spannableString = SpannableString(fullText)
        // In đậm "ITEMS LIST:"
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, listTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }


    private fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate()
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter(){})
            .rotation(if (rotate) 180f else 0f)
        return rotate
    }
    private fun toggleFabMode(v: View) {
        rotate = rotateFab(v, !rotate)
        if (rotate){
            showIn(binding.getVoucherLL)
            showIn(binding.itemsLL)
        } else{
            showOut(binding.getVoucherLL)
            showOut(binding.itemsLL)
            binding.backgroundOverlay.visibility = View.GONE
        }
    }

    private fun initShowout(v: View){
        v.apply {
            visibility = View.GONE
            translationY = height.toFloat()
            alpha = 0f
        }
    }
    private fun showOut(view: View) {
        view.apply {
            visibility = View.VISIBLE
            alpha = 1f
            translationY = 0f
            animate()
                .setDuration(200)
                .translationY(height.toFloat())
                .setListener(object : AnimatorListenerAdapter(){
                })
                .alpha(0f)
                .start()
        }
    }

    private fun showIn(view: View) {
        view.apply {
            visibility = View.VISIBLE
            alpha = 0f
            translationY = height.toFloat()
            animate()
                .setDuration(200)
                .translationY(0f)
                .setListener(object : AnimatorListenerAdapter(){})
                .alpha(1f)
                .start()
        }
    }

    private fun showRewardDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_open_gift, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        sensorManager.unregisterListener(shakeDetector)
        dialogView.findViewById<LottieAnimationView>(R.id.animation_gift).playAnimation()
        val btnClaim = dialogView.findViewById<Button>(R.id.btn_Claim)
        btnClaim.setOnClickListener {
            // Handle the reward claiming logic here
            sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(shakeDetector)
    }

    override fun onShake() {
        // Xử lý khi phát hiện người dùng lắc điện thoại
        showRewardDialog()
    }

    override fun onDestroy() {
        // Hủy Text-to-Speech khi Activity bị hủy
        ttsUtil.shutdown()
        super.onDestroy()
    }

}