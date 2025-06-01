package com.parkingapp.hulapark.Utilities.DataBase;

public class CurrentUserCreds
{
    public String getEmail()
    {
        return Email;
    }

    public void setEmail(String email)
    {
        Email = email;
    }

    public String getCreatedDate()
    {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate)
    {
        CreatedDate = createdDate;
    }

    public String getLastVisited()
    {
        return LastVisited;
    }

    public void setLastVisited(String lastVisited)
    {
        LastVisited = lastVisited;
    }

    private String Email;
    private String CreatedDate;
    private String LastVisited;
}
