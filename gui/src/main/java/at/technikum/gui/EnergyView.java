package at.technikum.gui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class EnergyView {

    private final VBox root = new VBox(10);
    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // Current Section
    private final Label communityPoolLabel = new Label("Community Pool: -");
    private final Label gridPortionLabel   = new Label("Grid Portion: -");

    // Historical Section
    private final TextField startField = new TextField("2025-01-10T13:00:00");
    private final TextField endField   = new TextField("2025-01-10T15:00:00");
    private final Label communityProducedLabel = new Label("Community produced: -");
    private final Label communityUsedLabel     = new Label("Community used: -");
    private final Label gridUsedLabel          = new Label("Grid used: -");

    public EnergyView() {
        root.setPadding(new Insets(15));

        // --- Current Section ---
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadCurrent());

        VBox currentBox = new VBox(5,
                new Label("=== Current Hour ==="),
                communityPoolLabel,
                gridPortionLabel,
                refreshBtn
        );

        // --- Historical Section ---
        Button showDataBtn = new Button("Show Data");
        showDataBtn.setOnAction(e -> loadHistorical());

        VBox histBox = new VBox(5,
                new Label("=== Historical Data ==="),
                new HBox(10, new Label("Start:"), startField),
                new HBox(10, new Label("End:  "), endField),
                showDataBtn,
                communityProducedLabel,
                communityUsedLabel,
                gridUsedLabel
        );

        root.getChildren().addAll(currentBox, new Separator(), histBox);

        // Beim Start gleich aktuelle Daten laden
        loadCurrent();
    }

    private void loadCurrent() {
        new Thread(() -> {
            try {
                String json = apiClient.fetchCurrent();
                JsonNode node = mapper.readTree(json);
                double depleted = node.get("communityDepleted").asDouble();
                double grid     = node.get("gridPortion").asDouble();

                Platform.runLater(() -> {
                    communityPoolLabel.setText("Community Pool: " + depleted + "% used");
                    gridPortionLabel.setText("Grid Portion: " + grid + "%");
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

                double totalProduced = 0, totalUsed = 0, totalGrid = 0;
                for (JsonNode entry : array) {
                    totalProduced += entry.get("communityProduced").asDouble();
                    totalUsed     += entry.get("communityUsed").asDouble();
                    totalGrid     += entry.get("gridUsed").asDouble();
                }

                double finalProduced = totalProduced;
                double finalUsed     = totalUsed;
                double finalGrid     = totalGrid;

                Platform.runLater(() -> {
                    communityProducedLabel.setText(
                            "Community produced: " + finalProduced + " kWh");
                    communityUsedLabel.setText(
                            "Community used: " + finalUsed + " kWh");
                    gridUsedLabel.setText(
                            "Grid used: " + finalGrid + " kWh");
                });
            } catch (Exception ex) {
                Platform.runLater(() ->
                        communityProducedLabel.setText("Fehler: " + ex.getMessage()));
            }
        }).start();
    }

    public VBox getRoot() {
        return root;
    }
}