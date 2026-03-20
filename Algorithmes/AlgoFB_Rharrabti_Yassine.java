package modele;

import java.util.List;

public class AlgoFB {
    
    private static double meilleurScoreTrouve = Double.MAX_VALUE;
    private static int[] meilleureConfiguration;

    public static void repartirForceBrute(Promotion promo, List<Groupe> groupes, String matiere) {
        List<Etudiant> etudiants = promo.getEtudiants();

        if (etudiants.size() > 10) {
            System.out.println("Trop d'étudiants pour Force Brute (>10). Bascule automatique sur Glouton.");
            AlgosGlouton.repartirGloutonCompensateur(promo, groupes, matiere);
            return;
        }

        System.out.println("Lancement Force Brute sur " + etudiants.size() + " étudiants...");
        
        meilleurScoreTrouve = Double.MAX_VALUE;
        meilleureConfiguration = new int[etudiants.size()];
        
        for(Groupe g : groupes) g.getEtudiantsInscrits().clear();

        if (backtrack(0, etudiants, groupes, matiere)) {
            appliquerSolution(etudiants, groupes);
            System.out.println("Répartition Force Brute terminée. Score : " + meilleurScoreTrouve);
        } else {
            System.out.println("Aucune solution valide trouvée avec ces contraintes.");
        }
    }

    private static boolean backtrack(int indexEtu, List<Etudiant> etudiants, List<Groupe> groupes, String matiere) {
        if (indexEtu == etudiants.size()) {
        	boolean existeGroupeVide = false;
            for (Groupe g : groupes) {
                if (g.getEtudiantsInscrits().isEmpty()) {
                    existeGroupeVide = true;
                    break;
                }
            }

            if (existeGroupeVide && etudiants.size() >= groupes.size()) {
                return false;
            }

            double score = AlgosGlouton.calculerScoreDesequilibre(groupes, matiere);
            if (score < meilleurScoreTrouve) {
                meilleurScoreTrouve = score;
                sauvegarderConfig(etudiants, groupes);
            }
            return true;
        }

        boolean solutionTrouvee = false;
        Etudiant e = etudiants.get(indexEtu);

        for (int i = 0; i < groupes.size(); i++) {
            Groupe g = groupes.get(i);
            
            if (g.ajouterEtudiant(e)) {
                
                if (g.verifierContraintes()) {
                    if (backtrack(indexEtu + 1, etudiants, groupes, matiere)) {
                        solutionTrouvee = true;
                    }
                }
                
                g.retirerEtudiant(e);
            }
        }
        return solutionTrouvee;
    }

    private static void sauvegarderConfig(List<Etudiant> etudiants, List<Groupe> groupes) {
        for (int i = 0; i < etudiants.size(); i++) {
            Etudiant e = etudiants.get(i);
            for (int j = 0; j < groupes.size(); j++) {
                if (groupes.get(j).getEtudiantsInscrits().contains(e)) {
                    meilleureConfiguration[i] = j;
                    break;
                }
            }
        }
    }

    private static void appliquerSolution(List<Etudiant> etudiants, List<Groupe> groupes) {
        for(Groupe g : groupes) g.getEtudiantsInscrits().clear();
        
        for (int i = 0; i < etudiants.size(); i++) {
            int indexGroupe = meilleureConfiguration[i];
            if (indexGroupe >= 0 && indexGroupe < groupes.size()) {
                groupes.get(indexGroupe).ajouterEtudiant(etudiants.get(i));
            }
        }
    }
}