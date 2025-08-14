package bmasec2.bmaapplication;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfViewController {

    @FXML private TableView<InvoiceItem> tableView;
    @FXML private TableColumn<InvoiceItem, Integer> serialNoColumn;
    @FXML private TableColumn<InvoiceItem, String> itemNameColumn;
    @FXML private TableColumn<InvoiceItem, Integer> qtyColumn;
    @FXML private TableColumn<InvoiceItem, Double> priceColumn;

    @FXML private Label tokenNoLabel;
    @FXML private Label subTotalLabel;
    @FXML private Label vatLabel;
    @FXML private Label discountLabel;
    @FXML private Label totalLabel;
    @FXML private Label dateLabel;
    @FXML private Label timeLabel;

    private ObservableList<InvoiceItem> invoiceItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        serialNoColumn.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        invoiceItems.add(new InvoiceItem(1, "Burger", 2, 120.0));
        invoiceItems.add(new InvoiceItem(2, "Pizza", 1, 250.0));
        invoiceItems.add(new InvoiceItem(3, "Coffee", 1, 80.0));
        tableView.setItems(invoiceItems);


        calculateTotals();


        updateDateTime();


        tokenNoLabel.setText("Token No.: " + generateTokenNumber());
    }

    private void calculateTotals() {
        double subtotal = invoiceItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        double vat = subtotal * 0.15;
        double discount = subtotal * 0.05;
        double total = subtotal + vat - discount;

        subTotalLabel.setText(String.format("SubTotal: %.2f", subtotal));
        vatLabel.setText(String.format("VAT (15%%): %.2f", vat));
        discountLabel.setText(String.format("Discount (5%%): %.2f", discount));
        totalLabel.setText(String.format("Total: %.2f", total));
    }

    private void updateDateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        dateLabel.setText("Date: " + dateFormatter.format(now));
        timeLabel.setText("Time: " + timeFormatter.format(now));
    }

    private String generateTokenNumber() {
        return "T" + System.currentTimeMillis() % 10000;
    }

    @FXML
    public void printBtnOnAction(ActionEvent event) throws FileNotFoundException {
        try {

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("file.pdf"));
            document.open();


            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
            Paragraph header = new Paragraph("IUB Cafeteria", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);

            Font subHeaderFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph subHeader1 = new Paragraph("Independent University, Bangladesh", subHeaderFont);
            Paragraph subHeader2 = new Paragraph("Plot-16, Aftab Uddin Ahmed Road, Block B", subHeaderFont);
            Paragraph subHeader3 = new Paragraph("Bashundhara, R/A, Dhaka", subHeaderFont);
            subHeader1.setAlignment(Element.ALIGN_CENTER);
            subHeader2.setAlignment(Element.ALIGN_CENTER);
            subHeader3.setAlignment(Element.ALIGN_CENTER);
            document.add(subHeader1);
            document.add(subHeader2);
            document.add(subHeader3);


            Font detailFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph token = new Paragraph("\n" + tokenNoLabel.getText(), detailFont);
            token.setAlignment(Element.ALIGN_CENTER);
            document.add(token);


            Font dateTimeFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            Paragraph dateTime = new Paragraph(dateLabel.getText() + "   " + timeLabel.getText(), dateTimeFont);
            dateTime.setAlignment(Element.ALIGN_CENTER);
            document.add(dateTime);


            PdfPTable pdfTable = new PdfPTable(4);
            pdfTable.setWidthPercentage(100);
            pdfTable.setSpacingBefore(20);
            pdfTable.setSpacingAfter(20);


            pdfTable.addCell(new Phrase("#", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            pdfTable.addCell(new Phrase("Item Name", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            pdfTable.addCell(new Phrase("Qty", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            pdfTable.addCell(new Phrase("Price", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));


            for (InvoiceItem item : invoiceItems) {
                pdfTable.addCell(String.valueOf(item.getSerialNo()));
                pdfTable.addCell(item.getItemName());
                pdfTable.addCell(String.valueOf(item.getQuantity()));
                pdfTable.addCell(String.format("%.2f", item.getPrice()));
            }

            document.add(pdfTable);


            Font totalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph(subTotalLabel.getText(), totalFont));
            document.add(new Paragraph(vatLabel.getText(), totalFont));
            document.add(new Paragraph(discountLabel.getText(), totalFont));
            document.add(new Paragraph(totalLabel.getText(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));


            Paragraph footer = new Paragraph("\nThank you for dining with us!",
                    FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();


        } finally {

        }
    }

}

