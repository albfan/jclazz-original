package ru.andrew.jclazz.apps.tester.tests.clazz;

import java.util.*;

public class InnerClazzGenerics
{
    private String str;
    private List<Integer> list;
    private int i;
    public String pstr;

    public void test()
    {
        IC ic1 = new IC();
        IC ic2 = new IC("123");
        IC ic3 = new IC(true);

        ic1.testIC();
    }

    public class IC<Type>
    {
        private String s;

        public IC()
        {
            s = "default";
        }

        public IC(boolean bool)
        {
            System.out.println("WITH BOOLEAN");
        }

        public IC(String ss)
        {
            s = ss;
        }

        public void testIC()
        {
            if ("sss".equals(str))
            {
                System.out.println("SSS");
            }
            Object obj = list.get(0);
            System.out.println(i);
            String my = pstr;
        }
    }
}
