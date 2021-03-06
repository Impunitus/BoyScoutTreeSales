//*********************************************************************
//  COPYRIGHT 2016
//    College at Brockport, State University of New York.
//    ALL RIGHTS RESERVED
//
// This file is the product of The College at Brockport and cannot
// be reproduced, copied, or used in any shape or form without
// the express written consent of The College at Brockport.
//********************************************************************
package userinterface;

import impresario.IModel;
import java.util.Properties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author mike
 */
public class ScoutFormView extends BaseView {
    
    protected TextField firstNameField;
    protected TextField middleNameField;
    protected TextField lastNameField;
    protected TextField dobField;
    protected TextField phoneNumField;
    protected TextField emailField;
    protected TextField troopIdField;
    protected ComboBox statusBox;
    protected Button submitButton;
//    protected Button clearFormButton;
    protected Button cancelButton;

    public ScoutFormView(IModel model) {
        super(model, "ScoutFormView");
        
        myModel.subscribe("UpdateStatusMessage", this);
    }

    @Override
    protected Node createContent() {
        EventHandler<ActionEvent> submitHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processAction(event);
            }
        };
        
        IFormItemStrategy formItemBuilder = FormItemFactory.getFormItem("TopAlignFormItem");
        Pane formItem;
        
        VBox content = new VBox(25);
        content.setFillWidth(true);
        content.setAlignment(Pos.CENTER);
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(25.0, 25.0, 25.0, 25.0));
        
        firstNameField = new TextField();
        firstNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("firstNameField"),
                firstNameField
        );
        formItem.setPrefWidth(200);
        formGrid.add(formItem, 0, 0);
        
        middleNameField = new TextField();
        middleNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("middleNameField"),
                middleNameField
        );
        formItem.setPrefWidth(200);
        formGrid.add(formItem, 0, 1);
        
        lastNameField = new TextField();
        lastNameField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("lastNameField"),
                lastNameField
        );
        formItem.setPrefWidth(350);
        formGrid.add(formItem, 0, 2);
        
        troopIdField = new TextField();
        troopIdField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("troopIdField"),
                troopIdField
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 3);
        
        dobField = new TextField();
        dobField.setOnAction(submitHandler);
        dobField.setPromptText(myResources.getProperty("datePrompt"));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("dobField"),
                dobField
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 4);
        
        phoneNumField = new TextField();
        phoneNumField.setOnAction(submitHandler);
        phoneNumField.setPromptText(myResources.getProperty("phoneNumPrompt"));
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("phoneNumField"),
                phoneNumField
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 5);
        
        emailField = new TextField();
        emailField.setOnAction(submitHandler);
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("emailField"),
                emailField
        );
        formItem.setPrefWidth(350);
        formGrid.add(formItem, 0, 6);
        
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Active",
                "Inactive"
        );
        statusBox = new ComboBox(statusOptions);
        statusBox.setValue("Active");
        formItem = formItemBuilder.buildControl(
                myResources.getProperty("statusBox"),
                statusBox
        );
        formItem.setPrefWidth(150);
        formGrid.add(formItem, 0, 7);
        
        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER);
        
        submitButton = new Button(myResources.getProperty("submitButton"));
        submitButton.setOnAction(submitHandler);
        submitButton.setPrefWidth(100);
        buttonContainer.getChildren().add(submitButton);
        
//        clearFormButton = new Button("Clear Form");
//        clearFormButton.setOnAction(submitHandler);
//        clearFormButton.setPrefWidth(100);
//        buttonContainer.getChildren().add(clearFormButton);
        
        cancelButton = new Button(myResources.getProperty("cancelButton"));
        cancelButton.setOnAction(submitHandler);
        cancelButton.setPrefWidth(100);
        buttonContainer.getChildren().add(cancelButton);
        
        content.getChildren().add(formGrid);
        content.getChildren().add(buttonContainer);
        
        return content;
    }
    
    protected void processAction(Event event) {
        clearErrorMessage();
        
        if (event.getSource() == cancelButton) {
            myModel.stateChangeRequest("Cancel", "");
        }
        //else if (event.getSource() == clearFormButton) {
        //    clearForm();
        //}
        else {
            // Verify information in fields
            if (validate()) {
                // Submit data
                Properties newScoutData = new Properties();
                newScoutData.setProperty("FirstName", firstNameField.getText());
                newScoutData.setProperty("MiddleName", middleNameField.getText());
                newScoutData.setProperty("LastName", lastNameField.getText());
                newScoutData.setProperty("TroopID", troopIdField.getText());
                newScoutData.setProperty("DateOfBirth", dobField.getText());
                newScoutData.setProperty("PhoneNumber", phoneNumField.getText());
                newScoutData.setProperty("Email", emailField.getText());
                newScoutData.setProperty("ScoutStatus", (String)statusBox.getValue());
                
                myModel.stateChangeRequest("Submit", newScoutData);
            }
        }
    }
    
    protected boolean validate() {
        // First Name is NOT NULL
        String value = firstNameField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errFirstNameNull"));
            firstNameField.requestFocus();
            return false;
        }
        
        // Last Name is NOT NULL
        value = lastNameField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errLastNameNull"));
            lastNameField.requestFocus();
            return false;
        }
        
        value = troopIdField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errTroopIdNull"));
            troopIdField.requestFocus();
            return false;
        }
        
        // Date of Birth is not null, and must match format
        value = dobField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errDOBNull"));
            dobField.requestFocus();
            return false;
        }
        if (!value.matches(myResources.getProperty("dateFormat"))) {
            displayErrorMessage(myResources.getProperty("errDOBFormat"));
            dobField.requestFocus();
            return false;
        }
        
        // Phone number is not null and must match format
        value = phoneNumField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errPhoneNumNull"));
            phoneNumField.requestFocus();
            return false;
        }
        if (!value.matches(myResources.getProperty("phoneNumFormat"))) {
            displayErrorMessage(myResources.getProperty("errPhoneNumFormat"));
            phoneNumField.requestFocus();
            return false;
        }
        
        // Email is not null
        value = emailField.getText();
        if ((value == null) || "".equals(value)) {
            displayErrorMessage(myResources.getProperty("errEmailNull"));
            phoneNumField.requestFocus();
            return false;
        }
        
        return true;
    }   
    
    /*
    private void clearForm() {
        firstNameField.setText("");
        middleNameField.setText("");
        lastNameField.setText("");
        dobField.setText("");
        phoneNumField.setText("");
        emailField.setText("");
        troopIdField.setText("");
        
        this.requestLayout();
    }
    */
    @Override
    public void updateState(String key, Object value) {
        if (key.equals("UpdateStatusMessage")) {
            displayMessage((String)myModel.getState("UpdateStatusMessage"));
        }
    }
}
