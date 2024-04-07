# lql-raft-kv

## 简介

这是一个Java实现的基于Raft算法的分布式KV存储。

主要技术：Java、RocksDB、GRPC

> 主要目的：
>
> 1. 自己实现，便于深入了解Raft协议；
>
> 2. 便于大家用于学习Raft协议
>
> **大家有问题交流或者程序有bug也可以提issue**🥰

## 已完成内容

1.  leader选举
2. 日志追加
3. 日志复制

## TODO

1. 增删节点
2. 日志压缩
3. 丰富状态机接口

> 以后或者进行一些奇奇怪怪的优化

## 启动

### idea启动

1. `maven`：先加载maven依赖，然后在plugins的protobuf插件中使用`compile`和`compile-custom`命令生成对应的protobuf类
2. `config.yml`配置：写入对应节点的address数组（可包含自身节点）
3. `idea`中配置：
   * 添加对应个数的application启动项
   * `VM options`添加参数：`-Dserver.port=8000`（即当前节点的rpc启动端口）

## 特别鸣谢

1. 感谢莫那鲁道的 lu-raft-kv 项目 https://github.com/stateIs0/lu-raft-kv

   > 学习过程中，部分参考了大佬的代码

2. 感谢maemual的 raft-zh_cn 仓库 https://github.com/maemual/raft-zh_cn

   > 论文中文翻译对一个英文不好的人真是太友好啦😆😆，有些地方还是会去看原文
