//
//
//
//import com.keafmd.dao.StudentDao;
//
//import com.keafmd.entity.StudentInfo;
//import org.apache.ibatis.io.Resources;
//import org.apache.ibatis.session.SqlSession;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.apache.ibatis.session.SqlSessionFactoryBuilder;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.InputStream;
//import java.util.List;
//
//
///**
// * Keafmd
// *
// * @ClassName: TestMybatis
// * @Description: 测试Mybatis
// * @author: 牛哄哄的柯南
// * @date: 2021-02-18 16:37
// */
//public class TestMybatis {
//
//    private InputStream in;
//    private SqlSessionFactory factory;
//    private SqlSession session;
//    private StudentDao studentDao;
//
//    @Before
//    public void init() throws Exception{
//        //加载配置文件
//        in = Resources.getResourceAsStream("SqlMapConfig.xml");
//        //创建SqlSessionFactory对象
//        factory = new SqlSessionFactoryBuilder().build(in);
//        //创建SqlSession对象
//        session = factory.openSession();
//        //获取到代理对象
//        studentDao = session.getMapper(StudentDao.class);
//    }
//
//    @After
//    public void destory() throws Exception{
//        session.commit();
//        session.close();
//        in.close();
//    }
//
//    /**
//     * 查询所有
//     */
//    @Test
//    public void run1(){
//        List<StudentInfo> studentInfos = studentDao.stuList();
//    }
//
//    /**
//     * 测试保存
//     */
//
//}
