package tools;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import dao.CommentDAO;
import dao.MemberDAO;
import dao.PostDAO;
import dao.SearchDAO;
import db.*;
import vo.*;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.print(Permission.GENREAL_ADMIN.compareTo(Permission.GUEST));
	}

}
