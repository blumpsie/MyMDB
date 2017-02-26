package mymdb;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import models.Actor;
import models.Movie;
import models.ORM;
import models.Role;

/**
 * @author Mark Erickson
 */
public class MyMDBController implements Initializable {
    
    // DATA MEMBERS
    // ------------
    @FXML
    private ListView<Movie> movieList;
    
    @FXML
    private ListView<Actor> actorList;
    
    private Node lastFocused = null;
    
    private final Collection<Integer> actorMovieIds = new HashSet<>();
    
    @FXML
    private MenuItem removeMovieMenuItem;
    
    @FXML
    private MenuItem editRoleMenuItem;
    
    @FXML
    private MenuItem addRoleMenuItem;
    
    // MEMBER FUNTIONS
    // ---------------
    @FXML
    private void activateMoviesMenu(Event event)
    {
        Movie movie = movieList.getSelectionModel().getSelectedItem();
        removeMovieMenuItem.setDisable(movie == null);
    }
    
    @FXML
    private void activateRolesMenu(Event event)
    {
        Movie movie = movieList.getSelectionModel().getSelectedItem();
        Actor actor = actorList.getSelectionModel().getSelectedItem();
        editRoleMenuItem.setDisable((movie == null) || (actor == null));
        addRoleMenuItem.setDisable((movie == null) || (actor == null));
    }
    // ACCESSOR METHODS
    // ----------------
    ListView<Movie> getMovieList()
    {
        return movieList;
    }
    
    ListView<Actor> getActorList()
    {
        return actorList;
    }
    
    TextArea getDisplay()
    {
        return display;
    }
    @FXML
    TextArea display;
    
    // HANDLER FUNCTIONS
    // -----------------
    
