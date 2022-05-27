package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Arco;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public Map<String, Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		Map<String, Genes> result = new HashMap<>();
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
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Genes> getEssentialGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome "
				+ "FROM Genes "
				+ "WHERE Essential = 'Essential'";
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

	public List<Arco> getArchi() {
		String sql = "SELECT `GeneID1`, `GeneID2`, `Type`, ABS(`Expression_Corr`) as Weight "
				+ "FROM `interactions` "
				+ "WHERE `GeneID1` != `GeneID2` AND `GeneID1` IN (SELECT DISTINCT `GeneID` "
				+ "											  FROM `genes` "
				+ "											  WHERE Essential = 'Essential') AND `GeneID2` IN (SELECT DISTINCT `GeneID` "
				+ "											 												   FROM `genes` "
				+ "																							   WHERE Essential = 'Essential')";
		List<Arco> result = new ArrayList<>();
		Map<String, Genes> map = this.getAllGenes();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Genes g1 = map.get(res.getString("GeneID1"));
				Genes g2 = map.get(res.getString("GeneID2"));
				
				result.add(new Arco(g1, g2, res.getString("Type"), res.getDouble("Weight")));
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

}
