package com.bhanumat.merck.barcodescanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        Log.i(TAG, "Hit onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "Hit onResume");
        if (mScannerView != null) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "Hit onPause");
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        String msg = "Contents = " + result.getText() +
                ", Format = " + result.getBarcodeFormat().toString();
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Log.i(TAG, msg);

        //URL: http://web01.mtha-cloud.com/ProductInfo/ProductInfo.asp?B=8852881026325

        Uri uriUrl = Uri.parse(String.format(AppConfig.VDO_TARGET_URL, result.getText()));
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(MainActivity.this);
            }
        }, 2000);
    }

}
