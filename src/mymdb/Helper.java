package mymdb;

import java.util.Calendar;
import models.Movie;
import models.Role;

/**
 *
 * @author Mark Erickson
 */
public class Helper {
    
    static String movieInfo(Movie movie)
    {
        return String.format("MOVIE INFORMATION\n"
                            + "Title: %s\n"
                            + "Director: %s\n"
                            + "Year Released: %s\n"
                            + "Description: %s\n", 
                            movie.getTitle(),
                            movie.getDirector().getName(),
                            movie.getYear(),
                            movie.getDescription());
    }
    
    static String roleInfo(Role role)
    {
        return String.format("ROLE INFORMATION\n"
                            + "Movie: %s\n"
                            + "Actor: %s\n"
                            + "Description: %s\n", 
                            role.getMovie().getTitle(),
                            role.getActor().getName(),
                            role.getDescription());
    }
    
    static int currentYear()
    {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        return year;
    }
}
