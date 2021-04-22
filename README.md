# 项目介绍
该项目为一款为实现Typora使用gitee作为图床的java版本插件

# 项目环境
1. 不限操作系统
2. 要求具体Java运行环境，即JRE环境，推荐JAVA8及以上
3. Maven
4. Typora软件

# 教程
1. 登录gitee并创建一个公开仓库
2. 申请access_token[gitee生成accessToken](https://gitee.com/profile/personal_access_tokens)
1. 下载该仓库源码
3. 配置`com.pang.Config`，其配置参考注释
4. 在源码根目录使用`maven:package`指令进行编译,编译生成的java文件为`./target/typora-custom-script-1.0-SNAPSHOT-jar-with-dependencies.jar`
5. 打开Typora进行配置
    1. 选择`偏好设置->图像->上传服务`
    2. 选择`Custom Command`
    3. 编辑自定义命令,指令如下`java -jar 生成的typora-custom-script-1.0-SNAPSHOT-jar-with-dependencies.jar的绝对路径`，例如`java -jar /home/pang/project/typora-custom-script/target/typora-custom-script-1.0-SNAPSHOT-jar-with-dependencies.jar`
    4. 点击`验证图片上传选项`，即可得到结果
    5. 点击`插入图片时..`后面的下拉框，选择`上传图片`即可配置完成
