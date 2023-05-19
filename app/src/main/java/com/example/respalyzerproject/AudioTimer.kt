package com.example.respalyzerproject

import android.os.Handler
import android.os.Looper

class AudioTimer (timeListener: OnTimerTickListener){

    interface OnTimerTickListener{
        fun onTimerTick(duration: String)
    }

    // handler schedules the update to happen and runnable performs the update
    private var updateScheduler = Handler(Looper.getMainLooper())
    private lateinit var updateAchiever: Runnable

    var timeDuration = 0L
    private var delay = 100L

    init {
       updateAchiever = Runnable{
           // this runs after start and increases the duration by 100L
           timeDuration += delay
           updateScheduler.postDelayed(updateAchiever, delay)
           timeListener.onTimerTick(format())
       }
    }

    fun start(){
        updateScheduler.postDelayed(updateAchiever, delay) // starts the runnable at 0
        //basically starts the time from 0
    }

    fun stop(){
        updateScheduler.removeCallbacks(updateAchiever) // stops the running of the runnable
        timeDuration = 0L // then resets the duration making it look like the runnable is back to zero
    }

    fun format(): String{
        val milli: Long = timeDuration % 1000
        val sec: Long = (timeDuration / 1000) % 60
        val min: Long = (timeDuration / (1000*60)) * 60 // 60 seconds in a minute and every second
                                                        // has 1000 milliseconds

        var properTimeDisplay = "%02d:%02d.%02d".format(min, sec, milli/10) //divide milli by 10 to have 2 digits showing
                                                                            // instead of 3
        return properTimeDisplay

    }
}