package com.financialtransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BatchResponseDTO {
    private String batchId;
    private String message;
}
