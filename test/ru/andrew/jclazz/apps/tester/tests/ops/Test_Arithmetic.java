package ru.andrew.jclazz.apps.tester.tests.ops;

public class Test_Arithmetic
{
    public void test()
    {
        int i1 = 1;
        int i2 = 2;
        long l1 = 1L;
        long l2 = 2L;
        float f1 = 1.0f;
        float f2 = 2.0f;
        double d1 = 1.0;
        double d2 = 2.0;

        System.out.println("Add");
        int i3 = i1 + i2;
        long l3 = l1 + l2;
        float f3 = f1 + f2;
        double d3 = d1 + d2;

        System.out.println("Sub");
        int i4 = i1 - i2;
        long l4 = l1 - l2;
        float f4 = f1 - f2;
        double d4 = d1 - d2;

        System.out.println("Mul");
        int i5 = i1 * i2;
        long l5 = l1 * l2;
        float f5 = f1 * f2;
        double d5 = d1 * d2;

        System.out.println("Div");
        int i6 = i1 / i2;
        long l6 = l1 / l2;
        float f6 = f1 / f2;
        double d6 = d1 / d2;

        System.out.println("Rem");
        int i7 = i1 % i2;
        long l7 = l1 % l2;
        float f7 = f1 % f2;
        double d7 = d1 % d2;

        System.out.println("Neg");
        int i8 = -i1;
        long l8 = -l1;
        float f8 = -f1;
        double d8 = -d1;
    }
}
