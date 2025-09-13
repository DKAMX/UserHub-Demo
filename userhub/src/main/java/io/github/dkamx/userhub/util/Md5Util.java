package io.github.dkamx.userhub.util;

import java.nio.charset.StandardCharsets;

import org.springframework.util.DigestUtils;

public class Md5Util {
    public static String encrypt(String input) {
        return DigestUtils.md5DigestAsHex(input.getBytes(StandardCharsets.UTF_8));
    }
}
