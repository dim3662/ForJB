package manager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.ls.LSOutput;
import services.ForNewService;

import java.net.*;

import java.io.*;
import java.nio.file.Files;

public class FileManagerForNew extends FileManagerForOld implements ForNewService {
    public FileManagerForNew(URL url) {
        super(url);
    }


    public void addFile(File file) {
        OutputStream out = null;
        try {
            HttpPost post = null;
            HttpURLConnection connection = null;

            connection = (HttpURLConnection) url.openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            FileBody fileBody = new FileBody(file);
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
            multipartEntity.addPart("file", fileBody);

            connection.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
            out = connection.getOutputStream();
            multipartEntity.writeTo(out);
            int status = connection.getResponseCode();
            while (status != 200 && !getListOfFails().contains(file.getName())) {
                out.close();
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                 fileBody = new FileBody(file);
                 multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
                multipartEntity.addPart("file", fileBody);

                connection.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
                out = connection.getOutputStream();
                multipartEntity.writeTo(out);
                status = connection.getResponseCode();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // add request parameters or form parameters


    }
}
