package ru.andrew.jclazz.apps.tester.tests.other;

public class Usa 
{
    public String name = "Detroit";
    public class England 
    {
        public String name = "London";
        public class Ireland 
        {
            public String name = "Dublin";
            public void print_names() 
            {
                System.out.println(name);
            }
        }
    }
}
