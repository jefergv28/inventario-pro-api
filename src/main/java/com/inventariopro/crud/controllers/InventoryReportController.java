package com.inventariopro.crud.controllers;

import com.inventariopro.crud.dto.ReportDTO;
import com.inventariopro.crud.services.InventoryReportService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;          // Importa esta excepción
import java.net.MalformedURLException; // Importa esta excepción
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/inventory-reports")
public class InventoryReportController {

    private final InventoryReportService inventoryReportService;

    public InventoryReportController(InventoryReportService inventoryReportService) {
        this.inventoryReportService = inventoryReportService;
    }

    // Endpoint para obtener la lista de informes generados
    @GetMapping
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        List<ReportDTO> reports = inventoryReportService.getAllGeneratedReports();
        return ResponseEntity.ok(reports);
    }

    // Endpoint para generar un informe de inventario en PDF
    @PostMapping("/generate-pdf")
    public ResponseEntity<ReportDTO> generatePdfReport() {
        try {
            ReportDTO report = inventoryReportService.generateInventoryReport("pdf");
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (IOException e) { // Captura IOException específicamente
            System.err.println("Error al generar el informe PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) { // Captura IllegalArgumentException si el formato es inválido
            System.err.println("Error de argumento al generar el informe PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) { // Captura cualquier otra excepción inesperada
            System.err.println("Error inesperado al generar el informe PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para generar un informe de inventario en Excel
    @PostMapping("/generate-excel")
    public ResponseEntity<ReportDTO> generateExcelReport() {
        try {
            ReportDTO report = inventoryReportService.generateInventoryReport("excel");
            return ResponseEntity.status(HttpStatus.CREATED).body(report);
        } catch (IOException e) { // Captura IOException específicamente
            System.err.println("Error al generar el informe Excel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IllegalArgumentException e) { // Captura IllegalArgumentException
            System.err.println("Error de argumento al generar el informe Excel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) { // Captura cualquier otra excepción inesperada
            System.err.println("Error inesperado al generar el informe Excel: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Endpoint para descargar un informe específico
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadReport(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new InputStreamResource(Files.newInputStream(filePath));


            if (resource.exists() && resource.isReadable()) {
                String contentType = "application/octet-stream";
                if (filename.endsWith(".pdf")) {
                    contentType = "application/pdf";
                } else if (filename.endsWith(".xlsx")) {
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                // Si el recurso no existe o no es legible, devuelve 404 Not Found
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) { // Captura si la URL del recurso es incorrecta
            System.err.println("Error de URL al descargar: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            System.err.println("Error inesperado al descargar el informe: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
