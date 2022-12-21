package com.demo.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Producer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class KaptchaController {

    private final String redisParam = "demo:verifyCode:uuid:";
    private final Producer producer;

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public KaptchaController(Producer producer,StringRedisTemplate stringRedisTemplate) {
        this.producer = producer;
        this.stringRedisTemplate=stringRedisTemplate;
    }

    @RequestMapping("/vc.png")
    public HashMap<String,String> getVerifyCode() throws IOException {
        //1.生成验证码和标识UUID
        String code = producer.createText();
        String uuid = UUID.randomUUID().toString();
        //redis存放验证码，UUID,过期时长五分钟
        stringRedisTemplate.opsForValue().set(redisParam+uuid,code,60L, TimeUnit.MINUTES);
        BufferedImage bi = producer.createImage(code);
        //2.写入内存
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(bi, "png", fos);
        System.out.println(code);
        //3.生成 base64,返回数据
        HashMap<String, String> ret = new HashMap<>();
        ret.put("uuid",uuid);
        ret.put("picture",Base64.encodeBase64String(fos.toByteArray()));
        return ret;
    }
}