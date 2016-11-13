package netgloo.search;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import netgloo.models.Document;
/**
 * Created by chengangbao on 2016/11/2.
 */
public class fileRead {

    public static Document[] refreshFileList(String strPath) {
        List<File> filelist=new ArrayList();
        filelist=getFileList(strPath,filelist);
        Document docarray[];
        docarray=new Document[filelist.size()];
        //System.out.println(files.length);
        if (filelist == null)
            return null;
        for (int i = 0; i <filelist.size(); i++) {
                Document document=new Document();
                String strFilePath=filelist.get(i).getPath();
                String strUrl;
                String strDesp;
                String strFileName = filelist.get(i).getName().split("\\.")[0];
                String strArray[]=strFileName.split("\\|");//before .txt
                String docDetial=readfile2String(strFilePath);
                strUrl=readurl2String(strFilePath);
                strDesp=readdesp2String(strFilePath);
                if(strUrl.length()>500){continue;}
                int len=strArray.length;
                String strFileSourse=strArray[len-1].split("\\.")[0];

                document.setDesp(strDesp);
                document.setDocurl(strUrl);
                document.setSource(strFileSourse);
                document.setTitle(strArray[0]);

//                System.out.println(i+": "+strFilePath);
                //System.out.println("描述:  "+strDesp);
//                System.out.println("源头:  "+strFileSourse);
//                System.out.println("URL:  "+strUrl);
                docarray[i]=document;
            }
        //System.out.println(files[47].getPath());
        return docarray;
    }

    /*按字节读取字符串*/
    public fileRead() {
    }
    private static String readfile2String(String filePath)
    {
        String str="";

        File file=new File(filePath);

        try {

            FileInputStream in=new FileInputStream(file);


            // size  为字串的长度 ，这里一次性读完

            int size=in.available();

            byte[] buffer=new byte[size];

            in.read(buffer);

            in.close();

            str=new String(buffer,"utf-8");

        } catch (IOException e) {

            return null;
        }
        return str;
    }
    private static String readurl2String(String filePath)
    {
        String str="";

        File file=new File(filePath);

        try {

            //FileInputStream in=new FileInputStream(file);
            BufferedReader bf= new BufferedReader(new FileReader(file));
            str=bf.readLine();
            bf.close();

        } catch (IOException e) {

            return null;
        }
        return str;
    }
    private static String readdesp2String(String filePath)
    {
        String str="";

        File file=new File(filePath);

        try {

            //FileInputStream in=new FileInputStream(file);
            BufferedReader bf= new BufferedReader(new FileReader(file));
            for(int i=0;i<3;i++){
             bf.readLine();
                if(i>0){str=str+bf.readLine();}
            }
            if(str.length()>100){
            str=str.substring(0,100);}
            bf.close();

        } catch (IOException e) {

            return null;
        }
        return str;
    }

    public static List<File> getFileList(String strPath,List<File> filelist) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath(),filelist); // 获取文件绝对路径
                } else if (fileName.endsWith("txt")) { // 判断文件名是否以.txt结尾
                    String strFileName = files[i].getAbsolutePath();
                    //System.out.println("---" + strFileName);
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }

        }
        return filelist;
    }
    public static void main(String[] args) {
//        List<File> filelist=new ArrayList();
//        filelist=getFileList("/Users/chengangbao/lucene/tech_data",filelist);
//        System.out.println(filelist.size());
//        for (int i=0;i<filelist.size();i++){
//        System.out.println(filelist.get(i).getPath());
//        }

         Document docarray[]=refreshFileList("/Users/chengangbao/lucene/tech_data");
         System.out.println(docarray.length);
//        int len=0,index=0;
//        for(int i=0;i<docarray.length;i++){
//            System.out.println(i);
//            System.out.println("标题：  "+docarray[i].getTitle());
////            System.out.println("描述:  "+docarray[i].getDesp());
//            System.out.println("源头:  "+docarray[i].getSource());
//            if(len<docarray[i].getDocurl().length()){
//                len=docarray[i].getDocurl().length();
//                index=i;
//            }

//            System.out.println("URL:   "+docarray[i].getDocurl());
//        }

        //System.out.println("URL length:   "+docarray[47].getDocurl() +"  "+index);
    }
}

