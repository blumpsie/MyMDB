/*
 * This class is the initial model extension. 
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Mark Erickson
 */
public class Initial extends Model{
    private int id = 0;
    
    // corresponding database table
    public static final String TABLE = "-- ZE TABLEAU --";
    
    // default constructor
    Initial() {}
    
    @Override
    public int getId()
    {
        return id;
    }
    
    // used for SELECT operations
    @Override
    void load(ResultSet rs) throws SQLException
    {
        id = rs.getInt("id");
    }
    
    // used for INSERT operations
    @Override
    void insert() throws SQLException
    {
        throw new UnsupportedOperationException("insert in " + this.getClass());
    }
    
    // used for UPDATE operations
    @Override
    void update() throws SQLException
    {
        throw new UnsupportedOperationException("update in " + this.getClass());
    }
}
