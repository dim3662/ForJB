import manager.FileManagerForNew;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import static javax.script.ScriptEngine.FILENAME;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Tests {
    /*Так как старый сервис заполнен и нужен нам для выполнения задания,
    то тестировать методы будет на новом сервисе,
    так как методы всеравно одинаковые за счет наследования*/

    @Test
    public void testGetListOfFails() {
        URL newUrl = null;
        try {
            newUrl = new URL("http://localhost:8080/newStorage/files");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FileManagerForNew fileManagerForNew = new FileManagerForNew(newUrl);

        LinkedList<String> list3 = fileManagerForNew.getListOfFails();
        list3.forEach((s) -> fileManagerForNew.deleteFile(s));

        LinkedList<String> list = new LinkedList<>();
        list.add("test.txt");
        list.add("test1.txt");
        list.add("test2.txt");
        list.forEach((s) -> {
            try {
                new File(s).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Assert.assertNotEquals(fileManagerForNew.getListOfFails().size(), list.size());
        Assert.assertNotEquals(fileManagerForNew.getListOfFails(), list);

        list.forEach((s) -> fileManagerForNew.addFile(new File(s)));

        Assert.assertEquals(fileManagerForNew.getListOfFails().size(), list.size());
        Assert.assertEquals(fileManagerForNew.getListOfFails(), list);
    }


    @Test
    public void testDownloadFile() throws IOException {
        URL newUrl = null;
        try {
            newUrl = new URL("http://localhost:8080/newStorage/files");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FileManagerForNew fileManagerForNew = new FileManagerForNew(newUrl);

        LinkedList<String> list3 = fileManagerForNew.getListOfFails();
        list3.forEach((s) -> fileManagerForNew.deleteFile(s));

        LinkedList<String> list = new LinkedList<>();
        list.add("test.txt");
        list.forEach((s) -> {
            try {
                new File(s).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        String text = "";
        try (FileWriter writer = new FileWriter("test.txt", false)) {
            text = "This my test!";
            writer.write(text);
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        list.forEach((s) -> fileManagerForNew.addFile(new File(s)));
        new File("test.txt").delete();
        fileManagerForNew.downloadFile(new File("test.txt"));
        BufferedReader br = new BufferedReader(new FileReader("test.txt"));
        Assert.assertEquals(br.readLine(), text);
    }

    @Test
    public void testDeleteFile() throws IOException {
        URL newUrl = null;
        try {
            newUrl = new URL("http://localhost:8080/newStorage/files");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FileManagerForNew fileManagerForNew = new FileManagerForNew(newUrl);

        LinkedList<String> list3 = fileManagerForNew.getListOfFails();
        list3.forEach((s) -> fileManagerForNew.deleteFile(s));

        LinkedList<String> list = new LinkedList<>();
        list.add("test.txt");
        list.forEach((s) -> {
            try {
                new File(s).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        list.forEach((s) -> fileManagerForNew.addFile(new File(s)));

        Assert.assertEquals(fileManagerForNew.getListOfFails().size(), list.size());
        Assert.assertEquals(fileManagerForNew.getListOfFails(), list);
        Assert.assertTrue(fileManagerForNew.getListOfFails().contains("test.txt"));
        fileManagerForNew.deleteFile("test.txt");
        Assert.assertEquals(fileManagerForNew.getListOfFails().size(), 0);
        Assert.assertFalse(fileManagerForNew.getListOfFails().contains("test.txt"));
    }

    @Test
    public void testAddFile() throws IOException {
        URL newUrl = null;
        try {
            newUrl = new URL("http://localhost:8080/newStorage/files");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FileManagerForNew fileManagerForNew = new FileManagerForNew(newUrl);

        LinkedList<String> list3 = fileManagerForNew.getListOfFails();
        list3.forEach((s) -> fileManagerForNew.deleteFile(s));

        LinkedList<String> list = new LinkedList<>();
        list.add("test.txt");
        list.forEach((s) -> {
            try {
                new File(s).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Assert.assertEquals(fileManagerForNew.getListOfFails().size(), 0);
        Assert.assertNotEquals(fileManagerForNew.getListOfFails(), list);
        Assert.assertFalse(fileManagerForNew.getListOfFails().contains("test.txt"));
        list.forEach((s) -> fileManagerForNew.addFile(new File(s)));

        Assert.assertEquals(fileManagerForNew.getListOfFails().size(), list.size());
        Assert.assertEquals(fileManagerForNew.getListOfFails(), list);
        Assert.assertTrue(fileManagerForNew.getListOfFails().contains("test.txt"));
    }

}
