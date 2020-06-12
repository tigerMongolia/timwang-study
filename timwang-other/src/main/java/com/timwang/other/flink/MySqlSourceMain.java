package com.timwang.other.flink;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author wangjun
 * @date 2019/3/25
 */
public class MySqlSourceMain {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.addSource(new SourceFromMySQL()).print();

        env.execute("Flink add data sourc");
    }
}
