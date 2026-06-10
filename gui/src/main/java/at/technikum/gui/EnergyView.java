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
    private final Label currentHourLabel   = new Label("Hour: -");

    // Historical Section
    private final TextField startField = new TextField("2025-01-10T13:00:00");
    private final TextField endField   = new TextField("2025-01-10T15:00:00");
    private final VBox historicalResultBox = new VBox(4);

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

        ScrollPane scrollPane = new ScrollPane(historicalResultBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);

        VBox histBox = new VBox(5,
                new Label("=== Historical Data ==="),
                new HBox(10, new Label("Start:"), startField),
                new HBox(10, new Label("End:  "), endField),
                showDataBtn,
                scrollPane
        );

        root.getChildren().addAll(currentBox, new Separator(), histBox);

        loadCurrent();
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

                Platform.runLater(() -> {
                    historicalResultBox.getChildren().clear();

                    if (array.isEmpty()) {
                        historicalResultBox.getChildren().add(new Label("Keine Daten gefunden."));
                        return;
                    }

                    // Tabellen-Header
                    HBox header = new HBox(10,
                            styledLabel("Hour", 160),
                            styledLabel("Produced (kWh)", 120),
                            styledLabel("Used (kWh)", 100),
                            styledLabel("Grid (kWh)", 100)
                    );
                    historicalResultBox.getChildren().add(header);
                    historicalResultBox.getChildren().add(new Separator());

                    for (JsonNode entry : array) {
                        String hour     = entry.get("hour").asText();
                        double produced = entry.get("communityProduced").asDouble();
                        double used     = entry.get("communityUsed").asDouble();
                        double grid     = entry.get("gridUsed").asDouble();

                        HBox row = new HBox(10,
                                styledLabel(hour, 160),
                                styledLabel(String.format("%.4f", produced), 120),
                                styledLabel(String.format("%.4f", used), 100),
                                styledLabel(String.format("%.4f", grid), 100)
                        );
                        historicalResultBox.getChildren().add(row);
                    }
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    historicalResultBox.getChildren().clear();
                    historicalResultBox.getChildren().add(new Label("Fehler: " + ex.getMessage()));
                });
            }
        }).start();
    }

    private Label styledLabel(String text, double width) {
        Label lbl = new Label(text);
        lbl.setMinWidth(width);
        lbl.setMaxWidth(width);
        return lbl;
    }

    public VBox getRoot() {
        return root;
    }
}