package com.wind.countservice;

/**
 * Created by zhangcong on 2018/2/3.
 */

public interface IcounterService {
    public void startCounter(int initVal, IcounterCallBack callBack);

    public void stopCounter();
}
