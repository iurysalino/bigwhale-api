package com.whale.web.documents.certificategenerator.dto;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

public record WorksheetRecordDto(
        @NotBlank(message = "No worksheet was provided")
        MultipartFile csvFile
) {
}
