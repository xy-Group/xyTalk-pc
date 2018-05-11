package com.rc.frames.components;



public interface HttpResponseListener<T extends Object>
{
    void onResult(T ret);


}
