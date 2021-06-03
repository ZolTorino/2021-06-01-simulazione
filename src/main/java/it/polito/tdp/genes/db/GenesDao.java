package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertices(Map<String, Genes> result){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes WHERE Essential='Essential'";
	
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.put(genes.getGeneId(), genes);
			}
			res.close();
			st.close();
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	public List<Arco>  getEdges(Map<String, Genes> idMap){
		String sql = "SELECT DISTINCT itr.GeneID1 AS id1, itr.GeneID2 AS id2, IF(g1.Chromosome=g2.Chromosome,2*ABS(itr.Expression_Corr),ABS(itr.Expression_Corr)) as peso "
				+ "FROM `interactions` AS itr, `genes` AS g1, `genes` AS g2 "
				+ "WHERE itr.GeneID1 !=itr.GeneID2  "
				+ "AND g1.GeneID=itr.GeneID1 "
				+ "AND g2.GeneID=itr.GeneID2 "
				+ "AND g1.Essential=\"Essential\" "
				+ "AND g2.Essential=\"Essential\" "
				+ "";
	
		Connection conn = DBConnect.getConnection();
		List<Arco> result = new ArrayList<Arco>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Arco arco = new Arco(idMap.get(res.getString("id1")), idMap.get(res.getString("id2")), res.getDouble("peso"));
				result.add(arco);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}
	}
	

	
}
