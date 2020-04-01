package result;

import manager.FileManagerForNew;
import manager.FileManagerForOld;

import java.io.File;
import java.net.URL;

public class ReWriterManager {
    private URL oldUrl;
    private URL newUrl;
    private FileManagerForNew fileManagerForNew;
    private FileManagerForOld fileManagerForOld;
public static int kol=0;
    public ReWriterManager(URL oldUrl, URL newUrl) {
        this.fileManagerForNew = new FileManagerForNew(newUrl);
        this.fileManagerForOld = new FileManagerForOld(oldUrl);
    }

    public FileManagerForNew getFileManagerForNew() {
        return fileManagerForNew;
    }

    public FileManagerForOld getFileManagerForOld() {
        return fileManagerForOld;
    }


    public void rewriteFile(File file) {
        fileManagerForNew.addFile(fileManagerForOld.downloadFile(file));
        fileManagerForOld.deleteFile(file.getName());
        file.delete();
        System.out.println(kol++ +"files rewrite");
    }
}
