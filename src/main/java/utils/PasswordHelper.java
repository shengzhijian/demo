package utils;

import bean.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordHelper {
    //随机数生成器
    private RandomNumberGenerator randomNumberGenerator=new SecureRandomNumberGenerator();
    //编码方式
    private String algorithmName="md5";
    //加盐次数
    private final int hashIterations=2;
    public void encryptPassword(User user){
        user.setSalt(randomNumberGenerator.nextBytes().toHex());
        String password=new SimpleHash(algorithmName,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),hashIterations).toHex();
        user.setPassword(password);
    }
}