    // User Selects a Movie
    @FXML
    private void movieSelect(Event event)
    {
        try
        {
            Actor actor = actorList.getSelectionModel().getSelectedItem();
            Movie movie = movieList.getSelectionModel().getSelectedItem();
            lastFocused = movieList;

                if((actor != null) && (movie != null))
                {
                    
                    Role role = ORM.findOne(Role.class, 
                                  "where actor_id=? and movie_id=?", 
                                  new Object[]{actor.getId(), movie.getId()});
                    
                    // Checks to see if a role was found
                    if(role == null)
                    {
                        throw new ExpectedException("Actor wasn't in "
                                                            + "that movie.");
                    }
                    display.setText(Helper.roleInfo(role));
                }
                else
                {
                    display.setText(Helper.movieInfo(movie));
                }
        } // End of Try
        catch(ExpectedException ex)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(ex.getMessage());
            alert.show();
            if (lastFocused != null)
            {
                lastFocused.requestFocus();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    // User selects an Actor
    @FXML
    private void actorSelect(Event event)
    {
        try
        {
            Movie movie = movieList.getSelectionModel().getSelectedItem();
            Actor actor = actorList.getSelectionModel().getSelectedItem();
            lastFocused = movieList;
            
            Collection<Role> roles = ORM.findAll(Role.class,
                                             "where actor_id=?", 
                                             new Object[]{actor.getId()});
            actorMovieIds.clear();
            
            // add the movie ids from the actors roles 
            //to the actorMovieIds collection
            for (Role role : roles)
            {
                actorMovieIds.add(role.getMovieId());
            }
            
            // pick up style changes in movieList
            movieList.refresh();
            
            if((actor != null) && (movie != null))
            {
                Role role = ORM.findOne(Role.class, 
                            "where actor_id=? and movie_id=?", 
                             new Object[]{actor.getId(), movie.getId()});
                
                // Checks to see if a role was found
                if(role == null)
                {
                    throw new ExpectedException("Actor wasn't in that movie");
                }
                
                display.setText(Helper.roleInfo(role));
            }
            else
            {
                display.clear();
            }
            
        } // end of try
        catch(ExpectedException ex)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(ex.getMessage());
            alert.show();
            if (lastFocused != null)
            {
                lastFocused.requestFocus();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    // Orders the Movies by Year
    @FXML
    private void orderByYear(Event event)
    {
        System.out.println("Order By Year");
        // TODO Write orderByYear method
    }
    
    // Orders the Movies by Title
    @FXML
    private void orderByTitle(Event event)
    {
        System.out.println("Orderd By Title");
        // TODO Write orderByTitle method
    }
    
    // Clears the user selections
    @FXML
    private void clear(Event event)
    {
        movieList.getSelectionModel().clearSelection();
        actorList.getSelectionModel().clearSelection();
        actorMovieIds.clear();
        movieList.refresh();
        display.setText("");
    }
    
    // Join an Actor and a Movie with a Role
    @FXML
    private void addRole(Event event)
    {
        try
        {
            Movie movie = movieList.getSelectionModel().getSelectedItem();
            Actor actor = actorList.getSelectionModel().getSelectedItem();
            
            Role role = ORM.findOne(Role.class, 
                        "where actor_id=? and movie_id=?", 
                        new Object[]{actor.getId(), movie.getId()});
            
            // Checks to see if there is already a role attached 
            // to the specified Actor and Movie
            if (role != null)
            {
                throw new ExpectedException("A role already exist.");
            }
            
            role = new Role(actor, movie);
            ORM.store(role); // TODO Fix Program Crash
            actorMovieIds.add(movie.getId());
            movieList.refresh();
            movieList.requestFocus();
            display.setText(Helper.roleInfo(role));
        }// End of Try
        catch (ExpectedException ex)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(ex.getMessage());
            alert.show();
            if (lastFocused != null)
            {
                lastFocused.requestFocus();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    // Add an Actor or Director
    @FXML
    private void addActorOrDirector(Event event)
    {
        try
        {
            // Creates a new FXMLLoader object with AddActorOrDirector arg
            URL fxml = getClass().getResource("AddActorOrDirector.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            fxmlLoader.load();
            
            // gets the scene from the loader
            Scene scene = new Scene(fxmlLoader.getRoot());
            
            // Create a stage for the scene
            Stage dialogStage = new Stage();
            dialogStage.setScene(scene);
            
            // specifies a dialog title
            dialogStage.setTitle("Add an Actor or a Director");
            
            // Blocks the application
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            
            // invoking the dialog
            dialogStage.show();
            
            AddActorOrDirectorController dialogController = 
                                                    fxmlLoader.getController();
            dialogController.setMainController(this);
            
            // Prevents vertical resizing
            double height = dialogStage.getHeight();
            dialogStage.setMaxHeight(height);
            dialogStage.setMinHeight(height);
            
            // Prevents the horizontal size from getting too small
            dialogStage.setMinWidth(350);
            
            // TODO fix window close error
            // make sure the user wants to close the window
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Are you sure that you want to exit?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() != ButtonType.OK) {
                        event.consume();
                    }
                }
            });
            
        } // End of Try
        catch (IOException ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    // Add a Movie
    @FXML
    private void addMovie(Event event)
    {
        try
        {
            //get fxmlLoader
            URL fxml = getClass().getResource("AddMovie.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(fxml);
            fxmlLoader.load();

            // get scene
            Scene scene = new Scene(fxmlLoader.getRoot());

            // create stage
            Stage dialogStage = new Stage();
            dialogStage.setScene(scene);

            // specify title
            dialogStage.setTitle("Add a Movie");

            // block the main application
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // invoke dialog
            dialogStage.show();
            
            // get controller
            AddMovieController dialogController = fxmlLoader.getController();
            
            //pass the MyMDB controller to the dialog
            dialogController.setMainController(this);
            
            // prevent the dialog from becoming too small
            double height = dialogStage.getHeight();
            double width = dialogStage.getWidth();
            dialogStage.setMinHeight(height);
            dialogStage.setMinWidth(width);
            
            // TODO fix window close error
            // make they user wants to close window
            dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Are you sure that you want to exit?");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() != ButtonType.OK) {
                        event.consume();
                    }
                }
            });
        }// End of Try
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    @FXML
    private void removeMovie(Event event)
    {
        try
        {
            Movie movie = movieList.getSelectionModel().getSelectedItem();
            
            // Find all roles that the movie is linked to
            Collection<Role> roles = ORM.findAll(Role.class,
                    "where movie_id=?", new Object[]{movie.getId()});
            
            
            // if Roles exist delete them
            if (!roles.isEmpty())
            {
                for(Role role : roles)
                {
                    ORM.remove(role);
                }
            }
            
            // Confirm that the user wants todelete selected movie
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get() != ButtonType.OK)
            {
                return;
            }
            
            // remove movie
            ORM.remove(movie);
            
            // remove from list
            movieList.getItems().remove(movie);
            movieList.getSelectionModel().clearSelection();
            
            // display nothing in the text area
            display.setText("");
        }
        catch (ExpectedException ex)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(ex.getMessage());
            alert.show();
            if(lastFocused != null)
            {
                lastFocused.requestFocus();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try // adding the movies and actors to the lists
        {
            Collection<Movie> movies = ORM.findAll(Movie.class);
            for(Movie movie : movies)
            {
                movieList.getItems().add(movie);
            } 
            
            Collection<Actor> actors = ORM.findAll(Actor.class);
            for(Actor actor : actors)
            {
                actorList.getItems().add(actor);
            } 
            
            // used for display of Actors and movies
            MovieCellCallback movieCellCallback = new MovieCellCallback();
            movieList.setCellFactory(movieCellCallback);
            
            ActorCellCallback actorCellCallback = new ActorCellCallback();
            actorList.setCellFactory(actorCellCallback);
            
            movieCellCallback.setMovieIds(actorMovieIds);
        } // end try
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }// end catch
    }   
    
    
}
