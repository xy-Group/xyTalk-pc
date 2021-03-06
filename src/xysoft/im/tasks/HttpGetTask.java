package xysoft.im.tasks;

import xysoft.im.utils.HttpUtil;
import org.json.JSONObject;

import java.io.IOException;


public class HttpGetTask extends HttpTask
{
    @Override
    public void execute(String url)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    String ret = HttpUtil.get(url, headers, requestParams);
                    JSONObject retJson = new JSONObject(ret);
                    if (listener != null)
                    {
                        listener.onSuccess(retJson);
                    }
                }
                catch (IOException e)
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
