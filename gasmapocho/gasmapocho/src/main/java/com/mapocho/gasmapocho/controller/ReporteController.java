package com.mapocho.gasmapocho.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.mapocho.gasmapocho.model.Cilindro;
import com.mapocho.gasmapocho.model.Movimiento;
import com.mapocho.gasmapocho.repository.CilindroRepository;
import com.mapocho.gasmapocho.repository.MovimientoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/reportes")
public class ReporteController {

    private final CilindroRepository cilindroRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteController(CilindroRepository cilindroRepository,
                             MovimientoRepository movimientoRepository) {
        this.cilindroRepository = cilindroRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("totalCilindros", cilindroRepository.count());
        model.addAttribute("totalMovimientos", movimientoRepository.count());
        return "reportes/index";
    }

    @GetMapping("/inventario")
    public void reporteInventario(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=inventario_cilindros.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        // Título
        doc.add(new Paragraph("REPORTE DE INVENTARIO - GAS MAPOCHO")
                .setBold().setFontSize(16).setMarginBottom(10));
        doc.add(new Paragraph("Generado: " +
                java.time.LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setFontSize(10).setMarginBottom(20));

        // Tabla
        Table tabla = new Table(UnitValue.createPercentArray(new float[]{3, 3, 2}))
                .useAllAvailableWidth();

        // Encabezados
        tabla.addHeaderCell(new Cell().add(new Paragraph("Tipo")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Estado")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Cantidad")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));

        // Datos
        List<Cilindro> cilindros = cilindroRepository.findAll();
        for (Cilindro c : cilindros) {
            tabla.addCell(c.getTipo().name().replace("KG_", "") + " kg");
            tabla.addCell(c.getEstado().name());
            tabla.addCell(String.valueOf(c.getCantidad()));
        }

        doc.add(tabla);
        doc.close();
    }

    @GetMapping("/movimientos")
    public void reporteMovimientos(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=reporte_movimientos.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        doc.add(new Paragraph("REPORTE DE MOVIMIENTOS - GAS MAPOCHO")
                .setBold().setFontSize(16).setMarginBottom(10));
        doc.add(new Paragraph("Generado: " +
                java.time.LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setFontSize(10).setMarginBottom(20));

        Table tabla = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 1, 2}))
                .useAllAvailableWidth();

        tabla.addHeaderCell(new Cell().add(new Paragraph("Fecha")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Tipo")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Cilindro")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Cantidad")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));
        tabla.addHeaderCell(new Cell().add(new Paragraph("Usuario")).setBold()
                .setBackgroundColor(ColorConstants.LIGHT_GRAY));

        List<Movimiento> movimientos = movimientoRepository.findTop20ByOrderByFechaHoraDesc();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (Movimiento m : movimientos) {
            tabla.addCell(m.getFechaHora().format(fmt));
            tabla.addCell(m.getTipo().name());
            tabla.addCell(m.getCilindro().getTipo().name().replace("KG_", "") + " kg");
            tabla.addCell(String.valueOf(m.getCantidad()));
            tabla.addCell(m.getUsuario().getNombre());
        }

        doc.add(tabla);
        doc.close();
    }
}