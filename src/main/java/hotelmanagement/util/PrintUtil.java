package hotelmanagement.util;

import hotelmanagement.model.Reservation;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PrintUtil {

    public static void printReservation(Reservation reservation) {
        String pdfPath = "reservation_" + reservation.getId() + ".pdf";
        try {
            PdfWriter writer = new PdfWriter(pdfPath);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add header
            Paragraph header = new Paragraph("Hotel Management System")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(24)
                    .setBold()
                    .setMarginBottom(20);
            document.add(header);

            // Add title
            Paragraph title = new Paragraph("Invoice")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold()
                    .setMarginBottom(20);
            document.add(title);

            // Fetch additional details
            String clientName = getClientNameById(reservation.getClientId());
            String roomType = getRoomTypeById(reservation.getRoomId());
            double roomPrice = getRoomPriceById(reservation.getRoomId());
            double totalPayment = calculateTotalPayment(roomPrice, reservation.getStartDate(), reservation.getEndDate());

            // Add reservation details table
            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(20);

            table.addHeaderCell(new Cell().add(new Paragraph("Field").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setBorder(Border.NO_BORDER)));
            table.addHeaderCell(new Cell().add(new Paragraph("Details").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY).setBorder(Border.NO_BORDER)));

            table.addCell(new Cell().add(new Paragraph("Reservation ID")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(reservation.getId()))).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("Client Name")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(clientName)).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("Room Type")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(roomType)).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("Room Price")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("$" + roomPrice)).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("Total Payment")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph("$" + totalPayment)).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("Start Date")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(reservation.getStartDate().toString())).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("End Date")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(reservation.getEndDate().toString())).setBorder(Border.NO_BORDER));

            table.addCell(new Cell().add(new Paragraph("Confirmed")).setBorder(Border.NO_BORDER));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(reservation.isConfirmed()))).setBorder(Border.NO_BORDER));

            // Add total amount row
            table.addCell(new Cell(1, 2).add(new Paragraph("Total Amount: $" + totalPayment).setBold().setTextAlignment(TextAlignment.RIGHT)).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.LIGHT_GRAY));

            document.add(table);

            // Add footer
            Paragraph footer = new Paragraph("Thank you for choosing our service!")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setMarginTop(20);
            document.add(footer);

            document.close();
            System.out.println("PDF generated successfully: " + pdfPath);

            // Open the PDF file
            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("Desktop is not supported. Cannot open the PDF file.");
                }
            } else {
                System.out.println("PDF file does not exist.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getClientNameById(int clientId) {
        String clientName = "";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT name FROM clients WHERE id = " + clientId;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                clientName = rs.getString("name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clientName;
    }

    private static String getRoomTypeById(int roomId) {
        String roomType = "";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT type FROM rooms WHERE id = " + roomId;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                roomType = rs.getString("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomType;
    }

    private static double getRoomPriceById(int roomId) {
        double roomPrice = 0.0;
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT price FROM rooms WHERE id = " + roomId;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                roomPrice = rs.getDouble("price");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomPrice;
    }

    private static double calculateTotalPayment(double roomPrice, LocalDate startDate, LocalDate endDate) {
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        return roomPrice * daysBetween;
    }
}