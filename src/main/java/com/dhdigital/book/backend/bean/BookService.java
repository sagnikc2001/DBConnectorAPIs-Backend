package com.dhdigital.book.backend.bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dhdigital.book.backend.model.BookEnquiry;
import com.dhdigital.book.backend.model.Books;

import oracle.jdbc.OracleTypes;

@Component
public class BookService {

	@Autowired
	private DataSource dataSource;

//	To fetch all books in the DB in url using GET method
	public List<Books> getBooks() {

		Connection conn = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = dataSource.getConnection();
			String strProcedure = "CALL GetBooks2023(?)";
			pstmt = conn.prepareCall(strProcedure);
			pstmt.registerOutParameter(1, OracleTypes.CURSOR);
			pstmt.execute();
			
//			Getting ResultSet response
			rs = (ResultSet) pstmt.getObject(1);  // The 1 inside the getObject(1) method call refers to the index or parameter position of the output parameter retrieving from a CallableStatement. This method is used to retrieve the value of the output parameter at position 1 in the stored procedure call.

//			Defining the ArrayList of Books which we will be returning
			List<Books> listBooks = new ArrayList<>();

			while (rs.next()) {
//				Defining new object of each book data
				Books book = new Books();
//				Setting values in the object
				book.setIsbn(rs.getInt(1));
				book.setBookName(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setGenre(rs.getString(4));
				book.setQty(rs.getInt(5));
//	            Adding the object into the ArrayList of Books
				listBooks.add(book);
			}
			return listBooks;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

//	For fetching book data of particular isbn using POST method containing isbn in body
	public Books getBookByIsbn(BookEnquiry bookEnquiry) {

//		Getting isbn from bookEnquiry object
		int isbn = bookEnquiry.getIsbn();

		Connection conn = null;
		CallableStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = dataSource.getConnection();
			String strProcedure = "CALL GetBookByISBN(?,?)";
			pstmt = conn.prepareCall(strProcedure);
			pstmt.setLong(1, isbn);
			pstmt.registerOutParameter(2, OracleTypes.CURSOR);  //The 2 inside the getObject(2) method call refers to the index or parameter position of the output parameter retrieving from a CallableStatement. This method is used to retrieve the value of the output parameter at position 2 in the stored procedure call.
			pstmt.execute();
			
//			Getting ResultSet response
			rs = (ResultSet) pstmt.getObject(2);
			
			if (rs.next()) {
//				Defining new object of book data
				Books book = new Books();
//				Setting values in the object
				book.setIsbn(rs.getInt(1));
				book.setBookName(rs.getString(2));
				book.setAuthor(rs.getString(3));
				book.setGenre(rs.getString(4));
				book.setQty(rs.getInt(5));
//	            Returning the object which has book data
				return book;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;

	}

//	To add book data into DB using POST method containing Books json
	public void addBook(Books bookData) {

		Connection conn = null;
		CallableStatement pstmt = null;

		try {

			conn = dataSource.getConnection();
			String strProcedure = "CALL InsertBook2023(?, ?, ?, ?, ?)";
			pstmt = conn.prepareCall(strProcedure);
			pstmt.setInt(1, bookData.getIsbn());
			pstmt.setString(2, bookData.getBookName());
			pstmt.setString(3, bookData.getAuthor());
			pstmt.setString(4, bookData.getGenre());
			pstmt.setInt(5, bookData.getQty());

//			Execute the query
			pstmt.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
