package no.ntnu.idatg1002.budgetapplication.frontend.dialogs;

import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import no.ntnu.idatg1002.budgetapplication.backend.Expense;
import no.ntnu.idatg1002.budgetapplication.backend.ExpenseCategory;
import no.ntnu.idatg1002.budgetapplication.backend.RecurringType;

/**
 * Represents a custom dialog for adding an expense in the budget application. The dialog includes
 * fields for entering expense details, such as amount, description, recurring type, and expense
 * category.
 *
 * @author Erik Bjørnsen, Eskil Alstad
 * @version 1.2
 */
public class AddExpenseDialog extends Dialog<Expense> {
  Expense newExpense;

  @FXML private TextField expenseAmountField;
  @FXML private TextField expenseDescriptionField;
  @FXML private ComboBox<ExpenseCategory> categoryComboBox;
  @FXML private ComboBox<RecurringType> recurringIntervalComboBox;
  @FXML private Button cancelButton;

  /**
   * Constructs an AddExpenseDialog, setting up the user interface components and necessary input
   * validation.
   */
  public AddExpenseDialog() {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmlfiles/addExpenseDialog.fxml"));
    loader.setController(this);
    DialogPane dialogPane = new DialogPane();
    try {
      dialogPane.setContent(loader.load());
    } catch (IOException e) {
      e.printStackTrace();
    }
    String css =
        Objects.requireNonNull(this.getClass().getResource("/cssfiles/dialog.css"))
            .toExternalForm();
    dialogPane.getStylesheets().add(css);

    this.setDialogPane(dialogPane);
    this.setTitle("Add Expense");

    // adds enums to combo boxes
    recurringIntervalComboBox.getItems().addAll(RecurringType.values());
    categoryComboBox.getItems().addAll(ExpenseCategory.values());

    configureExpenseAmountField();
    configureExpenseDescriptionField();
  }

  @FXML
  private void closeDialog() {
    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  private void handleSubmit() {
    if (assertAllFieldsValid()) {
      newExpense =
          new Expense(
              Integer.parseInt(getExpenseAmountField()),
              getExpenseDescriptionField(),
              getRecurringIntervalComboBox(),
              getExpenseCategoryComboBox());
      this.setResult(newExpense);
      this.close();
    } else {
      generateDynamicFeedbackAlert();
    }
  }

  /**
   * Configures the expenseAmountField to only accept numeric input. If a non-numeric character is
   * entered, it is removed from the input.
   */
  private void configureExpenseAmountField() {
    expenseAmountField
        .textProperty()
        .addListener(
            (observableValue, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) {
                expenseAmountField.setText(newValue.replaceAll("[^\\d]", ""));
              }
            });
  }

  /**
   * Configures the expenseDescriptionField to prevent input from starting with a space. If a space
   * is entered at the beginning of the input, it is removed.
   */
  private void configureExpenseDescriptionField() {
    expenseDescriptionField
        .textProperty()
        .addListener(
            (observableValue, oldValue, newValue) -> {
              if ((oldValue.isEmpty() || oldValue.isBlank()) && newValue.matches(" ")) {
                expenseDescriptionField.clear();
              }
            });
  }

  /**
   * Returns the text from the expense description input field.
   *
   * @return the description of the expense
   */
  private String getExpenseDescriptionField() {
    return expenseDescriptionField.getText();
  }

  /**
   * Returns the text from the expense amount input field.
   *
   * @return the amount of the expense
   */
  private String getExpenseAmountField() {
    return expenseAmountField.getText();
  }

  /**
   * Returns the selected value from the recurring interval combo box.
   *
   * @return the selected recurring type of the expense
   */
  private RecurringType getRecurringIntervalComboBox() {
    return recurringIntervalComboBox.getValue();
  }

  /**
   * Returns the selected value from the expense category combo box.
   *
   * @return the selected expense category of the expense
   */
  private ExpenseCategory getExpenseCategoryComboBox() {
    return categoryComboBox.getValue();
  }

  /**
   * Verifies that all input fields have valid values.
   *
   * @return true if all input fields are valid, false otherwise
   */
  private boolean assertAllFieldsValid() {
    return (!expenseDescriptionField.getText().isEmpty()
        && expenseAmountField.getText() != null
        && recurringIntervalComboBox.getValue() != null
        && categoryComboBox.getValue() != null);
  }

  /**
   * Generates an alert that gives feedback to the user of what fields still needs to be filled out.
   *
   * <p>The following fields are checked for completeness:
   *
   * <ul>
   *   <li>Amount
   *   <li>Description
   *   <li>Recurring interval
   *   <li>Category
   * </ul>
   */
  private void generateDynamicFeedbackAlert() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Error");
    alert.setHeaderText(null);

    StringBuilder builder = new StringBuilder("Please fill out the following field(s): \n");

    if (expenseAmountField.getText().isEmpty()) {
      builder.append("Amount \n");
    }
    if (expenseDescriptionField.getText().isEmpty()) {
      builder.append("Description \n");
    }
    if (recurringIntervalComboBox.getValue() == null) {
      builder.append("Recurring interval \n");
    }
    if (categoryComboBox.getValue() == null) {
      builder.append("Category \n");
    }

    alert.setContentText(builder.toString());
    alert.initModality(Modality.NONE);
    alert.initOwner(this.getDialogPane().getScene().getWindow());
    alert.showAndWait();
  }
}
