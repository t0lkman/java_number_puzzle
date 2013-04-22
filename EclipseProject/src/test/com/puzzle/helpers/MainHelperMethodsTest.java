package test.com.puzzle.helpers;

import junit.framework.Assert;

import org.junit.Test;

import com.puzzle.helpers.MainHelperMethods;

public class MainHelperMethodsTest {

	@Test
	public void testRegularDocumentedSequence() {
		String testArr[] = new String[]{" a1 ", "b1 ", " c1"};
		Assert.assertEquals(MainHelperMethods.trim(testArr).length, 3);
		// space has to be removed from " c1"?
		Assert.assertTrue(MainHelperMethods.trim(testArr)[2].length() == 2);
		Assert.assertEquals(MainHelperMethods.trim(testArr)[2], "c1");
	}
	
	@Test
	public void testTrimNull() {
		//testing for null value
		Assert.assertEquals(MainHelperMethods.trim(null), null);
	}
	
	@Test
	public void testTrimEmptyInput() {
		String testArr[] = new String[]{};
		Assert.assertEquals(MainHelperMethods.trim(testArr), testArr);
	}

	@Test
	public void testTrimOneCharacter() {
		String testArr[] = new String[]{"A"};
		Assert.assertEquals(MainHelperMethods.trim(testArr), testArr);
	}
	
	@Test
	public void testTrimOneCharacterPlusSpaces() {
		String testArr[] = new String[]{"A   "};
		Assert.assertEquals(MainHelperMethods.trim(testArr)[0], "A");
	}
	
	@Test
	public void testTrimOneCharacterPlusSpacesAsElements() {
		String testArr[] = new String[]{"A"," ", "  "};
		Assert.assertTrue(MainHelperMethods.trim(testArr).length == 3);
		//if element equals to 2 spaces then nothing should left
		Assert.assertTrue(MainHelperMethods.trim(testArr)[2].length() == 0);
	}
	
	@Test
	public void testGetTokensArrayNormalDocumentedInput() {
		String[] testArr = MainHelperMethods.getTokensArray("", "");
		Assert.assertTrue(testArr.length == 1);
		Assert.assertEquals(testArr[0], "");
	}
	
	@Test
	public void testGetTokensArrayNullInput() {
		Assert.assertEquals(MainHelperMethods.getTokensArray(null, ""), null);
	}

	@Test
	public void testGetTokensArrayEmptyInput() {
		String[] testArr = MainHelperMethods.getTokensArray("abcdzefghizlmnop", "z");
		Assert.assertTrue(testArr.length == 3);
		Assert.assertEquals(testArr[0], "abcd");
	}
}
