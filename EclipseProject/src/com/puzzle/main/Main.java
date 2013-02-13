package com.puzzle.main;

import com.puzzle.helpers.MainHelperMethods;

public class Main {
	public static void main(String[] args) {
		String[] strArr =  {"  This", " is", " a ", "Test " };
		//before helper applied
		for(String str:strArr) {
		  System.out.println(str);
		}
		System.out.println("-------------------");
		// after helper has been applied
		for(String str:MainHelperMethods.trim(strArr)) {
			  System.out.println(str);
		}
	}
}
