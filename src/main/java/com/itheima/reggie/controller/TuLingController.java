package com.itheima.reggie.controller;

import com.itheima.reggie.Tuling.TuLingUtils;
import com.itheima.reggie.common.RespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex
 * @version 1.0.0
 * @ClassName TuLingController.java
 * @Description TODO
 * @createTime 2022年02月05日 18:31:00
 */
@RestController
@Slf4j
@RequestMapping("/tulingchat")
public class TuLingController {
    TuLingUtils tuLingUtils = new TuLingUtils();

    @GetMapping
    public RespData<String> chat(String text) {
        String message = tuLingUtils.getMessage(text);
        return RespData.success(message);
    }
}
