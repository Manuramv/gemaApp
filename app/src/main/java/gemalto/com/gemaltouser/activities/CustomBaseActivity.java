package gemalto.com.gemaltouser.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import gemalto.com.gemaltodatalib.networking.response.genderquery.Login;
import gemalto.com.gemaltodatalib.serviceimpl.CommonUtils;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.util.CommonUtilities;
import gemalto.com.gemaltouser.util.GemAppConstants;

public class CustomBaseActivity extends AppCompatActivity implements IdleTimeOutService {
    Handler handlerObjRef;
    static AppCompatActivity appComptActivityRef=null;
    private long DISCONNECT_TIMEOUT= 120000;
    private boolean isActivityVisible=false;
    public static long startTime = 0;
    public static long endTime = 0;
    static AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_base);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        appComptActivityRef = this;

            startTimer();
    }

    public void setAppToolbar(String title, int resourceId) {
        Toolbar actionBarToolbar = (Toolbar) findViewById(R.id.tool_bar);
        actionBarToolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(actionBarToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(resourceId);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        TextView mTitleTextView = (TextView) findViewById(R.id.toolbar_titleText);
        mTitleTextView.setText(title);

    }


    public  void startTimer() {
        //Idle timeOut - Start 3
        Log.d("TAG","start timer called");
        handlerObjRef = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
        //Will enable this later.
        GemAppConstants.isUserLoggedin = true;
        appComptActivityRef = this;
        if (GemAppConstants.isUserLoggedin)
            handlerObjRef.postDelayed(runnableObjRef, DISCONNECT_TIMEOUT);
    }



    //Idle timeOut - Start
    public  void showIdleTimeOutNotification(final AppCompatActivity mActivityObj) {
        AlertDialog.Builder alertDialogBuilder = null;
        LayoutInflater inflaterObjRef = null;
        View layoutObjRef = null;
        Button positiveButtonRef = null;
        Button negativeButtonRef = null;
        try {
            //CommonUtils.showBusyIndicator(mActivityObj);
            endTimer();
            //timeOutServiceObj = new IdleTimeOutServiceImpl<>(mActivityObj, null);
            alertDialogBuilder = new AlertDialog.Builder(mActivityObj);
            alertDialogBuilder.setCancelable(false);
            inflaterObjRef = mActivityObj.getLayoutInflater();
            layoutObjRef = inflaterObjRef.inflate(R.layout.idle_timeout_notification, null);
            alertDialogBuilder.setView(layoutObjRef);
            TextView messageText = (TextView) layoutObjRef.findViewById(R.id.messageText);
            positiveButtonRef = (Button) layoutObjRef.findViewById(R.id.logout_timeout_button);
            //negativeButtonRef = (Button) layoutObjRef.findViewById(R.id.cancel_button);
            //messageText.setText("Session timeout");
            /*positiveButtonRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        CommonUtils.showBusyIndicator(mActivityObj);
                        resetHandler();
                        continueSession();
                        alertDialog.dismiss();
                    } catch (Exception e) {

                        ParkwayLog.printStackTrace(e);
                    }
                }
            });*/

            positiveButtonRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //CommonUtilities.showBusyIndicator(mActivityObj);
                        endTimer();
                        cancelSession(mActivityObj);
                        alertDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            //if (((CustomBaseActivity) mActivityObj).isActivityVisible) {
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
           // }
        } catch (Exception e) {
Log.d("TAG","exception in showing timer:");
        }
    }
    //Idle timeOut - End 3

    public  void endTimer() {
        Log.d("TAG","end timer called");
        handlerObjRef.removeCallbacks(runnableObjRef);
        handlerObjRef.removeCallbacksAndMessages(null);
    }

    public  void resetHandler() {
        try {
            startTime = System.currentTimeMillis();

            handlerObjRef.removeCallbacks(runnableObjRef);
            handlerObjRef.removeCallbacksAndMessages(null);
            if (GemAppConstants.isUserLoggedin)
                handlerObjRef.postDelayed(runnableObjRef, DISCONNECT_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private  Runnable runnableObjRef = new Runnable() {
        @Override
        public void run() {
            if (GemAppConstants.isUserLoggedin) {
                if (alertDialog == null) {
                    showIdleTimeOutNotification(appComptActivityRef);

                } else if (alertDialog != null && alertDialog.isShowing()) {
                    //Dont show
                } else if (alertDialog != null && !(alertDialog.isShowing())) {
                    showIdleTimeOutNotification(appComptActivityRef);
                }
            }
        }
    };

    @Override
    public  void cancelSession(AppCompatActivity mActivityObj) {
        //do the logout logic
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


    }

    @Override
    public void continueSession() {
        CommonUtilities.removeBusyIndicator(this);
    }


    @Override
    public void onUserInteraction() {
        startTime = System.currentTimeMillis();
        super.onUserInteraction();
        if (GemAppConstants.isUserLoggedin) {
            Log.d("abcxyz =====>", "onUserInteraction resetHandler called<======");
            resetHandler();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        appComptActivityRef = this;
    }
}
