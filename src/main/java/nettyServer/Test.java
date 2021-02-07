//package nettyServer;
//
//import com.mchange.v2.c3p0.ComboPooledDataSource;
//import org.apache.log4j.Logger;
//
//import java.sql.SQLException;
//
//
//public class Test {
//    public static Logger logger=Logger.getLogger(Test.class);
//    public static void main(String[] args) {
//        try {
//            ComboPooledDataSource dataSource= new ComboPooledDataSource("mysql");
//            System.out.println(dataSource.getConnection());
//            logger.info(dataSource.getConnection());
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//    }
//}
