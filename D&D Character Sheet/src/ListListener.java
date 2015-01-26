import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ListListener implements ActionListener {

	NewSheet newSheet;

	public ListListener(NewSheet newSheet) {
		this.newSheet = newSheet;
	}

	public void actionPerformed(ActionEvent arg0) {
		try {
			newSheet.addSpellsToList(newSheet.spellList.getSelectedValuesList());
			newSheet.spellList.clearSelection();
			newSheet.addSpells();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
