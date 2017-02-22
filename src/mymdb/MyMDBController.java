package mymdb;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import models.Actor;
import models.Movie;
import models.ORM;
import models.Role;

/**
 * @author Mark Erickson
 */
public class MyMDBController implements Initializable {
    
    // DATA MEMBERS
    @FXML
    private ListView<Movie> movieList;
    
    @FXML
    private ListView<Actor> actorList;
    
    private Node lastFocused = null;
    
    private final Collection<Integer> actorMovieIds = new HashSet<>();
    
    @FXML
    TextArea display;
    
    // HANDLER FUNCTIONS
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
                    // TODO fix errors when actor and movie aren't relatable
                    Role role = ORM.findOne(Role.class, 
                                  "where actor_id=? and movie_id=?", 
                                  new Object[]{actor.getId(), movie.getId()});
                    display.setText(Helper.roleInfo(role));
                }
                else
                {
                    display.setText(Helper.movieInfo(movie));
                }
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
    
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
                // TODO fix errors when actor and movie aren't relatable
                Role role = ORM.findOne(Role.class, 
                            "where actor_id=? and movie_id=?", 
                             new Object[]{actor.getId(), movie.getId()});
                display.setText(Helper.roleInfo(role));
            }
            else
            {
                display.clear();
            }
            
        } // end of try
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
