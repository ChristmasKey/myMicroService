# 指定基础镜像
FROM ubuntu:16.04
# 配置环境变量，JDK的安装目录
ENV JAVA_DIR=/usr/local

# 拷贝jdk和项目jar包
COPY ./jdk-8u401-linux-x64.tar.gz $JAVA_DIR/
COPY ./docker-demo.jar /tmp/app.jar

# 安装jdk
RUN cd $JAVA_DIR \
 && tar -xf ./jdk-8u401-linux-x64.tar.gz \
 && mv ./jdk1.8.0_401 ./java8

# 配置环境变量
ENV JAVA_HOME=$JAVA_DIR/java8
ENV PATH=$PATH:$JAVA_HOME/bin

# 暴露端口
EXPOSE 6789
# 入口，Java项目的启动命令
ENTRYPOINT java -jar /tmp/app.jar