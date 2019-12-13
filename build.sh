#!/usr/bin/env bash

mvn clean package -Dmaven.test.skip=true

docker build -t registry.cn-beijing.aliyuncs.com/springcloud-xiaoyan/order .

#!docker tag bf96e5ceb941 registry.cn-beijing.aliyuncs.com/springcloud-xiaoyan/order

#!docker push registry.cn-beijing.aliyuncs.com/springcloud-xiaoyan/order
