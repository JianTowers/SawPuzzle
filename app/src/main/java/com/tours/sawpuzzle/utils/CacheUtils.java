package com.tours.sawpuzzle.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: Jian
 * @Date: 2023/5/4
 **/
public class CacheUtils implements Serializable {

    private static final String TAG = "TLoad";

    // =========================================
    // ================  单例部分 ===============
    // =========================================
    private volatile static CacheUtils mInstance = null;

    private CacheUtils() {
    }

    public static CacheUtils getInstance() {
        if (mInstance == null) {
            synchronized (CacheUtils.class) {
                if (mInstance == null) {
                    mInstance = new CacheUtils();
                }
            }
        }
        return mInstance;
    }

    private Object readResolve() throws ObjectStreamException {
        return mInstance;
    }


    // =========================================
    // ================  线程部分 ===============
    // =========================================
    private abstract static class BasePriorityRunnable implements Runnable, Comparable<BasePriorityRunnable> {
        private final long priority;

        BasePriorityRunnable(long priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(BasePriorityRunnable o) {
            return (int) (o.priority - priority);
        }
    }

    /**
     * 核心线程数
     */
    private static final int INIT_CORE_POOL_SIZE = 5;

    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 15;

    /**
     * 最大存活时间
     */
    private static final int MAX_KEEP_ALIVE_TIME = 5;

    /**
     * 任务队列
     */
    private static final int MAX_TASK_QUEUE_LENGTH = 100;

    private ExecutorService mExecutorService;

    private void initThreadPool() {
        if (mExecutorService != null) {
            return;
        }
        try {
            mExecutorService = new ThreadPoolExecutor(INIT_CORE_POOL_SIZE, MAX_POOL_SIZE, MAX_KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS, new PriorityBlockingQueue<>(MAX_TASK_QUEUE_LENGTH, ((o1, o2) -> {
                return ((BasePriorityRunnable) o1).compareTo((BasePriorityRunnable) o2);
            })));
        } catch (IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    // =========================================
    // ================  内存缓存 ===============
    // =========================================

    /**
     * 最大内存
     */
    private final long max = Runtime.getRuntime().maxMemory() / 8;

    private final HashMap<String, SoftReference<Bitmap>> lruMap = new HashMap<>();
    private final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>((int) 50 * 1024 * 1024) {
        @Override
        protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
            return value.getAllocationByteCount();
        }

        @Override
        protected void entryRemoved(boolean evicted, @NonNull String key, @NonNull Bitmap oldValue, @Nullable Bitmap newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
            //从内存中移除时保存到本地内存
            if (newValue != null) {
                saveLocalBitmap(key, newValue);
                return;
            }
        }
    };

    private void setLru(String key, Bitmap bitmap) {
        lruCache.put(key, bitmap);
    }

    private Bitmap getLru(String key) {
        return lruCache.get(key);
    }

    // =========================================
    // ================  硬盘缓存 ===============
    // =========================================
    private Context mContext;

    private void saveLocalBitmap(String key, Bitmap bitmap) {
        if (key == null || TextUtils.isEmpty(key) || bitmap == null || mContext == null) {
            return;
        }
        checkMkdirs(getCachePath(mContext));
        String filePath = getCachePath(mContext) + key;
        try {
            OutputStream os = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getLocalBitmap(String key) {
        if (key == null || TextUtils.isEmpty(key) || mContext == null) {
            Log.e(TAG, "getLocalBitmap: 本地记录异常");
            return null;
        }
        String filePath = getCachePath(mContext) + key;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(filePath)));
            return bitmap;
        } catch (Exception e) {
        }
        return null;
    }

    // =========================================
    // ================  网络缓存 ===============
    // =========================================

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();
            if (code == 200) {
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return compressBitmap(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "downloadBitmap: " + e.getMessage());
        }
        return null;
    }


    // =========================================
    // ================  外部方法 ===============
    // =========================================
    //网络类型地址
    public static final int NETWORK_TYPE = 1;
    //本地路径
    public static final int LOCAL_TYPE = 2;

    public void init(Application application) {
        this.mContext = application;
    }

    public void destroy(){
        if(mExecutorService == null) {
            return;
        }
        mExecutorService.shutdown();
        mExecutorService = null;
    }

    public void load(Context context, int resources, ImageView imageView) {
        if (mContext == null) {
            mContext = context.getApplicationContext();
        }
        initThreadPool();
        mExecutorService.execute(new BasePriorityRunnable(System.currentTimeMillis()) {
            @Override
            public void run() {
                try {
                    show(context, imageView, BitmapFactory.decodeResource(context.getResources(), resources));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void load(Context context, String resources, ImageView imageView) {
        if (mContext == null) {
            mContext = context.getApplicationContext();
        }
        initThreadPool();
        switch (assessType(resources)) {
            case LOCAL_TYPE:
                mExecutorService.execute(new BasePriorityRunnable(System.currentTimeMillis()) {
                    @Override
                    public void run() {
                        try {
                            show(context, imageView, BitmapFactory.decodeStream(new FileInputStream(new File(resources))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case NETWORK_TYPE:
                mExecutorService.execute(new BasePriorityRunnable(System.currentTimeMillis()) {
                    @Override
                    public void run() {
                        cacheLoading(context, resources, imageView);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void cacheLoading(Context context, String url, ImageView imageView) {
        String key = md5(url);

        //从缓存拿
        Bitmap cacheBitmap = getLru(key);
        if (cacheBitmap != null) {
            show(context, imageView, cacheBitmap);
            return;
        }

        //从内存拿
        Bitmap localBitmap = getLocalBitmap(key);
        if (localBitmap != null) {
            setLru(key, localBitmap);
            show(context, imageView, localBitmap);
            return;
        }

        //从网络下载
        Bitmap netWorkBitmap = downloadBitmap(url);
        if (netWorkBitmap != null) {
            setLru(key, netWorkBitmap);
            show(context, imageView, netWorkBitmap);
            return;
        }
    }

    private void show(Context context, ImageView imageView, Bitmap bitmap) {
        if (context != null && imageView != null) {
            imageView.post(() -> {
                imageView.setImageBitmap(bitmap);
            });
        }
    }

    // =========================================
    // ================  通用部分 ===============
    // =========================================

    /**
     * 获取缓存目录
     */
    private String getCachePath(Context context) {
        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String secnodPath = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                secnodPath = context.getExternalCacheDir().getAbsolutePath();
            } else {
                secnodPath = context.getCacheDir().getAbsolutePath();
            }
        }
        return sdPath + secnodPath + File.separator + "bitmap" + File.separator;
    }

    private void checkMkdirs(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    private int assessType(String address) {
        if (Patterns.WEB_URL.matcher(address).matches()) {
            return NETWORK_TYPE;
        }
        return LOCAL_TYPE;
    }

    /**
     * md5加密
     */
    private String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 图片允许的最大空间,单位kb
     */
    private static final int MAX_SIZE = 200;

    /**
     * 压缩图片
     * 质量压缩
     */
    private Bitmap compressBitmap(Bitmap bitmap) {
        //压缩到指定大小
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        int options = 90;
        while (bao.toByteArray().length / 1024 > MAX_SIZE) {
            if (options < 10) {
                break;
            }
            bao.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, bao);
            options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(bao.toByteArray());
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null);
    }
}
