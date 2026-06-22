package at.technikum.gui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EnergyView {

    private final VBox root = new VBox(10);
    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Current Section
    private final Label communityPoolLabel = new Label("Community Pool: -");
    private final Label gridPortionLabel   = new Label("Grid Portion: -");
    private final Label currentHourLabel   = new Label("Hour: -");

    // Historical Section
    private final TextField startField = new TextField("2025-01-10T13:00:00");
    private final TextField endField   = new TextField("2025-01-10T15:00:00");
    private final Label summaryLabel = new Label("Summe: -");
    private final TableView<HourlyUsageRow> historicalTable = new TableView<>();

    public EnergyView() {
        root.setPadding(new Insets(15));

        // --- Current Section ---
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadCurrent());

        VBox currentBox = new VBox(5,
                new Label("=== Current Hour ==="),
                currentHourLabel,
                communityPoolLabel,
                gridPortionLabel,
                refreshBtn
        );

        // --- Historical Section ---
        Button showDataBtn = new Button("Show Data");
        showDataBtn.setOnAction(e -> loadHistorical());

        setupTableColumns();
        historicalTable.setPrefHeight(220);
        historicalTable.setPlaceholder(new Label("Keine Daten geladen."));

        VBox histBox = new VBox(5,
                new Label("=== Historical Data ==="),
                new HBox(10, new Label("Start:"), startField),
                new HBox(10, new Label("End:  "), endField),
                showDataBtn,
                historicalTable,
                summaryLabel
        );

        root.getChildren().addAll(currentBox, new Separator(), histBox);

        loadCurrent();
    }

    private void setupTableColumns() {
        TableColumn<HourlyUsageRow, String> hourCol = new TableColumn<>("Hour");
        hourCol.setCellValueFactory(new PropertyValueFactory<>("hour"));
        hourCol.setPrefWidth(160);

        TableColumn<HourlyUsageRow, Double> producedCol = new TableColumn<>("Produced (kWh)");
        producedCol.setCellValueFactory(new PropertyValueFactory<>("communityProduced"));
        producedCol.setPrefWidth(120);

        TableColumn<HourlyUsageRow, Double> usedCol = new TableColumn<>("Used (kWh)");
        usedCol.setCellValueFactory(new PropertyValueFactory<>("communityUsed"));
        usedCol.setPrefWidth(100);

        TableColumn<HourlyUsageRow, Double> gridCol = new TableColumn<>("Grid (kWh)");
        gridCol.setCellValueFactory(new PropertyValueFactory<>("gridUsed"));
        gridCol.setPrefWidth(100);

        historicalTable.getColumns().setAll(hourCol, producedCol, usedCol, gridCol);
    }

    private void loadCurrent() {
        new Thread(() -> {
            try {
                String json = apiClient.fetchCurrent();
                JsonNode node = mapper.readTree(json);
                double depleted = node.get("communityDepleted").asDouble();
                double grid     = node.get("gridPortion").asDouble();
                String hour     = node.get("hour").asText();

                Platform.runLater(() -> {
                    currentHourLabel.setText("Hour: " + hour);
                    communityPoolLabel.setText("Community Pool: " + String.format("%.2f", depleted) + "% depleted");
                    gridPortionLabel.setText("Grid Portion: " + String.format("%.2f", grid) + "%");
                });
            } catch (Exception ex) {
                Platform.runLater(() ->
                        communityPoolLabel.setText("Fehler: " + ex.getMessage()));
            }
        }).start();
    }

    private void loadHistorical() {
        new Thread(() -> {
            try {
                String json = apiClient.fetchHistorical(
                        startField.getText(), endField.getText());
                JsonNode array = mapper.readTree(json);

                ObservableList<HourlyUsageRow> rows = FXCollections.observableArrayList();
                double totalProduced = 0, totalUsed = 0, totalGrid = 0;
                for (JsonNode entry : array) {
                    double produced = entry.get("communityProduced").asDouble();
                    double used     = entry.get("communityUsed").asDouble();
                    double grid     = entry.get("gridUsed").asDouble();
                    totalProduced += produced;
                    totalUsed     += used;
                    totalGrid     += grid;
                    rows.add(new HourlyUsageRow(entry.get("hour").asText(), produced, used, grid));
                }

                final double p = totalProduced, u = totalUsed, g = totalGrid;
                Platform.runLater(() -> {
                    historicalTable.setItems(rows);
                    summaryLabel.setText(String.format(
                            "Summe: Produced %.3f kWh | Used %.3f kWh | Grid %.3f kWh", p, u, g));
                });

            } catch (Exception ex) {
                Platform.runLater(() -> {
                    historicalTable.setItems(FXCollections.observableArrayList());
                    historicalTable.setPlaceholder(new Label("Fehler: " + ex.getMessage()));
                });
            }
        }).start();
    }

    public VBox getRoot() {
        return root;
    }

    //Zeilen-Modell für die TableView.

    public static class HourlyUsageRow {
        private final SimpleStringProperty hour;
        private final SimpleDoubleProperty communityProduced;
        private final SimpleDoubleProperty communityUsed;
        private final SimpleDoubleProperty gridUsed;

        public HourlyUsageRow(String hour, double communityProduced, double communityUsed, double gridUsed) {
            this.hour = new SimpleStringProperty(hour);
            this.communityProduced = new SimpleDoubleProperty(communityProduced);
            this.communityUsed = new SimpleDoubleProperty(communityUsed);
            this.gridUsed = new SimpleDoubleProperty(gridUsed);
        }

        public String getHour() { return hour.get(); }
        public double getCommunityProduced() { return communityProduced.get(); }
        public double getCommunityUsed() { return communityUsed.get(); }
        public double getGridUsed() { return gridUsed.get(); }
    }
}