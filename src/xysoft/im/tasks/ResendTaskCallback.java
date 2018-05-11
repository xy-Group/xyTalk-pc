package xysoft.im.tasks;


public abstract class ResendTaskCallback
{
    long time;

    public ResendTaskCallback(long time)
    {
        this.time = time;
    }

    public long getTime()
    {
        return time;
    }

    public abstract void onNeedResend(String retJson);
}