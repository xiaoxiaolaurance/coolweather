package com.example.xiaoxiao.materialdesign;

import java.util.Random;

/**
 * Created by xiaoxiao on 2018/1/24.
 */

public class Randoms {

    public static String[]firstName ={"zhao","li","sun","qian","wang"};
    public static String[]lastName ={"xiaoming","xiaohong","xiaoli","mary"};

    static Random random =new Random();

    public static String getFirstName(){
        return firstName[random.nextInt(firstName.length)];
    }

    public static String getLastName(){
        return lastName[random.nextInt(lastName.length)];
    }
}
