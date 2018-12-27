package utils;

import javax.persistence.Persistence;
import javax.persistence.StoredProcedureQuery;

/**
 * JPA调用存储过程的实例
 * 1、标注Entity（实例如下（同时设置两个存储流程））
 * @NamedStoredProcedureQueries({ @NamedStoredProcedureQuery(name = "in_only_test",
 *               procedureName = "test_pkg.in_only_test",
 *               parameters = { @StoredProcedureParameter(mode = ParameterMode.IN,
 *               name = "inParam1", type = String.class) }),
 *               @NamedStoredProcedureQuery(name = "in_and_out_test",
 *               procedureName = "test_pkg.in_and_out_test",
 *               parameters = { @StoredProcedureParameter(mode = ParameterMode.IN,
 *               name = "inParam1", type = String.class),
 *               @StoredProcedureParameter(mode = ParameterMode.OUT,
 *               name = "outParam1", type = String.class) }) })
 *   关键要点：
 *      ①存储过程使用了注释@NamedStoredProcedureQuery，并绑定到一个JPA表。
 *      ②procedureName是存储过程的名字
 *      ③name是JPA中的存储过程的名字
 *      ④使用注释@StoredProcedureParameter来定义存储过程使用的IN/OUT参数
 * 2、调用存储过程：详细过程如下面代码；需要注意EntityManager的获取方式
 *     返回值类型根据实际情况进行调整。
 */
public class JPAProceduresUtils {
    public boolean procedures(String send_no,String send_qty) {
        String o_return=null;
        String o_str_auto=null;
        String o_test_auto=null;
        try{
            StoredProcedureQuery store= Persistence.createEntityManagerFactory("jpa").createEntityManager().createNamedStoredProcedureQuery("sp_code_delete_check");
            //设置存储流程的输入参数的值
            store.setParameter("i_send_no", send_no);
            store.setParameter("i_send_qty", send_qty);
            //执行存储流程的后台语句
            store.execute();
            o_str_auto=(String) store.getOutputParameterValue("o_str_auto");
            o_test_auto=(String) store.getOutputParameterValue("o_test_auto");
            o_return=(String) store.getOutputParameterValue("o_return");
        }catch(Exception e){
            return false;
        }
        return true;
    }
}
