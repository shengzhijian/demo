package dao.impl;

import dao.BaseJdbcDao;
import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class BaseJdbcDaoImpl implements BaseJdbcDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /*
    批量增、删、改
     */
    public int update(String sql, Object[] objs) {
        return jdbcTemplate.update(sql,objs);
    }
    /*
    根据条件查询数据库
     */
    public List<Map<String, Object>> queryForList(String sql, Object[] objs) {
        return jdbcTemplate.queryForList(sql,objs);
    }
    /*
    批量增、删、改
     */
    public int[] batchUpdate(String sql, List<Object[]> params) {
        return jdbcTemplate.batchUpdate(sql,params);
    }
    /*
    根据条件查询一个Object对象
     */
    public Object findById(String sql, Object[] objects) {
        return jdbcTemplate.queryForObject(sql,objects,new UserMapper());
    }
}
