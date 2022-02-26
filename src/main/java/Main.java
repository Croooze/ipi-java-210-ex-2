import java.util.Scanner;

public class Main {
    public static final short MAX_PTS_VIE = 100;
    public static final short PTS_BOUCLIER = 25;
    public static final short MAX_ATTAQUE_ENNEMI = 5;
    public static final short MAX_VIE_ENNEMI = 30;
    public static final short MAX_ATTAQUE_JOUEUR = 5;
    public static final short REGENARATION_BOUCLIER_PAR_TOUR = 10;

    public static String nomPersonnage;
    public static short ptsDeVie;
    public static short ptsBouclier;
    public static short nbEnnemisTues;
    public static boolean bouclierActif = true;
    public static short ptsDeVieEnnemi;

    public static void main(String[] args) {
        //TODO exercice 11
        initPersonnage();
        short[] ennemis = initEnnemis();
        int i = 0;
        for (short vieEnnemis : ennemis){
            boolean joueurAttaque;
            joueurAttaque = hasard(0.5);
            System.out.println("==========================================================");
            System.out.println("Combat avec un ennemi possédant " + vieEnnemis + " points de vie !");
            System.out.println();
            // je n'arrive pas à récupérer les points de vie générer par la fonction initEnnemis
            while (ptsDeVie > 0 /*|| vieEnnemis > 0*/) {
                vieEnnemis = attaque(vieEnnemis, joueurAttaque);
                joueurAttaque = !joueurAttaque;
                afficherPersonnage();
                System.out.println(" vs " + Util.color("ennemi" , Color.YELLOW) + " (" +
                Util.color((short) Math.max(vieEnnemis, 0), Color.PURPLE) + ")");
                attaqueJoueur(ptsDeVieEnnemi);
                attaqueEnnemi();
                if (ptsDeVie < 1){
                    System.out.println(nomPersonnage + " est mort mais a tué " + nbEnnemisTues + " ennemis");
                    break;
                }
            }
        }
    }

    public static void initPersonnage() {
        System.out.println("Saisir le nom de votre personnage");
        Scanner scanner = new Scanner(System.in);
        nomPersonnage = scanner.nextLine();
        System.out.println("OK " + Util.color(nomPersonnage, Color.GREEN) + " ! C'est parti !");
        ptsDeVie = MAX_PTS_VIE;
        ptsBouclier = bouclierActif ? PTS_BOUCLIER : 0;
        // scanner.close();
    }

    public static boolean hasard(double pourcentage) {
        //pourcentage < résultat du chiffre random => true
        // sinon faux
        return pourcentage < Math.random();
    }

    public static short nombreAuHasard(short nombre) {
        return (short) Math.round(Math.random() * nombre);
    }


    public static short attaqueJoueur(short ptsVieEnnemi) {
        //Déterminer la force de l'attaque du joueur
        short forceAttaque = nombreAuHasard(MAX_ATTAQUE_JOUEUR);
        //Retrancher les points de l'attaque
        ptsVieEnnemi -= forceAttaque;
        //Afficher les caractéristiques
        System.out.println(Util.color(nomPersonnage, Color.GREEN) + " attaque l'"
                + Util.color("ennemi", Color.YELLOW) + " ! Il lui fait perdre "
                + Util.color(forceAttaque, Color.PURPLE) + " points de dommages");
        //Retourner le nombre de points de vie de l'ennemi après l'attaque
        return ptsVieEnnemi;
    }

    public static void afficherPersonnage() {
        System.out.print(Util.color(nomPersonnage, Color.GREEN) + " (" + Util.color(ptsDeVie, Color.RED));
        if (bouclierActif) {
            System.out.print(" " + Util.color(ptsBouclier, Color.BLUE));
        }
        System.out.print(")");
    }

    public static void attaqueEnnemi() {
        //Le bouclier reçoit en priorité les dommages
        short dommages = nombreAuHasard(MAX_ATTAQUE_ENNEMI);
        if (ptsBouclier == 0) {
            ptsDeVie = (short) (ptsBouclier - dommages + ptsDeVie);
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dommages + " points de dommages ! " +
                    Util.color(nomPersonnage , Color.GREEN) + " perd " + Util.color(dommages , Color.RED) +
                    " points " + "de vie !");
        }
        else if (ptsBouclier < dommages) {
            ptsDeVie = (short) (ptsDeVie + (ptsBouclier - dommages));
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dommages + " points de dommages !" +
                    " Le bouclier perd " + Util.color(ptsBouclier , Color.BLUE) + " points. " +
                    Util.color(nomPersonnage , Color.GREEN) + " perd " +
                    Util.color((short) ((ptsBouclier - dommages)*-1) , Color.RED) + " points de vie !");
            ptsBouclier = 0;
        }
        else {
            ptsBouclier = (short) (ptsBouclier - dommages);
            System.out.println("L'" + Util.color("ennemi" , Color.YELLOW) + " attaque " +
                    Util.color(nomPersonnage , Color.GREEN) + " ! Il lui fait " + dommages + " points de dommages !" +
                    " Le bouclier perd " + Util.color(dommages , Color.BLUE) + " points.");
        }
    }

    public static short [] initEnnemis(){
        System.out.println("Combien souhaitez-vous combattre d'ennemis ?");
        Scanner scanner = new Scanner(System.in);
        int nbEnnemis = scanner.nextInt();
        System.out.println("Génération des ennemis...");
        short [] ennemis = new short[nbEnnemis];
        for (int i = 0; i < nbEnnemis; i++){
            ennemis[i] = nombreAuHasard(MAX_VIE_ENNEMI);
            System.out.println("Ennemi numéro " + (i+1) + " : " + Util.color(ennemis[i],Color.PURPLE));
            ennemis[i] = ptsDeVieEnnemi;
        }
        return ennemis;
        //int ennemis = initEnnemis();
    }

    public static short attaque(short ennemi, boolean joueurAttaque){
        //Verifier si l'un des deux combattants n'est pas mort => si oui, on ne fait aucune attaque
        if(ptsDeVie <= 0 || ennemi <= 0){
            return ennemi;}
        //On va faire attaquer le joueur si c'est à lui d'attaquer
        if(joueurAttaque){
            ennemi = attaqueJoueur(ennemi);}
        //Sinon on fait attaquer l'ennemi
        else {
            attaqueEnnemi();}
        //On renvoi un nombre de point à l'ennemi
        return ennemi;
    }
}