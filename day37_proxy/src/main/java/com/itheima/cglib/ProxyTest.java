package com.itheima.cglib;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyTest {
    @Test
    public void test1(){
        //目标对象：
        BaoBao baoBao = new BaoBao();
        //baoBao.sing("生日快乐");
        //String dance = baoBao.dance("肚皮舞");
        //System.out.println(dance);

        /**
         * Enhancer的create方法：
         *  第一个参数：Class  superClass： 是目标类的字节码对象
         *  第二个参数：CallBack callback： 用于写代理对象的行为的
         */
        BaoBao baoProxy = (BaoBao) Enhancer.create(BaoBao.class, new MethodInterceptor() {

            /**
             * 用于写代理对象（子类对象）的行为
             * @param o 代理对象
             * @param method  要执行的方法对象
             * @param objects 执行方法需要的实参
             * @param methodProxy 方法代理对象
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

                System.out.println("前置增强");

                //在子类里，通过反射调用父类的方法
                //Object invoke = method.invoke(baoBao, objects);

                //在子类里，直接调用父类方法
                Object invoke = methodProxy.invokeSuper(o, objects);

                System.out.println("后置增强");

                return invoke;
            }
        });

        baoProxy.sing("生日快乐");
        String dance = baoProxy.dance("肚皮舞");
        System.out.println(dance);
    }
}
