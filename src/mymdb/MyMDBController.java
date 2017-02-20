package mymdb;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import models.Actor;
import models.Movie;
import models.ORM;

/**
 * @author Mark Erickson
 */
public class MyMDBController implements Initializable {
    
    @FXML
    private ListView<Movie> movieList;
    
    @FXML
    private ListView<Actor> actorList;
    
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
        } // end try
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }// end catch
    }   
    
    
}
