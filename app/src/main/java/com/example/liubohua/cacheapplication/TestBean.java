package com.example.liubohua.cacheapplication;

import java.io.Serializable;

/**
 * Created by liubohua on 2018/7/20.
 */

public class TestBean implements Serializable{

    private int a;
    private String str;
    private A aa;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public A getAa() {
        return aa;
    }

    public void setAa(A aa) {
        this.aa = aa;
    }

    public String toString() {
        return "inta=" + a + ";str=" + str + ";aa.string" + aa.getString();
    }

    static class A implements Serializable{
        String string;

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
}
