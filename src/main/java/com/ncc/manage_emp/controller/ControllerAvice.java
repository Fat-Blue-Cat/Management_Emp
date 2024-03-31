//package com.ncc.manage_emp.controller;
//
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class ControllerAvice {
//
//    @Bean
//    public ErrorAttributes errorAttributes() {
//        return new DefaultErrorAttributes() {
//            @Override
//            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
//                return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults().excluding(ErrorAttributeOptions.Include.EXCEPTION));
//            }
//        };
//    }
//
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<Map<String, String>> handleCustomException(CustomException e) {
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("message", e.getLocalizedMessage());
//        errorResponse.put("status", e.getHttpStatus().toString());
//        return new ResponseEntity<>(errorResponse, e.getHttpStatus());
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException e) {
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("message", "Access denied");
//        errorResponse.put("status", String.valueOf(HttpStatus.FORBIDDEN.value()));
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, String>> handleException(HttpServletResponse res) {
//        Map<String, String> errorResponse = new HashMap<>();
//        errorResponse.put("message", "Something went wrong");
//        errorResponse.put("status", String.valueOf(HttpStatus.FORBIDDEN.value()));
//        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
//    }
//
//}
