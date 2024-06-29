package site.laoc.finaltest;

public  class Animal {
    private void test(){
        System.out.println("test");
    }

    public final void bark(){
        System.out.println("bark");
    }
}

class Dog extends Animal{

    // 这里的test方法和bark方法都不会被实现.
}
