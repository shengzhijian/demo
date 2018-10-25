package dao;

import java.util.List;
import java.util.Map;

public interface BaseJdbcDao {
    /*
    增加、修改、删除
     */
    int update(String sql ,Object[] objs);
    /*
    根据某条件查询数据库
     */
    List<Map<String,Object>> queryForList(String sql, Object[] objs);
    /*
    批量增加、删除、修改
     */
    int[] batchUpdate(String sql,List<Object[]> params);
    /*
    根据Id获取对象
     */
    Object findById(String sql,Object[] objects);

}
