import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Auteurs : 
 * Diego Gonzalez (20191459)
 * Sebastien Luciani (20175116)
 */

//classe abstraite utilisée pour mieux encapsuler Herbivore.java et Carnivore.java
public abstract class Animal extends Organisme{

    //Les autres attributs sont herites d'Organisme.java
    protected double debrouillardise;
    protected double voraciteMin;
    protected double voraciteMax;
    protected Set<String> aliments;

    //constructeur
    public Animal(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie, double resilience, double fertilite, int ageFertilite, double energieEnfant, double debrouillardise, HashSet<String> aliments, double tailleMaximum ){
        super( nomEspece,  energie,  age,  besoinEnergie,  efficaciteEnergie,  resilience,  fertilite,  ageFertilite,  energieEnfant, tailleMaximum);

        if(debrouillardise < 0 | debrouillardise > 1){
            throw new IllegalArgumentException("la debrouillardise doit être entre 0 et 1");
        }
        if (aliments == null){
            throw new IllegalArgumentException("la liste d'aliments à manger doit être non-null");
        }
        if (tailleMaximum <= 0){
            throw new IllegalArgumentException("la tailleMaximum doit être supérieure à 0");
        }

        this.debrouillardise = debrouillardise;
        this.aliments = aliments;
    }

    public double getDebrouillardise(){
        return this.debrouillardise;
    }

    public Set<String> getAliments(){
        return this.aliments;
    }

    //retourne l'index de l'organisme que l'animal mangera
    public int indexOrgMange(List<Organisme> listeOrganismesVivants){

        Organisme randomOrganisme;
        int indexRandomOrganisme;
        List<Organisme> listeAlimentsDispo = new ArrayList<Organisme>();


        //Creer la liste d'Organismes comestibles par l'Animal
        for (Organisme comestible : listeOrganismesVivants){
            for (String aliment : this.aliments){
                //l'organisme doit faire partie de la diette de l'animal, être plus petit que lui et ne pas être lui même
                if (comestible.getNomEspece().equals(aliment) & comestible.getTailleMaximum() < this.getTailleMaximum()){
                    listeAlimentsDispo.add(comestible);
                }
            }
        }
        //si l'animal ne peut rien manger, alors on retourne un index négatif. 
        if (listeAlimentsDispo.size() == 0){
            return -1;
        }

        //on choisit un index aléatoire de la liste des aliments que l'animal peut manger
        int randomListIndex = (int) getNombreRandom(0, (double) listeAlimentsDispo.size());
        //on trouve l'organisme choisi parmi la liste des organismes que notre animal pouvait manger
        randomOrganisme = listeAlimentsDispo.get(randomListIndex);
        //on trouve son index dans la liste générale
        indexRandomOrganisme = listeOrganismesVivants.indexOf(randomOrganisme);
        return indexRandomOrganisme;

        //N.B: Il n'y a pas deux organismes avec réference pareille donc l'organisme obtenu sera toujours le bon et pas un dupliqué
    }


    //utilisé pour le calcul de l'énergie prise
    public double getNombreRandom(double min, double max){
        return Math.random() * (max-min) + min;
    }

    //Creer une copie de l'usine utilisée pour l'instance d'un animal
    @Override
    public UsineOrganisme copieAUsine(){

        UsineOrganisme usine = super.copieAUsine();

        usine.setDebrouillardise(this.debrouillardise);

        //Ajouter les aliments de l'espèce
        for (String especeComestible : this.getAliments()){
            usine.addAliment(especeComestible);
        }
        
        return usine;
    }

    //détermine le nombre de fois que l'animal s'alimente en un cycle
    public int nombreFoisAlimente(){
        int nombreFoisAlimente = 0;
        double nombreRandom = Math.random();
        while (nombreRandom < this.debrouillardise){
            nombreFoisAlimente += 1;
            nombreRandom = Math.random();
        }
        return nombreFoisAlimente;
    }
}