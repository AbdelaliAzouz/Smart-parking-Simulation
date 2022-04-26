import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author {AZOUZ ABDELALI}
 *
 * {M1 MBD}
 */ 
//Déclaration de la clase voiture : 
public class Voiture extends JLabel implements Runnable { 
	String nom; 
	Parking park;
        ImageIcon carImg ;
        int posx;
        int posy;
        int positionY;
        int positionX;
        String side;
        public static Thread MesVoitures[];
        
    //Le constructeur de la classe voiture qui créé des voitures
    public Voiture(String name, Parking park,int x,int y){
    	this.nom    = name; 
    	this.park   = park; 
        this.posx   = x;
        this.posy   = y;
        this.positionY = y;
        this.positionX = x;
        this.carImg = new ImageIcon("images/3.png");
        this.setIcon(carImg);
        Dimension size = this.getPreferredSize();
        this.setBounds(posx, posy, size.width,size.height);
       }
    
    //Bloquer la voiture si le parking est pleine
    public boolean rentrer() throws InterruptedException{  
    	  if(this.park.accept(this)){
                    return true;      
    	  	}else{
    	  			return false;
    	  		 }
      }
      
      
    public void changeImage(String name){     
    	  this.setIcon(new ImageIcon(name));
    }
      
    public String toString(){
          return "Le nom du voiture : "+this.nom + " posX = "+posx + " posY = "+this.posy + " le coté du voiture : "+this.side;
      }

    public void run(){
    	  System.out.println(this);
    	  	try {
    	  		while(true){
                Thread.sleep((long)  (5000* Math.random())); //Le temps à attend à l'extérieur du parking
                
                //Demande l'autorisation d'entrer
                System.out.format("[%s]: Je demande à  rentrer  \n", this.nom);
               
                //Vérifier l'acces
                this.park.semaphore.acquire();
                if(!(this.rentrer())){    
                     System.out.format("[%s]  : Car want Go Inside A Gars  \n", this.nom);
                }
                this.park.semaphore.release();
                Thread.sleep((long)  (5000* Math.random()));
                if(this.park.infoVoitures.contains(this)){
                	System.out.format("[%s]: Je demande Ã  sortir  \n", this.nom);
                this.park.semaphore.acquire();
               //Quitter le parking et décrémenter le nombre des places réservées dans le parking
                this.park.leave(this);
                this.park.semaphore.release();
                	}
    	  		}
          }catch(InterruptedException e){
              System.out.println(e.getMessage());
          }
        }
        
      	public static void main(String[] args) {
        int TailleParking=4;
        JFrame frame 		= new JFrame("The Animation For Parking The Cars");
        Parking  parking       = new Parking(TailleParking);
        frame.setContentPane(parking);
        parking.setLayout(null);
        frame.setSize(1540,824);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        int nbVoitures = 8 ;
        String CarName = "car";
        JButton startAnimation = new JButton("Start Animation");
        parking.add(startAnimation);
        startAnimation.setVisible(true);
        startAnimation.setBounds(500, 400, 150, 50);
        startAnimation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        startAnimation.setBackground(Color.green);
        startAnimation.setForeground(Color.black);
        startAnimation.setBorder(null);
 
        //Créer 8 threades 
        Thread MesVoitures[][] = new Thread[4][2];
        int x ;
        int y = 150;
        for (int i = 0; i< 4; i++){
            x = 60;
            for(int j=0;j<2;j++){
                
            Voiture car =  new Voiture(CarName +" " + i+j,parking,x,y);
            if(j == 0){
                car.side = "l";
            }else{
                car.side = "r";
            }
            
            MesVoitures[i][j] =  new Thread(car);
            parking.add(car);
            parking.allCars.add(car);
            x = x + 80;
            }
            y = y + 160;
        }
        
      //Créer un ActioListener
       ActionListener actionListenerForButoon = new ActionListener() {
       public void actionPerformed(ActionEvent actionEvent) {
          
          //Exécuter le Thread
    	   for (int i = 0; i< 4; i++){
    		   for(int j=0;j<2;j++){
                   MesVoitures[i][j].start();
           }
      }
    }
 };
      startAnimation.addActionListener(actionListenerForButoon);
    }
}

        
