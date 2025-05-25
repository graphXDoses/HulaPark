package com.parkingapp.hulapark.Users;

import java.util.HashMap;

public class Guest
{
    private static final HashMap<Integer, Integer> displayMap = new HashMap<>();

    public static void setFragmentContainerActiveFrag(int fragmentContainer, int activeFrag)
    {
        displayMap.put(fragmentContainer, activeFrag);
    }

    public static int getFragmentContainerActiveFrag(int fragmentContainer)
    {
        return displayMap.get(fragmentContainer);
    }
}
