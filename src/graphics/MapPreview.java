package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Map;
import resources.Map.Tile;

@SuppressWarnings("serial")
public class MapPreview extends JPanel {

	private ArrayList<Map> maps;
	private Map map;
	private int index = 0;
	private static JLabel label;

	public MapPreview(ArrayList<Map> maps) {
		this.maps = maps;
		this.map = maps.get(0);

		addKeyListener(new TAdapter());
		setFocusable(true);
	}

	public void paintComponent(Graphics g) {

		g.clearRect(0, 0, 300, 300);

		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 48; j++) {

				Tile t = map.tileAt(i, j);

				if (t == null || t == (Tile.ABYSS) || t == (Tile.EDGE_ABYSS)) {
					g.setColor(Color.BLACK);
				} else if (t == Tile.WALL) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.GREEN);
				}

				g.fillRect(j * 5, i * 5, 4, 4);

			}
		}

	}

	public static void main(String[] args) {

		String root = "./resources/maps/";

		File folder = new File(root);
		File[] listOfFiles = folder.listFiles();

		ArrayList<Map> maps = new ArrayList<Map>();

		for (int i = 0; i < listOfFiles.length; i++) {

			String filename = listOfFiles[i].getName();
			
			maps.add(new Map(1200, 650, Map.World.CAVE, filename.substring(0, filename.length() - 4)));

		}

		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setSize(255, 200);
		frame.add(new MapPreview(maps), BorderLayout.CENTER);
		label = new JLabel(maps.get(0).getName());
		label.setHorizontalAlignment(JLabel.CENTER);
		frame.add(label, BorderLayout.SOUTH);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_ESCAPE) {

				System.exit(0);
			}

			if (key == KeyEvent.VK_RIGHT) {
				index++;

				if (index == maps.size()) {
					index = 0;
				}

				map = maps.get(index);

			}

			if (key == KeyEvent.VK_LEFT) {
				index--;

				if (index == -1) {
					index = maps.size() - 1;
				}

				map = maps.get(index);
			}

			repaint();
			label.setText(map.getName());
		}
	}
}
