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
                String strUrl;
                String strDesp;
                String strFileName = files[i].getName().split("\\.")[0];
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
    public static void main(String[] args) {
         Document docarray[]=refreshFileList("/Users/chengangbao/lucene/lucene_doc");
         System.out.println(docarray.length);
        int len=0,index=0;
        for(int i=48;i<docarray.length;i++){
            System.out.println(i);
            System.out.println("标题：  "+docarray[i].getTitle());
            System.out.println("描述:  "+docarray[i].getDesp());
            System.out.println("源头:  "+docarray[i].getSource());
            if(len<docarray[i].getDocurl().length()){
                len=docarray[i].getDocurl().length();
                index=i;
            }

            System.out.println("URL:   "+docarray[i].getDocurl());
        }

        //System.out.println("URL length:   "+docarray[47].getDocurl() +"  "+index);
    }
}

