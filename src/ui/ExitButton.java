package ui;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ExitButton extends JButton{
	
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
