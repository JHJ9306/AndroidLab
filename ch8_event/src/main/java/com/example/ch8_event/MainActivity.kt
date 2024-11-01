package com.example.ch8_event

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.WindowManager
import android.view.WindowMetrics
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ch8_event.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // 뷰 바인딩 변수 선언 (나중에 초기화 할 것이기 때문에 lateinit 사용)
    private lateinit var binding: ActivityMainBinding

    // Chronometer Variables
    // 뒤로가기 버튼을 누른 시각을 저장하는 속성
    var initTime = 0L
    // 멈춘 시각을 저장하는 속성
    var pauseTime = 0L

    // Activity가 처음 생성될 때 호출되는 메서드를 오버라이드
    override fun onCreate(savedInstanceState: Bundle?) {
        // 부모 클래스인 Activity의 기본 onCreate 메서드를 호출하여
        // Activity의 기본 동작을 유지합니다.
        super.onCreate(savedInstanceState)

        // 여기에 초기화 작업이나 화면 설정 관련 코드를 추가할 수 있습니다.

        // 시스템의 네비게이션 바, 상태 표시줄을 투명하게 설정하는 함수
        enableEdgeToEdge()

        // 뷰 바인딩 개체 초기화 및 레이아웃 설정
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startButton.setOnClickListener {
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            binding.chronometer.start()
            // 버튼 표시 여부 설정
            binding.stopButton.isEnabled = true
            binding.resetButton.isEnabled = true
            binding.startButton.isEnabled = false
        }

        binding.stopButton.setOnClickListener {
            // 크로노미터 현재 경과 시간 : binding.chronometer.base
            // 크로노미터가 시작된 이후의 시간 : SystemClock.elapsedRealtime()
            pauseTime = binding.chronometer.base - SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            binding.stopButton.isEnabled = false
            binding.resetButton.isEnabled = true
            binding.startButton.isEnabled = true
        }

        binding.resetButton.setOnClickListener {
            pauseTime = 0L
            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.stop()
            binding.stopButton.isEnabled = false
            binding.resetButton.isEnabled = false
            binding.startButton.isEnabled = true
        }

        // 시스템 창의 인셋을 적용하여 뷰의 패딩을 조정 (상태바, 네비게이션 바 영역에 맞춰 패딩 설정)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    // 뒤로가기 버튼 이벤트 핸들러
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 뒤로가기 버튼을 눌렀을 때 처리
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 뒤로가기 버튼을 처음 눌렀거나 누른 지 3초가 지났을 때 처리
            // System.currentTimeMillis()는 UTC 기준으로 1970년 1월 1일 자정부터 현재까지의 밀리초를 반환한다.
            if (System.currentTimeMillis() - initTime > 3000) {
                Toast.makeText(this, "종료하려면 한 번 더 누르세요!!", Toast.LENGTH_SHORT).show()
                // 뒤로가기 버튼을 누른 초를 저장한다
                initTime = System.currentTimeMillis()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    fun getScreenSize(windowManager: WindowManager): Pair<Int, Int> {
        // 안드로이드 API 30 이상 (Android 11 이상)에서는 WindowMetrics를 사용
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // WindowMetrics는 현재 창의 경계를 나타냅니다.
            val metrics: WindowMetrics = windowManager.currentWindowMetrics

            // 가로 크기 (bounds의 폭)
            val width = metrics.bounds.width()

            // 세로 크기 (bounds의 높이)
            val height = metrics.bounds.height()

            // 가로와 세로 크기를 Pair로 반환
            Pair(width, height)
        } else {
            // API 30 미만 (Android 10 이하)에서는 DisplayMetrics 사용
            val displayMetrics = DisplayMetrics()

            // defaultDisplay로부터 화면 크기 정보를 가져옴
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            // 가로 크기 (픽셀 단위)
            val width = displayMetrics.widthPixels

            // 세로 크기 (픽셀 단위)
            val height = displayMetrics.heightPixels

            // 가로와 세로 크기를 Pair로 반환
            Pair(width, height)
        }
    }
}