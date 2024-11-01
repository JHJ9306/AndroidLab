package com.example.ch_resource

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch_resource.databinding.ActivityMainBinding
import com.example.ch_resource.databinding.DialogInputBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var backPressedTime: Long = 0
    // 이전 상태를 유지하기 위한 함수
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 모서리 까지 화면을 확장하여 더 몰입감 있는 사용자 경험을 제공하는 함수
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 뷰의 패딩을 상태 바(상단바)와 네비게이션 바(하단바)의 크기에 맞게 조정하여 콘텐츠가 잘리지 않게 한다
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 뒤로가기 버튼을 두번 눌렀을때 종료되는 로직
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            @RequiresApi(Build.VERSION_CODES.R)
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime < 2000) {
                    finish()
                } else{
                    backPressedTime = System.currentTimeMillis()
                    showToast()
                }
            }
        })

        // 날짜 선택 다이얼로그
        DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener {
            override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

                val selectedDate = "$p1 - $p2 - $p3"
                Log.d("kkang", "year : $p1, month : ${p2+1}, dayOfMonth : $p3")

                binding.datePicker?.text = selectedDate
            }
        }, 2024,10,25).show()

        // 시간 선택 다이얼로그
        TimePickerDialog(this, object: TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {

                val selectedTime = "$p1 시 $p2 분"
                Log.d("kkang", "time : $p1, minute : $p2")

                binding.datePicker?.text = selectedTime
            }
        }, 15, 0, true).show()


        val eventHandler = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if (p1 == DialogInterface.BUTTON_POSITIVE) {
                    Log.d("kkang" , "positive button click")
                } else if (p1 == DialogInterface.BUTTON_NEGATIVE) {
                    Log.d("kkang", "negative button click")
                }
            }
        }
        vibratePhone()

    /*    // 알림창
        AlertDialog.Builder(this).run {
            setTitle("test dialog")
            setIcon(android.R.drawable.ic_dialog_info)
            setMessage("정말 종료하시겠습니까?")
            setPositiveButton("OK", eventHandler)
            setNegativeButton("Cancel", eventHandler)
            setNeutralButton("More", eventHandler)
            setPositiveButton("Yes", eventHandler)
            setNegativeButton("No", eventHandler)
        }.show() */

        /* // 목록을 출력하는 알림 창
        val items = arrayOf<String>("사과","복숭아","수박","딸기")
        AlertDialog.Builder(this).run {
            setTitle("items test")
            setIcon(android.R.drawable.ic_dialog_info)
            setItems(items, object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Log.d("jjang", "선택한 과일 : ${items[p1]}")
                }
            })
            setPositiveButton("닫기", null)
            show()
        } */
        /* // 체크박스를 포함하는 예
        val items = arrayOf<String>("사과","복숭아","수박","딸기")
        AlertDialog.Builder(this).run {
            setTitle("items test")
            setIcon(android.R.drawable.ic_dialog_info)
            setMultiChoiceItems(items, booleanArrayOf(true, false, true, false), object: DialogInterface.OnMultiChoiceClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int, p2: Boolean){
                    Log.d("jjang", "${items[p1]} 이 ${if(p2) "선택되었습니다." else "선택 해제되었습니다."}")
                }
            })
        setPositiveButton("닫기", null)
        show()
        } */

        /* 라디오 버튼을 포함하는 예
        val items = arrayOf<String>("사과", "복숭아", "수박", "딸기")
        AlertDialog.Builder(this).run {
            setTitle("radioButton-Test")
            setIcon(android.R.drawable.ic_dialog_info)
            setSingleChoiceItems(items, 0, object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Log.d("kkang", "${items[p1]} 이 선택되었습니다.")
                }
            })
        }.show()
        */

        /*
        // 알림 창을 닫는 설정(뒤로가기, 바깥 영역 터치) page 289
        val items = arrayOf<String>("사과", "복숭아", "수박", "딸기")
        AlertDialog.Builder(this).run {
            setTitle("items test")
            setIcon(android.R.drawable.ic_dialog_info)
            setItems(items, object: DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Log.d("kkang", "선택한 과일 : ${items[p1]}")
                }
            })
            setCancelable(true)
            setPositiveButton("닫기", null)
            show()
        }.setCanceledOnTouchOutside(true)
         */

        // 커스텀 다이얼로그 출력
        val dialogBinding = DialogInputBinding.inflate(layoutInflater)
        AlertDialog.Builder(this).run {
            setTitle("Input")
            setView(dialogBinding.root)
            setPositiveButton("닫기", null)
            show()
        }

        // ☆ 알림 빌더 작성 ---
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val builder: NotificationCompat.Builder

        // Android Oreo 이상에서는 NotificationChannel을 생성해야 합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "one-channel"
            val channelName = "My channel One"
            // NotificationChannel 객체를 생성합니다.
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            // 채널에 다양한 정보 설정
            channel.description = "My Channel One Description"
            channel.setShowBadge(true)
            val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(uri, audioAttributes)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)

            // 채널을 이용해 빌더 생성
            builder = NotificationCompat.Builder(this, channelId)
        } else {
            builder = NotificationCompat.Builder(this)
        }
        // ★ 알림 빌더 끝 ---

        /* 알림 객체에 액티비티 실행 정보 등록
        val intent = Intent(this, DetailActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 10, intent,
                PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent) */

    }

    /* Deprecated onBackPressed
    private var backPressedTime: Long = 0

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            super.onBackPressed()
        } else {
            backPressedTime = System.currentTimeMillis()
            showToast()
        }
    }
    */

    private fun vibratePhone() {
    // 진동 객체 얻기
        val vibrator = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = this.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
    } else {
        getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    500,
                    VibrationEffect.DEFAULT_AMPLITUDE
                 )
            )
            Log.d("jjang", "Vibrate 500milliseconds_DEFAULT_AMPLITUDE")
        } else {
            vibrator.vibrate(500)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(500, 1000, 500, 2000),
                intArrayOf(0, 50, 0, 200), -1))
            Log.d("jjang", "VibrationEffect의 createWaveform 메소드!!")
        } else {
            vibrator.vibrate(longArrayOf(500, 1000, 500, 2000), -1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun showToast() {
        val toast = Toast.makeText(this, "종료하려면 한 번 더 누르세요.", Toast.LENGTH_SHORT)
        toast.addCallback(
            object : Toast.Callback() {
                override fun onToastHidden() {
                    super.onToastHidden()
                    Log.d("kkang", "toast hidden")
                }

                override fun onToastShown() {
                    super.onToastShown()
                    Log.d("kkang", "toast shown")
                }
            })
        toast.show()
    }

}