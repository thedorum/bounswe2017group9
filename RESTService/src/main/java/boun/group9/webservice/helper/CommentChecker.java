package boun.group9.webservice.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import boun.group9.webservice.app.Application;
import boun.group9.webservice.app.data.Comments;
import boun.group9.webservice.app.data.Users;
import boun.group9.webservice.exception.NotSavedException;

public class CommentChecker {
	public static String insertCommentQuery(String jsonString) {
		Comments comment = Application.gson.fromJson(jsonString, Comments.class);
		String query = "INSERT INTO Comments (comment,commented_by,concert_id) VALUES ( ";
		query+="'"+comment.getComment()+"',";
		query+=comment.getCommented_by()+",";
		query+=comment.getConcert_id()+")";
		System.out.println(query);
		return query;
	}
	public static ArrayList<Comments> getCommentList(int concertID){
		ArrayList<Comments> result = new ArrayList<Comments>();
		Comments comment;
		Users user;
		String query = "SELECT Comments.up_votes AS Comments_up_votes, Comments.down_votes AS Comments_down_votes, Comments.comment AS Comments_comment, Users.name AS Users_name, Users.email AS Users_email, Users.photo_path AS Users_photo_path FROM Comments INNER JOIN Users ON Users.id = Comments.commented_by WHERE Comments.concert_id="+concertID+";";
		ResultSet rs;
		try {
			rs = Database.connect(query, Application.MODE_GET);
			while(rs.next()) {
				comment = new Comments();
				user = new Users();
				user.setName(rs.getString("Users_name"));
				user.setPhoto_path(rs.getString("Users_photo_path"));
				user.setEmail(rs.getString("Users_email"));
				comment.setComment(rs.getString("Comments_comment"));
				comment.setUp_votes(rs.getInt("Comments_up_votes"));
				comment.setDown_votes(rs.getInt("Comments_down_votes"));
				comment.setCommented_user(user);
				result.add(comment);
			}
		}catch(SQLException ex) {
			ex.printStackTrace();
			return null;
		}catch(NotSavedException ex) {
			ex.printStackTrace();
			return null;
		}
		return result;
	}
}
