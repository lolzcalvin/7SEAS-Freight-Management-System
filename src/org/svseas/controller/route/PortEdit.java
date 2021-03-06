package org.svseas.controller.route;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.svseas.model.route.Port;
import org.svseas.operations.PortOperations;
import org.svseas.utils.Validator;
import org.svseas.utils.manipulator.PortManipulator;

/**
 * Coded by Seong Chee Ken on 24/01/2017, 00:34.
 */
public class PortEdit extends PortManipulator {
    @FXML
    private StackPane portedit_root;
    @FXML
    private JFXTextField port_id, port_name, dist_nextPort;
    @FXML
    private JFXButton btn_edit, btn_cancel;

    @FXML
    public void initialize() {

        port_id.addEventFilter(KeyEvent.KEY_TYPED, Validator.validCharNo(20));
        port_name.addEventFilter(KeyEvent.KEY_TYPED, Validator.validCharNoSpace(50));
        dist_nextPort.addEventFilter(KeyEvent.KEY_TYPED, Validator.validPrice(20));

        port_id.setDisable(true);
        BooleanBinding binding = port_id.textProperty().isEmpty()
                .or(port_name.textProperty().isEmpty())
                .or(dist_nextPort.textProperty().isEmpty());
        btn_edit.disableProperty().bind(binding);
        manipulate(btn_edit);

        btn_cancel.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Stage thisStage = (Stage) portedit_root.getScene().getWindow();
                thisStage.close();
            }
        });
    }

    @Override
    public void manipulate(JFXButton manipulator) {
        manipulator.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)){
                Port port = new Port(port_id.getText(), port_name.getText(), dist_nextPort.getText());
                PortOperations potops = new PortOperations(port);
                potops.update(port.getPortId());
                Platform.runLater(()->{
                    Stage thisStage = (Stage) portedit_root.getScene().getWindow();
                    thisStage.close();
                });
            }
        });

    }

    @Override
    public <T> void initData(T port) {
        if (port instanceof Port){
            port_id.setText(((Port) port).getPortId());
            port_name.setText(((Port) port).getName());
            dist_nextPort.setText(((Port) port).getDistance_nextPort());
        }
    }
}
