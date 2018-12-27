package utils;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.jeecgframework.poi.excel.annotation.Excel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 数据库数据导入Excel和Excel导入数据库的相关方法。
 *
 数据导出Excel的逻辑（用到@Excel注解，用来映射表头名与实体类的参数名）
 1、两个映射
 ①@Excel注解的表头名与参数名映射
 ②参数名与参数的类型对应(用于约定get方法的返回值类型，便于调用poi的赋值方法给单元格赋值)
 2、将要写入Excel的数据写入Excel中
 3、调用浏览器的下载命令

 */
public class ExcelControlUtils {


    /**
     *
     * @param request
     * @param response
     * @param nameSheet Excel中的sheet名
     * @param fileName 想要命名的文件名
     * @param list
     * @param <T>
     * @throws IOException
     */
    public <T> void downLoagExcel(HttpServletRequest request, HttpServletResponse response,
                              String nameSheet,String fileName,List<T> list) throws IOException {
        T t=list.get(0);
        Map<String,String> mapAnno=dramAndAnnotate(t);
        Map<String,Class> mapType=nameAndtype(t);
        File file=loadExcel(mapAnno,mapType,list,nameSheet,fileName);
        InputStream instream=new FileInputStream(file);
        downloadLocal(request,response,fileName,instream);
        file.delete();
    }
    /**
     * 将实体类的集合写入到Excel文件中
     * @param mapAnnot 实体类中@Excel注解名与参数名对应的map
     * @param mapType 实体类中参数名与参数类型对应的map
     * @param list 实体类对象的集合
     * @param nameSheet 导出的Excel的sheet名
     * @param fileName 导出的文件名
     * @param <T> 泛型
     * @throws IOException
     */
    public <T> File loadExcel(Map<String,String> mapAnnot,Map<String,Class> mapType, List<T> list,String nameSheet,String fileName) throws IOException {
        URL url=this.getClass().getResource("/");
        String path=url.getPath()+"/"+fileName;
        File filenew=new File(path);
        if (!filenew.exists()){
            filenew.createNewFile();
        }
        //生成一个workbook对象
        HSSFWorkbook workbook=new HSSFWorkbook();
        //创建表对象
        HSSFSheet sheet=workbook.createSheet(nameSheet);
        HSSFRow row=sheet.createRow(0);
        int index=0;
        for(String key:mapAnnot.keySet()){
            HSSFCell cell=row.createCell(index);
            cell.setCellValue(key);
            index++;
        }
        for(int i=0;i<list.size();i++){
            HSSFRow row2 =sheet.createRow(i+1);
            for(int j=0;j<mapAnnot.keySet().size();j++){
                String key=row.getCell(j).getStringCellValue();
                String name=mapAnnot.get(key);
                String methodName="get"+name.substring(0,1).toUpperCase()+name.substring(1);
                try {
                    Method method=list.get(i).getClass().getMethod(methodName,null);
                    HSSFCell cell2=row2.createCell(j);
                    Object object=method.invoke(list.get(i),null);
                    //cell2.setCellValue((Boolean) mapType.get(name).cast(object));
                    if (mapType.get(name).equals(Double.class)){
                        cell2.setCellValue((Double) method.invoke(list.get(i),null));
                    }else if (String.class.equals(mapType.get(name))){
                        cell2.setCellValue((String) method.invoke(list.get(i),null));
                    }else if (Date.class.equals(mapType.get(name))){
                        cell2.setCellValue((Date) method.invoke(list.get(i),null));
                    }else if (Calendar.class.equals(mapType.get(name))){
                        cell2.setCellValue((Calendar) method.invoke(list.get(i),null));
                    }else if (RichTextString.class.equals(mapType.get(name))){
                        cell2.setCellValue((RichTextString) method.invoke(list.get(i),null));
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
        FileOutputStream out =new FileOutputStream(path);
        workbook.write(out);
        out.close();
        return new File(path);
    }
    public <T> Map<String,String> dramAndAnnotate(T t){
        Map<String,String> map=new HashMap<String,String>();
        Class clazz=t.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields=clazz.getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            //获取实体类的属性名
            String name=fields[i].getName();
            //判断该实体类属性上是否有Excel注解
            boolean fieldHasAnno = fields[i].isAnnotationPresent(Excel.class);
            String annoName=null;
            if(fieldHasAnno){
                //获取注解对象
                Excel fieldAnno = fields[i].getAnnotation(Excel.class);
                //输出注解属性
                annoName = fieldAnno.name();
            }
            if (annoName==null){
                continue;
            }
            map.put(annoName,name);
        }
        return map;
    }
    //将属性名与属性类型对应，放到map中（key为属性名，value为属性值）
    public  <T> Map<String,Class> nameAndtype(T t){
        Map<String,Class> paramType=new HashMap<String,Class>();
        Class clazz=t.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields=clazz.getDeclaredFields();
        for (int i=0;i<fields.length;i++){
            //获取属性的名称
            String name=fields[i].getName();
            //获取属性值类型
            Class type=fields[i].getType();
            paramType.put(name,type);
        }
        return paramType;
    }
    public void downloadLocal(HttpServletRequest request, HttpServletResponse response, String fileName,
                              InputStream inStream) throws FileNotFoundException {
        try {
            //InputStream inStream = new FileInputStream(pathAndFileName);
            response.reset();
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            setFileDownloadHeader(request, response, fileName);

            byte[] b = new byte[100];
            int len;
            try {
                while ((len = inStream.read(b)) > 0){
                    response.getOutputStream().write(b, 0, len);
                }
            } catch (IOException e) {

            } finally {
                try {
                    inStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            String str = "错误提示：该信息下包含无效的文件路径（无法找到对应文件）";
            response.setContentType("application/octet-stream");
            try {
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(str.getBytes("UTF-8"), "ISO-8859-1"));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            PrintWriter writer = null;
            try {
                writer = response.getWriter();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            writer.print("该信息下文件路径无法找到对应文件");
            writer.flush();
            writer.close();
        }

    }
    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName) {
        String encodedfileName;
        try {
            //中文文件名支持
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0 || request.getHeader("User-Agent").toLowerCase().indexOf("opera") > 0) {
                encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            } else {
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");
            }

            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
        } catch (UnsupportedEncodingException e) {
        }
    }
//=============================以下是Excel文件导入数据库的方法===========================

    /**
     * 将读取的Excel对象中的数据保存到实体类的集合中，便于后期将数据保存到数据库中
     * @param inputStream Excel文件的输入流
     * @param rowNo 第一个有效数据所在行数
     * @param delNO 第一个有效数据所在列号
     * @param clazz 实体类的class对象
     * @param <T>
     * @return
     */
    public <T>List<T> ArrayToEntryList(InputStream inputStream,int rowNo,int delNO, Class<T> clazz){
        List<T> listResult=new ArrayList <T>();
        try {
            //读取输入流中数据并保存到String[]的集合中
            List<String[]> listArray=loadingFile(inputStream,rowNo,delNO);
            //获取表头所在的坐标与参数名对应
            T t=clazz.newInstance();
            String[] top =listArray.get(0);
            Map<Integer,String> mapParam=indexAndParam(top,t);
            for (int i=1;i<listArray.size();i++){
                //将Excel的数据赋值给实体类
                String[] main=listArray.get(i);
                T tMain=clazz.newInstance();
                tMain=ArrayToEntry(top,main,t);
                listResult.add(tMain);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return listResult;
    }
    /**
     * 将正文数组保存为实体类
     * @param top 表头数组
     * @param main 正文数组
     * @param t 实体类
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public <T> T ArrayToEntry(String[] top,String[] main,T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<Integer,String> mapParam=indexAndParam(top,t);
        Map<String,Class> mapType=nameAndtype(t);
        for (int i=0;i<main.length;i++){
            String methodName="set"+mapParam.get(i).substring(0,1).toUpperCase()+mapParam.get(i).substring(1);
            Method method=t.getClass().getMethod(methodName,mapType.get(mapParam.get(i)));
            method.invoke(t,main[i]);
        }
        return t;
    }
    /**
     * 将Excel中的表头在表头元素的数组中的坐标与其所代表的参数名对应
     * @param top 表头元素的数组
     * @param t 实体类
     * @param <T>
     * @return
     */
    public  <T> Map<Integer,String> indexAndParam(String[] top,T t){
        Map<Integer,String> mapParam=new HashMap <Integer, String>();
        Map<String,String> mapAnno=dramAndAnnotate(t);
        for (int i=0;i<top.length;i++){
            String param=mapAnno.get(top[i]);
            mapParam.put(i,param);
        }
        return mapParam;
    }
    /**
     *
     * @param inputStream 文件的输入流
     * @param rowNo 第一个有效数据所在行号
     * @param delNo 第一个有效数据所在的列号
     * @return String[] 的集合，其中第一个是所有表头的集合
     * @throws IOException
     */
    public List<String[]> loadingFile(InputStream inputStream,int rowNo,int delNo) throws IOException {
        Workbook workbook=null;
        try {
            workbook= WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        //默认是第一张Sheet表
        Sheet sheet=workbook.getSheetAt(0);
        //获取总行数
        int trLength=sheet.getLastRowNum();
        //得到Excel工作表的行
    //        HSSFRow row=sheet.getRow(0);
        List<String[]> listValues=new ArrayList<String[]>();
        for(int i=rowNo;i<trLength+1;i++){
            Row row=sheet.getRow(i);
            String[] strs=new String[row.getLastCellNum()];
            for(int j=delNo;j<row.getLastCellNum();j++){
                Cell cell=row.getCell(j);
                String value="";
                if(cell!=null){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    try {
                        value=cell.getStringCellValue();
                    }catch (Exception e){
                        value=cell.getNumericCellValue()+"";
                    }

                }
                strs[j]=value;
            }
            listValues.add(strs);
        }
        return listValues;
    }
}
