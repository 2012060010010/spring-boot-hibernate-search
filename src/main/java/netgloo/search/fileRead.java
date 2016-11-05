package netgloo.search;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

import netgloo.models.Document;
/**
 * Created by chengangbao on 2016/11/2.
 */
public class fileRead {

    public static Document[] refreshFileList(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        Document docarray[];
        docarray=new Document[files.length];
        //System.out.println(files.length);
        if (files == null)
            return null;
        for (int i = 0; i <files.length; i++) {
            if (files[i].isDirectory()) {
                refreshFileList(files[i].getAbsolutePath());
            } else {
                Document document=new Document();
                String strFilePath=files[i].getPath();
                String strFileName = files[i].getName().split("\\.")[0];
                String strArray[]=strFileName.split("\\|");//before .txt
                String docDetial=readfile2String(strFilePath);
                int len=strArray.length;
                String strFileSourse=strArray[len-1].split("\\.")[0];

                document.setDesp(strFileName);
                document.setDocurl("www.baidu.com");
                document.setSource(strFileSourse);
                document.setTitle(strArray[0]);

//                System.out.println(i+": "+strFilePath);
//                System.out.println("描述:  "+strArray[0]);
//                System.out.println("源头:  "+strFileSourse);
                //System.out.println("文档:  "+docDetial.length()+"  "+docDetial);
                //docList.add(document);
                docarray[i]=document;
            }
        }
        return docarray;
    }
    /*
public static Document[] refreshFileList(String strPath) {
    File file = new File(strPath);

    Document docarray[];
    docarray=new Document[file.length];
    //System.out.println(files.length);
    if(file.isFile() && file.exists()){ //判断文件是否存在
        try {
            String encoding="GBK";
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    System.out.println(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
            Document document=new Document();


            document.setDesp(strFileName);
            document.setDocurl("www.baidu.com");
            document.setSource(strFileSourse);
            document.setTitle(strArray[0]);

//                System.out.println(i+": "+strFilePath);
//                System.out.println("描述:  "+strArray[0]);
//                System.out.println("源头:  "+strFileSourse);
            //System.out.println("文档:  "+docDetial.length()+"  "+docDetial);
            //docList.add(document);
            docarray[i]=document;
        }
    }
    return docarray;
}*/
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
    public static void main(String[] args) {
         Document docarray[]=refreshFileList("/Users/chengangbao/lucene/lucene_doc");
        //System.out.println(docarray.length);
        for(int i=0;i<docarray.length;i++){
            System.out.println(i);
            System.out.println("标题：  "+docarray[i].getTitle());
            System.out.println("描述:  "+docarray[i].getDesp());
            System.out.println("源头:  "+docarray[i].getSource());
            System.out.println("URL:   "+docarray[i].getDocurl());
        }
    }
}

