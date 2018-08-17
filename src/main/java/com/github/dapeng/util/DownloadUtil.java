package com.github.dapeng.util;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * DownloadUtil
 * @author struy
 */
public  class DownloadUtil {

    /**
     *
     * @param filePath
     * @param response
     * @param isOnLine
     * @throws Exception
     */
    public static void downLoad(String filePath, HttpServletResponse response,
                         boolean isOnLine) throws Exception {
        File f = new File(filePath);
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len;
        response.reset();
        // 在线打开
        if (isOnLine) {
            URL u = new URL("file:///"+filePath);
            response.setContentType(u.openConnection().getContentType());
            response.setHeader("Content-Disposition", "inline; filename="
                    + Tools.toUTF8(f.getName()));
        }
        // 下载
        else {
            // safari 下载会自动带上exe后缀
            //response.setContentType("application/x-msdownload");
            response.setContentType("applicatoin/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + Tools.toUTF8(f.getName()));
        }
        OutputStream out = response.getOutputStream();
        while ((len = br.read(buf)) > 0)
            out.write(buf, 0, len);
        out.flush();
        br.close();
        out.close();
    }

}
