package ui;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class OptionsButton extends JButton{
	
	public OptionsButton(){
		setText("Options");
		UIRes.customiseButton(this, true);
		addActionListener(e ->{
			UIRes.switchPanel(UIRes.optionsPanel);
		});
	}
}
