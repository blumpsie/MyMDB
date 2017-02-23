package mymdb;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Actor;
import models.Director;
import models.ORM;

/**
 * FXML Controller class
 *
 * @author Mark Erickson
 */
public class AddActorOrDirectorController implements Initializable {

    // DATA MEMBERS
    // ------------
    private MyMDBController mainController;
    
    @FXML
    private TextField nameField;
    
    @FXML
    private ComboBox<String> roleSelection;
    
    // Mutator Methods
    // ---------------
    void setMainController(MyMDBController mainController)
    {
        this.mainController = mainController;
    }
    
    // ACCESSOR METHODS
    // ----------------
    TextField getNameField()
    {
        return nameField;
    }
    
    ComboBox<String> getRoleSelection()
    {
        return roleSelection;
    }
    
    // HANDLER METHODS
    // ---------------
    @FXML
    private void add(Event event)
    {
        try
        {
            String role = roleSelection.getSelectionModel().getSelectedItem();
            String name = nameField.getText().trim();

            if(name.length() <= 0)
            {
                throw new ExpectedException("The Actor or Director "
                                                            + "must have a name.");
            }
            if (!name.matches("[a-zA-Z ]+"))
            {
                throw new ExpectedException("That is not a valid Name.");
            }

            // Check to see if the Actor already exists
            Actor actorWithName = 
                       ORM.findOne(Actor.class, "where name=?", new Object[]{name});
            if ((actorWithName != null) && role.equals("Actor"))
            {
                throw new ExpectedException("Actor already exists.");
            }

            if(role.equals("Actor"))
            {
                // access the Controller
                ListView<Actor> actorList = mainController.getActorList();
                TextArea display = mainController.getDisplay();
                
                // put it in the database
                Actor newActor = new Actor(name);
                ORM.store(newActor);
                
                // reload the list from the database
                actorList.getItems().clear();
                Collection<Actor> actors = ORM.findAll(Actor.class);
                for (Actor actor: actors)
                {
                    actorList.getItems().add(actor);
                }
                
                // select and scroll to added actor
                actorList.getSelectionModel().select(newActor);
                actorList.scrollTo(newActor);
                actorList.requestFocus();
                
                // set text display
                display.clear();
                
                ((Button)event.getSource()).getScene().getWindow().hide();
            }
            
            if(role.equals("Director"))
            {
                // access the Controller
                
                TextArea display = mainController.getDisplay();
                
                // put it in the database
                Director newDirector = new Director(name);
                ORM.store(newDirector); // TODO Fix Program Crash
                
                // set text display
                display.clear();
                
                ((Button)event.getSource()).getScene().getWindow().hide();
            }
            
        } // End of Try
        catch (ExpectedException ex)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(ex.getMessage());
            alert.show();
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    @FXML
    private void cancel(Event event)
    {
        ((Button)event.getSource()).getScene().getWindow().hide();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       roleSelection.getItems().add("Actor");
       roleSelection.getItems().add("Director");
       roleSelection.setValue("Actor"); 
    }    
    
}
