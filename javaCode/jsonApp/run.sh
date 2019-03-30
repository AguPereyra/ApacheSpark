#!/bin/bash

#Variables archivos
JAR_NAME="SparkTextToJson-1.0-GUI.jar";
DATA_NAME="text-750000.txt";
OUTPUT_NAME="out.json";
COLUMN_NAME="col.txt";
NUM_PARTITIONS="2000";
MASTER_IP="192.168.43.165:7077";

#Variables Ejecucion
SPARK_COMMAND="/spark/bin/spark-submit";
CLASE="com.spark.spark_text_to_json.Main";
MASTER="spark://"$MASTER_IP;
JAR_LOCATION="/opt/spark-apps/"$JAR_NAME;
DATA_LOCATION="/opt/spark-apps/$DATA_NAME";
COLUMN_NAME_LOCATION="/opt/spark-apps/$COLUMN_NAME";
OUTPUT_LOCATION="/opt/spark-apps/"$OUTPUT_NAME;

#Ejecucion
rm -f /mnt/spark-apps/$JAR_NAME;
rm -f ./$OUTPUT_NAME;
echo Copiando datos...;
cp $DATA_NAME /mnt/spark-apps/ &&
cp $COLUMN_NAME /mnt/spark-apps/ &&
cp mavenproject/target/$JAR_NAME /mnt/spark-apps/ &&
clear &&
docker exec -it spark-master $SPARK_COMMAND --class $CLASE --master $MASTER --deploy-mode client --driver-memory 2g --executor-memory 2g --conf spark.serializer=org.apache.spark.serializer.KryoSerializer $JAR_LOCATION $DATA_LOCATION $COLUMN_NAME_LOCATION $OUTPUT_LOCATION $NUM_PARTITIONS && 
cp /mnt/spark-apps/$OUTPUT_NAME ./;
rm -f /mnt/spark-apps/$DATA_NAME;
rm -f /mnt/spark-apps/$COLUMN_NAME;
rm -f /mnt/spark-apps/$JAR_NAME;
rm -f /mnt/spark-apps/$OUTPUT_NAME;
echo termino el trabajo, salida=$OUTPUT_NAME;
