package bean.XmlBean;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement
public class ClassRoom {
    private String id;
    private int grade;
    private String className;
    private List<Studnet> list;

    public ClassRoom() {
    }

    public ClassRoom(String id, int grade, String className, List <Studnet> list) {
        this.id = id;
        this.grade = grade;
        this.className = className;
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List <Studnet> getList() {
        return list;
    }

    public void setList(List <Studnet> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "id='" + id + '\'' +
                ", grade=" + grade +
                ", className='" + className + '\'' +
                ", list=" + list +
                '}';
    }
}
