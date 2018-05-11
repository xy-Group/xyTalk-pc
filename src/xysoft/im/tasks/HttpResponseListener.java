package xysoft.im.tasks;



public interface HttpResponseListener<T extends Object>
{
    void onSuccess(T ret);

    void onFailed();
}
