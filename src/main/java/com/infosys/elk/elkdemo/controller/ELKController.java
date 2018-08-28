package com.infosys.elk.elkdemo.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ELKController {

    @Autowired
    RestTemplate restTemplate;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/elkdemo")
    @ResponseStatus(HttpStatus.OK)
    public String helloWorld() {
        String response = "Hello user ! " + new Date();
        log.info("/elkdemo - &gt; " + response);
        return response;
    }

    @GetMapping("/elk")
    public String helloELK() {
        String response = restTemplate.getForObject("http://localhost:8080/elkdemo", String.class);
        log.info("/elk - &gt; " + response);
        try {
            String exceptionrsp = restTemplate.getForObject("http://localhost:8080/exception", String.class);
            log.info("/elk trying to print exception - &gt; " + exceptionrsp);
            response = response + " === " + exceptionrsp;
        } catch (Exception e) {
            // exception should not reach here. Really bad practice :)
        }

        return response;
    }

    @GetMapping("/exception")
    public String exception() {
        String rsp = "";
        try {
            int i = 1 / 0;
            // should get exception
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            log.error("Exception As String :: - &gt; " + sStackTrace);
            rsp = sStackTrace;
        }

        return rsp;
    }

}
