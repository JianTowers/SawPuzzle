package com.tours.sawpuzzle.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @Description: TODO
 * @Author: Jian
 * @Date: 2023/6/26
 **/
public class PermissionsUtils {
    private static final int REQUEST_PERMISSION_CODE = 1;

    public static void requestRequiredPermissions(Activity activity, String[] permissions) {
        if (permissions.length != 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions, REQUEST_PERMISSION_CODE);
        }
    }

    public static void handleRequestPermissionsResult(Activity activity, int code, String[] permissions, GrantedListener grantedListener) {
        if (code == REQUEST_PERMISSION_CODE) {
            boolean requestFinish = true;
            if (permissions.length > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String permission : permissions) {
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        requestFinish = false;
                    }
                }
            }
            grantedListener.onPermissionGrantedResult(requestFinish);
        }
    }

    /**
     * 权限授权结果委托
     */
   public interface GrantedListener {
        // 授予权限的结果，在对话结束后调用
        void onPermissionGrantedResult(boolean permissionGranted);
    }

}
