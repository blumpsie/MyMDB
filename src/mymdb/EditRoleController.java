package mymdb;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.WindowEvent;
import models.ORM;
import models.Role;

/**
 * FXML Controller class
 *
 * @author Mark Erickson
 */
public class EditRoleController implements Initializable {
    
    // DATA MEMBERS
    // ------------
    private MyMDBController mainController;
    
    @FXML
    private Label titleLabel;
    @FXML
    private Label actorLabel;
    
    @FXML
    private TextArea descriptionArea;
    
    private Role roleToModify;
    // MUTATOR METHODS
    // ---------------
    void setMainController(MyMDBController mainController)
    {
        this.mainController = mainController;
    }
    
    void setRoleToModify(Role roleToModify)
    {
        this.roleToModify = roleToModify;
    }
    
    // ACCESSOR METHODS
    // ----------------
    Label getTitleLabel()
    {
        return titleLabel;
    }
    
    Label getActorLabel()
    {
        return actorLabel;
    }
    
    TextArea getDescriptionArea()
    {
        return descriptionArea;
    }
    
    @FXML
    private void modify(Event event)
    {
        try
        {
            String description = descriptionArea.getText().trim();
            
            // access the features of MyMDBController
            //ListView<Movie> movieList = mainController.getMovieList();
            //ListView<Actor> actorList = mainController.getActorList();
            TextArea display = mainController.getDisplay();
            
            //make the modification and put in database
            roleToModify.setDescription(description);
            ORM.store(roleToModify);
            
            // select the movie and actor combination 
            // and display the modified informati
           display.setText(Helper.roleInfo(roleToModify));
           
           ((Button)event.getSource()).getScene().getWindow().hide();
        }// End of Try
        catch(Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    @FXML
    private void cancel(Event event)
    {
        // TODO fix the cancel confirmation
        String description = descriptionArea.getText();
        try
        {
            if(!description.equals(roleToModify.getDescription()))
            {
                throw new ExpectedException("Fields have been modified. "
                                          + "Are you sure that you want to exit?");  
            }
            else
            {
                ((Button)event.getSource()).getScene().getWindow().hide();
            }
        }
        catch(ExpectedException ex)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(ex.getMessage());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() != ButtonType.OK)
            {
                event.consume();
            }
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
