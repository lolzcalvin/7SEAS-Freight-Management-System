package org.svseas.utils;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import org.svseas.data.DataFile;
import org.svseas.model.ObjectList;
import org.svseas.model.account.ClientAccount;
import org.svseas.operations.AccountOperations;

import java.util.Locale;
import java.util.Objects;

/**
 * Coded by Seong Chee Ken on 22/01/2017, 17:37. Just in case for re-usability
 */
public class ShipManipulator {

    //populate combobox with username for adding ships
    protected void loadUsername(JFXComboBox<String> comboBox) {
        AccountOperations<ClientAccount> clientOps = new AccountOperations<>(DataFile.CLIENT);
        ObjectList<ClientAccount> clientList = clientOps.read();
        ObservableList<String> client_usernames = FXCollections.observableArrayList();
        for (ClientAccount clients : clientList.getList()) {
            client_usernames.add(clients.getUsername());
        }
        comboBox.setItems(client_usernames);
        new AutoCompleteCBoxListener<>(comboBox);
    }

    //populate combobox with countries
    protected void loadCountries(JFXComboBox<String> comboBox) {
        String[] locale = Locale.getISOCountries();
        for (String code : locale) {
            Locale country = new Locale("", code);
            comboBox.getItems().add(country.getDisplayCountry());
        }
        comboBox.getSelectionModel().select("United States");
        new AutoCompleteCBoxListener<>(comboBox);
    }

    //populate ship type
    protected void loadType(JFXComboBox<String> comboBox) {
        comboBox.getItems().addAll("Containers", "Barge", "Tankers", "Tugboats", "Cargo", "Cruises");
        new AutoCompleteCBoxListener<>(comboBox);
    }

    //switching between leasing drawers
    protected void switcher(JFXRadioButton radio1, JFXRadioButton radio2, JFXDrawer drawer1, JFXDrawer drawer2,
                            ToggleGroup group, JFXTextField value){
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (radio1.isSelected() && !Objects.equals(value.getText(), "")){
                drawer1.setDisable(false);
                drawer2.setDisable(true);
            } else if (radio2.isSelected() && !Objects.equals(value.getText(), "")){
                drawer1.setDisable(true);
                drawer2.setDisable(false);
            } else {
                drawer1.setDisable(true);
                drawer2.setDisable(true);
            }
        });
    }

    //for contract hire, listener on textfield and changing the price labels
    protected void contractHireCalculator(JFXTextField contract_period, JFXTextField ship_value,
                                          Label label_value, Label label_total){
        contract_period.textProperty().addListener((observable, oldValue, newValue) -> {

            if (Objects.equals(newValue, "")){
                label_value.setText("0.00");
                label_total.setText("0.00");
            } else {
                //i want to prevent lag as much as possible
                new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(()->{
                            double total;
                            double subtotal = 0.00d;
                            double value = Double.parseDouble(ship_value.getText());
                            System.out.println(value); //caught value
                            for (int i = 0; i < Integer.parseInt(contract_period.getText()); i++){
                                total = value * 10/100;
                                value = value * 97/100;
                                System.out.println(value);
                                subtotal += total;
                                Tester.SUCCESS.printer();
                            }

                            label_value.setText(String.valueOf(value));
                            label_total.setText(String.valueOf(subtotal));
                        });
                        return null;
                    }
                }.run();
            }
        });
    }

    //for operating leasing

}
