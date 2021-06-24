package com.macro.cloud.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.macro.cloud.domain.CommonResult;
import com.macro.cloud.domain.User;
import com.macro.cloud.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/user")
public class UserHystrixController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/testFallback/{id}")
    public CommonResult testFallback(@PathVariable Long id) {
        return userServiceImpl.getUser(id);
    }

    @GetMapping("/testCommand/{id}")
    public CommonResult testCommand(@PathVariable Long id) {
        return userServiceImpl.getUserCommand(id);
    }

    @GetMapping("/testException/{id}")
    public CommonResult testException(@PathVariable Long id) {
        return userServiceImpl.getUserException(id);
    }

    @GetMapping("/testCache/{id}")
    public CommonResult testCache(@PathVariable Long id) {
        userServiceImpl.getUserCache(id);
        userServiceImpl.getUserCache(id);
        userServiceImpl.getUserCache(id);
        return new CommonResult("操作成功", 200);
    }

    @GetMapping("/testRemoveCache/{id}")
    public CommonResult testRemoveCache(@PathVariable Long id) {
        userServiceImpl.create(id);
        userServiceImpl.removeCache(id);
        userServiceImpl.create(id);
        return new CommonResult("操作成功", 200);
    }


    @GetMapping("/testCollapser")
    public CommonResult testCollapser() throws ExecutionException, InterruptedException {
        Future<User> future1 = userServiceImpl.getUserFuture(1L);
        Future<User> future2 = userServiceImpl.getUserFuture(2L);
        future1.get();
        future2.get();
        ThreadUtil.safeSleep(200);
        Future<User> future3 = userServiceImpl.getUserFuture(3L);
        future3.get();
        return new CommonResult("操作成功", 200);
    }


}
