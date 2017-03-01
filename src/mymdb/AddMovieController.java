package mymdb;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Calendar;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Director;
import models.Movie;
import models.ORM;

/**
 * FXML Controller class
 *
 * @author Mark Erickson 
 */
public class AddMovieController implements Initializable {

     // DATA MEMEBERS
     // -------------
     private MyMDBController mainController;
     
     @FXML
     private TextField titleField;
     
     @FXML
     private TextField yearField;
     
     @FXML
     private ListView<Director> directorList;
     
     @FXML
     private TextArea descriptionArea;
     
     // MUTATOR METHODS
     // ----------------
     void setMainController(MyMDBController mainController)
     {
         this.mainController = mainController;
     }
     
     // ACCESSOR METHODS
     // ----------------
     TextField getTitleField()
     {
         return titleField;
     }
     
     TextField getYearField()
     {
         return yearField;
     }
     
     ListView<Director> getDirectorList()
     {
         return directorList;
     }
     
     TextArea getDescriptionArea()
     {
         return descriptionArea;
     }
     
     @FXML
     private void add(Event event)
     {
         try
         {
            String title = titleField.getText();
            String year = yearField.getText().trim();
            String description = descriptionArea.getText().trim();
            Director director = 
                            directorList.getSelectionModel().getSelectedItem();
         
            // replace any extra spaces and trim off whitespace at either end
            title = title.replaceAll("\\s+", " ").trim();
            // validation checks
            if (title.length() == 0)
            {
                throw new ExpectedException("The movie must have a title.");
            }
            if (director == null)
            {
                throw new ExpectedException("The movie had to have had a "
                                                            + "Director.");
            }
            
            int yearInt = Integer.valueOf(year);
            if ((yearInt < 1900) || (yearInt > Helper.currentYear()))
            {
                throw new ExpectedException("The movie has to have been made "
                                  + "between 1900 and " + Helper.currentYear());
            }
            
            Movie movieWithTitle = 
                 ORM.findOne(Movie.class, "where title=?", new Object[]{title});
                 if (movieWithTitle != null)
                 {
                     throw new ExpectedException("A movie with that title "
                                                    + "already exits.");
                 }
                 
                 //Movie is valid
                 
                 // access MyMDBController
                 ListView<Movie> movieList = mainController.getMovieList();
                 TextArea display = mainController.getDisplay();
                 
                 // put into database
                 Movie newMovie = 
                         new Movie(title, year, description, director.getId());
                 ORM.store(newMovie);
                 
                 // reload the movieList
                 movieList.getItems().clear();
                 if(mainController.getOrderedTitle())
                 {
                     Collection<Movie> movies = ORM.findAll(Movie.class, "order by title");
                     for (Movie movie : movies)
                     {
                         movieList.getItems().add(movie);
                     }
                     mainController.getOrderedLabel().setText("Movies are Ordered by Title");
                 }
                 else if (mainController.getOrderedYear())
                 {
                     Collection<Movie> movies = ORM.findAll(Movie.class, "order by year");
                     for (Movie movie : movies)
                     {
                         movieList.getItems().add(movie);
                     }
                     mainController.getOrderedLabel().setText("Movies are Ordered by Year");
                 }
                 else
                 {
                    Collection<Movie> movies = ORM.findAll(Movie.class);
                    for (Movie movie : movies)
                    {
                        movieList.getItems().add(movie);
                    }
                    mainController.getOrderedLabel().setText("Movies are NOT Ordered");
                 }
                 // select the new movie
                 movieList.getSelectionModel().select(newMovie);
                 movieList.scrollTo(newMovie);
                 movieList.requestFocus();
                 
                 // set display text
                 display.setText(Helper.movieInfo(newMovie));
                 
                 ((Button)event.getSource()).getScene().getWindow().hide();
         }// End of Try
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
        try
        {
            // add directors to list
            Collection<Director> directors = ORM.findAll(Director.class);
            for(Director director : directors)
            {
                directorList.getItems().add(director);
            }

            // prettify the the display of the directors
            DirectorCellCallback directorCellCallback = new DirectorCellCallback();
            directorList.setCellFactory(directorCellCallback);
        }// End of Try
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }    
    
}
