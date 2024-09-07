package it.unicam.cs.asdl2324.mp1;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * La struttura Set<DisjointSetElement> (HashSet<>()) sviluppa l'implementazione dei metodi.
 * Ci sono i "rappresentanti" che tengono traccia degli insiemi.
 * Sono presenti metodi offerti dalla classe DisjointSetElement,
 * per ottenere il rappresentante e altre cose che erano
 * vitali per la corretta implementazione della classe.
 *
 * @author Luca Tesei (template) **DAVUD, SUFA
 *         davud.sufa@studenti.unicam.it** (implementazione)
 *
 */
public class LinkedListDisjointSets implements DisjointSets {

    private Set<DisjointSetElement> rappresentanti;         //creazione di un insieme che contiene 'rappresentanti'
    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        this.rappresentanti = new HashSet<>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        return e.getRef1() != null;          //vede se è presente o meno l'elemento
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere
     * 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        if(e == null)
        {
            throw new NullPointerException("ERRORE! Fornire un elemento non nullo");
        }
        if(rappresentanti.contains(e.getRef1()))
        {
            throw new IllegalArgumentException("ERRORE! L'elemento è già contenuto nella lista");
        }
        e.setRef1(e);       //viene messo il puntatore al rappresentante dell'elemento 'e' su se stesso
        e.setNumber(1);
        rappresentanti.add(e);  //si aggiunge e all'insieme dei rappresentanti
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        if(e == null)
        {
            throw new NullPointerException("ERRORE! Fornire un elemento non nullo");
        }
        if(!rappresentanti.contains(e.getRef1()))       //condizione: la lista contiene 'e' ?
        {
            throw new IllegalArgumentException("ERRORE! L'elemento 'e' non è contenuto");
        }
        return e.getRef1();
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     *
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     *
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
        if(e1 == null || e2 == null)
        {
            throw new NullPointerException("L'elemento e1 o e2 è nullo");
        }
         if(!rappresentanti.contains(e1.getRef1()) || !rappresentanti.contains(e2.getRef1()))   //controllo se e1 o e2 non sono presenti nei set dei rappresentanti
        {
            throw new IllegalArgumentException("Almeno uno dei due non è presente in nessuno degli elementi disgiunti");
        }
        if(e1.getRef1() == e2.getRef1())  //controlla se e1 e e2 hanno lo stesso rappresentante
        {
            return;
        }
        DisjointSetElement newR;         //determina il nuovo 'newR' rappresentante e quello vecchio 'oldR'
        DisjointSetElement oldR;
        int getE1 = e1.getNumber();
        int getE2 = e2.getNumber();
        if(getE1 >= getE2)
        {
            newR = e1.getRef1();
            oldR = e2.getRef1();
        }
        else
        {
            newR = e2.getRef1();
            oldR = e1.getRef1();
        }

        DisjointSetElement current = newR;   //aggiorna gli elementi nel nuovo set di rappresentanti
        while(current.getRef2() != null)
        {
            current.setNumber(getE1+getE2);
            current = current.getRef2();
        }

        current.setNumber(getE1 + getE2);   //aggiorna i riferimenti e i numeri
        current.setRef2(oldR);

        current = current.getRef2();    //si sposta nell'insieme dei rappresentanti. Aggiorna i riferimenti e i numeri
        while(current.getRef2() != null)
        {
            current.setRef1(newR);
            current.setNumber(getE1+getE2);
            current = current.getRef2();
        }
        current.setRef1(newR);
        current.setNumber(getE1+getE2);
        rappresentanti.remove(oldR);        //si elimina l'ultimo rappresentante
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives()
    {
        return rappresentanti;
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(
            DisjointSetElement e) {
        if (e == null)
        {
            throw new NullPointerException("ERRORE! L'elemento non deve essere nullo");
        }

        if (!rappresentanti.contains(e.getRef1())) {
            throw new IllegalArgumentException("ERRORE! L'elemento 'e' non è presente.");
        }

        Set<DisjointSetElement> set = new HashSet<>();      //si crea un ulteriore insieme per contenere gli elementi dell'insieme corrente
        DisjointSetElement current = e.getRef1();

        while (current != null) {
            set.add(current);               //aggiunge l'elemento corrente all'insieme
            current = current.getRef2();    //passa all'elemento successivo
        }
        return set;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
        if (e == null)
        {
            throw new NullPointerException("ERRORE! L'elemento non deve essere nullo");
        }
        if (!rappresentanti.contains(e.getRef1()))
        {
            throw new IllegalArgumentException("ERRORE! L'elemento 'e' non è presente.");
        }
        return e.getNumber();
    }

}
