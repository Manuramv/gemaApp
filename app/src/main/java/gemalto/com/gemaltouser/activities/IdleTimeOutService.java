package gemalto.com.gemaltouser.activities;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Manuramv on 9/8/2018.
 */

interface IdleTimeOutService {
    public void cancelSession(AppCompatActivity mActivityObj);
    public void continueSession();
}
