package com.tim.wang.sourcecode.reflection;

/**
 * @author tim.wang
 * @date 2020/5/30 22:54
 */
public class Person {
    private int age;
    public String name;

    public void say(String word) {
        System.out.println("person is say: " + word);
    }

    private void drink() {
        System.out.println("person is drink");
    }

    public void hello() {
        System.out.println("I'am is person hello");
    }

    public Person() {
    }

    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    private Person(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
