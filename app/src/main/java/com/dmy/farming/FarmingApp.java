package com.dmy.farming;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import org.bee.BeeFrameworkApp;

import com.dmy.DataCleanManager;
import com.dmy.farming.service.LocationService;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;

public class FarmingApp extends BeeFrameworkApp
{
    // 两列活动
    public static DisplayImageOptions options_small_with_text;
    public static DisplayImageOptions options_long_with_text;
    public static DisplayImageOptions options_head;


    public static DisplayImageOptions options;
    public static DisplayImageOptions options_image;

    public static Context applicationContext;

    public static String CACHE_DIR = "/data";

    public LocationService locationService;
    public Vibrator mVibrator;

    public static DataCleanManager dataCleanManager;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();

        final Context mContext = this;
        new Runnable() {

            @Override
            public void run() {
                applicationContext = mContext;

                dataCleanManager = new DataCleanManager(applicationContext);

                options_small_with_text = new DisplayImageOptions.Builder()
                        //.showImageOnLoading(R.drawable.loading_animation) // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.default_activity) // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.default_activity) // 设置图片加载或解码过程中发生错误显示的图片
                        //.resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                        //.delayBeforeLoading(10)  // 下载前的延迟时间
                        .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true) // default  设置下载的图片是否缓存在SD卡中
                        //.preProcessor(...)
                        //.postProcessor(...)
                        //.extraForDownloader(...)
                        .considerExifParams(false) // default
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
                        //.decodingOptions(...)  // 图片的解码设置
                        //.displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                        //.handler(new Handler()) // default
                        .build();

                options_long_with_text = new DisplayImageOptions.Builder()
                        //.showImageOnLoading(R.drawable.loading_animation) // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.default_long) // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.default_long) // 设置图片加载或解码过程中发生错误显示的图片
                        //.resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                        //.delayBeforeLoading(10)  // 下载前的延迟时间
                        .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true) // default  设置下载的图片是否缓存在SD卡中
                        //.preProcessor(...)
                        //.postProcessor(...)
                        //.extraForDownloader(...)
                        .considerExifParams(false) // default
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
                        //.decodingOptions(...)  // 图片的解码设置
                        //.displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                        //.handler(new Handler()) // default
                        .build();

                options_head = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.img_portrait)			// 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.img_portrait)	// 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.img_portrait)		// 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(true)						// 设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true)							// 设置下载的图片是否缓存在SD卡中
                        //.displayer(new RoundedBitmapDisplayer(30))	// 设置成圆角图片
                        .build();

                options = new DisplayImageOptions.Builder()
                        //.showImageOnLoading(R.drawable.loading_animation) // 设置图片下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.default_activity) // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.default_activity) // 设置图片加载或解码过程中发生错误显示的图片
                        //.resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                        //.delayBeforeLoading(10)  // 下载前的延迟时间
                        .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true) // default  设置下载的图片是否缓存在SD卡中
                        //.preProcessor(...)
                        //.postProcessor(...)
                        //.extraForDownloader(...)
                        .considerExifParams(false) // default
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
                        //.decodingOptions(...)  // 图片的解码设置
                        //.displayer(new SimpleBitmapDisplayer()) // default  还可以设置圆角图片new RoundedBitmapDisplayer(20)
                        //.handler(new Handler()) // default
                        .build();

                options_image = new DisplayImageOptions.Builder()
                        .showImageForEmptyUri(R.drawable.default_image) // 设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.default_image) // 设置图片加载或解码过程中发生错误显示的图片
                        .cacheInMemory(false) // default  设置下载的图片是否缓存在内存中
                        .cacheOnDisk(true) // default  设置下载的图片是否缓存在SD卡中
                        .considerExifParams(false) // default
                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default 设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565) // default 设置图片的解码类型
                        .build();




                initImageLoader(applicationContext);

                //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
                Config.DEBUG = true;
                UMShareAPI.get(applicationContext);

                /***
                 * 初始化定位sdk，建议在Application中创建
                 */
                locationService = new LocationService(getApplicationContext());
                mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
                SDKInitializer.initialize(applicationContext);

                // 即时通讯
//                SmackAndroid.init(applicationContext);

                SpeechUtility.createUtility(applicationContext, "appid=5a2a0662");

                // 忽略URI曝光 否则拍照不好使
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

            }
        }.run();
    }

    public static ImageLoaderConfiguration.Builder imageLoaderConfig;
    public static void initImageLoader(Context context) {
        imageLoaderConfig = new ImageLoaderConfiguration.Builder(context);
        imageLoaderConfig.threadPriority(Thread.NORM_PRIORITY - 2);
        imageLoaderConfig.denyCacheImageMultipleSizesInMemory();
        imageLoaderConfig.tasksProcessingOrder(QueueProcessingType.LIFO);
        imageLoaderConfig.memoryCacheExtraOptions(500, 500);
        imageLoaderConfig.writeDebugLogs();
        imageLoaderConfig.diskCache(new UnlimitedDiskCache(DataCleanManager.cacheDir));//自定义缓存路径
        ImageLoader.getInstance().init(imageLoaderConfig.build());

        /*
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
//                .memoryCacheExtraOptions(480, 800) // maxwidth, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
//                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCacheFileCount(100) //缓存的文件数量
                .defaultDisplayImageOptions(FarmingApp.options)
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() // Remove for releaseapp
                .build();//开始构建
        ImageLoader.getInstance().init(config);
        */
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        PlatformConfig.setQQZone("1106516061", "ruVtenRpZwdxizs8");
        PlatformConfig.setWeixin("wx49d64bc041fbd908", "3f7b907499c8288f2017321b7173da94");
        //PlatformConfig.setAlipay("2015111700822536");
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("2917603303", "3effe5f14fb286dbf12c9eca5f65b10e","http://sns.whalecloud.com/sina2/callback");
    }
}