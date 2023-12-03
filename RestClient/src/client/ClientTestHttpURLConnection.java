package client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import com.google.gson.Gson;
import financialApp.Transactionn;
import financialApp.TransactionManagement;

public class ClientTestHttpURLConnection {	
	public static void main(String[] args) {
		addTransaction();
//		getTransactions();
//		updateBook();
		getTransactionById(21);

	}
	
	private static void addTransaction() {
		HttpURLConnection conn = null;
		Gson gson = new Gson();

		try {

			URL url = new URL("http://localhost:8080/RestServer/transaction/addTransaction");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			conn.setDoOutput(true);
			conn.setDoInput(true);

			String postData = gson.toJson(new Transactionn(150.00, "01-12-2023", "Groceries", "expense"), Transactionn.class);

			DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			wr.writeBytes(postData);
			wr.flush();
			wr.close();

			if (conn.getResponseCode() < 200 && conn.getResponseCode() > 299) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			} else {
				getTransactions();
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	
	private static void getTransactions() {
		HttpURLConnection conn = null;

		try {

			URL url = new URL("http://localhost:8080/RestServer/transaction/getTransactions");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			Gson gson = new Gson();
			List<Transactionn> transactions = Arrays.asList(gson.fromJson(br, Transactionn[].class));
			System.out.println("Output JSON from Server .... \n");
			for (Transactionn t : transactions) {
				System.out.println(t);
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private static void getTransactionById(int transactionId) {
		HttpURLConnection conn = null;

		try {

			URL url = new URL("http://localhost:8080/RestServer/transaction/getTransaction/" + Integer.toString(transactionId));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.connect();

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			Gson gson = new Gson();
			Transactionn transaction = gson.fromJson(br, Transactionn.class);
			System.out.println("Output JSON from Server .... \n");
			System.out.println(transaction);

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
