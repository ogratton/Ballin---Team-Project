package graphics;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import resources.Character;
import resources.Map;

public class GameComponent extends JPanel {

	private ArrayList<Character> characters;
	private ArrayList<CharacterModel> characterModels;
	private Map map;
	private MapModel mapModel;

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

		GameView view = new GameView(characterModels, mapModel);

		add(view, BorderLayout.CENTER);
		setVisible(true);

	}

	public void demo() {

	}

}
