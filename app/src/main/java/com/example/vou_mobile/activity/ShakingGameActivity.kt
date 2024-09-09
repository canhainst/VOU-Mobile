package com.example.vou_mobile.activity

import com.example.vou_mobile.utilities.TextToSpeechUtils
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.airbnb.lottie.LottieAnimationView
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityShakingGameBinding
import com.example.vou_mobile.databinding.DetailDialogBinding
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.games.ShakeDetector
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.EventService
import com.example.vou_mobile.services.api.ItemListResponse
import com.example.vou_mobile.services.api.PlayLacXiResponse
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModelProviderSingleton
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShakingGameActivity : AppCompatActivity(), ShakeDetector.OnShakeListener {
    private lateinit var binding: ActivityShakingGameBinding
    private val gameViewModel = GameViewModelProviderSingleton.getGameViewModel()
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()

    private var rotate = false
    private lateinit var userId: String
    private lateinit var eventId: String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var shakeDetector: ShakeDetector

    private lateinit var ttsUtil: TextToSpeechUtils
    private val scripts = listOf(
        "Chào mừng bạn đến với trò chơi Lắc xì! Lắc điện thoại để nhận các vật phẩm ngẫu nhiên.",
        "Ghép các vật phẩm lại để đổi thưởng hoặc tặng, yêu cầu vật phẩm từ bạn bè.",
        "Để nhận thêm lượt chơi bạn cần chia sẻ bài viết hoặc nhờ sự trợ giúp từ bạn bè!",
        "Hãy lắc điện thoại nào!"
    )
    private var currentScriptIndex = 0


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

        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("uuid", null)!!
        eventId = eventViewModel.curEvent.value!!.id

        //playThrough
        gameViewModel.loadPlaythrough(eventId, userId)
        binding.playthrough.text = gameViewModel.playthrough.value.toString()
        gameViewModel.playthrough.observe(this, Observer {
            binding.playthrough.text = it.toString()
        })

        binding.btnClose.setOnClickListener {
            gameViewModel.currentGame.value?.endGame(this)
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
            val api = RetrofitClient.instance.create(EventService::class.java)
            api.getEventByID(eventId).enqueue(object : Callback<Event> {
                override fun onResponse(call: Call<Event>, response: Response<Event>) {
                    if (response.isSuccessful) {
                        val event = response.body()
                        // Xử lý sự kiện, ví dụ, cập nhật UI
                        event?.let {
                            // Hiển thị thông tin sự kiện
                            showItemsDialog(event)
                        }
                    } else {
                        Log.e("API Error", "Response code: ${response.code()}, message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Event>, t: Throwable) {
                    Log.e("API Failure", "Error occurred: ${t.message}", t)
                }
            })
        }

        //Cảm biến gia tốc
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!
        shakeDetector = ShakeDetector(this)

        ttsUtil = TextToSpeechUtils(this) {
            // Callback được gọi khi TTS đã sẵn sàng
            playNextScript()
        }

        binding.root.setOnClickListener {
            playNextScript()
        }
    }

    private fun playNextScript() {
        if (currentScriptIndex < scripts.size) {
            // Cập nhật văn bản hiển thị lên màn hình
            binding.tvGuidance.text = scripts[currentScriptIndex]
            currentScriptIndex++
            // Đọc kịch bản hiện tại và định nghĩa hành động khi hoàn tất đọc
            ttsUtil.speak(scripts[currentScriptIndex - 1]) {}
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showItemsDialog(event: Event) {
        val binding = DetailDialogBinding.inflate(LayoutInflater.from(binding.root.context))
        val dialogBuilder = AlertDialog.Builder(binding.root.context)
            .setView(binding.root)
            .create()

        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(event.id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        binding.brandName.text = brand.brand_name
                        Picasso.get()
                            .load(brand.avatar)
                            .into(binding.brandAvt)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        binding.script.text = event.name
        binding.script2.text = event.name
        Picasso.get()
            .load(event.image)
            .into(binding.picture)
        binding.Time.text = Helper.getTimeRangeString(event)

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
        getUserItems {
            binding.detail.text = it
        }
        dialogBuilder.show()
    }

    private fun getUserItems(callback: (SpannableString) -> Unit){
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.getUserItemsForEvent(userId, eventId).enqueue(object :
            Callback<ItemListResponse> {
            override fun onResponse(call: Call<ItemListResponse>, response: Response<ItemListResponse>) {
                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList() // Lấy danh sách items từ phản hồi
                    val listContent = items.joinToString("\n") {
                        "${it.itemName}: ${it.quantity} pcs"
                    }

                    // Cập nhật giao diện người dùng với danh sách items
                    val spannableString = SpannableString("ITEMS LIST:\n$listContent").apply {
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            0,
                            "ITEMS LIST:\n".length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                    callback(spannableString)

                } else {
                    Log.e("API Error", "Response code: ${response.code()}, message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ItemListResponse>, t: Throwable) {
                Log.e("API Failure", "Error occurred: ${t.message}", t)
            }
        })
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
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.playLacXiEvent(eventId, userId).enqueue(object :
            Callback<PlayLacXiResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<PlayLacXiResponse>, response: Response<PlayLacXiResponse>) {
                if (response.isSuccessful) {
                    val playLacXiResponse = response.body()
                    Log.d("API Success", playLacXiResponse.toString())
                    if (playLacXiResponse != null) {
                        if (playLacXiResponse.code == 200) {
                            Log.d("API Success", "Item received: ${playLacXiResponse.item}")

                            // Xử lý logic khi thành công
                            Picasso.get()
                                .load(playLacXiResponse.item!!.image)
                                .into(dialogView.findViewById<LottieAnimationView>(R.id.animation_gift))
                            dialogView.findViewById<TextView>(R.id.textWithGift).text = "You have received a ${playLacXiResponse.item.name}"
                            gameViewModel.loadPlaythrough(eventId, userId)
                            val btnClaim = dialogView.findViewById<Button>(R.id.btn_Claim)
                            btnClaim.setOnClickListener {
                                // Handle the reward claiming logic here
                                sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
                                dialog.dismiss()
                            }
                            dialog.show()
                        } else {
                            Log.e("API Error", playLacXiResponse.message ?: "Error occurred")
                        }
                    }
                } else {
                    Log.e("API Error", "Response code: ${response.code()}, message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PlayLacXiResponse>, t: Throwable) {
                Log.e("API Failure", "Error occurred: ${t.message}", t)
            }
        })
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