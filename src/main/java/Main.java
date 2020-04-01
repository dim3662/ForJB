import manager.FileManagerForNew;
import manager.FileManagerForOld;
import result.ReWriterManager;
import services.ForNewService;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        try {
            URL oldUrl = new URL("http://localhost:8080/oldStorage/files");
            URL newUrl = new URL("http://localhost:8080/newStorage/files");

            ReWriterManager reWriterManager = new ReWriterManager(oldUrl, newUrl);

            LinkedList<String> list = reWriterManager.getFileManagerForOld().getListOfFails();
            list.forEach((s) -> reWriterManager.rewriteFile(new File(s)));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
