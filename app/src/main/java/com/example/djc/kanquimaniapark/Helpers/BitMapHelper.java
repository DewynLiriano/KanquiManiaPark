package com.example.djc.kanquimaniapark.Helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by dewyn on 27/03/16. :D
 */
public abstract class BitMapHelper {
    // convierte de bitmap a arreglo de bytes.
    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    // convierte de arreglo de bytes a bitmap.
    public static Bitmap getImage(byte[] image)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
