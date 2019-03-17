package bean.XmlBean;

import org.junit.Test;
import utils.XMLUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ClassRoomTest {
    @Test
    public void test01(){
        try {
            JAXBContext jct=JAXBContext.newInstance(ClassRoom.class);
            Marshaller marshaller=jct.createMarshaller();
            List<Studnet> list=new ArrayList <Studnet>();
            Studnet studnet1=new Studnet(111,"张三","男","zhangsan@163.com");
            Studnet studnet2=new Studnet(112,"李四","男","lisi@163.com");
            list.add(studnet1);
            list.add(studnet2);
            ClassRoom classRoom=new ClassRoom("123",2011,"电子信息工程",list);
            File file=new File("F:\\xmldocument\\classroomt.xml");
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream out=new FileOutputStream(file);
            marshaller.marshal(classRoom,out);
            System.out.println("输出成功");

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test02(){
        File file=new File("F:\\xmldocument\\classroomt.xml");
        try {
            FileInputStream inputStream=new FileInputStream(file);
            JAXBContext jaxbContext=JAXBContext.newInstance(ClassRoom.class);
            Unmarshaller unmarshaller=jaxbContext.createUnmarshaller();
            ClassRoom classRoom=(ClassRoom) unmarshaller.unmarshal(inputStream);
            System.out.println(classRoom.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test03(){
        XMLInputFactory factory=XMLInputFactory.newFactory();
        InputStream is=null;
        try {
            is=new FileInputStream(new File("F:\\xmldocument\\classroomt.xml"));
            XMLStreamReader reader=factory.createXMLStreamReader(is);
            while (reader.hasNext()){
                 int type=reader.next();
                 if (type== XMLStreamConstants.START_ELEMENT){
                     System.out.println(reader.getName());
                 }else if (type==XMLStreamConstants.CHARACTERS){
                     System.out.println(reader.getText().trim());
                 }else if (type==XMLStreamConstants.END_ELEMENT){
                     System.out.println(reader.getName());
                 }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test04(){
        String path="F:\\xmldocument\\classroomt.xml";
        ClassRoom classRoom=new ClassRoom();
        classRoom= XMLUtils.fileToXmlObject(path,classRoom);
        System.out.println(classRoom.toString());
    }

}