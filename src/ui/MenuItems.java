package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;

import gamemodes.PlayGame;
import graphics.GameComponent;
import graphics.LayeredPane;
import networking.NetworkingClient;
import resources.Resources;

public class MenuItems extends UIRes {


	

	JButton getBackToStartMenuButton(String name, JPanel backToPanel) {
		JButton button = goBack(backToPanel);
		button.setText(name);
		return button;
	}





	
}
