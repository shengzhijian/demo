import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;
import java.security.Key;

public class TextSiro {
    @Test
    public void textHelloWord(){
        Factory factory=new IniSecurityManagerFactory("classpath:shiro-passwordservice.ini");
        //首先通过 new IniSecurityManagerFactory 并指定一个 ini 配置文件来创建一个 SecurityManager 工厂
        org.apache.shiro.mgt.SecurityManager securityManager= (org.apache.shiro.mgt.SecurityManager) factory.getInstance();
        //接着获取 SecurityManager 并绑定到 SecurityUtils，这是一个全局设置，设置一次即可
        SecurityUtils.setSecurityManager((org.apache.shiro.mgt.SecurityManager) securityManager);
        //通过 SecurityUtils 得到 Subject，其会自动绑定到当前线程；如果在 web 环境在请求结束时需要解除绑定；然后获取身份验证的 Token，如用户名 / 密码；
        Subject subject=SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken( "wu","123" );
        //调用 subject.login 方法进行登录，其会自动委托给 SecurityManager.login 方法进行登录；
        subject.login(token);
        Assert.assertEquals(true,subject.isAuthenticated());
        System.out.println("登录成功");
        subject.logout();
    }
    private void login(String configFile){
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(configFile);
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("zhang", "1234");
        subject.login(token);

    }
    @Test
    public void testAllSuccessfulStrategyWithSuccess(){
        login("classpath:shiro_jdbc_realm.ini");
        Subject subject=SecurityUtils.getSubject();
        PrincipalCollection principalCollection=subject.getPrincipals();
        Assert.assertEquals(1,principalCollection.asList().size());
        System.out.println("登录成功");
    }
    @Test
    public void encodeAndDecode(){
        String str="hello world!";
        //加密
        String str1= Base64.encodeToString(str.getBytes());
        //解密
        String str2=Base64.decodeToString(str1);
        System.out.println(str1);
        System.out.println(str2);
    }
    @Test
    public  void Hexencode(){
        String str="hello world!";
        //加密
        String str1= Hex.encodeToString(str.getBytes());
        //解密
        String str2= new String(Hex.decode(str1.getBytes()));
        System.out.println(str1);
        System.out.println(str2);
    }
    @Test
    public void encodeMD5(){
        String str="hello world!";
        String salt="123";
        //加密
        String str1=new Md5Hash(str,salt).toString();
        System.out.println(str1);
    }
    @Test
    //对称加密
    public void aesEncode(){
        AesCipherService aesCipherService=new AesCipherService();
        //设置加密长度
        aesCipherService.setKeySize(128);
        Key key=aesCipherService.generateNewKey();
        String str="hello world!";
        //加密
        String encodeTest=aesCipherService.encrypt(str.getBytes(),key.getEncoded()).toHex();
        //解密
        String decodeTest=new String(aesCipherService.decrypt(Hex.decode(encodeTest),key.getEncoded()).getBytes());
        System.out.println(encodeTest);
        System.out.println(decodeTest);
    }

}
