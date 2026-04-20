public class EnergyView {
    private final VBox root = new VBox(10);
    private final ApiClient apiClient = new ApiClient();

    // Current data labels
    private final Label communityPoolLabel = new Label("Community Pool: -");
    private final Label gridPortionLabel   = new Label("Grid Portion: -");

    // Historical data
    private final TextField startField = new TextField("2025-01-10T13:00:00");
    private final TextField endField   = new TextField("2025-01-10T15:00:00");
    private final TextArea  historyArea = new TextArea();

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
        historyArea.setPrefHeight(150);
        historyArea.setEditable(false);

        VBox histBox = new VBox(5,
                new Label("=== Historical Data ==="),
                new HBox(10, new Label("Start:"), startField),
                new HBox(10, new Label("End:  "), endField),
                showDataBtn,
                historyArea
        );

        root.getChildren().addAll(currentBox, new Separator(), histBox);
        loadCurrent(); // beim Start gleich laden
    }

    private void loadCurrent() {
        new Thread(() -> {
            try {
                String json = apiClient.fetchCurrent();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(json);
                double depleted = node.get("communityDepleted").asDouble();
                double grid     = node.get("gridPortion").asDouble();

                Platform.runLater(() -> {
                    communityPoolLabel.setText("Community Pool: " + depleted + "% used");
                    gridPortionLabel.setText("Grid Portion: " + grid + "%");
                });
            } catch (Exception ex) {
                Platform.runLater(() ->
                        communityPoolLabel.setText("Error: " + ex.getMessage()));
            }
        }).start();
    }

    private void loadHistorical() {
        new Thread(() -> {
            try {
                String json = apiClient.fetchHistorical(
                        startField.getText(), endField.getText());
                Platform.runLater(() -> historyArea.setText(json));
            } catch (Exception ex) {
                Platform.runLater(() -> historyArea.setText("Error: " + ex.getMessage()));
            }
        }).start();
    }

    public VBox getRoot() { return root; }
}