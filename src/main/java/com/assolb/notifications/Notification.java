package com.assolb.notifications;

public class Notification {

    private String text;

    private int time;
    private float opacity;

    public Notification(String text)
    {
        this(text, 60);
    }

    public Notification(String text, int time)
    {
        this.text = text;
        this.time = time;
    }

    public boolean onUpdate()
    {
        if(time > 0)
        {
            time--;
            if(opacity < 1.0F) opacity += 0.1F;
        }
        else
        {
            if(opacity > 0.0F) opacity -= 0.1F;
            else return false;
        }
        return true;
    }

    public String getText()
    {
        return text;
    }

    public int getTime() { return time; }

    public float getOpacity()
    {
        return opacity;
    }
}
