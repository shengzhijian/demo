package utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class propertiesReader {
    /**
     * 根据文件路经和想要获取数据的参数名，来获取properties文件中的值
     * @param path resource文件夹以后的文件的路经，包含文件名
     * @param param  文件中想要获取数据的参数名
     * @return
     */
    public String propertiesRead(String path,String param) throws IOException {
        Resource resource=new ClassPathResource(path);
        Properties props= PropertiesLoaderUtils.loadProperties(resource);
        String value=props.getProperty(param);
        return value;
    }
}
