package edu.upenn.cis573;

import edu.upenn.cis573.R;
import android.app.Activity;
import android.os.Bundle;

/**
 * Displays the "About" section in the application
 *
 */
public class About extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);	
	}

}
