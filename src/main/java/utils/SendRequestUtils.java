package utils;


import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 调用接口发送请求的工具类
 * 1、发送post请求
 * 2、发送get请求
 * 3、发送post，但参数以json形式发送。
 */
public class SendRequestUtils {
    /**
     * 发送get请求的工具方法
     * @param url 请求地址
     * @param param 请求参数
     * @return
     */
    public static String sendGet(String url,String param){
        String result=null;
        BufferedReader in=null;
        String urlNameString=url+"?"+param;
        try {
            //创建URL对象
            URL realUrl =new URL(urlNameString);
            //打开和URL之间的连接
            URLConnection connection=realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestProperty("accept","*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //建立实际的连接
            connection.connect();
            //获取所有相应头字段
            Map<String,List<String>> map=connection.getHeaderFields();
            //遍历所有的响应头字段
//            for (String key:map.keySet()){
//                System.out.println(key+"--->"+map.get(key));
//            }
            //定义 BufferedReader输入流来读取URL的响应
            in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line=in.readLine())!=null){
                result+=line;
            }
        } catch (MalformedURLException e) {
            System.out.println("发送GET请求出现异常！"+e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("发送GET请求出现异常！"+e);
            e.printStackTrace();
        }finally {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 发送post请求
     * @param url 请求路径
     * @param param 请求参数
     * @return
     */
    public static String sendPost(String url,String param)  {
        PrintWriter out =null;
        BufferedReader in =null;
        String result="";
        try {
            URL realUrl=new URL(url);
            //打开和URL之间的连接
            URLConnection conn=realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept","*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //httpConn.setRequestMethod("POST");//待验证是否有存在的必要
            //获取URLConnection对象对应的输出流
            out=new PrintWriter(conn.getOutputStream());
            //2、中文有乱码的需要将PrintWriter改为如下
            //out=new OutputStreamWriter(conn.getOutputStream(),"UTF-8")
            // 发送请求参数
            out.print(param);
            //flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (out!=null){
                out.close();
            }
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 发送post请求，请求以json的形式发送
     * @param url 请求路径
     * @param params 请求参数
     * @return
     */
    public String sendPost2(String url,Map<String,String> params){
        JSONObject jsonObject=JSONObject.fromObject(params);
        String result=null;
        HttpClient client=new DefaultHttpClient();
        HttpPost post=new HttpPost(url);
        post.setHeader("Content-Type", "appliction/json");
        post.addHeader("Authorization", "Basic YWRtaW46");
        StringEntity s=new StringEntity(jsonObject.toString(),"utf-8");
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "appliction/json"));
        post.setEntity(s);
        try {
            HttpResponse httpResponse=client.execute(post);
            InputStream in=httpResponse.getEntity().getContent();
            BufferedReader br=new BufferedReader(new InputStreamReader(in, "utf-8"));
            StringBuilder strber=new StringBuilder();
            String line=null;
            while ((line=br.readLine())!=null) {
                strber.append(line);
            }
            in.close();
            result=strber.toString();
            if(httpResponse.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                result="服务器异常";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
