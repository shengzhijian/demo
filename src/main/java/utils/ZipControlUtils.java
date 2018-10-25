package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip文件压缩与解压缩工具
 */
public class ZipControlUtils {
    /*缓冲器大小*/
   private static final int BUFFER=512;

    /**
     * 得到给定源目录下的所有文件及空的子目录
     * @param srcFile
     * @return
     */
   private static List<File> getAllFiles(File srcFile){
       List<File> fileList=new ArrayList <File>();
       //获取该文件目录下的所有文件
       File[] files=srcFile.listFiles();
       for (int i=0;i<files.length;i++){
           //如果是文件就放到结果集中
           if (files[i].isFile()){
               fileList.add(files[i]);
           }
           //如果是目录，用递归的方式继续判断，直到将所有的文件都放到结果集中
           if (files[i].isDirectory()){
               if (files[i].listFiles().length!=0){
                   fileList.addAll(getAllFiles(files[i]));
               }else{
                   fileList.add(files[i]);
               }
           }
       }
       return fileList;
   }

    /**
     * 获取相对路径
     * 依据文件名和压缩源路径得到文件在压缩源路径下的相对路径
     * @param dirPath 压缩源路径
     * @param file 需要获取相对路径的文件对象
     * @return
     */
   private static String getRelativePath(String dirPath,File file){
       //压缩源文件的文件对象
       File dir=new File(dirPath);
       //获取需要获取相对路径的文件名
       String relativePath=file.getName();
       while (true){
           //获取请求文件的父文件
           file=file.getParentFile();
           //如果没有父文件，跳出循环
           if (file==null){
               break;
           }
           //如果父文件就是压缩原文件，跳出循环
           if (file.equals(dir)){
               break;
           }else {//将父文件的文件名拼接到已有路径的前面
               relativePath=file.getName()+"/"+relativePath;
           }
       }
       return relativePath;
   }

    /**
     * 创建文件
     * 根据压缩包内文件名和解压缩目的路经，创建解压缩目标文件
     * 生成中间目录
     * @param dstPath 解压缩目的路经
     * @param fileName 压缩包内文件名
     * @return
     */
   private static File createFile(String dstPath,String fileName){
       //将文件名的各级目录分解
       String[] dirs=fileName.split("/");
       //获取解压缩文件的目标路经
       File file=new File(dstPath);
       //如果dirs数组长度>1,证明该文件有父文件夹
       if (dirs.length>1){
           for (int i=0;i<dirs.length-1;i++){
               file=new File(file,dirs[i]);
           }
           if (!file.exists()){
               file.mkdirs();
           }
           file=new File(file,dirs[dirs.length-1]);
           return file;
       }else{
           if (!file.exists()){
               file.mkdirs();
           }
           file=new File(file,dirs[0]);
           return file;
       }
   }

    /**
     * 解压缩
     * @param zipFileName 压缩文件名
     * @param dstPath 压缩目标路经
     * @return
     */
   public  static boolean unzip(String zipFileName,String dstPath)  {

       try {
           ZipInputStream zipInputStream= new ZipInputStream(new FileInputStream(zipFileName));
           ZipEntry zipEntry=null;
           byte[] buffer=new byte[BUFFER];
           int readLength=0;
           while ((zipEntry=zipInputStream.getNextEntry())!=null){
               if (zipEntry.isDirectory()){
                   File dir=new File(dstPath+"/"+zipEntry.getName());
                   if (!dir.exists()){
                       dir.mkdirs();
                       continue;
                   }
               }
               //如果时文件，需要创建该文件
               File file=createFile(dstPath,zipEntry.getName());
               OutputStream outputStream=new FileOutputStream(file);
               while ((readLength=zipInputStream.read(buffer,0,BUFFER))!=-1){
                   outputStream.write(buffer,0,readLength);
               }
               outputStream.close();

           }
       } catch (FileNotFoundException e) {
           e.printStackTrace();
           return false;
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
        return true;
   }

    /**
     * 压缩文件的方法
     * @param srcPath 压缩源路经
     * @param zipFileName 目标压缩文件
     * @return
     */
   public static boolean zip(String srcPath,String zipFileName){
       File srcFile=new File(srcPath);
       List<File> fileList=getAllFiles(srcFile);//获取所有要压缩的文件对象
       byte[] buffer=new byte[BUFFER];//缓冲器
       ZipEntry zipEntry=null;
       int readLength=0;
       try {
           ZipOutputStream zipOutputStream=new ZipOutputStream(new FileOutputStream(zipFileName));
           for (File file:fileList){
               if (file.isFile()){
                   zipEntry=new ZipEntry(getRelativePath(srcPath,file));
                   zipEntry.setSize(file.length());
                   zipEntry.setTime(file.lastModified());
                   zipOutputStream.putNextEntry(zipEntry);
                   InputStream inputStream=new BufferedInputStream(new FileInputStream(file));
                   while ((readLength=inputStream.read(buffer,0,BUFFER))!=-1){
                       zipOutputStream.write(buffer,0,readLength);
                   }
                   inputStream.close();
               }else {
                   zipEntry=new ZipEntry(getRelativePath(srcPath,file)+"/");
                   zipOutputStream.putNextEntry(zipEntry);
               }
           }
       } catch (FileNotFoundException e) {
           e.printStackTrace();
           return false;
       } catch (IOException e) {
           e.printStackTrace();
           return false;
       }
       return true;
   }
}
