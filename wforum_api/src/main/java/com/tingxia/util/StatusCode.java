package com.tingxia.util;

public enum StatusCode {
    /**
     * 未知错误
     */
    ERROR(1,"ERROR"),
    /**
     * 服务器成功返回用户请求的数据。
     */
    OK(200,"OK"),
    /**
     * 用户新建或修改数据成功。
     */
    CREATED(201,"CREATED"),
    /**
     * 请求已进入后台排队
     */
    Accepted(202,"Accepted"),
    /**
     * 用户删除数据成功。
     */
    NO_CONTENT(204,"NO CONTENT"),
    /**
     * 请求错误。
     * @author tingxia
     */
    INVALID_REQUEST(400,"INVALID REQUEST"),
    /**
     * 没有权限,未登录。
     */
    UNAUTHORIZED(401,"UNAUTHORIZED"),
    /**
     * 已登录.
     */
    LOGGED(402,"LOGGED"),
    /**
     * 有权限，访问被禁止。
     */
    FORBIDDEN(403,"FORBIDDEN"),
    /**
     * 资源不存在
     */
    NOT_FOUND(404,"NOT FOUND"),
    /**
     * 登录超时
     */
    LOGIN_TIMEOUT(402,"LOGIN_TIMEOUT"),
    /**
     * 请求格式错误。
     */
    NOT_ACCEPTABLE(406,"NOT ACCEPTABLE"),
    /**
     * 资源被永久删除，不可得。
     */
    GONE(410,"GONE"),
    /**
     * 验证错误。
     */
    UNPROCESABLE_ENTITY(422,"UNPROCESABLE ENTITY"),
    /**
     * 服务器错误。
     */
    INTERNAL_SERVER_ERROR(500,"INTERNAL SERVER ERROR");

    private int code;
    private String error;
    StatusCode( int code, String error ){
        this.code = code;
        this.error = error;
    }

    public int getCode(){
        return code;
    }

    public String getError(){
        return error;
    }
}
