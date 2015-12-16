package com.triskelapps.etherchusky.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Util {

    public static final String EXTRA_TEXT = "ExtraText";
    public static final String EXTRA_ID = "ExtraId";
    public static final String EXTRA_INT = "ExtraInt";
    public static final String EXTRA_BOOLEAN = "ExtraBoolean";
    public static final String EXTRA_DOUBLE = "ExtraDouble";
    public static final String EXTRA_LONG = "ExtraLong";
    public static final String EXTRA_TEXT_ARRAY = "ExtraTextArray";
    public static final String EXTRA_INT_ARRAY = "ExtraIntArray";
    public static final String EXTRA_TYPE = "ExtraType";
    public static final String EXTRA_OBJECT = "ExtraObject";

    public static String getFechaHora() {

	// Esta es la forma de obtener un DATETIME con este formato:
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	return format.format(new Date());
    }
    

    public static int getPixels(Context contexto, int dipValue) {
	Resources r = contexto.getResources();
	int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		dipValue, r.getDisplayMetrics());
	return px;
    }

    public static boolean isConnected(Context context) {
	ConnectivityManager cm = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo network = cm.getActiveNetworkInfo();
	if (network != null) {
	    return network.isAvailable();
	}
	return false;

    }

    @SuppressLint("NewApi")
    public static int[] getDisplayDimensions(Context context) {

	// Fuente: http://stackoverflow.com/q/14341041/1365440

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	    Point size = new Point();
	    try {
		WindowManager wm = (WindowManager) context
			.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getSize(size);

		// O tambien:
		// ((Activity)context).getWindowManager().getDefaultDisplay().getSize(size);

		int width = size.x;
		int height = size.y;

		return new int[] { width, height };

	    } catch (NoSuchMethodError e) {
		Log.i("error", "it can't work");
	    }

	} else {
	    DisplayMetrics metrics = new DisplayMetrics();
	    WindowManager wm = (WindowManager) context
		    .getSystemService(Context.WINDOW_SERVICE);
	    wm.getDefaultDisplay().getMetrics(metrics);
	    int width = metrics.widthPixels;
	    int height = metrics.heightPixels;

	    return new int[] { width, height };
	}

	return new int[] { 0, 0 };

    }

}
