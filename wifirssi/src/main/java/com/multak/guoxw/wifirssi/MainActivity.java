package com.multak.guoxw.wifirssi;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAGA";
    int x = 1;
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private MediaProjectionManager projectionManager;
    boolean screenCapture = false;



    Handler mHandler = new Handler() {
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            takeScreenshot2();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                x=1;
                showAlertDialog();
                mHandler.sendEmptyMessageDelayed(100,1000);
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_MEDIA_PROJECTION ) {
            try {
                final int mWidth = getWindowManager().getDefaultDisplay().getWidth();
                final int mHeight = getWindowManager().getDefaultDisplay().getHeight();
                final ImageReader mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int mScreenDensity = metrics.densityDpi;
                final MediaProjection mProjection = projectionManager.getMediaProjection(-1, data);
                final VirtualDisplay virtualDisplay = mProjection.createVirtualDisplay("screen-mirror",
                        mWidth, mHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, null);
                mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        if (x != 1) {
                            return;
                        } else {
                            try {
                                /**
                                 * 去掉--->mProjection.stop(); 会出现不停的截图现象
                                 */
                                x = 2;
                                mProjection.stop();
                                Image image = mImageReader.acquireLatestImage();
                                final Image.Plane[] planes = image.getPlanes();
                                final ByteBuffer buffer = planes[0].getBuffer();
                                int offset = 0;
                                int pixelStride = planes[0].getPixelStride();
                                int rowStride = planes[0].getRowStride();
                                int rowPadding = rowStride - pixelStride * mWidth;
                                Bitmap bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                                bitmap.copyPixelsFromBuffer(buffer);
                                image.close();
                                // 保存路径
                                String pathImage = Environment.getExternalStorageDirectory().getPath() + File.separator + "AA" + File.separator;
                                String nameImage =  pathImage+"截图示例.png";
                                if (bitmap != null) {
                                    try {
                                        File fileImage = new File(nameImage);
                                        if (!fileImage.exists()) {
                                             fileImage.createNewFile();
                                        }
                                        FileOutputStream out = new FileOutputStream(fileImage);
                                        if (out != null) {
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                                            out.flush();
                                            out.close();
                                            Toast.makeText(MainActivity.this, "图片保存成功!", Toast.LENGTH_SHORT).show();
                                            Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                            Uri contentUri = Uri.fromFile(fileImage);
                                            media.setData(contentUri);
                                            getApplicationContext().sendBroadcast(media);
                                            screenCapture = false;
                                        }
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "cannot get phone's screen", Toast.LENGTH_SHORT).show();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    /**
     * 截图功能调用
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void takeScreenshot2() {
        try {
            projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(
                    projectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAlertDialog() {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("简单对话框");
        localBuilder.setIcon(R.mipmap.ic_launcher);
        localBuilder.setMessage("提示信息？");
        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                /**
                 * 确定操作
                 * */
            }
        });
        localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                /**
                 * 确定操作
                 * */
            }
        });

        /***
         * 设置点击返回键不会消失
         * */
        localBuilder.setCancelable(false).create();

        localBuilder.show();
    }
}
