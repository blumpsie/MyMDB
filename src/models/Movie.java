package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mark Erickson
 */
public class Movie extends Model {
    
    public static final String TABLE = "movie";
    
    public int id = 0;
    private String title;
    private String year;
    private String description;
    private int director_id;
    
    Movie() {};
    
    public Movie(String title, String year, String description, int director_id)
    {
        this.title = title;
        this.year = year;
        this.description = description;
        this.director_id = director_id;
    }
    
    
    @Override
    public int getId() // Returns the ID
    {
        return id;
    }
    
    // Returns the title of the movie
    public String getTitle()
    {
        return title;
    }
    
    // Returns the year the movie was made
    public String getYear()
    {
        return year;
    }
    
    // Returns the description of the movie
    public String getDescription()
    {
        return description;
    }
    
    // Returns the director_id
    public int getDirectorId()
    {
        return director_id;
    }
    
    // Assigns the title of the movie
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    // Assigns the year of the movie
    public void setYear(String year)
    {
        this.year = year;
    }
    
    // Assigns the description of the movie
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    // Assigns the director id
    public void setDirectorId(int director_id)
    {
        this.director_id = director_id;
    }
    
    @Override
    void load(ResultSet rs) throws SQLException
    {
        id = rs.getInt("id");
        title = rs.getString("title");
        year = rs.getString("year");
        description = rs.getString("description");
        director_id = rs.getInt("director_id");
    }
    
    @Override
    void insert() throws SQLException 
    {
        Connection cx = ORM.connection();
        String sql = String.format(
                "insert into %s (title,year,description,director_id) "
                                                  + "values (?,?,?,?)", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, title);
        st.setString(++i, year);
        st.setString(++i, description);
        st.setInt(++i, director_id);
        st.executeUpdate();
        id = ORM.getMaxId(TABLE);
    }
    
    @Override
    void update() throws SQLException
    {
        Connection cx = ORM.connection();
        String sql = String.format(
                "update %s set title=? where id=?", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, title);
        st.setString(++i, year);
        st.setString(++i, description);
        st.setInt(++i, id);
        st.executeUpdate();
    }
    
    @Override
    public String toString()
    {
        return String.format("(%s,%s,%s,%s,%s)", 
                                         id,title,year,description,director_id);
    }
}
