package com.timwang.interview.hypers;

/**
 * @author wangjun
 * @date 2020-03-02
 */
public class A {
    protected int a = 1;
    public A() {
        a = 2;
    }

    static class B extends A {
        protected  int b = 2;
        public B() {
            b = 3;
        }
    }

    static class C extends B {
        private int c = 2;
        public C() {
            super();
            c = 10;
            print();
        }

        public void print() {
            System.out.println("" + a + b + c);
        }
    }

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = new Integer(1);
        Integer c = new Integer(1);
        System.out.println(a == b);
        System.out.println(a.equals(b));
        System.out.println(b == c);
        System.out.println(b.equals(c));
    }
}
