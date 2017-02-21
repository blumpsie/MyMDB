package mymdb;

import java.util.Collection;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.Movie;

/**
 *
 * @author Mark Erickson
 */
public class MovieCellCallback implements Callback<ListView<Movie>, 
                                                              ListCell<Movie>> {
    
    // ids of movies that the actor is in
    private Collection<Integer> movieIds = null;
    
    void setMovieIds(Collection<Integer> movieIds)
    {
        this.movieIds = movieIds;
    }

    @Override
    public ListCell<Movie> call(ListView<Movie> p)
    {
        ListCell<Movie> cell = new ListCell<Movie>()
        {
            @Override
            protected void updateItem(Movie movie, boolean empty)
            {
                super.updateItem(movie, empty);
                if (empty)
                {
                    this.setText(null);
                    return;
                }
                this.setText(movie.getTitle() 
                        + " (" + movie.getYear() + ")");
                
                // css for the movies that the actor is in
                if (movieIds == null)
                {
                    return;
                }
                
                String css = ""
                        + "-fx-text-fill: #c00;"
                        + "-fx-font-weight: bold;"
                        + "-fx-font-style: italic;";
                
                if (movieIds.contains(movie.getId()))
                {
                    this.setStyle(css);
                }
                else
                {
                    this.setStyle(null);
                }
            } // end updateItem
        };
        return cell;
    } // end call
}
