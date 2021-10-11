package com.hd.permissiondetect.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.hd.permissiondetect.MyApplication;

import java.io.File;


public class ShareUtil {
    // 应用的fileProvider id与xml中配置一致
    public static final String APP_FILE_PROVIDER = "com.hd.permissiondetect.fileProvider";

    private ShareUtil() {
    }

    // 調用系統方法分享文件
    public static void shareFile(Context context, File file, String title) {
        if (context == null) {
            return;
        }

        if (null != file && file.exists()) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(context, APP_FILE_PROVIDER, file);
                intent.putExtra(Intent.EXTRA_STREAM, contentUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setType(getMimeType(file.getAbsolutePath()));
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setType(getMimeType(file.getAbsolutePath()));
            }
            try {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, title));
            } catch (Exception e) {
                Toast.makeText(context, "分享失败 " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "分享失败  文件不存在", Toast.LENGTH_SHORT).show();
        }
    }


    private static String getMimeType(File file) {
        Uri uri = Uri.fromFile(file);
        ContentResolver cR = MyApplication.getAppContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getExtensionFromMimeType(cR.getType(uri));
        return type;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param filePath
     * @return
     * @see <a href="参考地址">https://stackoverflow.com/questions/8589645/how-to-determine-mime-type-of-file-in-android/46752076#46752076</a>
     */
    private static String getMimeType(String filePath) {
        String mime = getMimeType(new File(filePath));
        if (TextUtils.isEmpty(mime)) {
            return "*/*";
        } else {
            return mime;
        }
    }

}
