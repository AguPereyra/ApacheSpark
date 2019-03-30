#Environment variables used by the spark workers
#Do not touch this unless you modify the compose master
SPARK_MASTER=spark://192.168.1.131:7077
#Allocation Parameters
SPARK_WORKER_CORES=2
SPARK_WORKER_MEMORY=8G
SPARK_DRIVER_MEMORY=8G
SPARK_EXECUTOR_MEMORY=8G
