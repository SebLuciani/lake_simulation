import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Auteurs : 
 * Diego Gonzalez (20191459)
 * Sebastien Luciani (20175116)
 */


public class Carnivore extends Animal {

    //constructeur
    public Carnivore(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie, double resilience, double fertilite, int ageFertilite, double energieEnfant, double debrouillardise, HashSet<String> aliments, double tailleMaximum) {
        super(nomEspece, energie, age, besoinEnergie, efficaciteEnergie, resilience, fertilite, ageFertilite, energieEnfant, debrouillardise, aliments, tailleMaximum);
    }

    //tentatives du carnivore pour se reproduire, retourne les enfants du carnivore, s'il y en a
    public List<Carnivore> essaisReproduction(Double energieAbsorbee){

        // Énergie supplémentaire
        Double energieSupp = energieAbsorbee - this.besoinEnergie;
        //Liste Dynamique pour garder les nouveaux nés
        List<Carnivore> nouveauxNesCarnivore = new ArrayList<>();

        //Est-ce qu'on a de l'énergie supplémentaire et l'âge suffisante pour avoir un enfant?
        if ( this.age >= this.ageFertilite & energieSupp > 0){
            
            //Variables pour préparer un enfant
            double energieApresRep = this.energie + energieSupp - this.energieEnfant;
            int chancesDeReproduire = (int) Math.floor(energieSupp);

            while (chancesDeReproduire > 0) {
                
                if (energieSupp > 0 & energieApresRep > 0) {
                    boolean aUnEnfant = Math.random() <= this.fertilite;
                    
                    if (aUnEnfant){
                        chancesDeReproduire = (int) Math.floor(chancesDeReproduire - this.energieEnfant);
                        energieSupp -= this.energieEnfant;
                        energieApresRep -= this.energieEnfant; 
                        
                        //on cree une usine identique à l'herbivore mère
                        UsineOrganisme usineEnfant = this.copieAUsine();
                        
                        Carnivore nouveauCarnivore = usineEnfant.creerCarnivore();
                        nouveauxNesCarnivore.add(nouveauCarnivore);

                    }
                    else {
                        chancesDeReproduire -= 1;
                    }
                }
                else {
                    //pas besoin d'épuiser les chances de se reproduire pour sotir, on sait que le carnivore ne se reproduira pas
                    return nouveauxNesCarnivore;
                }    
            }
        }
        return nouveauxNesCarnivore;
    }    
    
    public double mangerHerbivore(Herbivore herbMange){
        double energieMangee;
        energieMangee = herbMange.getEnergie();

        //Modifier l'energie de l'herbivore à 0
        herbMange.vaMourir();

        return energieMangee;
    }

    public double mangerCarnivore(Carnivore carnMange){
        double energieMangee;
        energieMangee = carnMange.getEnergie() ;
        
        //Modifier l'energie du Carnivore à 0
        carnMange.vaMourir();

        return energieMangee;
    }

}