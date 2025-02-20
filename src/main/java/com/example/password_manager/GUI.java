package com.example.password_manager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GUI implements Initializable {
    private ArrayList<String> usages = new ArrayList<>();
    private String current_usage;
    private Database database = new Database();
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;
    @FXML
    private TextField usageField;
    @FXML
    private Label header_label;
    @FXML
    private AnchorPane secondary_anchorpane;
    @FXML
    private ListView<String> passwords_listview;
    @FXML
    private Label password_label;
    @FXML
    private Button remove_current_button;

    @FXML
    private void on_button_add(){

        if (is_valid_input(usageField.getText(),passwordField.getText(),repeatPasswordField.getText())) {

            database.insertPassword(usageField.getText(), passwordField.getText());

            database.printAllPasswords();
            usages.add(usageField.getText());

            add_item_to_password_listview();

        }else{
            passwordField.clear();
            repeatPasswordField.clear();
            header_label.setText("passwords not equal");

        }

    }//end of on_button_add

    private boolean is_valid_input(String usage,  String password1, String password2){
        if(usage != "" && password1 != "" && password2 != "" && password1.equals(password2)){
            return true;
        }else{
            return false;
        }
    }//end of is_valid_input

    @FXML
    private void on_open_second_window_button(){
        secondary_anchorpane.setVisible(true);
    }//end of on_open_second_window_button

    @FXML
    private void on_close_second_window_button(){
        secondary_anchorpane.setVisible(false);
    }


    private void add_item_to_password_listview(){
        for(int i = 0; i < usages.size(); i++) {
            if(!passwords_listview.getItems().contains(usages.get(i))){
                passwords_listview.getItems().add(usages.get(i));
            }

        }
    }//end of add_item_to_password_listview

    @FXML
    private void on_remove_current_button(){
        usages.remove(passwords_listview.getSelectionModel().getSelectedItem());
        passwords_listview.getItems().remove(passwords_listview.getSelectionModel().getSelectedItem());
        database.deletePassword(current_usage);
        database.printAllPasswords();
    }//end of on_remove_current_button

    @FXML
    private void onButtonRandPw(){
        Password_Generator.createPassword();
        String password = Password_Generator.getPassword();
        passwordField.setText(password);
        repeatPasswordField.setText(password);
    }//end of onButtonRandPw


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //connect to sqlite database and setup a table
        database.connect();
        database.createTable();

        database.deletePassword("2");

        usages = database.get_all_usages();

        add_item_to_password_listview();
        database.printAllPasswords();

        passwords_listview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                current_usage = oldValue;

                password_label.setText(database.getPassword(newValue));
            }
        });

    }


}//end of GUI