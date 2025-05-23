package com.parkingapp.hulapark.DataModels;

import android.os.Handler;
import android.os.Looper;

import com.parkingapp.hulapark.Utilities.ParkingTimeManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ParkingCardModel
{
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Map<Integer, Runnable> timerRunnables = new HashMap<>();

    public ParkingCardModel(LocalDateTime startTime, LocalDateTime finishTime)
    {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public ParkingCardModel(LocalDateTime startTime, int minutesPass)
    {
        this.startTime = startTime;
        this.finishTime = startTime.plusMinutes(minutesPass);
    }

    public LocalDateTime getStartTime()
    {
        return startTime;
    }

    public LocalDateTime getFinishTime()
    {
        return finishTime;
    }

    public Duration getStaticDuration()
    {
        return Duration.between(startTime, finishTime);
    }
    public Duration getDuration()
    {
        return Duration.between(LocalDateTime.now(), finishTime);
    }

    public void setOnTickListener(int position, ParkingTimeManager parkingManager)
    {
        Runnable oldRunnable = timerRunnables.get(position);
        if (oldRunnable != null)
            handler.removeCallbacks(oldRunnable);

        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                int remaining = (int)getDuration().toMillis();

                if (remaining > 0)
                {
                    parkingManager.onTickUpdate(remaining);
                    handler.postDelayed(this, 500);
                } else {
                    if(parkingManager.onFinish())
                        timerRunnables.remove(position);
                }
            }
        };

        timerRunnables.put(position, timerRunnable);
        handler.post(timerRunnable);
    }

    public String getDurationAsString()
    {
        Duration duration = Duration.between(LocalDateTime.now(), finishTime);
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
