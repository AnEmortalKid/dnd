import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;

public class SpellSlotListener implements MouseListener {

	JLabel temp;
	int index;
	SpellSheet newSheet;

	public SpellSlotListener(JLabel temp, int index, SpellSheet newSheet) {
		this.temp = temp;
		this.index = index;
		this.newSheet = newSheet;
	}

	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			temp.setText(Integer.parseInt(temp.getText()) + 1 + "");
			newSheet.maxSlots[index] = Integer.parseInt(temp.getText());
		} else if (arg0.getButton() == MouseEvent.BUTTON3) {
			if (Integer.parseInt(temp.getText()) > 0) {
				temp.setText(Integer.parseInt(temp.getText()) - 1 + "");
				newSheet.maxSlots[index] = Integer.parseInt(temp.getText());
			}
		}
		try {
			newSheet.saveToText();
		} catch (IOException e) {
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
