package Model;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.shape.Path;
import javafx.scene.shape.LineTo; 
import javafx.scene.shape.MoveTo; 
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.image.WritableImage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class PenShape extends Shape {
	
	private ArrayList<Coord> shapeCoord;
	private Color color;
	private int appSize;

	public PenShape(Coord C, Color color, int appSize) {
		super();
		this.appSize = appSize;
		this.color = color;
		initializeCoord(C);
	}

	public ArrayList<Coord> getShapeCoord() {
		return shapeCoord;
	}

  	public void initializeCoord(Coord C){
		shapeCoord = new ArrayList<Coord>();
		shapeCoord.add(C);
		int halfSize = this.appSize;
		for (int i = -halfSize; i <= halfSize; i++) {
			for (int j = -halfSize; j <= halfSize; j++) {
				if (i == 0 && j == 0) {
					continue; // La coordonnée principale a déjà été ajoutée
				}
				shapeCoord.add(new Coord(C.x + i, C.y + j));
			}
		}
 	}

	 public void addCoord(Coord c) {
		shapeCoord.add(c);
		int halfSize = this.appSize;
		for (int i = -halfSize; i <= halfSize; i++) {
			for (int j = -halfSize; j <= halfSize; j++) {
				if (i == 0 && j == 0) {
					continue; // La coordonnée principale a déjà été ajoutée
				}
				shapeCoord.add(new Coord(c.x + i, c.y + j));
			}
		}
	}

	public void deleteShape(GraphicsContext g){
		g.setStroke(Color.WHITE);
		for (int i = 0; i < this.shapeCoord.size(); i++) {
			if (i+1 < this.shapeCoord.size()) {
			  Coord pos1 = this.getShapeCoord().get(i);
			  Coord pos2 = this.getShapeCoord().get(i+1);
			  double cpx = (pos1.x + pos2.x) / 2.0;
			  double cpy = (pos1.y + pos2.y) / 2.0;

			  g.beginPath();
			  g.quadraticCurveTo(pos1.x, pos1.y, cpx,cpy);
			  g.stroke();
			  g.closePath();
			  g.moveTo(cpx, cpy);
			}
		  }
	}
	
	

  	public boolean isInList(Coord c) {
		for(int i = 0; i < shapeCoord.size(); i++){
			if (shapeCoord.get(i).x==c.x && shapeCoord.get(i).y==c.y){
				return (true);
			}
		}
		return(false);
  	}

  	public void deleteCoord(Coord c){
		if (isInList(c)){
			shapeCoord.remove(c);
		}
  	}

  	public void printCoord(){
		for(int i = 0; i < shapeCoord.size(); i++){
			System.out.println(shapeCoord.get(i).x + " " + shapeCoord.get(i).y);
  		}
	}
}
