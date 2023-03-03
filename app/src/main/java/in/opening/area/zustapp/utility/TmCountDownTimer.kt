package `in`.opening.area.zustapp.utility

import android.os.CountDownTimer

class TmCountDownTimer(private var totalTime: Long, private var callbackTimeCurrent: ((String) -> Unit), private var stopped: (() -> Unit)) {
    var timer: CountDownTimer? = null
    fun startCountDownTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(totalTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minute = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                if (seconds < 10) {
                    callbackTimeCurrent.invoke("0$minute:0$seconds")
                } else {
                    callbackTimeCurrent.invoke("0$minute:$seconds")
                }
            }
            override fun onFinish() {
                stopped.invoke()
            }
        }
        timer?.start()
    }

    fun stopCountDownTimer() {
        timer?.cancel()
        stopped.invoke()
    }
}

