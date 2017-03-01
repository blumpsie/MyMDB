package mymdb;

import java.util.Collection;
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
    
    // ids of actors that were in the movie
    private Collection<Integer> actorIds;
    
    void setActorIds(Collection<Integer> actorIds)
    {
        this.actorIds = actorIds;
    }
    
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
                
                if (actorIds == null)
                {
                    return;
                }
                
                String css = ""
                        + "-fx-text-fill: #f4428f;"
                        + "-fx-font-weight: bold;"
                        + "-fx-font-style: italic;";
                    
                if (actorIds.contains(actor.getId()))
                {
                    this.setStyle(css);
                }
                else
                {
                    this.setStyle(null);
                }
                
            }
        };
        return cell;
    }
}
