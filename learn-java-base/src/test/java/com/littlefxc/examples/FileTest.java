package com.littlefxc.examples;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author fengxuechao
 * @date 2019/1/4
 **/
public class FileTest {

    String projectPath = System.getProperty("user.dir");

    /**
     * 创建文件
     */
    @Test
    public void createNewFile() {
        try {
            File file = new File(projectPath + File.separator + "test.txt");
            boolean newFile = file.createNewFile();
            System.out.println("create file success? " + newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     */
    @Test
    public void delete() {
        File file = new File(projectPath + File.separator + "test.txt");
        if (file.exists()) {
            boolean delete = file.delete();
            System.out.println("delete file success? " + delete);
        }
    }

    /**
     * 创建文件夹
     */
    @Test
    public void createFileDir() {
        String builder = projectPath +
                File.separator + "src" +
                File.separator + "test" +
                File.separator + "resources";
        File file = new File(builder);
        if (file.exists() && file.isDirectory()) {
            boolean delete = file.delete();
            System.out.println("delete file success? " + delete);
        }
        boolean mkdir = file.mkdir();
        System.out.println("create folder success? " + mkdir);
        File[] files = file.getParentFile().listFiles();
        assert files != null;
        Arrays.asList(files).forEach(System.out::println);
    }
}
