package mymdb;

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
                this.setText(movie.getTitle());
            }
        };
        return cell;
    }
}
