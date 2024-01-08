
/**
 * Auteurs : 
 * Diego Gonzalez (20191459)
 * Sebastien Luciani (20175116)
 */


public abstract class Organisme {
    //set of plant attributes
    protected String nomEspece;
    protected double energie;
    protected int age;
    protected double besoinEnergie;
    protected double efficaciteEnergie;
    protected double resilience;
    protected double fertilite;
    protected int ageFertilite;
    protected double energieEnfant;
    protected double tailleMaximum;
    
    public Organisme(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie, double resilience, double fertilite, int ageFertilite, double energieEnfant, double tailleMaximum){
        if (nomEspece.equals("") ){
            throw new IllegalArgumentException("La plante doit avoir un nom d'espèce!");
        }
        if (energie < 0 ){
            throw new IllegalArgumentException("L'énergie de la plante doit être supérieur ou égale à zéro!");
        } 
        if (age < 0 ){
            throw new IllegalArgumentException("L'âge de la plante doit être supérieur ou égal à zéro!");
        }
        if (besoinEnergie <= 0 ){
            throw new IllegalArgumentException("Le besoin d'énergie de la plante doit être supérieur à zéro!");
        }
        if (efficaciteEnergie < 0 | efficaciteEnergie > 1 ){
            throw new IllegalArgumentException("L'efficacité d'énergie de la plante doit être comprise entre zéro et un!");
        }
        if (resilience < 0 | resilience > 1 ){
            throw new IllegalArgumentException("La resilience de la plante doit être comprise entre zéro et un!");
        }
        if (fertilite < 0 | fertilite > 1 ){
            throw new IllegalArgumentException("La fertilité de la plante doit être comprise entre zéro et un!");
        }
        if (ageFertilite < 0 ){
            throw new IllegalArgumentException("L'âge de fertilité de la plante doit être supérieur ou égal à zéro!");
        }
        if (energieEnfant <= 0 ){
            throw new IllegalArgumentException("L'énergie des enfants de la plante doit être supérieur ou égal à zéro!");
        }

        this.nomEspece = nomEspece;
        this.energie = energie;
        this.age = age;
        this.besoinEnergie = besoinEnergie;
        this.efficaciteEnergie = efficaciteEnergie;
        this.resilience = resilience;
        this.fertilite = fertilite;
        this.ageFertilite = ageFertilite;
        this.energieEnfant = energieEnfant;
        this.tailleMaximum = tailleMaximum;
    }

    //set of getters
    public String getNomEspece(){
        return this.nomEspece;
    }

    public double getEnergie(){
        return this.energie;
    }

    public int getAge(){
        return this.age;
    }

    public double getBesoinEnergie(){
        return this.besoinEnergie;
    }

    public double getEfficaciteEnergie(){
        return this.efficaciteEnergie;
    }

    public double getResilience(){
        return this.resilience;
    }
    
    public double getFertilite(){
        return this.fertilite;
    }
    
    public int getAgeFertilite(){
        return this.ageFertilite;
    }
    
    public double getEnergieEnfant(){
        return this.energieEnfant;
    }

    //getter de l'attribut additionel
    public double getTailleMaximum(){
        return this.tailleMaximum;
    }

    public boolean encoreVivant(){
        return this.energie > 0;
    }

    public void perdNRJmanquante(Double NRJmanquante){
        this.energie -= NRJmanquante;
    }
    //met l'energie de la plante à 0 et la prépare à mourir à la fin de l'itération.
    public void vaMourir(){
        this.energie = 0;
    }
    //ajoute l'énergie absorbée restante à la plante, après avoir essayé de se reproduire
    public void ajouteNRJrestante(Double energieRestante){
        this.energie += energieRestante * this.efficaciteEnergie;
    }

    //modificateurs de l'âge de la plante
    public void ageUnCycle(){
        this.age += 1;
    }

    public void estMangee(double fractionEnergie){
        if( this instanceof Plante ){
            this.energie = this.energie - this.energie * fractionEnergie;
        }
        else {
            this.vaMourir();
        }
    }


    //met à preuve la résilience de l'organisme, avec la possibilité qu'il meure
    public void preuveResilience(Double energieAbsorbee){
        //l'arrondi à l'unité inférieure prévient l'apparition de valeurs entre 0 et 1,
        //ce qui donnerait l'erreur d'une probabilité supérieure à 1
        Double  energieManquante = Math.floor(this.besoinEnergie - energieAbsorbee);
        Double chanceSurvie;
        boolean survie;
        if (energieManquante > 0){
            chanceSurvie = Math.pow(this.resilience, energieManquante);

            //Savoir si la plante a survécu le cycle
            survie = Math.random() <= chanceSurvie;
            if (!survie){
                this.vaMourir();
            }
            else{
                this.energie -= energieManquante;
            }
        }
    }

    //Creer une copie de l'usine utilisée pour l'instance de cette plante
    public UsineOrganisme copieAUsine(){

        UsineOrganisme usine = new UsineOrganisme();
        usine.setNomEspece(this.nomEspece);
        usine.setEnergieEnfant(this.energieEnfant);
        usine.setBesoinEnergie(this.besoinEnergie);
        usine.setEfficaciteEnergie(this.efficaciteEnergie);
        usine.setResilience(this.resilience);
        usine.setFertilite(this.fertilite);
        usine.setAgeFertilite(this.ageFertilite);

        return usine;
    }

}