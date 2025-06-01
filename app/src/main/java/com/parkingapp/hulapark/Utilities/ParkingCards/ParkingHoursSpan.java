package com.parkingapp.hulapark.Utilities.ParkingCards;

import androidx.annotation.NonNull;

public enum ParkingHoursSpan
{
    HALF("½ ώρα", 30),
    ONE_HOUR("1 ώρα", 60),
    ONE_HOUR_HALF("1½ ώρες", 90),
    TWO_HOURS("2 ώρες", 120),
    TWO_HOURS_HALF("2½ ώρες", 150),
    THREE_HOURS("3 ώρες", 180),
    THREE_HOURS_HALF("3½ ώρες", 210),
    FOUR_HOURS("4 ώρες", 240);


    private final String strRepr;
    private final int minRepr;

    ParkingHoursSpan(String strRepr, int minRepr)
    {
        this.strRepr = strRepr;
        this.minRepr = minRepr;
    }

    public static ParkingHoursSpan fromString(String strRepr)
    {
        for (ParkingHoursSpan hoursSpan : values()) {
            if (hoursSpan.strRepr.equals(strRepr)) {
                return hoursSpan;
            }
        }

        try {
            int minutes = Integer.parseInt(strRepr);
            for (ParkingHoursSpan hoursSpan : values()) {
                if (hoursSpan.minRepr == minutes) {
                    return hoursSpan;
                }
            }
        } catch (NumberFormatException ex) {}

        throw new IllegalArgumentException("No enum constant with label: " + strRepr);
    }

    @NonNull
    @Override
    public String toString()
    {
        return strRepr;
    }

    public int getMinutes()
    {
        return minRepr;
    }
}
