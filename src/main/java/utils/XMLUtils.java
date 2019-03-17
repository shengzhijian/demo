package utils;

import org.apache.poi.ss.formula.functions.T;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * XML相关操作
 * 2019.3.17
 * shengzhijian
 */
public class XMLUtils {
    /**
     * 将对象转换为XMl字符串
     * @param object
     * @return
     */
    public static String convertToXml(Object object){
        //创建输出流
        StringWriter sw=new StringWriter();
        try {
            JAXBContext context=JAXBContext.newInstance(object.getClass());
            Marshaller marshaller=context.createMarshaller();
            //格式化xml输出格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
            //将对象转换成输出流形式的xml
            marshaller.marshal(object,sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    /**
     * 将xml格式的字符串转换为对象
     * @param clazz
     * @param xmlStr
     * @return
     */
    public static Object convertXmlStrToObject(Class clazz,String xmlStr){
        Object xmlObject=null;
        try {
            JAXBContext context=JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller=context.createUnmarshaller();
            StringReader sr=new StringReader(xmlStr);
            xmlObject=unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }

    /**
     * 将对象转换为xml文件输出
     * @param <T> t 需转换的对象
     * @param path 文件路径
     * @param name 文件名
     */
    public static <T> void  xmlObjectToFile(T t, String path, String name){
        String filePath=path+"\\"+name;
        File file=new File(filePath);
        FileOutputStream out=null;
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            JAXBContext context=JAXBContext.newInstance(t.getClass());
            Marshaller marshaller=context.createMarshaller();
            out =new FileOutputStream(file);
            marshaller.marshal(t,out);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文件的路径将xml中的内容转换为对象返回（使用时可修改为依据输入流实现转换）
     * @param path
     * @param t 空字符串
     * @param <T>
     * @return
     */
    public static  <T> T fileToXmlObject(String path,T t){
        File file=new File(path);
        FileInputStream inputStream=null;
        try {
            inputStream=new FileInputStream(file);
            JAXBContext jaxbContext=JAXBContext.newInstance(t.getClass());
            Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
            t=(T) unmarshaller.unmarshal(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return t;
    }

}
