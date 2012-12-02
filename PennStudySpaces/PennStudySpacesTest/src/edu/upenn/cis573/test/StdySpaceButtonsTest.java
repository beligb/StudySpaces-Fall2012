package edu.upenn.cis573.test;

import org.junit.Before;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.jayway.android.robotium.solo.Solo;

import edu.upenn.cis573.SearchActivity;

public class StdySpaceButtonsTest extends
		ActivityInstrumentationTestCase2<SearchActivity> {

	private Solo solo;
	private SearchActivity searchActivityObject;

	public StdySpaceButtonsTest() {
		super("edu.upenn.cis573", SearchActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		searchActivityObject = getActivity();
	}

	public void testEditDateButton() {
		solo.clickOnButton(1);
		assertTrue("Start date picker was not shown.", solo.searchText("Pick"));
	}

	public void testEditStartTimeButton() {
		solo.clickOnButton(2);
		assertTrue("Start time picker was not shown.", solo.searchText("start"));
	}

	public void testEditEndTimeButton() {
		solo.clickOnButton(3);
		assertTrue("Start time picker was not shown.", solo.searchText("end"));

	}

	public void testCheckBoxes() {
		solo.clickOnCheckBox(5);
		solo.clickOnCheckBox(6);
		solo.clickOnCheckBox(7);
	}

	@SmallTest
	public void testFavouritesButton() throws InterruptedException {

		solo.clickOnCheckBox(5);
		solo.clickOnCheckBox(6);
		solo.clickOnCheckBox(7);

		int i = solo.getButton(0).getId();
		assertTrue("something went wrong " + i,
				edu.upenn.cis573.R.id.favoritesButton == i);
		solo.clickOnButton(0);

	}

	public void testSearchButton() throws InterruptedException {

		solo.clickOnCheckBox(4);
		solo.clickOnCheckBox(5);
		solo.clickOnCheckBox(6);
		solo.clickOnCheckBox(7);

		solo.clickOnButton(4);
		solo.assertCurrentActivity("SearchButton not clicked .",
				"SearchActivity");

	}

	public void testFindNowButton() throws InterruptedException {

		solo.clickOnCheckBox(4);
		solo.clickOnCheckBox(5);
		solo.clickOnCheckBox(6);
		solo.clickOnCheckBox(7);

		solo.clickOnButton(5);
		solo.sleep(10000);
		solo.assertCurrentActivity("FindNow button not clicked.",
				"SearchActivity");

	}
	

}