package gemalto.com.gemaltouser.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gemalto.com.gemaltodatalib.networking.response.genderquery.GetGenderQueryInfoResponse;
import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltouser.R;

/**
 * Created by Manuramv on 9/7/2018.
 */

public class CommonUtilities {
    static AlertDialog alertDialog;
    static ProgressDialog plainProgressIndicator=null;
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivity.getActiveNetworkInfo();

        boolean isConnected = info != null && info.isConnectedOrConnecting();
        return isConnected;

    }
    //convert the time to unix time stamp.
    public static long convertToUnixTImestamp(String timeToCOnvert) {
        long unixTime = 0;
        try {
            //new java.util.Date(Long.parseLong(timeToCOnvert));

            //String dateString = "Fri, 09 Nov 2012 23:40:18 GMT";
            DateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm");
            Date date = dateFormat.parse(timeToCOnvert);
            unixTime = (long) date.getTime() / 1000;
            System.out.println("UNix time==="+unixTime);//<- prints 1352504418
        } catch (Exception e){
            Log.d("TAG","Exception while converting the date to unix::"+e);
        }
        return  unixTime;
    }

    //MEthod to show the popup messages.
    public static void showCustomPopupMessage(final Activity mContext, String msg){
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = null;
        LayoutInflater inflaterObjRef = null;
        View layoutObjRef = null;
        Button dontAllowButtonRef = null;
        TextView messageText;
        Button allowButtonRef = null;
        try {
            // mFragmentObjGlobal=mFragmentObj;
            alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
            alertDialogBuilder.setCancelable(false);
            inflaterObjRef = mContext.getLayoutInflater();
            layoutObjRef = inflaterObjRef.inflate(R.layout.popup_screen, null);
            alertDialogBuilder.setView(layoutObjRef);
            dontAllowButtonRef = (Button) layoutObjRef.findViewById(R.id.okbutton);
            messageText = (TextView) layoutObjRef.findViewById(R.id.messageText);
            messageText.setText(msg);
            // allowButtonRef = (Button) layoutObjRef.findViewById(R.id.allow_button);
            dontAllowButtonRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //CommonUtils.setStringKeyPreferences(ParkwayConstants.IS_USERCLICKED_ONALLOW_DISALLOW, ParkwayConstants.IS_USERCLICKED_ONALLOW_DISALLOW_DISALLOW);
                    alertDialog.dismiss();
                }
            });
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //To show the loader
    public static void showBusyIndicator(final AppCompatActivity activityObj) {
        try {
            if (activityObj != null) {
                activityObj.runOnUiThread(new Runnable() {
                    public void run() {
                        if (plainProgressIndicator == null)
                            plainProgressIndicator = new ProgressDialog(activityObj);

                        if (!(plainProgressIndicator.isShowing())) {
                            plainProgressIndicator = ProgressDialog.show(activityObj, "", "Loading", true, false);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //dismiss the progress bar.
    public static void removeBusyIndicator(final AppCompatActivity activityObj) {
        try {
            activityObj.runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        if (plainProgressIndicator != null && plainProgressIndicator.isShowing()) {
                            plainProgressIndicator.dismiss();
                            plainProgressIndicator = null;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static JSONObject generateJsonObj(GetGenderQueryInfoResponse list,int position) {
       /* JSONObject obj = new JSONObject();
        try {
            obj.put("id", list.getId().getValue());
            obj.put("name", list.getName().getFirst()+" "+ list.getName().getLast());
            obj.put("gender", list.getGender().toString());
            obj.put("age", list.getDob().getAge().toString());
            obj.put("dob", list.getDob().getDate().toString());
            obj.put("email", list.getEmail().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;*/

        JSONObject obj = new JSONObject();
        try {
            if(list.getInfo()!=null){
                obj.put("id", list.getInfo().getSeed());
            } else {
                obj.put("id", " ");
            }

            obj.put("name", list.getResults().get(position).getName().getFirst()+" "+ list.getResults().get(0).getName().getLast());
            obj.put("gender", list.getResults().get(position).getGender().toString());
            obj.put("age", list.getResults().get(position).getDob().getAge().toString());
            obj.put("dob", list.getResults().get(position).getDob().getDate().toString());
            obj.put("email", list.getResults().get(position).getEmail().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }


    @SuppressLint("NewApi")
    public static boolean isFingerPrintAvailableInTheDevice(AppCompatActivity mActivity) {
        boolean showFingerprintPopup = false;
        KeyguardManager keyguardManager;
        FingerprintManager fingerprintManager;
        try {
            keyguardManager = mActivity.getSystemService(KeyguardManager.class);
            fingerprintManager = mActivity.getSystemService(FingerprintManager.class);
            if (!(keyguardManager.isKeyguardSecure())) {
                //Show a message that the user hasn't set up a fingerprint or lock screen.
                showFingerprintPopup = false;
                return showFingerprintPopup;
            }
            // Now the protection level of USE_FINGERPRINT permission is normal instead of dangerous.
            // See http://developer.android.com/reference/android/Manifest.permission.html#USE_FINGERPRINT
            // The line below prevents the false positive inspection from Android Studio
            // noinspection ResourceType
            if (!(fingerprintManager.hasEnrolledFingerprints())) {
                //This happens when no fingerprints are registered.
                showFingerprintPopup = false;
                return showFingerprintPopup;
            }
            /*keyguardManager=null;
            fingerprintManager=null;
            defaultCipher=null;
            mKeyStore=null;
            mKeyGenerator=null;*/
            showFingerprintPopup = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return showFingerprintPopup;
    }
}
