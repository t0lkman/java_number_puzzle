package com.puzzle.helpers;

public class MainHelperMethods {

	/**
	 * This String utility method can be used to trim all the String values in
	 * the string array. For input {" a1 ", "b1 ", " c1"} the output will be
	 * {"a1", "b1", "c1"} Method takes care of null values.
	 * 
	 * @param values
	 * @return
	 */
	public static String[] trim(final String[] values) {
		for (int i = 0, length = values.length; i < length; i++) {
			if (values[i] != null) {
				values[i] = values[i].trim();
			}
		}
		return values;
	}

	/**
	 * This method is used to split the given string into different tokens at
	 * the occurrence of specified delimiter An example :
	 * <code>"abcdzefghizlmnop"</code> and using a delimiter <code>"z"</code>
	 * would give following output <code>"abcd" "efghi" "lmnop"</code>
	 * 
	 * @param str
	 *            The string that needs to be broken
	 * @param delimeter
	 *            The delimiter used to break the string
	 * @return a string array
	 */
	public static String[] getTokensArray(String str, String delimeter) {
		if (str != null) {
			return str.split(delimeter);
		}
		return null;
	}

}