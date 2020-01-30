package com.itheima.jdkproxy;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * JDK的动态代理：基于接口的
 */
public class ProxyTest {
    @Test
    public void test1(){
        //直接调用目标对象：没有增强
        Star star = new BaoBao();
        //star.sing("说好不哭");
        //String dance = star.dance("广播体操");
        //System.out.println(dance);

        //在不修改源码的情况下，进行功能增强。使用代理模式
        Star starProxy = (Star) Proxy.newProxyInstance(
                star.getClass().getClassLoader(),
                star.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("前置增强");

                        //调用目标对象的方法
                        Object result = method.invoke(star, args);

                        System.out.println("后置增强");

                        return result;
                    }
                }
        );

        starProxy.sing("绿光");
        String dance = starProxy.dance("钢管舞");
        System.out.println(dance);
    }

    @Test
    public void test2(){
        //创建Star接口的代理对象
        Star star = (Star) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{Star.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("前置增强");

                        System.out.println("一切工作，都由代理对象完成" + method.getName());

                        System.out.println("后置增强");

                        return null;
                    }
                }
        );

        star.sing("男人哭吧不是罪");
        String dance = star.dance("海草舞");
        System.out.println(dance);
    }
}
