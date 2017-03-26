package ui;

import javax.swing.JButton;

/**
 * Class for the exit button.
 * 
 * @author Andreea Diana Dinca
 *
 */
@SuppressWarnings("serial")
public class ExitButton extends JButton{
	
	/**
	 * Constructor for the exit button.
	 */
	public ExitButton(){
		setText("Exit");
		UIRes.customiseButton(this, true);
		addActionListener(e ->{
			if(UIRes.cModel.getConnection() != null)
				UIRes.cModel.getConnection().close();
			System.exit(0);
		});
	}

}
