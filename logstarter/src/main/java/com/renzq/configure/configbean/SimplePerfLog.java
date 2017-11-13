package com.renzq.configure.configbean;

/**
 * SimplePerfLog is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/13 0013
 * Time: 14:32
 * Description:
 */
public class SimplePerfLog implements IPerfLog {
    @Override
    public void echo() {
        System.out.println("Simple per log......");
    }
}