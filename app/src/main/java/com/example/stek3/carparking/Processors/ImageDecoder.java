package com.example.stek3.carparking.Processors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.provider.ContactsContract;
import android.util.Base64;

import java.io.ByteArrayOutputStream;


/**
 * Created by Stek3 on 09-Apr-18.
 */

public class ImageDecoder {

    public Bitmap DecodeImage(String ImageString){

        byte[] ImageBytes= Base64.decode(ImageString,Base64.DEFAULT);

        Bitmap bm= BitmapFactory.decodeByteArray(ImageBytes,0,ImageBytes.length);

        return bm;
}

    public String EncodeImage(Bitmap Image){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();


        return  Base64.encodeToString(byteArray,0);
    }


}
