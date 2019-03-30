package com.mycompany.mavenproject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;


public class Main {
    static int cont = 0;

    public static void main(String[] args) {
        Logger.getLogger("org.apache").setLevel(Level.ERROR);
        
        SparkConf conf = new SparkConf().setAppName("JavaSparkTextToJson").setMaster("spark://spark-master:7077");

        JavaSparkContext jsc = new JavaSparkContext(conf);        
        
        System.out.println("Comienza el programa");
        
        String[] campos;
            
        try {
            FileInputStream FileCampos=new FileInputStream(args[1]);
            int c;
            String aux = "";
            while((c = FileCampos.read()) != -1 ){
                aux += (char)c;
            } 
            FileCampos.close();
            campos = aux.split(",");

        } catch (Exception ex) {
            System.out.println("Error al Leer el archivo.");
            return;
        }
               
        JavaRDD<String> lines = jsc.textFile(args[0]);
        
        JavaRDD<String> json = lines.map(
            new Function<String, String>() {
                public String call(String s) { 
                    String[] valores = s.split(",");
                    if(campos.length == valores.length){
                        String res = "{";
                        
                        for(int i=0;i<campos.length;i++){
                            res+= "\""+campos[i]+"\":\""+valores[i]+"\",";
                        }
                        
                        res = res.substring(0, res.length()-1) + "},";
                        
                        if(cont %100 == 0){
                            System.out.println("filas procesadas:" + cont);
                        }
                        
                        cont+=1;
                        return res;
                    }else {
                        return "";
                    }                    
                }
            });    
        try {
            FileOutputStream file = new FileOutputStream("/opt/spark-apps/out.json");
            String str = json.reduce((a,b) -> a + b);
            str = str.substring(0, str.length()-1);
            byte[] strToBytes = ("["+str+"]").getBytes();
            file.write(strToBytes);
            file.close();
        } catch (Exception ex) {
            System.out.println("Error al escribir en el archivo.");
            ex.printStackTrace();
            return;
        }
        
        jsc.close();
    }
}
