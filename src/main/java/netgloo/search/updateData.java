package netgloo.search;
import netgloo.models.Document;

import java.sql.*;

import static netgloo.search.fileRead.refreshFileList;

/**
 * Created by chengangbao on 2016/11/4.
 * use document dir to update the data in mysql database
 */
public class updateData {
    public void DBhelper(){
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://localhost:3306/lucene?useUnicode=true&amp;characterEncoding=UTF-8 ";
        //MySQL配置时的用户名
        String user = "root";
        //MySQL配置时的密码
        String password="";
        //加载驱动程序
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");

            //insert data to table Document
            PreparedStatement psql;
            ResultSet res;

            psql = con.prepareStatement("TRUNCATE TABLE Document");
            psql.executeUpdate();
            Document docarray[]=refreshFileList("/Users/chengangbao/lucene/tech_data");
            for(int i=0;i<docarray.length;i++){
                //String title = new String(docarray[i].getTitle().getBytes("ISO-8859-1"),"utf-8");
//                title = new String(title.getBytes("ISO-8859-1"),"utf-8");
//                title = new String(title.getBytes("ISO-8859-1"),"utf-8");
//                title = new String(title.getBytes("ISO-8859-1"),"utf-8");
               // if(i==47){continue;}
                psql = con.prepareStatement("insert into Document values(?,?,?,?,?)");

                psql.setInt(1,i+1);
                psql.setString(2, docarray[i].getTitle());
                psql.setString(3, docarray[i].getSource());
                psql.setString(4, docarray[i].getDesp());
                psql.setString(5, docarray[i].getDocurl());
                psql.executeUpdate();			//执行更新
                //System.out.println(i);
//                System.out.println("标题：  "+docarray[i].getTitle());
//                System.out.println("描述:  "+docarray[i].getDesp());
//                System.out.println("源头:  "+docarray[i].getSource());
//                System.out.println("URL:   "+docarray[i].getDocurl());
            }

//            //2.创建statement类对象，用来执行SQL语句！！
//            Statement statement = con.createStatement();
//            //要执行的SQL语句
//            String sql = "select * from Document";
//            //3.ResultSet类，用来存放获取的结果集！！
//            ResultSet rs = statement.executeQuery(sql);
////            System.out.println("-----------------");
//              System.out.println(rs.getFetchSize());
//            System.out.println("-----------------");

//            String id = null;
//            String title = null;
//            String desp = null;
//            String source = null;
//            String docurl = null;
//            int i=0;
//            while(rs.next()){
//                //获取stuname这列数据
//                //id= rs.getString("docID");
//                //获取stuid这列数据
//                title = rs.getString("title");
//                desp = rs.getString("desp");
//                source = rs.getString("source");
//                docurl = rs.getString("docurl");
////                System.out.println(i++);
////                System.out.println(title+" || " + desp+ " || " + source +" || "+docurl);
//            }
//            rs.close();
//            con.close();
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            System.out.println("数据库数据成功获取！！");
        }
    }
    public static void main(String[] args){
        updateData ud=new updateData();
        ud.DBhelper();
    }
}
