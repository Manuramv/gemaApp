package gemalto.com.gemaltouser.activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.util.CommonUtilities;
import gemalto.com.gemaltouser.util.GemAppConstants;

public class LoginActivity extends AppCompatActivity {

    private FragmentManager fm = null;
    private FingerprintRequestDialogFragment fingerprintRequestDialogFragment;
    AppCompatActivity mActivity;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private KeyStore keyStore;
    private FingerprintManager.CryptoObject cryptoObject;
    TextView fingerprintStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mActivity = this;
        showFingerPrintorLoginPage();


    }


    public void showFingerPrintorLoginPage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && CommonUtilities.isFingerPrintAvailableInTheDevice(this)) {
            try {
                fm = this.getFragmentManager();
                fingerprintRequestDialogFragment = new FingerprintRequestDialogFragment();
                fingerprintRequestDialogFragment.show(fm, "fingerprintDialogFramgment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Fingerprint Start
    @SuppressLint("ValidFragment")
    private class FingerprintRequestDialogFragment extends DialogFragment {
        private Button cancelButton;
        FingerprintHandler fingerprintHandler;

        private FingerprintRequestDialogFragment() {
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            setCancelable(false);
            setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Light_Dialog);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                                 Bundle savedInstance) {
            View v = inflater.inflate(R.layout.activity_fingerprint_challenge, viewGroup, false);
            cancelButton = (Button) v.findViewById(R.id.cancel_button);
            fingerprintStatus = (TextView) v.findViewById(R.id.fingerprint_status);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            fingerprintHandler = new FingerprintHandler();
            return v;
        }

        @Override
        public void onResume() {
            super.onResume();
            fingerprintHandler.startAuthentication("Gemaltouser",fingerprintStatus);
        }

        @Override
        public void onPause() {
            super.onPause();
            if (fingerprintHandler.cancellationSignal != null) {
                fingerprintHandler.cancellationSignal.cancel();
                fingerprintHandler.cancellationSignal = null;
            }
        }

        @Override
        public void onDestroyView() {
            if (getDialog() != null && getRetainInstance()) {
                getDialog().setDismissMessage(null);
            }
            super.onDestroyView();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private class FingerprintHandler extends FingerprintManager.AuthenticationCallback {
        private final String TAG = "FIngerprintLogin";
        private Context context = getApplicationContext();
        CancellationSignal cancellationSignal = new CancellationSignal();

        public FingerprintHandler() {
            //We are not supporting any functionality for method as of now.
        }

        public void startAuthentication(String keyName, TextView fingerprintStatus) {
            try {
                FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);

                generateKey();
                Cipher cipher = generateCipher();
                cryptoObject = new FingerprintManager.CryptoObject(cipher);

                //noinspection MissingPermission
                fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

            } catch (Exception e) {
                fingerprintRequestDialogFragment.dismiss();
                //showErrorNotificationPanel(e.getMessage());
                //need to show the normal login page
                //openLoginView();
                Log.d(TAG, "Failed to start 'authenticate'.", e);
            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            fingerprintRequestDialogFragment.dismiss();
            Intent intent = new Intent(mActivity,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            GemAppConstants.isUserLoggedin = true;
            startActivity(intent);


        }

        @Override
        public void onAuthenticationFailed() {
            fingerprintStatus.setText("Failed to authenticate.");
            fingerprintStatus.setTextColor(Color.RED);
        }

        /*public void onClickCancelDialog(View v){
            Fragment fg = fm.findFragmentByTag("FingerprintRequestDialogFragment");
            if(fg!=null){
                DialogFragment df = (DialogFragment)fg;
                df.dismiss();
            }
        }*/
    }

    private void generateKey() {
        try {
            // Get the reference to the key store
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            // Key generator to generate the key
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder("MANU",
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();

        }
    }


    private Cipher generateCipher() {
        try {
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            SecretKey key = (SecretKey) keyStore.getKey("MANU",
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException
                | InvalidKeyException
                | UnrecoverableKeyException
                | KeyStoreException exc) {
            exc.printStackTrace();

        }
        return cipher;
    }

    //Fingerprint End

}
