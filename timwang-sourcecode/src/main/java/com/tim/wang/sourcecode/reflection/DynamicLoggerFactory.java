package com.tim.wang.sourcecode.reflection;

import com.tim.wang.sourcecode.reflection.logger.Log4jFactory;
import com.tim.wang.sourcecode.reflection.logger.LogJdkFactory;
import com.tim.wang.sourcecode.reflection.logger.LoggerFactory;

/**
 * @author wangjun
 * @date 2020-07-04
 */
public class DynamicLoggerFactory {
    private static LoggerFactory getLoggerFactory() {
        LoggerFactory logger = null;
        if (isClassPresent("com.tim.wang.sourcecode.reflection.logger.Log4jFactory")) {
            logger = new Log4jFactory();
        } else {
            logger = new LogJdkFactory();
        }
        return logger;
    }

    public static void main(String[] args) {
        LoggerFactory loggerFactory = getLoggerFactory();
        System.out.println(loggerFactory.toString());
    }

    private static boolean isClassPresent(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
