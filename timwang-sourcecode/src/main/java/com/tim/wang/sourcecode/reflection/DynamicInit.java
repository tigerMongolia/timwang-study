package com.tim.wang.sourcecode.reflection;

/**
 * @author wangjun
 * @date 2020-07-04
 */
public class DynamicInit {
    public static void main(String[] args) {
        if (args.length > 0) {
            create(args[0]);
        }
    }

    private static void create(String arg) {
        String one = new String(arg);
    }
}
