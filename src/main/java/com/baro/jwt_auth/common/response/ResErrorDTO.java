package com.baro.jwt_auth.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResErrorDTO {
    private ErrorDetail error;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorDetail {
        private String code;
        private String message;
    }

    public static ResErrorDTO of(String code, String message) {
        return new ResErrorDTO(new ErrorDetail(code, message));
    }
}
