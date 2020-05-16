package com.example.pin.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * 要写注释呀
 */
public class BinarySaver {
    public final static String url = "https://kxg-neituibao-jianli.oss-cn-beijing.aliyuncs.com/data/1589622579751.jpg";

    private final static String filePath="/Users/mac/Desktop/"+ UUID.randomUUID().toString()+".jpg";
    public static void main(String[] args) {
        try {
            URL root = new URL(url);
            saveBinary(root);
        } catch (MalformedURLException e) {
            // TODO: handle exception
            System.out.println(url + "is not URL");
        } catch (IOException e) {
            // TODO: handle exception
            System.out.println(e);
        }
    }

    public static void saveBinary(URL u) throws IOException {

        // TODO Auto-generated method stub
        URLConnection uc = u.openConnection();
        String contentType = uc.getContentType();
        int contentLength = uc.getContentLength();
        /*
         * 可以限制不下载哪种文本文件
        if (contentType.startsWith("text/") || contentLength == -1) {
            throw new IOException("This is not a binary file.");
        }*/

        try (InputStream raw = uc.getInputStream()) {
            InputStream in = new BufferedInputStream(raw);
            byte[] data = new byte[contentLength];
            int offset = 0;
            while (offset < contentLength) {
                int bytesRead = in.read(data, offset, data.length - offset);
                if (bytesRead == -1) {
                    break;
                }
                offset += bytesRead;
            }

            if (offset != contentLength) {
                throw new IOException("Only read " + offset
                        + " bytes; Expected " + contentLength + " bytes");
            }
            String filename = filePath;
            try (FileOutputStream fout = new FileOutputStream(filename)) {
                fout.write(data);
                fout.flush();
            }
        }
    }

}
