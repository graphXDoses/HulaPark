package com.parkingapp.hulapark.Users;

import com.parkingapp.hulapark.Utilities.Users.UserFragDisplayConfigurator;

public class Guest
{
    public static void setFragmentContainerActiveFrag(int container, int frag) {
        UserFragDisplayConfigurator.setFragmentContainerActiveFrag(Guest.class, container, frag);
    }

    public static int getFragmentContainerActiveFrag(int container) {
        return UserFragDisplayConfigurator.getFragmentContainerActiveFrag(Guest.class, container);
    }
}
