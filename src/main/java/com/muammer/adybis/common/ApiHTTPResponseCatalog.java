package com.muammer.adybis.common;

public final class ApiHTTPResponseCatalog {

    public static final String OK_CODE = "200";
    public static final String CREATED_CODE = "201";
    public static final String NO_CONTENT_CODE = "204";
    public static final String BAD_REQUEST_CODE = "400";
    public static final String UNAUTHORIZED_CODE = "401";
    public static final String NOT_FOUND_CODE = "404";
    public static final String FORBIDDEN_CODE = "403";
    public static final String CONFLICT_CODE = "409";
    public static final String INTERNAL_SERVER_ERROR_CODE = "500";

    public static final String OK_MESSAGE = "Request success";
    public static final String CREATED_MESSAGE = "Resource created!";
    public static final String BAD_REQUEST_MESSAGE = "Request data is invalid!";
    public static final String UNAUTHORIZED_MESSAGE = "Authentication credentials could not be verified!";
    public static final String NOT_FOUND_MESSAGE = "Resource not found!";
    public static final String FORBIDDEN_MESSAGE = "You do not have permission to access this resource!";
    public static final String CONFLICT_MESSAGE = "Resource already exist!";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Something went wrong!";

    private ApiHTTPResponseCatalog() {
        throw new UnsupportedOperationException("This is a class and cannot be instantiated");
    }
}
