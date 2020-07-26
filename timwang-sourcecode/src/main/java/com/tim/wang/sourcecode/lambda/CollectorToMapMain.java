package com.tim.wang.sourcecode.lambda;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomUtils;

import	java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wangjun
 * @date 2020-07-26
 */
public class CollectorToMapMain {

    public static void main(String[] args) {
        CollectorToMapMain main = new CollectorToMapMain();
        main.lambdaToMap();
        main.partitionToMap();
        main.groupByMap();
    }

    private void groupByMap() {
        List<Student> students = Lists.newArrayList();
        students.add(new Student("zs", 12));
        students.add(new Student("ls", 13));
        Map<String, List<Student>> collect = students.stream().collect(Collectors.groupingBy(Student::getName));
        System.out.println(JSONUtil.toJsonStr(collect));

        Map<String, Long> map = students.stream().collect(Collectors.groupingBy(Student::getName, Collectors.counting()));
        System.out.println(JSONUtil.toJsonStr(map));

        Map<String, List<Integer>> collect1 = students.stream()
                .collect(Collectors.groupingBy(Student::getName, Collectors.mapping(Student::getAge, Collectors.toList())));
        System.out.println(collect1);
    }

    private void partitionToMap() {
        List<Student> students = Lists.newArrayList();
        students.add(new Student("zs", 12));
        students.add(new Student("ls", 13));
        Map<Boolean, List<Student>> collect = students.stream().collect(Collectors.partitioningBy(s -> s.getAge() > 12));
        System.out.println(JSONUtil.toJsonStr(collect));
    }

    private void lambdaToMap() {
        List<Student> students = Lists.newArrayList();
        students.add(new Student("zs", 12));
        students.add(new Student("ls", 13));
        Map<Student, Double> studentGpaMap = students.stream()
                .collect(Collectors.toMap(Function.identity(), this::computeGPA));
        System.out.println(JSONUtil.toJsonStr(studentGpaMap));
    }

    private Double computeGPA(Student student) {
        return student.getAge() * RandomUtils.nextInt() * 1.0D;
    }

    class Student {
        private String name;
        private Integer age;

        public Student(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {return true;}
            if (o == null || getClass() != o.getClass()) {return false;}
            Student student = (Student) o;
            return Objects.equals(getName(), student.getName()) &&
                    Objects.equals(getAge(), student.getAge());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getAge());
        }
    }
}
