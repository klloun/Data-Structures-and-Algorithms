/**
 *
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 *
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 *
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 *
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 *
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 * @author Luca Tesei (template) DAVUD, SUFA davud.sufa@studenti.unicam.it (implementazione)
 *
 *
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {

    //crea un set vuoto 'arc' che può contenere elementi univoci di tipo 'GraphEdge' con parametri di tipo 'L'
    Set<GraphEdge<L>> arc = new HashSet<>();
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount()
    {
       return nodesIndex.size();
    }

    @Override
    public int edgeCount()
    {
        return arc.size();
    }

    @Override
    public void clear()
    {
        this.arc.clear();
        this.matrix.clear();
        this.nodesIndex.clear();
    }

    @Override
    public boolean isDirected()
    {
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node)
    {
        if (node == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        if (nodesIndex.containsKey(node))           //controllo se il nodo è già presente
        {
        return false;
    }

    int newIndex = nodesIndex.size();           //salvo l'indice 'newIndex'
        nodesIndex.put(node, newIndex);
        for (int i = 0; i < matrix.size(); i++)
        {
        matrix.get(i).add(null);
         }
    ArrayList<GraphEdge<L>> list = new ArrayList<>();

        for (int i = 0; i < nodesIndex.size(); i++)         //Scorro la mappa tramite un for e aggiungo null alla lista
        {
        list.add(null);
         }
        matrix.add(list);
        return true;
}


    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label)
    {
        if (label == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        return addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node)
    {
        if (node == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        if (!nodesIndex.containsKey(node))          //Controllo se il Grafo contiene node.
        {
            throw new IllegalArgumentException("ERRORE! Non è presente il nodo");
        }
        int index = nodesIndex.get(node);
        nodesIndex.remove(node);

        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet())
        {
            if (entry.getValue() > index) {
                nodesIndex.put(entry.getKey(), entry.getValue() - 1);
            }
        }
        matrix.remove(index);       //rimuovo la row del nodo

        for (ArrayList<GraphEdge<L>> row : matrix) {    //elimino la colonna del nodo rimosso
            row.remove(index);
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label)
    {

        GraphNode<L> node = getNode(label);        //Associo il nodo al label
        if (node == null)
        {
            throw new IllegalArgumentException("ERRORE! Non sono accettati valori nulli");
        }
        else
        {
            removeNode(node);
        }

    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i)
    {
        GraphNode<L> node = this.getNode(i);        //Associo al nodo il 'i'
        removeNode(node);
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node)
    {
        if (node == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        return nodesIndex.containsKey(node) ? node : null;
    }

    @Override
    public GraphNode<L> getNode(L label)
    {
        if (label == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        for (GraphNode<L> node : nodesIndex.keySet())
        {
            if (node.getLabel().equals(label)) {        //se label è uguale ritorna il nodo
                return node;
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(int i)
    {
        if ( i > this.nodeCount() - 1 || i < 0 ) {
            throw new IndexOutOfBoundsException("ERRORE! L'indice è fuori dai limiti");
        }
        for (Map.Entry<GraphNode<L>, Integer> entry : nodesIndex.entrySet())
        {
            if (entry.getValue().equals(i))
            {
                return entry.getKey();          //Se 'entry' è uguale a 'i' ritorna la chiave di 'i'
            }
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node)
    {
        if (node == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        if (!nodesIndex.containsKey(node)) //vedo se la mappa contiene 'node', ritorno una eccezione
        {
            throw new IllegalArgumentException("ERRORE! 'node' non è contenuto");
        }
        return nodesIndex.getOrDefault(node, -1);
    }

    @Override
    public int getNodeIndexOf(L label)
    {
        if (label == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        GraphNode<L> node = getNode(label);        //creo un 'node' a cui assegno il 'label'
        if (node == null)
        {
            throw new IllegalArgumentException("ERRORE! Non sono accettati valori nulli");
        }
        return this.getNodeIndexOf(node);
    }

    @Override
    public Set<GraphNode<L>> getNodes()
    {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge)
    {
        if (edge == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        if (edge.isDirected())      //controllo se il grafo è orientato
        {
            throw new IllegalArgumentException("ERRORE! Il grafo non è orientato");
        }
        GraphNode<L> Node1 = edge.getNode1();        //assegno a 'Node1' il suo nodo dell'arco
        GraphNode<L> Node2 = edge.getNode2();       //assegno a 'Node2' il suo nodo dell'arco
        if (!nodesIndex.containsKey(Node2) || !nodesIndex.containsKey(Node1))       //controllo se sono contenuti effettivamente
        {
            throw new IllegalArgumentException("ERRORE! Uno dei due nodi non esiste");
        }
        int index1 = nodesIndex.get(Node1);
        int index2 = nodesIndex.get(Node2);

        if (matrix.get(index1).get(index2) != null && matrix.get(index1).get(index2).equals(edge)) //Controllo se gli indici sono uguali all'arco o se hanno valore null
            {
            return false;
            }
        matrix.get(index2).set(index1, edge);
        matrix.get(index1).set(index2, edge);
        arc.add(edge); //aggiungo 'arc' al set
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2)
    {
        if (node2 == null || node1 == null)
        {
            throw new NullPointerException("ERRORE! Non si accettano valori nulli");
        }
        GraphEdge<L> Arc = new GraphEdge<>(node1, node2, false);        //Dichiaro un 'Arc' con i suoi nodi (1 e 2)
        return this.addEdge(Arc);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2,
            double weight)
    {
        if (node2 == null || node1 == null)
        {
            throw new NullPointerException("ERRORE! Non si accettano valori nulli");
        }
        return addEdge(new GraphEdge<>(node1, node2, isDirected(), weight));
    }

    @Override
    public boolean addEdge(L label1, L label2)
    {
        if (label2 == null || label1 == null)
        {
            throw new NullPointerException("ERRORE! Non si accettano valori nulli");
        }
        GraphNode<L> node1 = this.getNode(label1);      //assegno a 'node1' il suo 'label1'
        GraphNode<L> node2 = this.getNode(label2);      //assegno a 'node2' il suo 'label2'
        return this.addEdge(node1, node2);
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight)
    {
        if (label2 == null || label1 == null)
        {
            throw new NullPointerException("ERRORE! Non si accettano valori nulli");
        }
        GraphNode<L> node1 = this.getNode(label1);      //assegno a 'node1' il suo 'label1'
        GraphNode<L> node2 = this.getNode(label2);      //assegno a 'node2' il suo 'label2'
        return addEdge(new GraphEdge<>(node1, node2, isDirected(), weight));
    }

    @Override
    public boolean addEdge(int i, int j)
    {
        GraphNode<L> node1 = this.getNode(i);   //assegno a 'node1' il suo 'i'
        GraphNode<L> node2 = this.getNode(j);   //assegno a 'node2' il suo 'j'
        return addEdge(new GraphEdge<L>(node1, node2, isDirected()));
    }


    @Override
    public boolean addWeightedEdge(int i, int j, double weight)
    {
        GraphNode<L> node1 = this.getNode(i);   //assegno a 'node1' il suo 'i'
        GraphNode<L> node2 = this.getNode(j);   //assegno a 'node2' il suo 'j'
        return addEdge(new GraphEdge<L>(node1, node2, isDirected(), weight));
    }

    @Override
    public void removeEdge(GraphEdge<L> edge)
    {
        if (edge == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        GraphNode<L> nodo1 = edge.getNode1();
        GraphNode<L> nodo2 = edge.getNode2();

        if (!nodesIndex.containsKey(nodo1) || !nodesIndex.containsKey(nodo2)) //Controllo se i nodi non sono contenuti nella map
        {
            throw new IllegalArgumentException("ERRORE! Uno dei due nodi non è contenuto");
        }

        int index1 = nodesIndex.get(nodo1);
        int index2 = nodesIndex.get(nodo2);
        if (!arc.contains(edge))
        {
            throw new IllegalArgumentException("ERRORE! L'arco non esiste");
        }
        matrix.get(index1).set(index2, null);
        matrix.get(index2).set(index1, null);
        arc.remove(edge);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2)
    {
        if (node1 == null || node2 == null)
        {
            throw new NullPointerException("ERRORE! Uno dei due nodi è nullo");
        }
        GraphEdge<L> Arc = new GraphEdge<>(node1, node2, false);
        this.removeEdge(Arc);
    }

    @Override
    public void removeEdge(L label1, L label2)
    {
        if (label1 == null || label2 == null)
        {
            throw new NullPointerException("ERRORE! Uno dei due label è nullo");
        }
        GraphNode<L> Node1 = this.getNode(label1);    //assegno a Node1 il label1
        GraphNode<L> Node2 = this.getNode(label2);     //assegno a Node2 il label2
        this.removeEdge(Node1, Node2);
    }

    @Override
    public void removeEdge(int i, int j)
    {
        GraphNode<L> Node1 = this.getNode(i);   //assegno a Node1 il 'i'
        GraphNode<L> Node2 = this.getNode(j);   //assegno a Node2 il 'j'
        this.removeEdge(Node1, Node2);
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge)
    {
        if (edge == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        return this.findEdge(edge);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2)
    {
        if (node2 == null || node1 == null)
        {
            throw new NullPointerException("ERRORE! Almeno uno dei due nodi è nullo");
        }
        GraphEdge<L> Arc = new GraphEdge<>(node1, node2, false);
        return this.findEdge(Arc);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2)
    {
        if (label2 == null || label1 == null)
        {
            throw new NullPointerException("ERRORE! Almeno uno dei due label è nullo");
        }
        GraphNode<L> Node1 = this.getNode(label1);
        GraphNode<L> Node2 = this.getNode(label2);
        return this.getEdge(Node1, Node2);
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j)
    {
        GraphNode<L> Node1 = this.getNode(i);
        GraphNode<L> Node2 = this.getNode(j);
        return this.getEdge(Node1, Node2);

    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node)
    {
        int flag = this.getNodeIndexOf(node);   //assegno node al flag
        Set<GraphNode<L>> Set = new HashSet<>();
        ArrayList<GraphEdge<L>> Arcs = matrix.get(flag);        //creo un arraylist di flag
        for (int i = 0; i < Arcs.size(); i++)
        {
            if (Arcs.get(i) != null)
            {
                Set.add(this.getNode(i));
            }
        }
        return Set;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label)
    {
        if (label == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        return getAdjacentNodesOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i)
    {
        return getAdjacentNodesOf(getNode(i));      //getNode lancerà eventuali eccezioni
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node)
    {
        int index = this.getNodeIndexOf(node);
        Set<GraphEdge<L>> Set = new HashSet<>();        //creo un set
        ArrayList<GraphEdge<L>> Arcs = matrix.get(index);   //creo un array list 'Arcs'
        for (int i = 0; i < Arcs.size(); i++)
        {
            if (Arcs.get(i) != null)
            {
                Set.add(Arcs.get(i));       //se diverso da null, aggiungo l'arco al set
            }
        }
        return Set;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label)
    {
        if (label == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        return getEdgesOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i)
    {
        if (i < 0 || i >= nodeCount())
        {
            throw new IndexOutOfBoundsException("ERRORE! L'indice non è valido");
        }
        return getEdgesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException(
                "Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges()
    {
        return new HashSet<>(arc);
    }

    public GraphEdge<L> findEdge(GraphEdge<L> edge)     //metodo interno
    {
        if (edge == null)
        {
            throw new NullPointerException("ERRORE! Non sono ammessi valori null");
        }
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2))   //controllo se effettivamente esistono i nodi
        {
            throw new IllegalArgumentException("ERRORE! I nodi non esistono");
        }
        for (GraphEdge<L> Arc : arc)
        {
            if (Arc.equals(edge))
            {
                return Arc;          //ritorno l'arco se equivale a quello precedente
            }
        }
        return null;
    }
}
