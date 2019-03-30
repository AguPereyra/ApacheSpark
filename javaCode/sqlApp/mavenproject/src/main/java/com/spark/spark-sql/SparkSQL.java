import org.apache.spark.sql.*;
import org.apache.spark.api.java.function.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class SparkSQL{
public static void main(String[] args){
	Logger.getLogger("org.apache").setLevel(Level.ERROR);

	System.out.println("Iniciando programa...");

	SparkSession spark = SparkSession.builder().appName("Java SQL Spark App").config("setMaster", "spark://spark-master:7077").getOrCreate();

	//Obtener datos de un json y consultarlos
	Dataset<Row> df1 = spark.read().json(args[0]);

	//Cambiar budget a double
	Dataset<Row> df = df1.withColumn("budget", df1.col("budget").cast("double"));

	System.out.println("Creando tabla para la consulta...");
	df.createOrReplaceTempView("tabla1");

	System.out.println("Iniciando consulta: " + args[2]);
	Dataset<Row> sqlDf = spark.sql(args[2]);

	//Escribir en archivo
	System.out.println("Escribiendo datos en " + args[1] + "...");
	//Mover todo a una particion
	sqlDf.coalesce(1).write().csv(args[1]);

	spark.stop();
 }
}
