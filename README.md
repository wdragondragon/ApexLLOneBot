### 项目介绍

始于建设用于围绕着apex辅助的机器人卡密发卡系统，具有生成绑定qq群成员的机器码绑定密钥的作用，并用于提供校验机器码是否具有访问权限的接口。

提供转发discord大师以上的封禁记录，关注某个uid封禁（封禁后会在你发起关注指令的群中，在封禁记录底下at你）

可用命令：

1. @某人 查询ag授权
2. 获取[申请类型]体验卡
3. @某人 授权[时间类型]卡[申请类型]
4. 生成[数字]张[时间类型]卡[申请类型]
5. 查封[uid]
6. 关注封禁[uid]

tips：

1. 时间类型：天|月|周|年|永久
2. 申请类型：ai|apex_recoils|apex_recoils_server|auto_upgrade_script

### 快速开始

1. 使用新版QQ，安装LiteLoaderQQNT。[安装参考文档](https://liteloaderqqnt.github.io/guide/install.html)
2. 安装LLOneBot插件。[安装参考文档](https://llonebot.github.io/zh-CN/guide/getting-started)
3. 在LLOneBot中配置http上报事件。
4. 启动项目。

当有消息来到时，默认的消息处理器会打印以下日志：

```text
接受到聊天消息：ChatMessage()
```
