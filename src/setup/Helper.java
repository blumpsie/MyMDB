package setup;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import models.DBProps;

class Helper {

  static String getResourceContent(String filename) throws IOException {
    InputStream istr = Helper.class.getResourceAsStream(filename);
    if (istr == null) {
      throw new IOException("Missing file: " + filename);
    }
    Scanner s = new Scanner(istr).useDelimiter("\\A");  
    return s.next();
  }

  public static void createTables(Properties props) throws 
    IOException, SQLException, ClassNotFoundException {

    String url = props.getProperty("url");
    String username = props.getProperty("username");
    String password = props.getProperty("password");
    String driver = props.getProperty("driver");
    if (driver != null) {
      Class.forName(driver); // load driver if necessary
    }
    Connection cx = DriverManager.getConnection(url, username, password);
    
    @SuppressWarnings("unchecked")
    ArrayList<String> create_order
        = new ArrayList(Arrays.asList( "actor", "director", "movie", "role" ));

    @SuppressWarnings("unchecked")    
    ArrayList<String> drop_order = (ArrayList<String>) create_order.clone(); 
    Collections.reverse(drop_order);    

    Statement stmt = cx.createStatement();
        
    System.out.format("\n---- drop tables\n");
    for (String table : drop_order) {
      String sql = String.format("drop table if exists %s", table);
      System.out.println(sql);
      stmt.execute(sql);
    }

    System.out.format("\n---- create tables\n");
    for (String table : create_order) {
      String filename = String.format("tables/%s-%s.sql", table, DBProps.which);
      String sql = getResourceContent(filename).trim();
      System.out.println(sql);
      stmt.execute(sql);
    }
  }

  static void populateTables(Properties props) 
      throws SQLException, ClassNotFoundException, IOException
  {
    String url = props.getProperty("url");
    String username = props.getProperty("username");
    String password = props.getProperty("password");
    String driver = props.getProperty("driver");
    if (driver != null) {
      Class.forName(driver); // load driver if necessary
    }
    Connection cx = DriverManager.getConnection(url, username, password);
    PreparedStatement stmt;

    // these are for internal use to correspond names to table id values
    Map<String, Integer> actorId = new HashMap<>();
    Map<String, Integer> directorId = new HashMap<>();
    Map<String, Integer> movieId = new HashMap<>();
    int id;
    
    //========================================================
    System.out.println("\n--- add actors");
    
    stmt = cx.prepareStatement( "insert into actor (name) values(?)" );
    String[] actors = {
        "Al Pacino",
        "Marlon Brando",
        "Jack Nicholson",
        "Humphrey Bogart",
        "Lauren Bacall",
        "Robert DeNiro",
//        "Ben Affleck",
      };

    id = 0;
    for (String name : actors) {
      System.out.println(name);
      
      stmt.setString(1, name);
      stmt.execute();
      actorId.put(name, ++id);
    }
    
    //========================================================
    System.out.println("\n--- add directors");
    
    stmt = cx.prepareStatement( "insert into director (name) values(?)" );
    
    String[] directors = {
        "Francis Ford Coppola",
        "Martin Scorsese",
        "Harold Ramis",
        "Milos Forman",
        "Michael Curtiz",
        "Howard Hawks",
        "Arthur Penn",
//        "Ben Affleck",
      };

    id = 0;
    for (String name : directors) {
      System.out.println(name);
      
      stmt.setString(1, name);
      stmt.execute();
      directorId.put(name, ++id);
    }
    
    //========================================================
    System.out.println("\n--- add movies");

    stmt = cx.prepareStatement(
      "insert into movie (title,year,director_id,description) values(?,?,?,?)"
    );
    
    Object[][] movies = new Object[][] {
      {"The Godfather", 1972, 
        "Francis Ford Coppola", getResourceContent("movies/Godfather.txt")},
      {"Goodfellas", 1990, 
        "Martin Scorsese", getResourceContent("movies/Goodfellas.txt")},
      {"Analyze This", 1999, 
        "Harold Ramis", getResourceContent("movies/AnalyzeThis.txt")},
      {"One Flew Over the Cuckoo's Nest", 1975, 
        "Milos Forman", getResourceContent("movies/CuckooNest.txt")},
      {"Casablanca", 1942, 
        "Michael Curtiz", getResourceContent("movies/Casablanca.txt")},
      {"The Big Sleep", 1946, 
        "Howard Hawks", getResourceContent("movies/BigSleep.txt") },
      {"The Missouri Breaks", 1976, 
        "Arthur Penn", getResourceContent("movies/MissouriBreaks.txt")},      
//      {"The Godfather: Part II", 1974, 
//        "Francis Ford Coppola", getResourceContent("movies/GodfatherII.txt")},
    };
    
    id = 0;
    for (Object[] movie : movies) {
      System.out.println(movie[0]);

      String name = (String) movie[0];
      int year = (int) movie[1];
      String director = (String) movie[2];
      String synopsis = (String) movie[3];
      
      int director_id = directorId.get(director);
            
      stmt.setString(1, name);
      stmt.setInt(2, year);
      stmt.setInt(3, director_id);
      stmt.setString(4, synopsis);
      stmt.execute();
      movieId.put(name, ++id);
    }

    //========================================================
    System.out.println("\n--- add roles");
    
    stmt = cx.prepareStatement(
      "insert into role (actor_id,movie_id,description) values(?,?,?)" );
    
    String roles[][] = new String[][]{
      {"Al Pacino", "The Godfather", 
        getResourceContent("roles/Pacino-Godfather.txt")},
      {"Marlon Brando", "The Godfather", 
        getResourceContent("roles/Brando-Godfather.txt")},
      {"Humphrey Bogart", "Casablanca", 
        getResourceContent("roles/Bogart-Casablanca.txt")},
      {"Humphrey Bogart", "The Big Sleep", 
        getResourceContent("roles/Bogart-BigSleep.txt")},
      {"Jack Nicholson", "The Missouri Breaks", ""},
      {"Jack Nicholson", "One Flew Over the Cuckoo's Nest", ""},
      {"Lauren Bacall", "The Big Sleep", ""},
      {"Marlon Brando", "The Missouri Breaks", ""},
      {"Robert DeNiro", "Goodfellas", ""},
//      {"Robert DeNiro", "Analyze This", ""},
//      {"Al Pacino", "The Godfather: Part II", ""},
//      {"Robert DeNiro", "The Godfather: Part II", ""},
    };
    
    for (String[] role : roles) {
      String actor = role[0];
      String movie = role[1];
      String description = role[2];

      System.out.printf("%s -- %s\n", actor, movie );
      
      stmt.setInt(1, actorId.get(actor));
      stmt.setInt(2, movieId.get(movie));
      stmt.setString(3, description);
      stmt.execute();      
    }
  }
}
