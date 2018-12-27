package bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 使用JDBC调用存储流程的试用实体类
 */
@Entity
@Table(name = "CODE_REPORT_STAT")
public class CodeReportStat {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "STAT_TYPE")
    private String statType;//统计类型
    @Column(name = "STAT_MAN")
    private String statMan;//统计人
    @Column(name = "SN")
    private Integer sn;//序号
    @Column(name = "VARCHAR01")
    private String varchar01;//内容1
    @Column(name = "VARCHAR02")
    private String varchar02;//内容2
    @Column(name = "VARCHAR03")
    private String varchar03;//内容3
    @Column(name = "VARCHAR04")
    private String varchar04;//内容4
    @Column(name = "VARCHAR05")
    private String varchar05;//内容5
    @Column(name = "NUMBER01")
    private Integer number01;//数字1
    @Column(name = "NUMBER02")
    private Integer number02;//数字2
    @Column(name = "NUMBER03")
    private Integer number03;//数字3
    @Column(name = "NUMBER04")
    private Integer number04;//数字4
    @Column(name = "NUMBER05")
    private Integer number05;//数字5

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatType() {
        return statType;
    }

    public void setStatType(String statType) {
        this.statType = statType;
    }

    public String getStatMan() {
        return statMan;
    }

    public void setStatMan(String statMan) {
        this.statMan = statMan;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public String getVarchar01() {
        return varchar01;
    }

    public void setVarchar01(String varchar01) {
        this.varchar01 = varchar01;
    }

    public String getVarchar02() {
        return varchar02;
    }

    public void setVarchar02(String varchar02) {
        this.varchar02 = varchar02;
    }

    public String getVarchar03() {
        return varchar03;
    }

    public void setVarchar03(String varchar03) {
        this.varchar03 = varchar03;
    }

    public String getVarchar04() {
        return varchar04;
    }

    public void setVarchar04(String varchar04) {
        this.varchar04 = varchar04;
    }

    public String getVarchar05() {
        return varchar05;
    }

    public void setVarchar05(String varchar05) {
        this.varchar05 = varchar05;
    }

    public Integer getNumber01() {
        return number01;
    }

    public void setNumber01(Integer number01) {
        this.number01 = number01;
    }

    public Integer getNumber02() {
        return number02;
    }

    public void setNumber02(Integer number02) {
        this.number02 = number02;
    }

    public Integer getNumber03() {
        return number03;
    }

    public void setNumber03(Integer number03) {
        this.number03 = number03;
    }

    public Integer getNumber04() {
        return number04;
    }

    public void setNumber04(Integer number04) {
        this.number04 = number04;
    }

    public Integer getNumber05() {
        return number05;
    }

    public void setNumber05(Integer number05) {
        this.number05 = number05;
    }
}
