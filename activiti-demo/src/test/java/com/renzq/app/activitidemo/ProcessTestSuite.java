package com.renzq.app.activitidemo;

import com.renzq.app.activitidemo.process.ReqProcessTester;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * ProcessTestSuite is Created by Renzq
 * Email: renzq@asiainfo.com
 * Date: 2017/11/27 0027
 * Time: 14:11
 * Description:
 */
@RunWith(Suite.class)
// 指定运行器
@Suite.SuiteClasses({ReqProcessTester.class})
public class ProcessTestSuite {
}