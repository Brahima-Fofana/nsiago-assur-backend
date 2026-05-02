package ci.nsiago.assur.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class ValidationDto {
    @Builder
    @Data
    @AllArgsConstructor
    public static class ValidationInput{
        String code;
    }
}
