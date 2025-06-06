package org.example.codereview.util;


public class AsyncUtils {

    //  @Async
    public static void commit(Runnable runnable) {
        runnable.run();
    }

    // 1. 冗余参数和变量
    public void processData(String input, int unusedParam, boolean flag) {
        String temp = input; // 冗余赋值
        int x = 10; // 未使用的局部变量
        if(flag) {
            System.out.println(temp.toLowerCase());
        }
    }

    // 2. 过度复杂的条件判断
    public boolean isEven(int number) {
        if(number % 2 == 0) {
            return true;
        } else if(number % 2 != 0) {
            return false;
        }
        return false; // 不可达代码
    }

    // 3. 重复代码块
    public void printUserInfo(User user) {
        System.out.println("Name: " + user.getName());
        System.out.println("Age: " + user.getAge());

        // 下面这段与上面完全重复
        System.out.println("Name: " + user.getName());
        System.out.println("Age: " + user.getAge());
    }

}
