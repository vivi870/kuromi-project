package com.example.kuromi.common;

import lombok.Data;

/**
 * 统一返回结果类，适配前端接收格式
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    // 状态码：200成功，404失败/不存在，500服务器错误
    private Integer code;
    // 提示信息
    private String msg;
    // 返回数据
    private T data;

    // 静态方法：快速构建成功结果
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("请求成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500); // 或400，根据业务定
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}