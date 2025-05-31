package com.parkingapp.hulapark.Utilities.Users.DataSchemas.Cards;

import android.os.Handler;
import android.os.Looper;

import com.parkingapp.hulapark.Utilities.ParkingCards.ParkingTimeManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ParkingCardDataModel
{

    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Map<Integer, Runnable> timerRunnables = new HashMap<>();

    private String plate_number = "";
    private String location_id = "";
    private String charged_hours = "";
    private String price = "";

    public String getPlateNumber()
    {
        return plate_number;
    }

    public ParkingCardDataModel setPlateNumber(String plate_number)
    {
        this.plate_number = plate_number;
        return this;
    }
    public String getSectorID()
    {
        return location_id;
    }

    public ParkingCardDataModel setLocationID(String location_id)
    {
        this.location_id = location_id;
        return this;
    }

    public String getChargedHours()
    {
        Duration diff = Duration.between(startTime, finishTime);
        return String.format("%d", diff.toMinutes());
    }

    public ParkingCardDataModel setChargedHours(String charged_hours)
    {
        this.charged_hours = charged_hours;
        return this;
    }

    public String getPrice()
    {
        return price;
    }

    public ParkingCardDataModel setPrice(String price)
    {
        this.price = price;
        return this;
    }

    public ParkingCardDataModel(LocalDateTime startTime, LocalDateTime finishTime)
    {
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public ParkingCardDataModel(LocalDateTime startTime, int minutesPass)
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
                    handler.postDelayed(this, 50);
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

    public String getDate()
    {
        return this.startTime.format(DateTimeFormatter.ofPattern("d/M/Y"));
    }

    public String getStartHourString()
    {
        return this.startTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getFinishHourString()
    {
        return this.finishTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
