package com.hrant.pixabayclientapp.view.activities.imagePreviewActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.hrant.pixabayclientapp.R;
import com.hrant.pixabayclientapp.shared.configurations.Constants;
import com.hrant.pixabayclientapp.view.activities.BaseActivity;

public class ImagePreviewActivity extends BaseActivity {
    private DownloadManager mDownloadManager;
    private String mImageUrl;
    private int PERMISSION_REQUEST_CODE = 576;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mImageUrl = getIntent().getStringExtra("imageUrl");
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        initViews();
    }

    private void initViews() {
        PhotoView previewImageView = findViewById(R.id.image_preview_image_view);
        ProgressBar previewImageLoading = findViewById(R.id.image_preview_loading);
        ImageView previewImageDownloadFAB = findViewById(R.id.image_preview_download_fab);

        Glide.with(this)
                .load(mImageUrl)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        previewImageLoading.setVisibility(View.GONE);
                        if (e != null)
                            onError(e.getMessage());
                        else
                            onError(Constants.DEFAULT_ERROR_MESSAGE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        previewImageLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(previewImageView);

        previewImageDownloadFAB.setOnClickListener(view -> downloadImage());
    }

    private void downloadImage() {
        if (hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(mImageUrl));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setAllowedOverRoaming(false);
            request.setVisibleInDownloadsUi(true);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    mImageUrl.substring(mImageUrl.lastIndexOf("/") + 1, mImageUrl.length()));
            mDownloadManager.enqueue(request);
        } else {
            requestPermissionsSafely(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    boolean showRationale = shouldShowRequestPermissionRationale(permission);
                    if (!showRationale) {
                        // user also CHECKED "never ask again"

                        // the app setting
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, 100);
                        break;
                    } else {
                        // user did NOT check "never ask again"

                        break;
                    }
                } else {
                    // permission granted
                    if (i == permissions.length - 1)
                        downloadImage();
                }
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
