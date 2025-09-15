SET JAVA_HOME=D:\jdk-17
SET PATH="D:\apache-maven-3.8.9\bin";%PATH%

CD sw_shape_core
CALL mvn clean install dependency:copy-dependencies
CD ..
CD sw_shape
CALL mvn clean install
CD ..
COPY sw_shape_core\target\*.jar ..\libs
COPY sw_shape\target\*.jar ..\libs

