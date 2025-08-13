package com.inventariopro.crud.services;

import java.io.FileNotFoundException;

import com.inventariopro.crud.dto.ReportDTO;
import com.inventariopro.crud.models.ProductoModel;
import com.inventariopro.crud.repositories.ProductoRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryReportService {

    private final ProductoRepository productoRepository;
    private final Path uploadsDir = Paths.get("uploads");

    public InventoryReportService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        try {
            Files.createDirectories(uploadsDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de carga", e);
        }
    }

    private final List<ReportDTO> generatedReports = new ArrayList<>();

    public List<ReportDTO> getAllGeneratedReports() {
        return new ArrayList<>(generatedReports);
    }

    public ReportDTO generateInventoryReport(String format) throws IOException {
        List<ProductoModel> products = productoRepository.findAll();

        String filename;
        String reportType;

        if ("pdf".equalsIgnoreCase(format)) {
            filename = "inventario_actual_" + UUID.randomUUID().toString() + ".pdf";
            reportType = "PDF";
            generatePdfReport(products, filename); // ✅ Ya no se sobreescribe
        } else if ("excel".equalsIgnoreCase(format)) {
            filename = "inventario_actual_" + UUID.randomUUID().toString() + ".xlsx";
            reportType = "Excel";
            generateExcelReport(products, filename);
        } else {
            throw new IllegalArgumentException("Formato de informe no soportado: " + format);
        }

        String reportDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ReportDTO newReport = new ReportDTO(UUID.randomUUID().toString(), reportDate, reportType, filename);
        generatedReports.add(newReport);
        return newReport;
    }

    private void generateExcelReport(List<ProductoModel> products, String filename) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario Actual");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Nombre", "Descripción", "Cantidad", "Precio Unitario", "Categoría"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (ProductoModel product : products) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(product.getId());
            row.createCell(1).setCellValue(product.getNombreProducto());
            row.createCell(2).setCellValue(product.getDescripcionProducto());
            row.createCell(3).setCellValue(product.getCantidadProducto());
            row.createCell(4).setCellValue(product.getPrecioProducto());
            row.createCell(5).setCellValue(product.getCategoria() != null ? product.getCategoria().getNombre() : "N/A");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try (FileOutputStream fileOut = new FileOutputStream(uploadsDir.resolve(filename).toFile())) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }
    }

    private void generatePdfReport(List<ProductoModel> products, String filename) throws IOException {
        Path pdfPath = uploadsDir.resolve(filename);

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath.toFile()));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Reporte de Inventario Actual", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);

            String[] headers = {"ID", "Nombre", "Descripción", "Cantidad", "Precio", "Categoría"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

         for (ProductoModel product : products) {
    table.addCell(String.valueOf(product.getId()));
    table.addCell(product.getNombreProducto());
    table.addCell(product.getDescripcionProducto());
    table.addCell(String.valueOf(product.getCantidadProducto()));
    table.addCell(String.valueOf(product.getPrecioProducto()));
    table.addCell(product.getCategoria() != null ? product.getCategoria().getNombre() : "N/A");
}


            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            throw new IOException("Error al generar el PDF", e);
        }
    }
}
