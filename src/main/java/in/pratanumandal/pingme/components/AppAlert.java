package in.pratanumandal.pingme.components;

import in.pratanumandal.pingme.common.Constants;
import in.pratanumandal.pingme.state.PrimaryStage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AppAlert extends Alert {

    public AppAlert(AlertType alertType, String headerText, String contentText, ButtonType... buttonTypes) {
        super(alertType, contentText, buttonTypes);

        this.setTitle(Constants.APP_NAME);
        this.setHeaderText(headerText);
        this.initOwner(PrimaryStage.getInstance().getStage());
    }

}
