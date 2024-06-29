package site.laoc.finaltest;

import java.io.FileInputStream;
import java.io.InputStream;

public class Test implements TestInterface{

    @Override
    public void print() {
        // test = 11;  // 报错，不能赋值为final变量test
        try(InputStream in = new FileInputStream("/test.txt")){
            //in = new FileInputStream("m.txt");  // // 报错，不能赋值为final变量in
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
