package com.parkingapp.hulapark.Utilities.Users;

import java.util.HashMap;
import java.util.Map;

public class UserFragDisplayConfigurator
{
    private static final Map<Class<?>, Map<Integer, Integer>> registry = new HashMap<>();

    private static Map<Integer, Integer> getMap(Class<?> cls)
    {
        return registry.computeIfAbsent(cls, k -> new HashMap<>());
    }

    public static void setFragmentContainerActiveFrag(Class<?> cls, int container, int frag)
    {
        getMap(cls).put(container, frag);
    }

    public static int getFragmentContainerActiveFrag(Class<?> cls, int container)
    {
        return getMap(cls).getOrDefault(container, -1);
    }
}

