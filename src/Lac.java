/**
 * Auteurs : 
 * Diego Gonzalez (20191459)
 * Sebastien Luciani (20175116)
*/


import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.*;


public final class Lac {
    private final int energieSolaire;
    private final List<Plante> plantes;
    private final List<Herbivore> herbivores;
    private final List<Carnivore> carnivores;

    public Lac(int energieSolaire, List<Plante> plantes, List<Herbivore> herbivores, List<Carnivore> carnivores) {
        this.energieSolaire = energieSolaire;
        this.plantes = plantes;
        this.herbivores = herbivores;
        this.carnivores = carnivores;
    }

    
    //Avance la simulation d'un cycle
    public void tick() {

        //************************************************Plantes************************************************\\
        
        //Variables non dependantes de l'instance
        Double energieTotale = 0.0;
        //On utilise un index externe au loop car la liste est en constante modification
        int indexPlante = 0;
        
        //Réinitialisation des enfants à aucun
        List<Plante> nouveauxNesTotalPlante = new ArrayList<>();

        //calcul l'énergie totale avant les modifications du cycle
        for (Plante plante1 : this.plantes){ //somme de l energie de toutes les plantes
            energieTotale += plante1.getEnergie();
        }

        //Int pour itérer à travers toute la liste de plantes
        int limitePlante = this.plantes.size();

        for (int iteration1 = 0; iteration1 < limitePlante; iteration1++) {

            //mûrit la plante d'une unité
            this.plantes.get(indexPlante).ageUnCycle();

            //Phase de distribution d'énergie solaire:
            Double energieAbsorbeePlante = this.plantes.get(indexPlante).energieAbsorbee(energieSolaire, energieTotale);
            
            //Résilience / Survie de la plante:
            this.plantes.get(indexPlante).preuveResilience(energieAbsorbeePlante);

            //Essai de reproduction de la plante
            //on récupère les enfants de la plante, s'il y en a, et on les met dans la variable nouveauxNesPlante
            List<Plante> nouveauxNesPlante = this.plantes.get(indexPlante).essaisReproduction(energieAbsorbeePlante);

            //Calcul de l'énergie perdue lors de la reproduction
            Double energiePerduePlante = nouveauxNesPlante.size() * this.plantes.get(indexPlante).getEnergieEnfant();
            //Variable pour ajouter l'énergie restante à l'énergie de la plante
            Double NRJabsorbeeRestantePlante = energieAbsorbeePlante - energiePerduePlante;
            
            //Ajouter l'énergie restante
            if (this.plantes.get(indexPlante).encoreVivant() & NRJabsorbeeRestantePlante > 0 ){
                this.plantes.get(indexPlante).ajouteNRJrestante(NRJabsorbeeRestantePlante);
            } 
            //Si la plante est morte l'éliminer de la liste des vivants
            else if (!this.plantes.get(indexPlante).encoreVivant()){
                this.plantes.remove(indexPlante);
                indexPlante -=1;
            }
            
            indexPlante += 1;
            //ajoute les enfants de cette instance de plante dans une liste de nouveaux nés 
            for (Plante nouveauNe : nouveauxNesPlante ){
                nouveauxNesTotalPlante.add(nouveauNe);
            }

        }

        //************************************************Herbivores************************************************\\
    
        //Variables non dépendantes de l'instance
        int indexHerb = 0;

        //Réinitialisation des enfants à aucun
        List<Herbivore> nouveauxNesTotalHerbivore = new ArrayList<>();
        //Int pour itérer à travers toute la liste d'herbivores
        int limiteHerbivore = this.herbivores.size();

        for (int iteration2 = 0 ; iteration2 < limiteHerbivore; iteration2++) {

            //l'energie absorbée est 0 par défaut
            Double energieAbsorbeeHerbivore = 0.0;
            int nbFoisAlimenteHerbivore;
            
            //mûrit l'herbivore d'une unité
            this.herbivores.get(indexHerb).ageUnCycle();

            //  Phase d'alimentation
            nbFoisAlimenteHerbivore = this.herbivores.get(indexHerb).nombreFoisAlimente();

            for (int i=0; i< nbFoisAlimenteHerbivore ; i++){
                //Transforme une liste de Plantes a une liste d'Organismes pour pouvoir les passer en paramètre
                List<Organisme> plantesOrganisme = transformerPlantesAOrganismes(); 
                //Trouver la plante aleatoire à manger :
                int indexPlanteAManger = this.herbivores.get(indexHerb).indexOrgMange(plantesOrganisme);
                if (indexPlanteAManger != -1){
                    //Manger la plante aleatoire:

                    energieAbsorbeeHerbivore += this.herbivores.get(indexHerb).mangerPlante(this.plantes.get(indexPlanteAManger));
                    if (!this.plantes.get(indexPlanteAManger).encoreVivant()){
                        this.plantes.remove(indexPlanteAManger);
                    }
                }
            }

            //Resilience / Survie de l'herbivore :

            this.herbivores.get(indexHerb).preuveResilience(energieAbsorbeeHerbivore);

            //Essai de reproduction de l'Herbivore
            //on récupère les enfants de l'herbivore, s'il y en a, et on les met
            //dans la variable nouveauxNesHerbivores
            List<Herbivore> nouveauxNesHerbivore = this.herbivores.get(indexHerb).essaisReproduction(energieAbsorbeeHerbivore);

            //calcul de l'énergie perdue lors de la reproduction
            Double energiePerdueHerbivore = nouveauxNesHerbivore.size() * this.herbivores.get(indexHerb).getEnergieEnfant();

            //Variable pour ajouter l'énergie restante a l'énergie de la plante
            Double NRJabsorbeeRestanteHerbivore = energieAbsorbeeHerbivore - energiePerdueHerbivore;

            if (this.herbivores.get(indexHerb).encoreVivant() & NRJabsorbeeRestanteHerbivore > 0 ){
                this.herbivores.get(indexHerb).ajouteNRJrestante(NRJabsorbeeRestanteHerbivore);
            } 
            else if (!this.herbivores.get(indexHerb).encoreVivant()){
                this.herbivores.remove(indexHerb);
                indexHerb -=1;
            }

            indexHerb += 1;
            //ajoute les enfants de cette instance de plante dans une liste de nouveaux nés 
            for (Herbivore nouveauNe : nouveauxNesHerbivore ){
                nouveauxNesTotalHerbivore.add(nouveauNe);
            }

        }

        //************************************************Carnivores************************************************\\

        //On utilise un index externe au loop car la liste est en constante modification
        int indexCarniv = 0;

        //Réinitialisation des enfants à aucun
        List<Carnivore> nouveauxNesTotalCarnivore = new ArrayList<>();
        
        int limiteCarnivore = this.carnivores.size();

        for (int iteration3 = 0 ; iteration3 < limiteCarnivore; iteration3++) {

            //l'energie absorbée est 0 par défaut
            Double energieAbsorbeeCarnivore = 0.0;
            
            //mûrit le carnivore d'une unité
            this.carnivores.get(indexCarniv).ageUnCycle();

            //phase d'alimentation:
            int nbFoisAlimenteCarnivore = this.carnivores.get(indexCarniv).nombreFoisAlimente();

            for (int i=0; i< nbFoisAlimenteCarnivore ; i++){

                //Transforme une liste de d'herbivores a une liste d'Organismes pour pouvoir les passer en paramètre
                List<Organisme> herbivoresOrganisme = transformerHerbivoresAOrganismes();
                //On a choisi que les carnivores peuvent manger d'autres carnivores et peuvent aussi pratiquer le cannibalisme
                List<Organisme> carnivoresOrganisme = transformerCarnivoresAOrganismes(indexCarniv);
                
                //On crée une liste de tous les herbivores et tous les carnivores ensemble
                List<Organisme> animauxOrganisme = new ArrayList<>();
                for(Organisme herbivore : herbivoresOrganisme){
                    animauxOrganisme.add(herbivore);
                }
                for (Organisme carnivore : carnivoresOrganisme){
                    animauxOrganisme.add(carnivore);
                }

                //Trouver l'animal aléatoire à manger :
                int indexAnimalAManger = this.carnivores.get(indexCarniv).indexOrgMange(animauxOrganisme);

                //Index de -1 indique qu'il mange pas

                //Cas où le carnivore mange un autre carnivore
                if (indexAnimalAManger != -1 & indexAnimalAManger >= herbivoresOrganisme.size()){
                    //On obtient l'index correct pour chercher dans la liste des carnivores
                    indexAnimalAManger-= herbivoresOrganisme.size();

                    //Changement d'index pour éviter qu'il se mange lui même
                    if (indexAnimalAManger >= indexCarniv){
                        indexAnimalAManger += 1;
                    }

                    energieAbsorbeeCarnivore += this.carnivores.get(indexCarniv).mangerCarnivore(this.carnivores.get(indexAnimalAManger));
                    
                    //Éliminer le carnivore de la liste de vivants
                    if (!this.carnivores.get(indexAnimalAManger).encoreVivant()){
                        this.carnivores.remove(indexAnimalAManger);
                        indexCarniv -= 1;
                    }
                }
                //Cas où le carnivore mange un herbivore
                else if (indexAnimalAManger != -1) {
                    energieAbsorbeeCarnivore += this.carnivores.get(indexCarniv).mangerHerbivore(this.herbivores.get(indexAnimalAManger));
                    //Éliminer l'herbivore de la liste des vivants
                    if (!this.herbivores.get(indexAnimalAManger).encoreVivant()){
                        this.herbivores.remove(indexAnimalAManger);
                    }
                }

            }

            //Resilience / Survie du carnivore :
            this.carnivores.get(indexCarniv).preuveResilience(energieAbsorbeeCarnivore);

            //Essai de reproduction du Carnivore
            //on récupère les enfants du carnivore, s'il y en a, et on les met dans la variable nouveauxNesCarnivore
            List<Carnivore> nouveauxNesCarnivore = this.carnivores.get(indexCarniv).essaisReproduction(energieAbsorbeeCarnivore);

            //Calcul de l'énergie perdue lors de la reproduction
            Double energiePerdueCarnivore = nouveauxNesCarnivore.size() * this.carnivores.get(indexCarniv).getEnergieEnfant();
            
            //Ajout de l'énergie restante a l'énergie du carnivore
            Double NRJabsorbeeRestanteCarnivore = energieAbsorbeeCarnivore - energiePerdueCarnivore;
            if (this.carnivores.get(indexCarniv).encoreVivant() & NRJabsorbeeRestanteCarnivore > 0 ){
                this.carnivores.get(indexCarniv).ajouteNRJrestante(NRJabsorbeeRestanteCarnivore);
            } 
            else if (!this.carnivores.get(indexCarniv).encoreVivant()){
                this.carnivores.remove(indexCarniv);
                indexCarniv -=1;
            }
            
            indexCarniv += 1;

            //ajoute les enfants de cette instance de plante dans une liste de nouveaux nés 
            for (Carnivore nouveauNe : nouveauxNesCarnivore ){
                nouveauxNesTotalCarnivore.add(nouveauNe);
            }

        }

        //************************************************Nouveaux-nés************************************************\\

        //On ajoute les nouveaux nés à la fin puisqu'ils ne peuvent pas être mangés par des animaux s'ils ne sont toujours pas nés.

        //ajoute tous les enfants type plante nés dans ce cycle à la liste de toutes les plantes
        for (Plante nouveauNe : nouveauxNesTotalPlante){
            this.plantes.add(nouveauNe);
        }

        //ajoute tous les enfants type herbivore nés dans ce cycle à la liste de tous les herbivores
        for (Herbivore nouveauNe : nouveauxNesTotalHerbivore){
            this.herbivores.add(nouveauNe);
        }

        //ajoute tous les enfants type carnivore nés dans ce cycle à la liste de tous les carnivores
        for (Carnivore nouveauNe : nouveauxNesTotalCarnivore){
            this.carnivores.add(nouveauNe);
        }

    }

    
    //Méthode pour transformer une liste de plantes à une liste d'organismes
    public List<Organisme> transformerPlantesAOrganismes(){
        List<Plante> listePlantes = this.plantes;
        List<Organisme> listeOrganismes = new ArrayList<>();
        for (Plante plante : listePlantes){
            listeOrganismes.add(plante);
        }
        return listeOrganismes;
    }
    
    
    //Méthode pour transformer une liste d'herbivores à une liste d'organismes
    public List<Organisme> transformerHerbivoresAOrganismes(){
        List<Herbivore> listeHerbivores = this.herbivores;
        List<Organisme> listeOrganismes = new ArrayList<>();
        for (Herbivore herbivore : listeHerbivores){
            listeOrganismes.add(herbivore);
        }
        return listeOrganismes;
    }

