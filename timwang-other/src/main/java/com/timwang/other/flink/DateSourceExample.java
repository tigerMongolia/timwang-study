package com.timwang.other.flink;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author wangjun
 * @date 2019/3/25
 */
public class DateSourceExample {

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Person> input = env.fromElements(
                new Person(1, "name", 12),
                new Person(2, "name2", 13),
                new Person(3, "name3", 14)
        );
    }

}
