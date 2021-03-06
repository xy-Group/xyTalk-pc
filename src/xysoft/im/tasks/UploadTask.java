package xysoft.im.tasks;

import xysoft.im.utils.HttpUtil;

import java.io.IOException;


public class UploadTask
{
    UploadTaskCallback listener ;

    public UploadTask(UploadTaskCallback listener)
    {
        this.listener = listener;
    }

    public void execute(String url, String type, byte[] part)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    if (HttpUtil.upload(url, type, part))
                    {
                        if (listener != null)
                        {
                            listener.onTaskSuccess();
                        }
                    }
                    else
                    {
                        if (listener != null)
                        {
                            listener.onTaskError();
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