    //Méthode pour transformer une liste de carnivores à une liste d'organismes
    public List<Organisme> transformerCarnivoresAOrganismes(int indexCarniv){
        List<Carnivore> listeCarnivores = this.carnivores;
        List<Organisme> listeOrganismes = new ArrayList<>();
        for (Carnivore carnivore : listeCarnivores){
            //Le carnivore ne peut pas se manger lui même
            if (carnivore != this.carnivores.get(indexCarniv))
            listeOrganismes.add(carnivore);
        }
        return listeOrganismes;
    }


    //Imprime le rapport des espèces qui ont survécu aux 50 cycles en quelles quantités et avec
    //le cumul d'énergie, ceci est valable pour plantes, herbivores et carnivores.
    public void imprimeRapport(PrintStream out) {
        var especesPlantes = this.plantes.stream().collect(groupingBy(
                Plante::getNomEspece,
                summarizingDouble(Plante::getEnergie)));
        out.println("Il reste(nt) " + especesPlantes.size() + " espèce(s) de plantes.");
        for (var entry : especesPlantes.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.2f unités d'énergie.",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }

        System.out.println("\n");

        var especesHerbivores = this.herbivores.stream().collect(groupingBy(
                Herbivore::getNomEspece,
                summarizingDouble(Herbivore::getEnergie)));
        out.println("Il reste(nt) " + especesHerbivores.size() + " espèce(s) d'herbivores.");
        for (var entry : especesHerbivores.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.2f unités d'énergie.",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }

        System.out.println("\n");

        var especesCarnivores = this.carnivores.stream().collect(groupingBy(
                Carnivore::getNomEspece,
                summarizingDouble(Carnivore::getEnergie)));
        out.println("Il reste(nt) " + especesCarnivores.size() + " espèce(s) de carnivores.");
        for (var entry : especesCarnivores.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.2f unités d'énergie.",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }

        System.out.println("\n");
    }
}