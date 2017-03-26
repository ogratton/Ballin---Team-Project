package ui;

import javax.swing.JButton;

/**
 * Class for the options button.
 * 
 * @author Andreea Diana Dinca
 *
 */
@SuppressWarnings("serial")
public class OptionsButton extends JButton{
	
	/**
	 * Constructor for the options button.
	 */
	public OptionsButton(){
		setText("Options");
		UIRes.customiseButton(this, true);
		addActionListener(e ->{
			UIRes.switchPanel(UIRes.optionsPanel);
		});
	}
}
