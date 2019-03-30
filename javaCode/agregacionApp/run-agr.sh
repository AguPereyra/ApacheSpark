#!/bin/bash

#Variables archivos
JAR_NAME="spark-agr-1.0.jar";
DATA_NAME="out.json";
OUTPUT_NAME="agr.csv";

#Variables Ejecucion
SPARK_COMMAND="/spark/bin/spark-submit";
CLASE="SparkAgr";
MASTER="spark://192.168.1.131:7077";
JAR_LOCATION="/opt/spark-apps/"$JAR_NAME;
DATA_LOCATION="/opt/spark-apps/"$DATA_NAME;
OUTFILE_LOCATION="/opt/spark-data/"$OUTPUT_NAME;

#Ejecucion
rm -f -r /mnt/spark-apps/$JAR_NAME;
rm -f -r /mnt/spark-data/$OUTPUT_NAME;
rm -f -r ./$OUTPUT_NAME;
rm -f -r /mnt/spark-data/$OUTPUT_NAME;
cp mavenproject/target/$JAR_NAME /mnt/spark-apps/ &
cp $DATA_NAME /mnt/spark-apps/$DATA_NAME &

docker exec -it spark-master $SPARK_COMMAND --class $CLASE --master $MASTER --deploy-mode client --driver-memory 4g --executor-memory 4g --conf spark.serializer=org.apache.spark.serializer.KryoSerializer $JAR_LOCATION $DATA_LOCATION $OUTFILE_LOCATION &&
cp -r /mnt/spark-data/$OUTPUT_NAME ./ &&
echo termino el trabajo, salida=$OUTPUT_NAME;
