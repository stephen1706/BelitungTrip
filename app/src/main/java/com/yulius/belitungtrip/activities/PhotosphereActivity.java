package com.yulius.belitungtrip.activities;

import android.content.Intent;
import android.os.Bundle;

public class PhotosphereActivity  extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startPanorama();
    }

    private void startPanorama() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setComponent(new ComponentName("com.google.android.gms", "com.google.android.gms.panorama.PanoramaViewActivity"));
//        intent.setDataAndType(Uri.parse("file://" + "/sdcard/DCIM/Camera/20140829_131503.jpg"), "image/*");
//        Log.d("test","open : " + Environment.getExternalStorageDirectory() + "/req_images/test1.jpg");
//        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory() + "/req_images/test1.jpg"), "image/*");
//        intent.setDataAndType(Uri.parse("/storage/emulated/0/Android/data/com.yulius.belitungtrip/Files/MI_12012015_1426.jpg"), "image/jpeg");
//        intent.setDataAndType(Uri.parse(mContext.getFilesDir() + "test.jpg"), "image/*");



        startActivity(intent);

    }
}
