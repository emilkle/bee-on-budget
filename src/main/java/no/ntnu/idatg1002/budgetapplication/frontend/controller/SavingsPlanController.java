package no.ntnu.idatg1002.budgetapplication.frontend.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SavingsPlanController {

  private Stage stage;
  private Scene scene;
  private Parent parent;
  private PrimaryController primaryController = new PrimaryController();
  public void switchToPrimaryFromSavingPlan(ActionEvent event) throws IOException {
      Parent root = FXMLLoader.load(getClass().getResource("/fxmlfiles/primary.fxml"));
      stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
  }
}
