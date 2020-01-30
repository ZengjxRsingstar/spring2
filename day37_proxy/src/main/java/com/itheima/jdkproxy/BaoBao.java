package com.itheima.jdkproxy;

public class BaoBao implements Star {
    @Override
    public void sing(String song) {
        System.out.println("BaoBao.sing方法执行了");
        System.out.println("宝宝唱：" + song);
    }

    @Override
    public String dance(String dance) {
        System.out.println("Baobao.dance方法执行了");
        return "宝宝跳：" + dance;
    }
}
