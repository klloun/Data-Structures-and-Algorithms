package it.unicam.cs.asdl2324.mp1;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;


/**
 *
 * Una Map (Map<k,v>) è una struttura dati utilizzata per organizzare e implementare i metodi
 *  * di una classe. Ogni elemento generico 'E' funge da chiave in questa mappa e il valore associato
 * a quella chiave rappresenta il numero di occorrenze di ciascun elemento. In altre parole,
 * la mappa tiene traccia di quante volte appare ciascun elemento (Integer),
 * consentendo di stabilire una relazione tra gli elementi e la loro frequenza di occorrenza.
 * 
 * @author Luca Tesei (template) **DAVUD, SUFA
 *         davud.sufa@studenti.unicam.it** (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
public class MyMultiset<E> implements Multiset<E> {

    private Map<E, Integer> map; //la chiave è elemento (E), i valori associati, occorrenze (Integer).
    int nModifiche=0; //conta il numero di modifiche al multiset, servirà nell' iteratore fail-fast

    //implementazione dell'iteratore con la classe interna Itr
    private class Itr implements Iterator<E> {
        private List<E> iterator;
        private int modificheItr;

        //costruttore dell'iteratore
        private Itr(Collection<E> elementi){
            this.iterator = new ArrayList<>(elementi);
            this.modificheItr = nModifiche;
        }

        //verifica se sono presenti elementi successivi
        @Override
        public boolean hasNext(){
            return !iterator.isEmpty();
        }

        //restituisce l'elemento successivo se presente
        @Override
        public E next(){
            if(!hasNext()){
                throw new NoSuchElementException("ERRORE! Non sono presenti elementi");
            }else if(modificheItr < nModifiche){
                throw new ConcurrentModificationException("ERRORE! C'è stata una modifica.");
            } else {
                return iterator.remove(0);
            }
        }
    }

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
    map = new HashMap<>();
    }

    //restituisce il numero totale di occorrenze nel multiset
    @Override
    public int size()
    {
        int flag = 0;
        for(E key : map.keySet())
        {
            flag += map.get(key);
        }
        return flag;
    }

    //metodo che restituisce il numero di occorrenze di un elemento nel multiset
    @Override
    public int count(Object element) {
        if(element == null)
        {
            throw new NullPointerException("ERROE! Fornire un elemento non nullo");
        }
        int valore = map.getOrDefault(element, 0);             //col metodo getOrDefault dell'interfaccia Map ottengo il valore della chiave associata all'elemento,
                                                                         // se la chiave specificata non è presente allora restiuirà 0
        return valore;
    }

    //aggiunge un numero di occorrenze nel multiset
    @Override
    public int add(E element, int occurrences) {
        if(element==null)
        {
            throw new NullPointerException("ERROE! Fornire un elemento non nullo");
        }
        if(occurrences < 0)
        {
            throw new IllegalArgumentException("ERRORE! Occurrences non può essere negativo");
        }
        // Variabile di flag per memorizzare il valore precedente delle occorrenze dell'elemento
        int flag=0;
        if(map.containsKey(element)){           // Verifica se l'elemento è già presente nel multiset
            if((map.get(element) + occurrences) >0)  // Verifica se l'aggiunta del numero specificato di occorrenze non supera Integer.MAX_VALUE
            {
                flag=map.get(element);           // Memorizza il valore precedente delle occorrenze
                map.put(element, map.get(element)+ occurrences);   // Aggiunge il numero di occorrenze all'elemento
                nModifiche++;
            }//nel caso le occorrenze superano Integer.MAX_Value
            else throw new IllegalArgumentException("ERRORE, il numero di occorrenze supera Integer.MAX_VALUES");
        }else{
            map.put(element, occurrences);      //se l'elemento non esiste viene aggiunto l'elemento con le sue occorrenze
            nModifiche++;
        }
        return flag;
    }

    //aggiunge un' unica occorrenza di un elemento al multiset
    @Override
    public void add(E element) {
        if(element==null)
        {
            throw new NullPointerException("ERROE! Fornire un elemento non nullo");
        }
       add(element,1);
    }

    //rimuove un numero di occorrenze nel multiset
    @Override
    public int remove(Object element, int occurrences) {
        if (element == null)
        {
            throw new NullPointerException("ERROE! Fornire un elemento non nullo");
        }
        if (occurrences < 0)
        {
            throw new IllegalArgumentException("ERRORE! Occurrences non può essere negativo");
        }
        int flag = 0;
        if (map.containsKey(element))           //verifica se element è nel multiset
        {
            flag = map.get(element);            //si memorizza il valore precedente delle occorrenze
            if (occurrences < map.get(element))    //Vede se il numero di occorrenze da rimuovere è minore alle occorrenze
            {
                try
                {
                    map.put((E) element, map.get(element) - occurrences);  // Rimuove il numero di occorrenze dall'elemento
                    if (occurrences > 0)
                    {
                        nModifiche++;
                    }
                }
                catch (ClassCastException e)
                {
                    throw new IllegalArgumentException("L'elemento non è valido");
                }
            }
            else
            {
                map.remove(element);   //rimozione elemento se le condizioni sono soddisfatte
                if (occurrences > 0)
                {
                    nModifiche++;
                }
            }
        }
        return flag;
    }

    //rimuove una sola occorrenza nel multiset
    @Override
    public boolean remove(Object element)
    {
        if (element == null)
        {
            throw new NullPointerException("ERROE! Fornire un elemento non nullo");
        }
        return remove(element, 1) > 0;
    }

    //imposta il numero di occorrenze di un elemento nel multiset
    @Override
    public int setCount(E element, int count)
    {
        if (element == null)
        {
            throw new NullPointerException("ERROE! Fornire un elemento non nullo");
        }
        if(count < 0)
        {
            throw new IllegalArgumentException("ERRORE! Count non può essere minore di 0 ");
        }
        int flag= count(element);     //conteggio delle occorrenze dell'elemento
        map.put(element, count);       //nuovo count impostato
        if(flag != count) nModifiche++;
        return flag;
    }

    //mostra un set di un count
    @Override
    public Set<E> elementSet()
    {
        Set<E> newSet= new HashSet<>(map.keySet());
        return newSet;
    }

    /*
    * metodo fail-fast iterator, se viene aggiunto o una cancellato almeno un'occorrenza di un
    * elemento nuovo o già presente dopo la creazione dell'iteratore,
    * esso lancerà una java.util.ConcurrentModificationException alla prossima chiamata del metodo next()
    */
    @Override
    public Iterator<E> iterator()
    {
        List<E> list = new ArrayList<>();
        for(E key: map.keySet())
        {
            for(int i=0; i<map.get(key); i++)
            {
                list.add(key);
            }
        }
        return new Itr(list);
    }

    //verifica se un certo elemento è contenuto nel multiset
    @Override
    public boolean contains(Object element)
    {
        if(element == null)
        {
            throw new NullPointerException("ERROE! Fornire un elemento non nullo");
        }
        return map.containsKey(element);
    }

    //ripulisce il multiset
    @Override
    public void clear()
    {
       map.clear();
       nModifiche++;
    }

    //verifica se il multiset è vuoto
    @Override
    public boolean isEmpty()
    {
        if (size()==0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            throw new NullPointerException("ERORRE! L'oggetto obj non può essere nullo");
        }
        if (!(obj instanceof MyMultiset))
        {
            return false;
        }
        MyMultiset other = (MyMultiset) obj;    //cast in oggetto
        Set<E> thisElementSet = elementSet();   //ottiene l'insieme di elementi
        Set<E> otherElementSet = other.elementSet();
        if (!thisElementSet.equals(otherElementSet))  //verifica se gli elementi sono diversi
        {
            return false;
        }
        for (E key : thisElementSet)   //loop for-each che itera su ogni elemento key dell'insieme
        {
            if (count(key) != other.count(key))
            {
                return false;
            }
        }
        return true;
    }
    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return map.hashCode();      //restituisce il codice hash dell' oggetto MyMultiset sul codice della mappa "map"
    }

}
