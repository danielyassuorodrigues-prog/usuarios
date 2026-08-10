package com.daniels.usuarios.business.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {
    private LocalDateTime timeStamp;
    private int status;
    private String erro;
    private String message;
    //request que originou minha excessão
    private String path;
}
