package it.unicam.cs.asdl2324.mp2;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) DAVUD, SUFA davud.sufa@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets()
    {
    currentElements= new HashMap<>();
    }

    @Override
    public boolean isPresent(E e)
    {
        return currentElements.containsKey(e);  //ritorna true se c'è il key è nella map
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e)
    {
        if (e == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        if(currentElements.containsKey(e))      //controllo se l'elemento è gia contenuto
        {
            throw new IllegalArgumentException("ERRORE! L'oggetto è già contenuto");
        }
        Node<E> Nodo = new Node<>(e);
        currentElements.put(e, Nodo);       //creo un nodo e inserisco col metodo put
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e)           //algoritmo di compressione del cammino che usa la struttura UNION-FIND, che serve per gestire insiemi disgiunti
    {                               //e risolvere problemi di connettività in grafi
                                    //viene applicato durante le operazioni di ricerca all'interno di un insieme e modifica la struttura dell'albero per ridurre la lunghezza del percorso fra nodo e root
        if (e == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        if(!currentElements.containsKey(e))
        {
            return null;
        }
        Node<E> node = currentElements.get(e);      //creo un nodo
        if(node == null)
        {
            return null;        //se è nullo restituisce null
        }

        if(node==node.parent)
        {
            return node.item;       //vedo se il nodo è lui stesso l'item
        }

        E root = findSet(node.parent.item);
        Node<E> Nodo = currentElements.get(root);
        node.parent=Nodo;
        return root;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2)
    {
        if( e1 == null || e2 == null)
        {
            throw new NullPointerException("ERRORE! Almeno uno dei due elementi è null");
        }

        if(!currentElements.containsKey(e1) || !currentElements.containsKey(e2))
        {
            throw new IllegalArgumentException("ERRORE! Almeno uno dei due elementi non è presente");
        }

        E first  = findSet(e1);         //assegno a 'first' e 'second' il findSet corrispondente
        E second = findSet(e2);

        if(first == second)
        {
            return;
        }
        Node<E> node1 = currentElements.get(first);     //assegno a 'node1' e 'node2' i nodi
        Node<E> node2 = currentElements.get(second);
        Link(node1, node2);
    }

    @Override
    public Set<E> getCurrentRepresentatives()
    {
        Set<E> result = new HashSet<>();
        for(Node<E> node : currentElements.values())  //In questo metodo controllo solo se il nodo ha se stesso come parent
        {
            if(node.parent.equals(node)) result.add(node.item);     //le root verranno aggiornate ogni volta che viene eseguito 'union'
        }
        return result;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e)
    {
        if(e == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        //Controllo se currentElement contiene l'elemento passato.
        if(!currentElements.containsKey(e))
        {
            throw new IllegalArgumentException("ERRORE! L'elemento non è contenuto");
        }
        Set<E> elements = new HashSet<>();
        E root = findSet(e);        //trova 'root' dell'elemento
        for(E currentElement : currentElements.keySet())    //keySet fa scorrere tutte le chiavi
        {

            if(findSet(currentElement) == root)   //aggiungo  'currentElement' al set se sono uguali
            {
                elements.add(currentElement);
            }
        }
        return elements;
    }

    @Override
    public void clear()
    {
       currentElements.clear();
    }

    private void Link(Node<E> e1, Node<E> e2)
    {
        if(e1.rank > e2.rank){                             //assegno il parent in base al rank
            e2.parent =e1;
        }else {
            e1.parent = e2;
            e2.rank += (e1.rank == e2.rank) ? 1 : 0;        //incrementa il rank di e2 di 1 se i due ranks sono uguali usando un operatore ternario
        }
    }
}
