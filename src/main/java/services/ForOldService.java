package services;

import java.io.File;
import java.util.LinkedList;

public interface ForOldService {
    public LinkedList<String> getListOfFails();
    public File downloadFile(File dstFile);
    public void deleteFile(String nameFail);
}
