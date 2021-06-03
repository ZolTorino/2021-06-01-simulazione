package it.polito.tdp.genes.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;



public class Model {
	
	private GenesDao dao;
	private SimpleWeightedGraph<Genes, DefaultWeightedEdge> grafo; 
	Map<String , Genes> idMap;
	public Model() {
		dao= new GenesDao();
	}
	
	public String creaGrafo() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap=new HashMap<>();
		dao.getVertices(idMap);
		Graphs.addAllVertices(grafo, idMap.values());
		String s="Vertici: "+grafo.vertexSet().size();
		LinkedList<Arco> archi= new LinkedList<>(dao.getEdges(idMap));
		System.out.println(archi.size());
		for(Arco a: archi)
		{
			if(grafo.containsVertex(a.g1)&&grafo.containsVertex(a.g2))
			{
				if(grafo.getEdge(a.g1, a.g2)==null&&grafo.getEdge(a.g2, a.g1)==null)
					Graphs.addEdge(grafo,a.g1, a.g2, a.peso);
			}
		}
		s+="Archi: "+grafo.edgeSet().size();
		return s;
	}
	public List<Genes> vertici()
	{
		LinkedList<Genes> vertici= new LinkedList<>(grafo.vertexSet());
		Collections.sort(vertici,(v1,v2)->v1.getGeneId().compareTo(v2.getGeneId()));
		return vertici;
	}
	
	public String adiacenti(Genes in)
	{
		LinkedList<Arco> archi= new LinkedList<>();

		for(Genes g: Graphs.neighborListOf(grafo, in))
		{
			archi.add(new Arco(in, g, grafo.getEdgeWeight(grafo.getEdge(in, g))));

		}
		Collections.sort(archi,(a1,a2)->(int)((10000)*(-a1.peso+a2.peso)));
		String out="";
		
		for(Arco a: archi)
		{
			out+=a.g2.toString()+" "+ a.peso+"\n";
		}
			return out;
	}
	public LinkedList<Arco> pesiAdiacenti(Genes in)
	{
		LinkedList<Arco> archi= new LinkedList<>();

		for(Genes g: Graphs.neighborListOf(grafo, in))
		{
			archi.add(new Arco(in, g, grafo.getEdgeWeight(grafo.getEdge(in, g))));

		}
		Collections.sort(archi,(a1,a2)->(int)((10000)*(-a1.peso+a2.peso)));
		System.out.println(archi.size());
		return archi;
	}
	
	
	//SIMULAZIONE---------------------------------------------------------
	private int n=0;
	Genes gene0= null;
	Map<String, Genes> instudio= new HashMap<String, Genes>();
	public void init(Genes zero, int ing) {
		n=ing;
		gene0=zero;
		idMap.get(gene0.getGeneId()).setIng(n);
		instudio= new HashMap<String, Genes>();
		instudio.put(gene0.getGeneId(),gene0);
		simulator();
	}
	
	public void simulator() {
		System.out.println("Simulazione: \n");
		//Simulo per 12*3 mesi
		for(int i=0; i<((12*3)-1);i++)
		{
			
			//per ogni gene in studio, ricolloco ogni volta gli ing.
			Map<String, Genes> itera= new HashMap<String, Genes>(instudio);
			for(Genes g: itera.values())
			{
				if(g.getIng()>0)
				{
					boolean val0 = new Random().nextInt(3)==0;
					if(!val0){
						//se non devo lasciare uguale, calcolo i pesi di tutti gli archi
						LinkedList<Arco> adiacenti= new LinkedList<>(pesiAdiacenti(g));
						double pesotot=0;
						for(Arco a: adiacenti)
						{
							pesotot+=a.getPeso();
							
						}
						
						//per ogni adiacente
						for(Arco a: adiacenti)
						{
							Random random = new Random();
	
							if(random.nextDouble() < (a.getPeso()/pesotot))
							{
								System.out.println(pesotot);
								if(instudio.get(a.g2.getGeneId())==null)
								{
									instudio.put(a.g2.getGeneId(),a.g2);
									
								}
								instudio.get(a.g2.getGeneId()).setIng(instudio.get(a.g2.getGeneId()).getIng()+1);
								instudio.get(g.getGeneId()).setIng(instudio.get(g.getGeneId()).getIng()-1);
								break;
							}
							
							pesotot=pesotot-a.getPeso().intValue();
						}
						
					}
				}
			}
		}
		for(Genes g: instudio.values())
		{
			if(g.getIng()!=0)
				System.out.println("Gene "+g.getGeneId()+"Con ingegneri: "+g.getIng());
		}
	}
}
