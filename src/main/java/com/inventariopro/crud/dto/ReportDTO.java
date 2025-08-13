// src/main/java/com/inventariopro/crud/dto/ReportDTO.java
package com.inventariopro.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
    private String id;
    private String date; // Formato "yyyy-MM-dd"
    private String type; // "PDF" o "Excel"
    private String filename; // Nombre del archivo guardado
}