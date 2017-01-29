package graphics;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;

import resources.Character;
import resources.Map;

public class GameComponent extends JPanel {

	private ArrayList<Character> characters;
	private ArrayList<CharacterModel> characterModels;
	private Map map;
	private MapModel mapModel;

	int oldValueX, newValueX, oldValueY, newValueY;
	
	public GameComponent(ArrayList<Character> characters, Map map) {

		setLayout(new BorderLayout());

		mapModel = new MapModel(map);

		JButton button = new JButton("exit");
		button.addActionListener(e -> System.exit(0));
		add(button, BorderLayout.SOUTH);

		
		
		characterModels = new ArrayList<CharacterModel>();

		for (Character character : characters) {

			CharacterModel model = new CharacterModel(character);
			characterModels.add(model);

		}

		JSlider sliderX = new JSlider(0, 1920, 45);
		JSlider sliderY = new JSlider(JSlider.VERTICAL, 0, 1200, 1155);
		
		oldValueX = 45;
		newValueX = 45;
		oldValueY = 45;
		newValueY = 45;
		
		sliderX.addChangeListener(e -> sliderChangedX(sliderX));
		add(sliderX, BorderLayout.NORTH);
		sliderY.addChangeListener(e -> sliderChangedY(sliderY));
		add(sliderY, BorderLayout.WEST);
		
		GameView view = new GameView(characterModels, mapModel);
		
		for(CharacterModel model : characterModels){
			model.addObserver(view);
		}
		
		add(view, BorderLayout.CENTER);
		setVisible(true);

	}

	public void sliderChangedX(JSlider slider){
		CharacterModel model = characterModels.get(0);
		
		oldValueX = newValueX;
		newValueX = slider.getValue();
		
		if(oldValueX < newValueX){
			model.setRight(true);
		} else {
			model.setLeft(true);
		}
		

		model.setX(slider.getValue());
		
		model.setLeft(false);
		model.setRight(false);

	}
	
	public void sliderChangedY(JSlider slider){
		CharacterModel model = characterModels.get(0);
		
		oldValueY = newValueY;
		newValueY = slider.getValue();
		
		if(oldValueY < newValueY){
			model.setUp(true);
		} else {
			model.setDown(true);
		}
		

		model.setY(1200 - slider.getValue());
		
		model.setUp(false);
		model.setDown(false);

	}
	
}
