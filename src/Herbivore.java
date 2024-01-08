import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * Auteurs : 
 * Diego Gonzalez (20191459)
 * Sebastien Luciani (20175116)
 */


public class Herbivore extends Animal {

    //constructeur
    public Herbivore(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie, double resilience, double fertilite, int ageFertilite, double energieEnfant, double debrouillardise, double voraciteMin, double voraciteMax, HashSet<String> aliments, double tailleMaximum) {
        super(nomEspece, energie, age, besoinEnergie, efficaciteEnergie, resilience, fertilite, ageFertilite, energieEnfant, debrouillardise, aliments, tailleMaximum);

        if (voraciteMin < 0 | voraciteMin > 1){
            throw new IllegalArgumentException("la voracité minimale doit être entre 0 et 1");
        }
        if (voraciteMax < 0 | voraciteMax > 1){
            throw new IllegalArgumentException("la voracité maximale doit être entre 0 et 1");
        }
        if (voraciteMin > voraciteMax){
            throw new IllegalArgumentException("la voracité minimale doit être inférieure ou égale à la voracité maximale");
        }

        this.voraciteMin = voraciteMin;
        this.voraciteMax = voraciteMax;
    }

    //Getters non hérités

    public double getVoraciteMin(){
        return this.voraciteMin;
    }

    public double getVoraciteMax(){
        return this.voraciteMax;
    }

    public List<Herbivore> essaisReproduction(Double energieAbsorbee){
        

        // Énergie supplémentaire
        Double energieSupp = energieAbsorbee - this.besoinEnergie;
        List<Herbivore> nouveauxNesHerbivore = new ArrayList<>();

        //Est-ce qu'on a de l'énergie supplémentaire pour avoir un enfant?
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
                        
                        Herbivore nouvelHerbivore = usineEnfant.creerHerbivore();
                        nouveauxNesHerbivore.add(nouvelHerbivore);

                    }
                    else {
                        chancesDeReproduire -= 1;
                    }
                }
                else {
                    //pas besoin d'épuiser les chances de se reproduire pour sotir, on sait que l' herbivore ne se reproduira pas
                    return nouveauxNesHerbivore;
                }
                    
            }

        }
        return nouveauxNesHerbivore;
        
    }

    
    public double mangerPlante(Plante planteMangee){
        double fractionEnergie;
        double energieMangee;

        //on trouve la fraction de l'énergie de l'organisme que l'animal mangera
        fractionEnergie = getNombreRandom(this.voraciteMin, this.voraciteMax);
        energieMangee = planteMangee.getEnergie() * fractionEnergie;

        //Modifier l'energie de la plante
        planteMangee.estMangee(fractionEnergie);

        return energieMangee;
    }

    @Override
    public UsineOrganisme copieAUsine(){

        UsineOrganisme usine = super.copieAUsine();

        usine.setVoraciteMin(this.voraciteMin);
        usine.setVoraciteMax(this.voraciteMax);

        return usine;
    }

}