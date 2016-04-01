package com.fupinyou.shanci.shanciapp;

import android.util.Log;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
/**
 * Created by fupinyou on 2016/3/31.
 */
public class SSFTPsync {
    public static Vector vector;
    public static String[] strings;
    private static String ip="42.96.186.133";
    private static String user="root";
    private static String psw="cOSv3572";
    private static int port=-1;
    private static String remoteDirectory="/home/fupinyou";
    private static String Remotefilename;
    public static void connectSftp() throws Exception{
        Session session = null;
        Channel channel = null;

        JSch jsch = new JSch();


        if(port <=0){
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;


            //进入服务器指定的文件夹
            sftp.cd(remoteDirectory);

            //列出服务器指定的文件列表
            vector = sftp.ls("*.*");
            strings=new String[vector.size()];
            for(int i=0;i<vector.size();i++){
                strings[i]=((ChannelSftp.LsEntry)(vector.get(i))).getFilename();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }

    public static void sshSftp(String localFilePath,String Remotefilename) throws Exception{
        Session session = null;
        Channel channel = null;


        JSch jsch = new JSch();


        if(port <=0){
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;


            //进入服务器指定的文件夹
            sftp.cd(remoteDirectory);

            //列出服务器指定的文件列表
            //vector = sftp.ls("*.*");
          /*  for(int i=0;i<v.size();i++){
                System.out.println(v.get(i));
            }*/

            //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
            OutputStream outstream = sftp.put(Remotefilename);
            InputStream instream = new FileInputStream(new File(localFilePath));

            byte b[] = new byte[1024];
            int n;
            while ((n = instream.read(b)) != -1) {
                outstream.write(b, 0, n);
            }

            outstream.flush();
            outstream.close();
            instream.close();
            Log.d("SSHSYNC","sync succ");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }
    public static void sshSftpDOWN( String localFilePath,String Remotefilename) throws Exception{
        Session session = null;
        Channel channel = null;


        JSch jsch = new JSch();


        if(port <=0){
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;


            //进入服务器指定的文件夹
            sftp.cd(remoteDirectory);

            //列出服务器指定的文件列表
//            Vector v = sftp.ls("*.txt");
//            for(int i=0;i<v.size();i++){
//                System.out.println(v.get(i));
//            }
            File fi=new File(localFilePath);

            vector = sftp.ls("*.*");
            //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
            InputStream instream = sftp.get(Remotefilename);
            OutputStream outstream= new FileOutputStream(fi);
            //InputStream instream = new FileInputStream(new File(localFilePath));

            byte b[] = new byte[1024];
            int n;
            while ((n = instream.read(b)) != -1) {
                outstream.write(b, 0, n);
            }

            outstream.flush();
            outstream.close();
            instream.close();
            Log.d("SSHSYNC","download sync succ");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }
}
