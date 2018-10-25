package utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * word模板填充数据后导出工具，这里的word模板采用ftl格式文件
 * 模板文件手动生成
 * 1、创建word模板
 * 2、将word模板修改为xml格式
 * 3、用el表达式修改xml格式模板后，将模板修改为.ftl格式.
 */
public class WordControlUtils {
    private static Configuration configuration=null;
    private static final String templateFolder="D:/ftl";//这个后期需要写在配置文件中
    static {
        configuration=new Configuration();
        configuration.setDefaultEncoding("utf-8");
        try {
            configuration.setDirectoryForTemplateLoading(new File(templateFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private WordControlUtils(){
        throw new AssertionError();
    }
/*===========================================实现下载功能Controller层需要调用的方法=====================================*/
    /**
     * 下载合同
     * @param request
     * @param response
     * @param map 要填充的数据模型
     * @param docName 下载保存的名称
     * @param ftlFile 模板名称
     */
    public static void downContract(HttpServletRequest request,HttpServletResponse response,Map<?, ?> map,String docName,String ftlFile) throws IOException {
        File file=null;
        FileInputStream fin=null;
        ServletOutputStream out=null;
        setDownHeader(request,response,docName);//设置下载头
        //调用createDoc方法生成word文档
        file=createDoc(request,response,map,docName,ftlFile);
        try {
            out=response.getOutputStream();
            int len=0;
            byte[] buf=new byte[1024];
            while ((len=fin.read(buf))!=-1){
                out.write(buf,0,len);
            }
            out.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }finally {
            if (fin!=null){
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file!=null){
                file.delete();
            }
        }

    }
    /**
     * 导入ftl模板，并创建doc
     * @param request
     * @param response
     * @param dataMap
     * @param docName
     * @param ftlFile
     * @return
     */
    public static File createDoc(HttpServletRequest request,HttpServletResponse response,Map<?,?> dataMap,String docName,String ftlFile) throws IOException {
        //获取ftl模板的对象
        Template template=configuration.getTemplate(ftlFile);
        //生成导出doc文件的路经和文件名
        String name=templateFolder+File.separator+(int)(Math.random()*100000)+docName;
        File file=new File(name);
        //生成文件输出流
        Writer writer=new OutputStreamWriter(new FileOutputStream(file),"utf-8");
        try {
            //将数据Map映射到模板中，并写入生成的word文件中
            template.process(dataMap,writer);
            writer.flush();
            writer.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return file;
    }
    /**
     * object转map
     * @param object
     * @return
     */
    public Map<?,?> objectToMap(Object object){
        if (object==null){
            return null;
        }
        return new BeanMap(object);
    }

    /**
     * map转换为object
     * @param map
     * @param beanClass
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public Object mapToObject(Map<String,Object> map,Class<?> beanClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (map==null){
            return null;
        }
        Object object=beanClass.newInstance();
        BeanUtils.populate(object,map);
        return object;
    }

    /**
     * 设置导出文件的响应头
     * @param request
     * @param response
     * @param fileName
     */
    public static void setDownHeader(HttpServletRequest request, HttpServletResponse response,String fileName){
        //识别浏览器,判断是否为IE浏览器
        String userAgent=request.getHeader("user_Agent");
        boolean isIE=(userAgent!=null)&&(userAgent.toLowerCase().indexOf("msie")!=-1);
       //设置头信息
        response.setHeader("Pragma","No-cache");
        response.setDateHeader("Expires",0L);
        response.setHeader("Cache-Control", "must-revalidate, no-transform");
        response.setContentType("application/x-download");//设置数据格式
        try {
            if (isIE){
                fileName=new String(fileName.getBytes("gb2312"),"iso-8859-1");
                response.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
            }else{
                fileName = new String(fileName.getBytes("gb2312"), "iso-8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
