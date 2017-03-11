package graphics;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import resources.Resources;

public class TopBar extends JPanel {

	private PlayerPanel players;
	private PlayerStats stats;
	
	public TopBar(Resources resources){
		
		super();
		
		setLayout(new BorderLayout());
		
		players = new PlayerPanel(resources);
		stats = new PlayerStats(resources.getPlayerList().get(0));
		
		add(players, BorderLayout.CENTER);
		add(stats, BorderLayout.EAST);
		
	}
	
	public void setOrder(){
		players.setOrder();
	}
	
	public void updateStats(){
		stats.updateStats();
	}
	
}
