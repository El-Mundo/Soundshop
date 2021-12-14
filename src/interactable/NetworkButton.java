package interactable;

import network.Room;
import resources.GraphicResouces;

public class NetworkButton extends Button {
	public static Room room;

	public NetworkButton(int x, int y, float scale) {
		super(GraphicResouces.BOKEH, x, y, scale);
	}

	@Override
	public void interact() {
		if(room == null)
			room = new Room();
		else if(!Room.running)
			room.restart();
		else
			room.requestFocus();
	}

}
