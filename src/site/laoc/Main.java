package site.laoc;

import java.util.Dictionary;
import java.util.HashMap;

public class Main {

    private String name;
    private int age;

    public Main(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public static void main(String[] args) {
        String str = "管理员";

        System.out.println(Integer.valueOf(str));
    }
}
