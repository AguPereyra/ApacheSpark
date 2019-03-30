import org.apache.spark.sql.*;
import org.apache.spark.api.java.function.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SparkAgr{
    public static void main(String[] args){
        Logger.getLogger("org.apache").setLevel(Level.ERROR);

        System.out.println("Iniciando programa...");

        SparkSession spark = SparkSession.builder().appName("Java Spark App Funciones de Agregacion").config("setMaster", "spark://spark-master:7077").getOrCreate();

        //Obtener datos del json
        Dataset<Row> df1 = spark.read().json(args[0]);

        //Cambiar rating a int
        Dataset<Row> df2 = df1.withColumn("rating", df1.col("rating").cast("int"));

        //Contar la cantidad de veces que se nombra cada pelicula
        Dataset<Row> cantDf = df2.groupBy("original_title").count();
        //Calcular promedio de rating por pelicula
        Dataset<Row> ratingDf = df2.groupBy("original_title").avg("rating");
        //Unir todo
        Dataset<Row> finalDf = ratingDf.join(cantDf, "original_title").sort("count");
        finalDf.show();
        //Escribir en archivo
        System.out.println("Escribiendo datos en " + args[1] + "...");
        //Mover todo a una particion
        finalDf.coalesce(1).write().csv(args[1]);

        spark.stop();

    }
}

