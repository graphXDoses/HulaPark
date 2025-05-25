package com.parkingapp.hulapark.Users;

import com.parkingapp.hulapark.Utilities.Users.UserFragDisplayConfigurator;

public class User
{
    public static void setFragmentContainerActiveFrag(int container, int frag) {
        UserFragDisplayConfigurator.setFragmentContainerActiveFrag(User.class, container, frag);
    }

    public static int getFragmentContainerActiveFrag(int container) {
        return UserFragDisplayConfigurator.getFragmentContainerActiveFrag(User.class, container);
    }
}
