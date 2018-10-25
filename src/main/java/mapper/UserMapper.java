package mapper;

import bean.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper {
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        User user=new User();
        user.setUserId(resultSet.getString("user_id"));
        user.setUserName(resultSet.getString("user_name"));
        user.setPassword(resultSet.getString("password"));
        user.setNickName(resultSet.getString("nick_name"));
        user.setEmail(resultSet.getString("email"));
        user.setSalt(resultSet.getString("salt"));
        return user;
    }
}
