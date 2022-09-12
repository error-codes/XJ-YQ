package com.young.xjyq.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author YoungMan [BayMax]
 * @email PlutoYCR520@outlook.com
 * @since 2022/9/12 20:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WiseResult {

    private String result;

    private List<Predictions> predictions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Predictions {

        private String label;

        private Map<String, String> predictions;
    }
}
