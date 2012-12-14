package edu.upenn.cis573;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Checks to see if device has an internet connection
 *
 */
public class ConnectionDetector {

	private Context _context;

	/**
	 * Gets context of activity
	 */
	public ConnectionDetector(Context context){
		this._context = context;
	}

	/**
	 * Checks to see if there is an internet connection
	 */
	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null)
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}


	/**
	 * Function to display simple Alert Dialog
	 * @param context - application context
	 * @param title - alert dialog title
	 * @param message - alert message
	 * @param status - success/failure (used to set icon)
	 * */
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog alertDialog = builder.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);

		// Setting OK Button
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}
}