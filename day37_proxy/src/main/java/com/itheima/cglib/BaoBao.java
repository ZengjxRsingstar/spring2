package com.itheima.cglib;

public class BaoBao {

    public void sing(String song) {
        System.out.println("BaoBao.sing方法执行了");
        System.out.println("宝宝唱：" + song);
    }

    public String dance(String dance) {
        System.out.println("Baobao.dance方法执行了");
        return "宝宝跳：" + dance;
    }

}
