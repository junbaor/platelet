## Gitlab Webhook 推送到企业微信 [![build status](https://api.travis-ci.org/junbaor/gitlab_workwechat.svg?branch=master)](https://travis-ci.org/junbaor/gitlab_workwechat)

### 部署
#### java 方式
```
mvn clean package -Dmaven.test.skip=true 
java -jar target/app.jar
```

#### docker 方式
```
docker run -d -p 8081:8081 --name=platelet junbaor/platelet:latest
```

### 使用
1、申请企业微信机器人获取 Webhook 地址.  
例如 : `https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=92dc7b26-1234-4568-5678-99ebccf461b0`  
2、记录 `key` 参数. 
例如 : `92dc7b26-1234-4568-5678-99ebccf461b0`  
3、打开 `Gitlab` 项目主页, 点击 `settings - integrations`  
4、`URL` 处输入 `http://机器IP:8081/webhook/key参数`.   
例如 : `http://10.200.13.33:8081/webhook/92dc7b26-1234-4568-5678-99ebccf461b0`  
5、勾选自己关心的 `Trigger`  
6、点击 `Add webhook`
