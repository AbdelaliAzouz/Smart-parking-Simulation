import java.awt.Graphics;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.util.concurrent.Semaphore;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author {AZOUZ ABDELALI}
 *
 *         {M1 MBD}
 */
//Déclaration de la classe Parking
public class Parking extends JPanel {
	int PlacesOccupees;
	int capacite;
	ImageIcon image;
	boolean Place[];
	public Semaphore semaphore;

	public ArrayList<Voiture> allCars;

	// Créer un objet HashSet appelé infoVoitures qui stocke les différents objets de la classe Voitures qui seront garées dans le parking
	public HashSet<Voiture> infoVoitures = new HashSet<Voiture>();

	// le constructeur de la classe Parking qui définit la capacité du parking.
	Parking(int size) {

		capacite = size;
		this.PlacesOccupees = 0;

		this.allCars = new ArrayList();
		this.semaphore = new Semaphore(5, true);

		Place = new boolean[4];
		for (int i = 0; i < Place.length; i++) {
			Place[i] = false;
		}
	}
	int car3Number;

	// Une fonction qui retourne les places disponibles dans le parking
	int places() {
		return (capacite - this.PlacesOccupees);
	}

	// Une fonction qui retourne vrai s'il y a de place disponible,
	boolean accept(Voiture car) {
		//Vérifier s'il y a des places disponibles
		if (places() > 0) {

			//If Found A Place For A Car Then Incremment Number Of Place Busy
			PlacesOccupees++;
			this.checkFreePlaceForMove(car);
			infoVoitures.add(car);
			System.out.format("[Parking] :%s acceptée, il reste %d places \n", car.nom, places());
			System.out.format("Voiture Inside The Garees\n");
			System.out.println(infoVoitures);
			return (true);
		} else {
			System.out.format("Parking : %s refusée, il reste  %d places \n", car.nom, places());
			goAroundTheParking(car, car.posx, car.posy);
			return (false);
		}
	}

	public void goAroundTheParking(Voiture car, int x, int y) {

		while (y > 70) {
			y -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		// Up To Top
		if (car.side == "l") {

			while (y > 0) {
				y -= 10;
				car.setLocation(x, y);
				try {
					Thread.sleep(50);
				} catch (InterruptedException ex) {
					Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		}
		car.changeImage("images/1.png");
		car.setBounds(x, y, 150, 141);
		while (x < 940) {
			x += 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if (car.side == "l") {
			x += 70;
			car.setLocation(x, y);
		}

		//Switch Icon Maening Round The Car 
		car.changeImage("images/2.png");
		while (y < 690) {
			y += 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		car.changeImage("images/4.png");
		car.setBounds(x, y, 150, 141);

		while (x > car.positionX) {

			x -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		// Switch Icon Maening Round The Car
		car.changeImage("images/3.png");
		while (y > car.positionY) {
			y -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		// Return A Defualt Image
		car.posx = x;
		car.posy = y;

	}

	void leave(Voiture car) {
		//Decrement Number Of The Cars In The Parking
		PlacesOccupees--;
		infoVoitures.remove(car);
		System.out.format("Parking :[%s] est sortie, reste  %d places and posY = %d\n", car.nom, places(), car.posy);

		switch (car.posy) {
		case 10:
			Place[0] = false;

			break;
		case 160:
			Place[1] = false;

			break;
		case 330:
			Place[2] = false;

			break;

		case 490:
			Place[3] = false;
			break;
		default:
			Place[0] = false;

			break;
		}
		this.moveCarByRoundOutside(car, car.posx, car.posy);
	}

	//Function For Set A Background In Our Panel And This Function Inbuild In For Class JPANEL AND We Overrding That Function
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		image = new ImageIcon("images/bg.jpg");
		image.paintIcon(this, g, WIDTH, WIDTH);
	}

	public void checkFreePlaceForMove(Voiture car) {
		
		//Check If Palce One Empty
		if (!Place[0]) {
			Place[0] = true;
			this.moveCarDirect(car, car.posx, car.posy);
		}
		// Check If Palce Two Empty
		else if (!Place[1]) {
			Place[1] = true;
			this.moveCarByRound(car, car.posx, car.posy, 160);

		}	
		//Check If Palce Three Empty                 
		else if (!Place[2]) {
			Place[2] = true;
			this.moveCarByRound(car, car.posx, car.posy, 330);

		}
		// Check If Palce Four Empty
		else if (!Place[3]) {
			Place[3] = true;
			this.moveCarByRound(car, car.posx, car.posy, 490);

		} else {

			System.out.println("All Palce Filled");
		}

	}

	public void moveCarDirect(Voiture car, int x, int y) {

		while (y > 60) {
			y -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if (car.side == "l") {

			while (y > 0) {
				y -= 10;
				car.setLocation(x, y);
				try {
					Thread.sleep(50);
				} catch (InterruptedException ex) {
					Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		}
		car.changeImage("images/1.png");
		car.setBounds(x, y, 150, 141);

		while (x < 1200) {

			x += 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		car.posx = x;
		car.posy = y;

	}

	public void moveCarByRound(Voiture car, int x, int y, int Distance) {
		while (y > 70) {

			y -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		if (car.side == "l") {

			while (y > 0) {
				y -= 10;
				car.setLocation(x, y);
				try {
					Thread.sleep(50);
				} catch (InterruptedException ex) {
					Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		}
		car.changeImage("images/1.png");
		car.setBounds(x, y, 150, 141);
		while (x < 940) {
			x += 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		if (car.side == "l") {
			x += 70;
			car.setLocation(x, y);
		}

		//Switch Icon Maening Round The Car 
		car.changeImage("images/2.png");

		while (y < Distance) {

			y += 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

//         Return A Defualt Image

		if (car.side == "r") {

			for (Voiture cl : this.allCars) {

				if (y == (int) cl.getLocation().getY()) {

					try {
						Thread.sleep(500);
					} catch (InterruptedException ex) {
						Logger.getLogger(Parking.class.getName()).log(Level.SEVERE, null, ex);
					}
				}

			}
		}
		car.changeImage("images/1.png");
		car.setBounds(x, y, 150, 141);

		while (x < 1200) {

			x += 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		car.posx = x;
		car.posy = y;

	}

	public void moveCarByRoundOutside(Voiture car, int x, int y) {

		//Go Back
		while (x > 1500) {
			x -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		for (Voiture cl : this.allCars) {

			if (y == (int) cl.getLocation().getY()) {

				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					Logger.getLogger(Parking.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

		}
		while (x > 1020) {
			x -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}	
		//Switch Icon Maening Round The Car 
		// Go To Down
		car.changeImage("images/2.png");
		while (y < 690) {

			y += 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}

		}
		car.changeImage("images/4.png");// Go To Left
		car.setBounds(x, y, 150, 141);
		while (x > car.positionX) {
			x -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		car.changeImage("images/3.png");
		while (y > car.positionY) {
			y -= 10;
			car.setLocation(x, y);
			try {
				Thread.sleep(50);
			} catch (InterruptedException ex) {
				Logger.getLogger(Voiture.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		car.posx = x;
		car.posy = y;

	}
}
