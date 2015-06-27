package ru.andrew.jclazz.apps.tester.tests.composite;

import java.io.DataInputStream;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class TestByte extends ArrayList {

	public static int field = 0;

	static final String lm_errlist[] = {
		"err1", "err2", "err3"
	};

	
	public TestByte() {
		super(2);
	}
	
	public void testdec1() {
		
		int i = 1;
		for(;;) {
			System.out.println("0");
			while(i>3) {
				if(i>1) {
					System.out.println("5");
				}
				switch(i) {
				case 0:
					System.out.println("1");
					break;
				case 1:
					continue;
				case 2:
					return;
				case 3:
					System.out.println("2");
				}
				System.out.println("3");
			} 
		}
		
	}
	
	public static void main(String[] args) throws Throwable {
		
		
		int a = 10; // (int)Math.rint(34);
		
		for(int b = 0, c = 0; b<1;b++) {
			
		}
		
		if(a != 10); 
			System.out.println("12");
		
		a = ~a;
		
		System.out.println(Integer.toHexString(-1));
		System.out.println(Long.toHexString(-1));
	
	}

	public static int trans(int m) {
		return (m & 17043521) | ((m & 8521760)>>>4) | 
		((m & 532610)<<4);
	}
	
	public static int trans1(int m) {
		return (m & 17043521) | ((m & 8521760)>>>4) | 
		((m & 532610)<<4);
	}

	
	public void test() throws Throwable{

		Class cl1 = Integer.class; 
		
		
		Vector vector = new Vector();
		DataInputStream datainputstream = new DataInputStream(null);
		try {
			do {
				vector.addElement(datainputstream.readUTF());
			} while(true);
		}
		catch(EOFException eofexception) {
			;
		} catch(Throwable throwable) {
			throw throwable;
		}
		System.out.println("1");
		return;
		
	}
	
	public static void test1(){
		
		int i = 1;
		for(;;) {
			switch(i) {
			case 1:
				if(i==1) {
					break;
				}
			default:
				return;
			}
		}
		
	}
	
	public static void printIntValue(int val) {
		
		int a = 0;
		for(;;) {
			
			if(a>1) {
				break;
			}
			System.out.println("1");
		}
		System.out.println("2");
	
	}
	
	public void graph() {

		int i=0;
		while(i>4) {
			System.out.println("1");
			try {
				System.out.println("2");
				if(i>2) {
					continue;
				}
			} finally {
				System.out.println("3");
			}
			System.out.println("4".length());
		}
	}
	
	public static void testdec() {
		
		Object a = new Object();
		Comparable b = new Integer(1);
		b = new Long(1);
		a = b;
		
		if(a == null) {
			;
		}
		
		System.out.println();
		
		for(int i=0, aa;(aa = 3) < 2;i++) {
			System.out.println(aa);
		}
		
		int v = 2;
		System.out.println("a"+v+"b"+v+"c");
		
		List<Integer> lst = new ArrayList<Integer>();
		
		for(Integer in: lst) {
			System.out.println(in.intValue());
		}
		
		int tz = 5;
		tz+=6 + tz;
	}
	
	public static void testb(byte b) {
			try {
				if(Boolean.FALSE.booleanValue()) {
					return;
				}
			} finally {
				System.out.println("1");
			}
		
	}
	
	public void graph1() {
		
		int i=0;
		for(;;) {
			synchronized (this){
				if(Boolean.FALSE.booleanValue()) {
					continue;
				}
			}
			System.out.println("4".length());
		}
	}
	
	public int testf() {
		
		int a = 0;
		try {
			System.out.println("1");
			return 1;
		} finally {
			System.out.println("2");
			return 2;
		}
		
	}
	
	public void testloop() {
		
		int a = 10; // (int)Math.rint(34);
		
		a = ~a;
		
		System.out.println(a);
		
		for(;;) {
			
			if(a > 0) {
				System.out.println("1");
				continue;
			}
			
			if(a > 0) {
				System.out.println("2");
				continue;
			}
			
			if(a > 0) {
				System.out.println("3");
				continue;
			}

			if(a > 0) {
				System.out.println();
				continue;
			}

			if(a > 0) {
				System.out.println();
				continue;
			}
			
		}
		
		
	}
	
}
