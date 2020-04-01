package manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import services.ForOldService;

import java.io.*;
import java.util.LinkedList;

public class FileManagerForOld implements ForOldService {
    protected URL url;

    public FileManagerForOld(URL url) {
        this.url = url;
    }


    public LinkedList<String> getListOfFails() {
        LinkedList<String> listFiles = new LinkedList<String>();
        URL currentUrl = url;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response;
            HttpGet getRequest = new HttpGet(
                    currentUrl.toURI());
            getRequest.addHeader("accept", "application/json");
            response = httpClient.execute(getRequest);
            while (response.getStatusLine().getStatusCode() != 200) {
                httpClient = new DefaultHttpClient();
                getRequest = new HttpGet(
                        currentUrl.toURI());
                getRequest.addHeader("accept", "application/json");
                response = httpClient.execute(getRequest);
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            output = br.readLine();
            output = output.substring(1, output.length() - 1);
            if (!output.equals(""))
                for (String str : output.split(",")) {
                    listFiles.add(str.substring(1, str.length() - 1));
                }
            httpClient.getConnectionManager().shutdown();

        }catch (Exception e) {
            System.out.println("You have a problem with your server connection");
        }
        return listFiles;
    }

    public File downloadFile(File dstFile) {
        CloseableHttpClient httpclient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods
                .build();
        File downloaded = null;
        int response =0;
        URL currentUrl;
        try {
            if (getListOfFails().contains(dstFile.getName())) {
                while (response != 200) {
                    currentUrl = new URL((url.toString().concat("/" + dstFile.getName())));
                    HttpGet get = new HttpGet(currentUrl.toURI()); // we're using GET but it could be via POST as well
                    downloaded = httpclient.execute(get, new FileDownloadResponseHandler(dstFile));
                    response=FileDownloadResponseHandler.responseCod;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return downloaded;
    }

    static class FileDownloadResponseHandler implements ResponseHandler<File> {

        private final File target;
public static int responseCod=0;
        public FileDownloadResponseHandler(File target) {
            this.target = target;
        }

        @Override
        public File handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            InputStream source = response.getEntity().getContent();
            responseCod=response.getStatusLine().getStatusCode();
            FileUtils.copyInputStreamToFile(source, this.target);
            return this.target;
        }

    }

    public void deleteFile(String nameFail) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response;
            URL currentUrl = url;
            currentUrl = new URL((url.toString().concat("/" + nameFail)));
            HttpDelete deleteRequest = new HttpDelete(
                    currentUrl.toURI());
            response = httpClient.execute(deleteRequest);
            while (response.getStatusLine().getStatusCode() != 200 && getListOfFails().contains(nameFail)) {
                httpClient = new DefaultHttpClient();
                deleteRequest = new HttpDelete(
                        currentUrl.toURI());
                response = httpClient.execute(deleteRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}