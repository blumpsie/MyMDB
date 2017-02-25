package mymdb;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.Director;

/**
 *
 * @author Mark Erickson
 */
public class DirectorCellCallback implements Callback<ListView<Director>, 
                                                          ListCell<Director>> {
    @Override
    public ListCell<Director> call(ListView<Director> p)
    {
        ListCell<Director> cell = new ListCell<Director>()
        {
            @Override
            protected void updateItem(Director director, boolean empty)
            {
                super.updateItem(director, empty);
                if(empty)
                {
                    this.setText(null);
                    return;
                }
                this.setText(director.getName());
            }
        };
        return cell;
    }
}
