package com.miniproject.library.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import com.miniproject.library.entity.Book;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BookReport {

    public byte[] generateBookReport(List<Book> bookReport) throws DocumentException {
        Document document = new Document(PageSize.A2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        try {
            document.open();

            // Your existing PDF generation logic
            Font fontTitle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
            fontTitle.setSize(20);

            Paragraph paragraph1 = new Paragraph("Book Report", fontTitle);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph1);

            PdfPTable table = createTable();
            populateTable(table, bookReport);
            document.add(table);

        } finally {
            // Close the document in a finally block
            if (document.isOpen()) {
                document.close();
            }
        }
        return baos.toByteArray();
    }

    private PdfPTable createTable() {
        PdfPTable table = new PdfPTable(9);
        // Setting width of the table, its columns, and spacing
        table.setWidthPercentage(100f);
        table.setSpacingBefore(5);

        // Set the widths of individual columns in the table
        float[] columnWidths = {50f, 150f, 100f, 150f, 100f, 100f, 50f, 50f, 60f};
        table.setTotalWidth(columnWidths);
        table.setLockedWidth(true); // Lock the width of the table

        // Add headers to the table
        addTableHeaders(table);

        return table;
    }

    private void addTableHeaders(PdfPTable table) {
        String[] headers = {"No", "Title", "Author", "Summary", "Publication Date", "Publisher", "Stock", "Read", "Wishlist"};
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        font.setColor(Color.WHITE);

        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(Color.DARK_GRAY);
            headerCell.setPadding(5);
            headerCell.setPhrase(new Phrase(header, font));
            headerCell.setUseAscender(true);
            headerCell.setUseDescender(true);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }
    }

    private void populateTable(PdfPTable table, List<Book> bookReport) {
        int idCounter = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("en", "EN"));

        for (Book book : bookReport) {
            PdfPCell cell = new PdfPCell();
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            cell.setPhrase(new Phrase(String.valueOf(idCounter)));
            table.addCell(cell);

            cell.setPhrase(new Phrase(book.getTitle()));
            table.addCell(cell);

            cell.setPhrase(new Phrase(book.getAuthor()));
            table.addCell(cell);

            cell.setPhrase(new Phrase(book.getSummary()));
            table.addCell(cell);

            String formattedDate = dateFormat.format(book.getPublicationDate());
            cell.setPhrase(new Phrase(formattedDate));
            table.addCell(cell);

            cell.setPhrase(new Phrase(book.getPublisher()));
            table.addCell(cell);

            cell.setPhrase(new Phrase(String.valueOf(book.getStock())));
            table.addCell(cell);

            cell.setPhrase(new Phrase(String.valueOf(book.getRead())));
            table.addCell(cell);

            cell.setPhrase(new Phrase(String.valueOf(book.getWishlist())));
            table.addCell(cell);

            idCounter++;
        }
    }

}