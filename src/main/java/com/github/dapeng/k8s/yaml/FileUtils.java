package com.github.dapeng.k8s.yaml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * @author huyj
 * @Created 2019-06-05 09:36
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static String readFromeFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            for (String line : lines) {
                sb.append(line).append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String readFromeFileByline(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
            for (String line : lines) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static String saveFile(String fileName, String content) {
        FileWriter fw = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                // 先得到文件的上级目录，并创建上级目录，在创建文件
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            fw = new FileWriter(file);
            fw.write(content);
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /************* 操作 resource 配置文件********************************************************/
    public static String getResourceFilePath(String fileName) {
        //获取文件路径
        String _fileName = Objects.requireNonNull(FileUtils.class.getClassLoader().getResource(fileName)).getPath();
//        logger.info("加载配置文件地址：" + _fileName);
        //获取文件路径
       /* String _fileName1 = FileUtils.class.getResource(fileName).getFile();
        logger.info("加载配置文件地址：" + _fileName1);*/
        return _fileName;
    }

    public static InputStream getResourceFileInputStream(String fileName) {
        return FileUtils.class.getClassLoader().getResourceAsStream(fileName);
    }

    public static String getResourceFileContext(String fileName) {
        return convertStreamToString(getResourceFileInputStream(fileName));
    }

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
