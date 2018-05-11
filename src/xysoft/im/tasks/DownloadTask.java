package xysoft.im.tasks;

import xysoft.im.utils.HttpUtil;


public class DownloadTask extends HttpTask
{
    HttpUtil.ProgressListener progressListener;

    public DownloadTask(HttpUtil.ProgressListener progressListener)
    {
        this.progressListener = progressListener;
    }

    public void execute(String url)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    byte[] data = HttpUtil.download(url, null, null, progressListener);
                    if (listener != null)
                    {
                        listener.onSuccess(data);
                    }
                } catch (Exception e)
                {
                    if (listener != null)
                    {
                        listener.onFailed();
                    }
                }
            }
        }).start();
    }
}
