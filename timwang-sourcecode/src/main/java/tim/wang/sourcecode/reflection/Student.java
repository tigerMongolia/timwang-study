package tim.wang.sourcecode.reflection;

/**
 * @author tim.wang
 * @date 2020/5/30 22:53
 */
public class Student extends Person {
    private String birth;
    public int score;

    public Student(int age, String name, String birth, int score) {
        super(age, name);
        this.birth = birth;
        this.score = score;
    }

    public void learn() {
        System.out.println("student is learn");
    }

    private void work() {
        System.out.println("student is work");
    }

    public static void eat() {
        System.out.println("I'am static");
    }

    public void hello() {
        System.out.println("I'am is student hello");
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
