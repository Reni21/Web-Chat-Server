package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {
        return URL + ":" + PORT;
    }

    public static <T> String toJSON(T object) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(object);
    }

    public static byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int wasRead;

        do {
            wasRead = is.read(buf);
            if (wasRead > 0) bos.write(buf, 0, wasRead);
        } while (wasRead != -1);

        return bos.toByteArray();
    }

    public static <T> T fromJSON(String s, Class<T> clazz) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(s, clazz);
    }
}
