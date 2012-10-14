package edu.upenn.cis573;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
 
public class ConnectionDetector {
 
    private Context _context;
 
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
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
    AlertDialog alertDialog = new AlertDialog.Builder(context).create();

    // Setting Dialog Title
    alertDialog.setTitle(title);

    // Setting Dialog Message
    alertDialog.setMessage(message);

    // Setting alert dialog icon
    //alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

    // Setting OK Button
    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        }
    });

    // Showing Alert Message
    alertDialog.show();

}
}