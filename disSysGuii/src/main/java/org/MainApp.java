public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        EnergyView view = new EnergyView();
        Scene scene = new Scene(view.getRoot(), 500, 400);
        stage.setTitle("Energy Community Monitor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}