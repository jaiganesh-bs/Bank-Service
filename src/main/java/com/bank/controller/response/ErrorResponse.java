package com.bank.controller.response;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ErrorResponse {
    private final String message;
    private final List<String> details;

}
