package it.unicam.cs.asdl2324.mp2;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;



/**
 *
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 *
 * @author Luca Tesei (template) DAVUD, SUFA davud.sufa@studenti.unicam.it (implementazione)
 *
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

  List<GraphEdge<L>> graphEdgeList;

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST()
    {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     *
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g)
    {
        if (g == null)          //controllo g è null
        {
            throw new NullPointerException("ERROR! Non sono ammessi valori null");
        }
        if (g.isDirected())         //controllo se il grafo è orientato
        {
            throw new IllegalArgumentException("ERRORE! Il grafo non dev'essere orientato");
        }
        disjointSets.clear();       //ripulisco il disjointSets
        for (GraphNode<L> node : g.getNodes())
        {
            disjointSets.makeSet(node);         //insieme singoletto con makeSet
        }

        graphEdgeList = new ArrayList<>(g.getEdges());
        quickSort(0, graphEdgeList.size() - 1);     //uso quicksort per il sorting degli 'edges'

        Set<GraphEdge<L>> result = new HashSet<>();

        for (GraphEdge<L> edge : graphEdgeList)
        {
            if (!edge.hasWeight() || edge.getWeight() < 0) {
                throw new IllegalArgumentException("ERRORE! Almeno un arco non è pesato o è negativo");
            }
            if (!disjointSets.findSet(edge.getNode2()).equals(disjointSets.findSet(edge.getNode1())))   //controllo se i findSet sono diversi unisco l'insieme e aggiungo l' 'edge' a 'result'
            {
                result.add(edge);
                disjointSets.union(edge.getNode1(), edge.getNode2());
            }
        }
        return result;
    }

    private void quickSort(int begin, int end)      //metodo del quicksort per riordinare gli archi
    {
        if (end > begin)
        {
            int pivot = Partizione(begin, end);
            quickSort(begin, pivot - 1);
            quickSort(pivot + 1, end);
        }
    }

    private int Partizione(int begin, int end) {
        GraphEdge<L> source = graphEdgeList.get(end);       //arco source con indice i
        int i = begin - 1;

        for (int j = begin; j < end; j++) {
            if (graphEdgeList.get(j).getWeight() <= source.getWeight()) {       //controllo se l'elemento con index j ha un peso minore o uguale al 'source', incremento di l'indice i
                i++;
                swap(graphEdgeList, i, j);      //richiamo 'swap' per scambiare gli elementi
            }
        }
        swap(graphEdgeList, i + 1, end);
        return i + 1;
    }

    private void swap(List<GraphEdge<L>> list, int i, int j) {
        GraphEdge<L> temp = list.get(i);            //immagazzino l'elemento con indice i in una variabile temporale
        list.set(i, list.get(j));                   //metto il valore l'elemento all'indice i nell' elemento con indice j
        list.set(j, temp);                          //diamo il valore di j a 'temp'
    }
}
