#拉取一个jdk1.8版本的docker镜像
FROM sgrio/java:server_jre_8_alpine
# 将项目jar包添加到容器
ADD target/learning-1.0-SNAPSHOT.jar app.jar
# ENTRYPOINT 执行项目test.jar及外部配置文件
ENTRYPOINT ["java", "-jar", "app.jar"]
