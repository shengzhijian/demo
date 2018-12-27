package utils;

import bean.CodeReportStat;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过spring的JDBC的方式调用orcale的存储流程（本案例的存储流程的返回的是结果集）
 */
public class JDBCProceduresUtils {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    //statMan和in_wd是存储流程的输入值
    public List<CodeReportStat> getCodeServiceCallStatis(final String statMan, final String in_wd) {
        Map <Integer, Object> resMap = jdbcTemplate.execute(new CallableStatementCreator() {
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                String storedProc = "Call SP_CODE_REPORT_STAT(?,?,?,?,?)";
                CallableStatement cs = con.prepareCall(storedProc);
                //设置输入参数的值
                cs.setString(1, statMan);
                cs.setString(2, in_wd);
                //注册输出参数的类型 
                cs.registerOutParameter(3, OracleTypes.CURSOR);
                cs.registerOutParameter(4, OracleTypes.NUMBER);
                cs.registerOutParameter(5, OracleTypes.VARCHAR);
                return cs;
            }
        }, new CallableStatementCallback <Map <Integer, Object>>() {
            public Map <Integer, Object> doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                Map <Integer, Object> resultMap = new HashMap <Integer, Object>();
                cs.execute();
                Long successFalg = cs.getLong(4);
                ResultSet rs = null;
                if (successFalg == -1) {
                    resultMap.put(4, successFalg);
                    resultMap.put(5, cs.getString(5));
                } else {
                    rs = (ResultSet) cs.getObject(3);
                    try {
                        List <CodeReportStat> reportStats = new ArrayList <CodeReportStat>();
                        while (rs.next()) {
                            CodeReportStat codeReportStat = new CodeReportStat();
                            codeReportStat.setId(rs.getInt("id"));
                            codeReportStat.setNumber01(rs.getInt("number01"));
                            codeReportStat.setNumber02(rs.getInt("number02"));
                            codeReportStat.setNumber03(rs.getInt("number03"));
                            codeReportStat.setNumber04(rs.getInt("number04"));
                            codeReportStat.setNumber05(rs.getInt("number05"));
                            codeReportStat.setVarchar01(rs.getString("varchar01"));
                            codeReportStat.setVarchar02(rs.getString("varchar02"));
                            codeReportStat.setVarchar03(rs.getString("varchar03"));
                            codeReportStat.setVarchar04(rs.getString("varchar04"));
                            codeReportStat.setVarchar05(rs.getString("varchar05"));
                            codeReportStat.setSn(rs.getInt("sn"));
                            codeReportStat.setStatMan(rs.getString("stat_Man"));
                            codeReportStat.setStatType(rs.getString("stat_Type"));
                            reportStats.add(codeReportStat);
                        }
                        resultMap.put(3, reportStats);
                    } finally {
                        rs.close();
                    }
                }
                return resultMap;
            }
        });
        return (List <CodeReportStat>) resMap.get(3);
    }
}
