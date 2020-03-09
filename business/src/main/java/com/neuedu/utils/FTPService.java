package com.neuedu.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.net.ftp.FtpClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class FTPService {
    @Value("${ftp.host}")
    private String ftpIp;
    @Value("${ftp.user}")
    private String ftpUser;
    @Value("${ftp.pass}")
    private String ftpPass;

    FTPClient ftpClient = null;


    //1.连接到ftp服务器
    public boolean connectFTPServer(){
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpIp);
            ftpClient.login(ftpUser,ftpPass);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("========FTP connect error========");
        }
        return false;
    }

    //2.文件上传

    public boolean uploadFile(List<File> fileList){
        return uploadFile("img",fileList);
    }

    public boolean uploadFile(String remotePath,List<File> fileList){

        //切换工作目录
        FileInputStream fileInputStream = null;
        try {
            if (connectFTPServer()){//先登录ftp
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开被动传输模式

                for (File file:fileList){
                    fileInputStream = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),fileInputStream);
                }
                System.out.println("====ftp upload success====");
                return true;
            }else {
                System.out.println("====login ftp fail====");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("====ftp upload error====");
        }finally {
            try {
                if (fileInputStream!=null){
                    fileInputStream.close();
                }
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }







}
