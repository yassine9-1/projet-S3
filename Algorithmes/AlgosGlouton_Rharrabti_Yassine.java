package modele;

import java.util.ArrayList;
import java.util.List;

public class AlgosGlouton {

    public static double calculerScoreDesequilibre(List<Groupe> groupes, String matiere) {
        double sommeMoyennes = 0;
        int countNonVide = 0;

        for (Groupe g : groupes) {
            if (!g.getEtudiantsInscrits().isEmpty()) {
                sommeMoyennes += calculerMoyenneGroupe(g, matiere);
                countNonVide++;
            }
        }

        if (countNonVide == 0) return 0.0;
        double moyenneGlobale = sommeMoyennes / countNonVide;

        double score = 0;
        for (Groupe g : groupes) {
            if (!g.getEtudiantsInscrits().isEmpty()) {
                double moy = calculerMoyenneGroupe(g, matiere);
                score += Math.pow(moy - moyenneGlobale, 2);
            }
        }
        return score;
    }

    private static double calculerMoyenneGroupe(Groupe g, String matiere) {
        if (g.getEtudiantsInscrits().isEmpty()) return 0.0;
        double sum = 0;
        for (Etudiant e : g.getEtudiantsInscrits()) {
            sum += e.calculerMoyenne(matiere);
        }
        return sum / g.getEtudiantsInscrits().size();
    }

    public static void repartirGloutonDistributeur(Promotion promo, List<Groupe> groupes) {
        for (Groupe g : groupes) g.getEtudiantsInscrits().clear();

        List<Etudiant> etudiants = promo.getEtudiants();
        int indexGroupe = 0;

        for (Etudiant e : etudiants) {
            boolean place = false;
            int tours = 0;

            while (!place && tours < groupes.size()) {
                Groupe g = groupes.get(indexGroupe);
                
                if (g.ajouterEtudiant(e)) {
                    if (g.verifierContraintes()) {
                        place = true;
                    } else {
                        g.retirerEtudiant(e);
                    }
                }

                indexGroupe = (indexGroupe + 1) % groupes.size();
                tours++;
            }
            
            if (!place) {
                System.err.println("Attention : Impossible de placer l'étudiant " + e.getNom() + " (Contraintes ou Capacité)");
            }
        }
        System.out.println("Répartition Glouton Distributeur terminée.");
    }

    public static void repartirGloutonCompensateur(Promotion promo, List<Groupe> groupes, String matiere) {
        if (groupes.isEmpty()) return;

        List<Etudiant> etudiantsTries = new ArrayList<>(promo.getEtudiants());
        etudiantsTries.sort((e1, e2) -> Double.compare(e2.calculerMoyenne(matiere), e1.calculerMoyenne(matiere)));

        for (Groupe g : groupes) g.getEtudiantsInscrits().clear();

        for (Etudiant e : etudiantsTries) {
            Groupe meilleurGroupe = null;
            double minMoyenne = Double.MAX_VALUE;

            for (Groupe g : groupes) {
                if (g.ajouterEtudiant(e)) { 
                    boolean contraintesOK = g.verifierContraintes();
                    
                    g.retirerEtudiant(e); 

                    if (contraintesOK) {
                        if (g.getEtudiantsInscrits().isEmpty()) {
                            meilleurGroupe = g;
                            break; 
                        }
                        
                        double moyActuelle = calculerMoyenneGroupe(g, matiere);
                        if (moyActuelle < minMoyenne) {
                            minMoyenne = moyActuelle;
                            meilleurGroupe = g;
                        }
                    }
                }
            }

            if (meilleurGroupe != null) {
                meilleurGroupe.ajouterEtudiant(e);
            } else {
                System.err.println("Impossible de placer " + e.getNom() + " (Aucun groupe valide trouvé)");
            }
        }
        System.out.println("Répartition Glouton Compensateur terminée.");
    }
}