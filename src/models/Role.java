package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Mark Erickson
 */
public class Role extends Model {
    
    public static final String TABLE = "role";
    
    public int id = 0;
    private int actor_id;
    private int movie_id;
    private String description;
    
    Role() {};
    
    public Role(Actor actor, Movie movie, String description)
    {
        this.actor_id = actor.getId();
        this.movie_id = movie.getId();
        this.description = description;
    }
    
    public Role(Actor actor, Movie movie)
    {
        this.actor_id = actor.getId();
        this.movie_id = movie.getId();
    }
    
    public Role(Movie movie, Actor actor)
    {
        this.movie_id = movie.getId();
        this.actor_id = actor.getId();
    }
    
    
    @Override
    public int getId() // Returns the ID
    {
        return id;
    }
    
    // Returns the id of the actor
    public int getActorId()
    {
        return actor_id;
    }
    
    // Returns the id of the movie
    public int getMovieId()
    {
        return movie_id;
    }
    
    // Returns the description of the role
    public String getDescription()
    {
        return description;
    }
    
    // Returns the actor
    public Actor getActor()
    {
        try
        {
            return ORM.load(Actor.class, actor_id);
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
    // Returns the movie
    public Movie getMovie()
    {
        try
        {
            return ORM.load(Movie.class, movie_id);
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
            return null;
        }
    }
    
    // Assigns the description to the role
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    @Override
    void load(ResultSet rs) throws SQLException
    {
        id = rs.getInt("id");
        actor_id = rs.getInt("actor_id");
        movie_id = rs.getInt("movie_id");
        description = rs.getString("description");
    }
    
    @Override
    void insert() throws SQLException 
    {
        Connection cx = ORM.connection();
        String sql = String.format(
                "insert into %s (actor_id,movie_id,description) "
                                                    + "values (?,?,?)", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setInt(++i, actor_id);
        st.setInt(++i, movie_id);
        st.setString(++i, description);
        st.executeUpdate();
        id = ORM.getMaxId(TABLE);
    }
    
    @Override
    void update() throws SQLException
    {
        Connection cx = ORM.connection();
        String sql = String.format(
                "update %s set description=? where id=?", TABLE);
        PreparedStatement st = cx.prepareStatement(sql);
        int i = 0;
        st.setString(++i, description);
        st.setInt(++i, id);
        st.executeUpdate();
    }
    
    @Override
    public String toString()
    {
        return String.format("(%s,%s,%s,%s)", id,actor_id,movie_id,description);
    }
}