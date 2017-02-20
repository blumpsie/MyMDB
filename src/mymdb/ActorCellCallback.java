package mymdb;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import models.Actor;

/**
 *
 * @author Mark Erickson
 */
public class ActorCellCallback implements Callback<ListView<Actor>, 
                                                             ListCell<Actor>> {
    @Override
    public ListCell<Actor> call(ListView<Actor> p)
    {
        ListCell<Actor> cell = new ListCell<Actor>()
        {
            @Override
            protected void updateItem(Actor actor, boolean empty)
            {
                super.updateItem(actor, empty);
                if (empty)
                {
                    this.setText(null);
                    return;
                }
                this.setText(actor.getName());
            }
        };
        return cell;
    }
}
