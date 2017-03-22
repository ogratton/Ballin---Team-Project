package ui;

import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BackButton extends JButton{

	public BackButton(JPanel panel, String text){
		setText(text);
		UIRes.customiseButton(this, true);
		addActionListener(e ->{;
			UIRes.switchPanel(panel);
			System.out.println(UIRes.cModel.getConnection() == null);
			if(UIRes.cModel.getConnection() != null){
				UIRes.cModel.getConnection().close();
			}
		});
	}	
	
}
