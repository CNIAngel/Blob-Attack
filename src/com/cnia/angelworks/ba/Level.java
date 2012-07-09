package com.cnia.angelworks.ba;

import it.randomtower.engine.Camera;
import it.randomtower.engine.ResourceManager;
import it.randomtower.engine.World;
import it.randomtower.engine.actors.StaticActor;

import java.util.Arrays;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

import com.cnia.angelworks.ba.entity.Player;

/**
 * 
 * @author CNIAngel This is a World skeleton class. A simple class that has the
 *         init, update, and render methods set up along with the class
 *         constructor. We're gonna add a single entity and that'll be it.
 */
public class Level extends World {

	private boolean hideTiles = false;
	private Player player; // An instance of the Player entity
	private TiledMap map;

	public Level(int id, GameContainer gc) throws SlickException {
		super(id, gc);

		map = ResourceManager.getMap("map");

		player = new Player(0, 0); // Set the parameters for the entity
		add(player); // Then add it to the scene

		// Set up the camera to follow time
		// this.setCamera(new Camera(player, gc.getWidth(),
		// gc.getHeight()));

		loadEntityFromMap(map, Arrays.asList("SOLID"));
	}

	public void loadEntityFromMap(TiledMap map, List<String> types)
			throws SlickException {
		if (map == null) {
			Log.error("unable to load map information");
			return;
		}
		if (types == null || types.isEmpty()) {
			Log.error("no types defined to load");
			return;
		}
		// layer have property type, so check it
		for (String type : types) {
			// try to find a layer with property type set to solid
			int layerIndex = -1;
			for (int l = 0; l < map.getLayerCount(); l++) {
				String value = map.getLayerProperty(l, "type", null);
				if (value != null && value.equalsIgnoreCase(type)) {
					layerIndex = l;
					break;
				}
			}
			if (layerIndex != -1) {
				Log.debug("Entity layer found on map");
				int loaded = 0;
				for (int w = 0; w < map.getWidth(); w++) {
					for (int h = 0; h < map.getHeight(); h++) {
						Image img = map.getTileImage(w, h, layerIndex);
						if (img != null) {
							// load entity from Tiled map position and set Image
							// for static actor using image reference stored
							// into tiled map
							StaticActor te = new StaticActor(
									w * img.getWidth(), h * img.getHeight(),
									img.getWidth(), img.getHeight(), img);
							add(te);
							loaded++;
						}
					}
				}
				Log.debug("Loaded " + loaded + " entities");
			} else {
				Log.info("Entity layer not found on map");
			}
		}
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super.init(gc, sbg);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		super.update(gc, sbg, delta);

		Input input = container.getInput();
		
		// If the space key is pressed, hide all tiles
		// except the tiles that have collision.
		if (input.isKeyPressed(Input.KEY_SPACE)) {
			hideTiles = hideTiles ? false : true;
		}
		
		// If the escape key is pressed, the game instance
		// ends.
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// render all except entities
		if (!hideTiles) {
			for (int l = 0; l < map.getLayerCount(); l++) {
				String value = map.getLayerProperty(l, "type", null);
				if (value == null || !value.equalsIgnoreCase("entity")) {
					for (int w = 0; w < map.getWidth(); w++) {
						for (int h = 0; h < map.getHeight(); h++) {
							Image img = map.getTileImage(w, h, l);
							if (img != null) {
								g.drawImage(img, w * img.getWidth(),
										h * img.getHeight());
							}
						}
					}
				}
			}
		}

		super.render(gc, sbg, g);
	}

}
