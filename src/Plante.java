import java.util.ArrayList;
import java.util.List;

/**
 * Auteurs : 
 * Diego Gonzalez (20191459)
 * Sebastien Luciani (20175116)
 */


public class Plante extends Organisme {
    //Les attributs sont herites d'Organisme.java
        
    public Plante(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie, double resilience, double fertilite, int ageFertilite, double energieEnfant, double tailleMaximum){
        super(nomEspece,  energie,  age,  besoinEnergie,  efficaciteEnergie,  resilience,  fertilite,  ageFertilite, energieEnfant, tailleMaximum);
    }


    // modificateurs conditionnés de l'energie
    public Double energieAbsorbee( int NRJsoleil, Double NRJtotale){
        return NRJsoleil * (this.energie / NRJtotale);
    }

    //tentatives de la plante pour se reproduire, retourne les enfants de la plante, s'il y en a
    public List<Plante> essaisReproduction2(Double energieAbsorbee){
        
        //variables locales
        List<Plante> nouveauxNesPlante = new ArrayList<>();
        Double energieSupp = energieAbsorbee - this.besoinEnergie;
        //energie qu'aurait la plante après reproduction
        Double NRJapresRep = this.energie + energieAbsorbee - this.energieEnfant;
        Double chancesDeReproduire = Math.floor(energieSupp);
    
        //la plante doit avoir au moins l'age de fertilite pour pouvoir se reproduire
        if (this.age >= this.ageFertilite & energieSupp > 0){

            while (chancesDeReproduire>0){
                if (energieAbsorbee > 0 & NRJapresRep > 0){
                    boolean aUnEnfant = Math.random() <= this.fertilite;
                    if (aUnEnfant){
                        //la plante perd l'energie de l'enfant lors de sa création
                        //on met a jour les chances de se reproduire,
                        chancesDeReproduire = Math.floor(chancesDeReproduire-this.energieEnfant);
                        //l'energie supplementaire
                        energieAbsorbee -= this.energieEnfant;
                    //et l'energie qu'aurait la plante apres reproduction
                        NRJapresRep -= this.energieEnfant;

                        //´on prend l'énergie de la plante parent elle-même
                        if (energieAbsorbee < 0){
                            this.energie += energieAbsorbee;
                        }
                        //on cree une usine identique à la plante mère
                        UsineOrganisme usineEnfant = this.copieAUsine();
                        Plante nouvellePlante = usineEnfant.creerPlante();
                        nouveauxNesPlante.add(nouvellePlante);
                    }
                    // si la plante ne se reproduit pas, on perd juste une chance et on reessaye
                    else chancesDeReproduire -= 1;
                }
            }
        }
        return nouveauxNesPlante;
    }
    
    public List<Plante> essaisReproduction(Double energieAbsorbee){
        

        // Énergie supplémentaire
        Double energieSupp = energieAbsorbee - this.besoinEnergie;
        List<Plante> nouveauxNesPlante = new ArrayList<>();

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
                        
                        //on cree une usine identique à la plante mère
                        UsineOrganisme usineEnfant = this.copieAUsine();
                        
                        Plante nouvellePlante = usineEnfant.creerPlante();
                        nouveauxNesPlante.add(nouvellePlante);

                    }
                    else {
                        chancesDeReproduire -= 1;
                    }
                }
                else {
                    //pas besoin d'épuiser les chances de se reproduire pour sotir, on sait que la plante ne se reproduira pas
                    return nouveauxNesPlante;
                }
                    
            }

        }
        return nouveauxNesPlante;
        
    }
    

}
