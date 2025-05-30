package com.parkingapp.hulapark.Utilities.Frags;

import android.view.View;

import com.parkingapp.hulapark.Utilities.Users.UserType;

public interface IActiveUserFragSetter
{
    void setActiveUserFrag(UserType userType, View thisView, int fragContainer);
}
