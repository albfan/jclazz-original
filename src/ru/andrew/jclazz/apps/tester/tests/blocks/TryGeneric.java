package ru.andrew.jclazz.apps.tester.tests.blocks;

public class TryGeneric
{
    public void test1()
    {
        System.out.println("Before try");
        try
        {
            System.out.println("TRY BLOCK");
        }
        catch (Exception e)
        {
            System.out.println("CATCH BLOCK 1");
        }
        catch (Error err)
        {
            System.out.println("CATCH BLOCK 2");
        }
        catch (Throwable t)
        {
            System.out.println("CATCH BLOCK 3");
        }
        finally
        {
            System.out.println(".");
            System.out.println("..");
            System.out.println("...");
            System.out.println("FINALLY BLOCK");
            System.out.println("...");
            System.out.println("..");
            System.out.println(".");
        }
        System.out.println("After try");
    }


    public void test2()
    {
        System.out.println("Before try");
        try
        {
            System.out.println("TRY BLOCK start");
            try
            {
                System.out.println("INNER TRY BLOCK");
            }
            catch (Exception ee)
            {
                System.out.println("INNER CATCH BLOCK 1");
            }
            catch (Throwable tt)
            {
                System.out.println("INNER CATCH BLOCK 2");
            }
            System.out.println("TRY BLOCK end");
        }
        catch (Exception e)
        {
            System.out.println("CATCH BLOCK 1");
            throw new RuntimeException();
        }
        catch (Error err)
        {
            System.out.println("CATCH BLOCK 2");
        }
        catch (Throwable t)
        {
            System.out.println("CATCH BLOCK 3");
        }
        finally
        {
            System.out.println(".");
            System.out.println("..");
            System.out.println("...");
            System.out.println("FINALLY BLOCK");
            System.out.println("...");
            System.out.println("..");
            System.out.println(".");
        }
        System.out.println("After try");
    }
}
