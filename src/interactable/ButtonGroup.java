package interactable;

import java.util.ArrayList;

import processing.core.PImage;

public abstract class ButtonGroup {
	public ArrayList<GroupedButton> buttons;
	protected int sel, size, defaultValue;

	public ButtonGroup(int defaultValue) {
		this.sel = 0;
		this.size = 0;
		this.buttons = new ArrayList<GroupedButton>();
		this.defaultValue = defaultValue;
	}
	
	public abstract void interact();
	
	public static class GroupedButton extends Button {
		ButtonGroup parent;
		int index;

		public GroupedButton(PImage image, int x, int y, float scale, ButtonGroup parent) {
			super(image, x, y, scale);
			this.parent = parent;
			parent.buttons.add(this);
			this.index = parent.size;
			parent.size ++;
			
			if(this.index == parent.defaultValue) {
				this.interact();
			}
		}

		@Override
		public void interact() {
			parent.sel = this.index;
			for(GroupedButton b : parent.buttons) {
				b.inactive = false;
			}
			this.inactive = true;
			parent.interact();
		}
		
	};

}
