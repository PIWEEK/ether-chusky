package com.triskelapps.etherchusky.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class BitmapUtils {

    public static Bitmap getRotatedBitmap(String imagePath) {

	/*
	 * Para limitar el tamaño, descomentar lineas comentadas y comentar:
	 * BitmapFactory.decodeFile(rutaImagen);
	 */

	// final int WIDTH_OUTPUT = 700;
	// final int HEIGHT_OUTPUT = 500;

	int orientation1 = 1;
	int rotation1 = 0;
	try {
	    ExifInterface exif = new ExifInterface(imagePath);
	    orientation1 = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
		    1);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	switch (orientation1) {
	case 3:
	    rotation1 = 180;
	    break;
	case 6:
	    rotation1 = 90;
	    break;
	case 8:
	    rotation1 = 270;
	    break;
	}

	try {

	    // Bitmap foto1 = decodeSampledBitmap(rutaImagen, WIDTH_SALIDA,
	    // HEIGHT_SALIDA);
	    // float aspectRatio = (float)foto1.getWidth() /
	    // (float)foto1.getHeight();
	    // float scaleX = (float)WIDTH_SALIDA / (float)foto1.getWidth();
	    // float scaleY = ((float)WIDTH_SALIDA / aspectRatio) /
	    // (float)foto1.getHeight();

	    Bitmap bm1 = BitmapFactory.decodeFile(imagePath);

	    Matrix matrix1 = new Matrix();
	    matrix1.postRotate(rotation1);
	    // if(foto1.getWidth() > WIDTH_SALIDA)
	    // matrix1.postScale(scaleX, scaleY);

	    return Bitmap.createBitmap(bm1, 0, 0, bm1.getWidth(),
		    bm1.getHeight(), matrix1, false);

	} catch (OutOfMemoryError e) {
	    e.printStackTrace();
	}

	return null;
    }

    /*
     * Las siguientes dos funciones evitan un OutOfMemoryError al decodificar
     * una imagen 1- Calcula su tamaño antes de procesar 2- Si es mas grande que
     * el reqWidth o reqHeight, calcula el divisor inSampleSize 3- Decodifica
     * con ese tamaño.
     */
    public static Bitmap decodeSampledBitmap(String rutaImagen, int reqWidth,
	    int reqHeight) {

	// First decode with inJustDecodeBounds=true to check dimensions
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeFile(rutaImagen, options);

	// Calculate inSampleSize
	options.inSampleSize = calculateInSampleSize(options, reqWidth,
		reqHeight);

	// Decode bitmap with inSampleSize set
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeFile(rutaImagen, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
	    int reqWidth, int reqHeight) {
	// Raw height and width of image
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;

	if (height > reqHeight || width > reqWidth) {

	    // Calculate ratios of height and width to requested height and
	    // width
	    final int heightRatio = Math.round((float) height
		    / (float) reqHeight);
	    final int widthRatio = Math.round((float) width / (float) reqWidth);

	    // Choose the smallest ratio as inSampleSize value, this will
	    // guarantee
	    // a final image with both dimensions larger than or equal to the
	    // requested height and width.
	    inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	}

	return inSampleSize;
    }


}
