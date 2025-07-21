package View;

import Model.Configuration;
import Model.TicketPool;
import Model.Vendor;
import Model.Customer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TicketSystemApp extends Application {

    private TextField eventNameField;
    private TextField ticketPriceField;
    private TextField totalTicketsField;
    private TextField ticketReleaseRateField;
    private TextField customerRetrievalRateField;
    private TextField maxTicketCapacityField;
    private TextField numberOfVendorsField;
    private TextField numberOfCustomersField;
    private TextArea statusArea;
    private ProgressBar ticketProgressBar;
    private Label progressLabel;

    private Configuration configuration;
    private TicketPool ticketPool;
    private List<Thread> vendorThreads;
    private List<Thread> customerThreads;
    private boolean isRunning = false;
    private int elapsedSeconds = 0;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ticket Management System");

        // Title and Header
        Label headerLabel = new Label("Ticket Management System");
        headerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Input fields with styling
        eventNameField = new TextField();
        ticketPriceField = new TextField();
        totalTicketsField = new TextField();
        ticketReleaseRateField = new TextField();
        customerRetrievalRateField = new TextField();
        maxTicketCapacityField = new TextField();
        numberOfVendorsField = new TextField();
        numberOfCustomersField = new TextField();

        applyFieldStyles(eventNameField, "Enter event name");
        applyFieldStyles(ticketPriceField, "Enter ticket price");
        applyFieldStyles(totalTicketsField, "Enter total tickets");
        applyFieldStyles(ticketReleaseRateField, "Enter release rate (ms)");
        applyFieldStyles(customerRetrievalRateField, "Enter retrieval rate (ms)");
        applyFieldStyles(maxTicketCapacityField, "Enter max ticket capacity");
        applyFieldStyles(numberOfVendorsField, "Enter number of vendors");
        applyFieldStyles(numberOfCustomersField, "Enter number of customers");

        // Labels and layout
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.setPadding(new Insets(15));
        inputGrid.addRow(0, new Label("Event Name:"), eventNameField);
        inputGrid.addRow(1, new Label("Ticket Price:"), ticketPriceField);
        inputGrid.addRow(2, new Label("Total Tickets:"), totalTicketsField);
        inputGrid.addRow(3, new Label("Ticket Release Rate (ms):"), ticketReleaseRateField);
        inputGrid.addRow(4, new Label("Customer Retrieval Rate (ms):"), customerRetrievalRateField);
        inputGrid.addRow(5, new Label("Max Ticket Capacity:"), maxTicketCapacityField);
        inputGrid.addRow(6, new Label("Number of Vendors:"), numberOfVendorsField);
        inputGrid.addRow(7, new Label("Number of Customers:"), numberOfCustomersField);

        // Buttons
        Button startButton = new Button("Start");
        Button stopButton = new Button("Stop");
        Button resetButton = new Button("Reset");
        startButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        stopButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        resetButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white;");

        startButton.setOnAction(e -> startSystem());
        stopButton.setOnAction(e -> stopSystem());
        resetButton.setOnAction(e -> resetSystem());

        HBox buttonBox = new HBox(15, startButton, stopButton, resetButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Progress bar and label for tickets
        ticketProgressBar = new ProgressBar(0);
        ticketProgressBar.setPrefWidth(400);
        progressLabel = new Label("0% sold");
        VBox progressBox = new VBox(10, ticketProgressBar, progressLabel);
        progressBox.setAlignment(Pos.CENTER);

        // Status area
        statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setPrefHeight(200);
        statusArea.setStyle("-fx-control-inner-background: #f7f9f9; -fx-font-family: 'Courier New'; -fx-font-size: 14px; -fx-text-fill: green;");

        ScrollPane statusScrollPane = new ScrollPane(statusArea);
        statusScrollPane.setFitToWidth(true);
        statusScrollPane.setFitToHeight(true);

        // Main layout
        VBox mainLayout = new VBox(20, headerLabel, inputGrid, buttonBox, new Label("Ticket Progress:"), progressBox, new Label("Status:"), statusScrollPane);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #ecf0f1;");

        // Scene and stage
        Scene scene = new Scene(mainLayout, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void applyFieldStyles(TextField field, String promptText) {
        field.setPromptText(promptText);
        field.setStyle("-fx-background-color: #f7f9f9; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-padding: 5;");
    }

    private void startSystem() {
        if (isRunning) {
            appendStatus("System is already running.");
            return;
        }

        try {
            // Parse input values
            String eventName = eventNameField.getText();
            BigDecimal ticketPrice = new BigDecimal(ticketPriceField.getText());
            int totalTickets = Integer.parseInt(totalTicketsField.getText());
            int ticketReleaseRate = Integer.parseInt(ticketReleaseRateField.getText());
            int customerRetrievalRate = Integer.parseInt(customerRetrievalRateField.getText());
            int maxTicketCapacity = Integer.parseInt(maxTicketCapacityField.getText());
            int numberOfVendors = Integer.parseInt(numberOfVendorsField.getText());
            int numberOfCustomers = Integer.parseInt(numberOfCustomersField.getText());

            // Create configuration and ticket pool
            configuration = new Configuration(eventName, ticketPrice, totalTickets, ticketReleaseRate,
                    customerRetrievalRate, maxTicketCapacity, numberOfVendors, numberOfCustomers);
            ticketPool = new TicketPool(maxTicketCapacity, totalTickets, eventName, ticketPrice);

            // Start vendor threads
            vendorThreads = new ArrayList<>();
            for (int i = 0; i < numberOfVendors; i++) {
                int ticketsToProduce = totalTickets / numberOfVendors + (i < totalTickets % numberOfVendors ? 1 : 0);
                Vendor vendor = new Vendor(ticketPool, ticketsToProduce, ticketReleaseRate, i + 1);
                Thread vendorThread = new Thread(vendor, "Vendor-" + (i + 1));
                vendorThreads.add(vendorThread);
                vendorThread.start();
            }

            // Start customer threads
            customerThreads = new ArrayList<>();
            for (int i = 0; i < numberOfCustomers; i++) {
                Customer customer = new Customer(ticketPool, customerRetrievalRate, i + 1);
                Thread customerThread = new Thread(customer, "Customer-" + (i + 1));
                customerThreads.add(customerThread);
                customerThread.start();
            }

            isRunning = true;
            elapsedSeconds = 0; // Reset time counter
            appendStatus("System started successfully.");

            // Monitor real-time status
            new Thread(this::monitorStatus).start();

        } catch (Exception e) {
            appendStatus("Error: " + e.getMessage());
        }
    }

    private void stopSystem() {
        if (!isRunning) {
            appendStatus("System is not running.");
            return;
        }

        isRunning = false;

        // Interrupt all threads
        for (Thread thread : vendorThreads) {
            thread.interrupt();
        }

        for (Thread thread : customerThreads) {
            thread.interrupt();
        }

        appendStatus("System stopped.");
    }

    private void resetSystem() {
        stopSystem();
        Platform.runLater(() -> {
            ticketProgressBar.setProgress(0);
            progressLabel.setText("0% sold");
            statusArea.clear();
            appendStatus("System reset successfully.");
        });
    }

    private void monitorStatus() {
        int totalTickets = configuration.totalTickets;

        while (isRunning) {
            Platform.runLater(() -> {
                int remainingTickets = ticketPool.getRemainingTickets();
                int ticketsSold = totalTickets - remainingTickets;
                double progress = (double) ticketsSold / totalTickets;

                ticketProgressBar.setProgress(progress);
                progressLabel.setText(String.format("%.1f%% sold", progress * 100));

                appendStatus("Remaining Tickets: " + remainingTickets);
                appendStatus("Tickets in Pool: " + ticketPool.getCurrentPoolSize());
            });

            elapsedSeconds++;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void appendStatus(String message) {
        Platform.runLater(() -> statusArea.appendText(message + "\n"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}