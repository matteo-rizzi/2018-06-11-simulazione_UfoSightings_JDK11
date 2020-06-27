package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.ufo.model.Adiacenza;
import it.polito.tdp.ufo.model.AnnoAvvistamenti;
import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.State;

public class SightingsDAO {

	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<Sighting> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<AnnoAvvistamenti> getAnniAvvistamenti() {
		String sql = "SELECT YEAR(datetime) AS anno, COUNT(*) AS numeroAvvistamenti FROM sighting GROUP BY YEAR(datetime)";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			List<AnnoAvvistamenti> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				AnnoAvvistamenti aa = new AnnoAvvistamenti(res.getInt("anno"), res.getInt("numeroAvvistamenti"));
				list.add(aa);
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public void getStates(Map<String, State> idMap) {
		String sql = "SELECT * FROM state";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet res = st.executeQuery();

			while (res.next()) {
				if (!idMap.containsKey(res.getString("id"))) {
					State state = new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
							res.getDouble("Lat"), res.getDouble("Lng"), res.getInt("Area"), res.getInt("Population"));
					idMap.put(res.getString("id"), state);
				}
			}

			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<State> getStatesByYear(int anno) {
		String sql = "SELECT DISTINCT state.* FROM state, sighting WHERE state.id = sighting.state AND YEAR(datetime) = ?";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);

			List<State> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				State state = new State(res.getString("id"), res.getString("Name"), res.getString("Capital"),
						res.getDouble("Lat"), res.getDouble("Lng"), res.getInt("Area"), res.getInt("Population"));
				list.add(state);
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public List<Adiacenza> getAdiacenze(int anno, Map<String, State> idMap) {
		String sql = "SELECT DISTINCT s1.state AS primo, s2.state AS secondo FROM sighting AS s1, sighting AS s2 WHERE s1.state <> s2.state AND YEAR(s1.datetime) = YEAR(s2.datetime) AND YEAR(s1.datetime) = ? AND s1.datetime < s2.datetime";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);

			List<Adiacenza> list = new ArrayList<>();

			ResultSet res = st.executeQuery();

			while (res.next()) {
				Adiacenza a = new Adiacenza(idMap.get(res.getString("primo").toUpperCase()), idMap.get(res.getString("secondo").toUpperCase()));
				list.add(a);
			}

			conn.close();
			return list;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
