import java.util.HashMap;
import java.util.HashSet;

/**
 * Auteurs : Diego Gonzalez (20191459)
 *  Sebastien Luciani (20175116)
 */

public  class UsineOrganisme {
    private String nomEspece;
    private Double energieEnfant;
    private Double energie;
    private int age;
    private Double besoinEnergie;
    private Double efficaciteEnergie;
    private Double resilience;
    private Double fertilite;
    private Integer ageFertilite;

    private Double debrouillardise;
    private Double voraciteMin;
    private Double voraciteMax;
    private HashSet<String> aliments = new HashSet<>();

    private double tailleMaximum;

    private HashMap<String, Boolean> conditionsInit = new HashMap<String, Boolean>();

    public UsineOrganisme(){
        this.conditionsInit.put("nomEspece", false);
        this.conditionsInit.put("energieEnfant", false);
        this.conditionsInit.put("besoinEnergie", false);
        this.conditionsInit.put("efficaciteEnergie", false);
        this.conditionsInit.put("resilience", false);
        this.conditionsInit.put("fertilite", false);
        this.conditionsInit.put("ageFertilite", false);
        this.conditionsInit.put("debrouillardise", false);
        this.conditionsInit.put("voraciteMin", false);
        this.conditionsInit.put("voraciteMax", false);
        this.conditionsInit.put("aliments", false);
        this.conditionsInit.put("tailleMaximum", false);
    }


    //Méthode pour vérifier que tous les attributs soient bien initialisés
    private void verificateur(String typeOrganisme, String sousEspece) throws ConditionsInitialesInvalides{

        if (!this.conditionsInit.get("nomEspece")){
            throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé nomEspece");
        }
        if (!this.conditionsInit.get("energieEnfant")){
            throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé energieEnfant.");
        }
        if (!this.conditionsInit.get("besoinEnergie")){
            throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé besoinEnergie.");
        }
        if (!this.conditionsInit.get("efficaciteEnergie")){
            throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé efficaciteEnergie.");
        }
        if (!this.conditionsInit.get("resilience")){
            throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé resilience.");
        }
        if (!this.conditionsInit.get("fertilite")){
            throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé fertilite.");
        }
        if (!this.conditionsInit.get("ageFertilite")){
            throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé ageFertilite.");
        }

        if (typeOrganisme.equals("Animal")){

            if(!this.conditionsInit.get("debrouillardise")){
                throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé debrouillardise");
            }
            if(!this.conditionsInit.get("aliments")){
                throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé aliments");
            }
            if (!this.conditionsInit.get("tailleMaximum") ){
                this.tailleMaximum = this.energieEnfant * 10;
            }

            if (sousEspece == "Herbivore"){
                if(!this.conditionsInit.get("voraciteMin")){
                    throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé voraciteMin");
                }
                if(!this.conditionsInit.get("voraciteMax")){
                    throw new ConditionsInitialesInvalides("Vous n'avez pas initialisé voraciteMax");
                }
            }
        }
    }

    //Ensemble des setters communs à tous
    public void setNomEspece(String newNomEspece){
        this.nomEspece= newNomEspece;
        this.conditionsInit.replace("nomEspece", true);
    }

    public void setAge(int newAge){
        this.age = newAge;
        this.conditionsInit.replace("age", true);
    }

    public void setBesoinEnergie(double newBesoinEnergie){
        this.besoinEnergie = newBesoinEnergie;
        this.conditionsInit.replace("besoinEnergie", true);
    }

    public void setEfficaciteEnergie(double newEfficaciteEnergie){
        this.efficaciteEnergie = newEfficaciteEnergie;
        this.conditionsInit.replace("efficaciteEnergie", true);
    }

    public void setResilience( double newResilience){
        this.resilience = newResilience;
        this.conditionsInit.replace("resilience", true);
    }

    public void setFertilite(double newFertilite){
        this.fertilite = newFertilite;
        this.conditionsInit.replace("fertilite", true);
    }

    public void setAgeFertilite(int newAgeFertilite){
        this.ageFertilite = newAgeFertilite;
        this.conditionsInit.replace("ageFertilite", true);
    }

    public void setEnergieEnfant(double newEnergieEnfant){
        this.energieEnfant = newEnergieEnfant;
        this.conditionsInit.replace("energieEnfant", true);
    }

    //Setters utilisés pour créer un herbivore
    public void setDebrouillardise(double newDebrouillardise){
        this.debrouillardise = newDebrouillardise;
        this.conditionsInit.replace("debrouillardise", true);
    }

    public void setVoraciteMin(double newVoraciteMin){
        this.voraciteMin = newVoraciteMin;
        this.conditionsInit.replace("voraciteMin", true);
    }

    public void setVoraciteMax(double newVoraciteMax){
        this.voraciteMax = newVoraciteMax;
        this.conditionsInit.replace("voraciteMax", true);
    }

    //Setter des attributs supplémentaires 

    public void setTailleMaximum(double newtailleMaximum){
        this.tailleMaximum = newtailleMaximum;
        this.conditionsInit.replace("tailleMaximum", true);
    }

    public void addAliment(String newAliment){
        this.aliments.add(newAliment);
        this.conditionsInit.replace("aliments", true);
    }

    //Méthode pour créer une instance d'une Plante en vérifiant que les attributs soient bien initialisés
    public Plante creerPlante(){
        this.energie = this.energieEnfant;
        this.age = 0;
        
        //Une plante n'a pas de taille mais une méthode d'Animal en a besoin donc on l'initialise à -1 (comme valeur neutre)
        this.tailleMaximum = -1;
        try {
            this.verificateur("Plante", "na");
        } catch (ConditionsInitialesInvalides e) {
            e.printStackTrace();
        }
        //Succès
        return new Plante(this.nomEspece, this.energie, this.age, this.besoinEnergie, this.efficaciteEnergie, this.resilience, this.fertilite, this.ageFertilite, this.energieEnfant, this.tailleMaximum);
    }

    
    //Méthode pour créer une instance d'un Herbivore en vérifiant que les attributs soient bien initialisés
    public Herbivore creerHerbivore(){
        this.energie = this.energieEnfant;
        this.age = 0;
        try {
            this.verificateur("Animal", "Herbivore");
        } catch (ConditionsInitialesInvalides e) {
            e.printStackTrace();
        }
        //Succès
        return new Herbivore(this.nomEspece, this.energie, this.age, this.besoinEnergie, this.efficaciteEnergie, this.resilience, this.fertilite, this.ageFertilite, this.energieEnfant, this.debrouillardise, this.voraciteMin, this.voraciteMax, this.aliments, this.tailleMaximum);
    }

    
    //Méthode pour créer une instance d'un Carnivore en vérifiant que les attributs soient bien initialisés
    public Carnivore creerCarnivore(){
        this.energie = this.energieEnfant;
        this.age = 0;
        try {
            this.verificateur("Animal", "Carnivore");
        } catch (ConditionsInitialesInvalides e) {
            e.printStackTrace();
        }
        //Succès
        return new Carnivore(this.nomEspece, this.energie, this.age, this.besoinEnergie, this.efficaciteEnergie, this.resilience, this.fertilite, this.ageFertilite, this.energieEnfant, this.debrouillardise, this.aliments, this.tailleMaximum);
    }
}