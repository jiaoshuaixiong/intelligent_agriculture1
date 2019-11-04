package com.topwulian.utils;

import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParseBase64ImgUtil {

    private ParseBase64ImgUtil(){}

    public static InputStream parse(String img) throws IOException {
        String imageFile = img.replaceFirst(".*;base64,", "");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes1 = decoder.decodeBuffer(imageFile);
        return new ByteArrayInputStream(bytes1);
    }

}
