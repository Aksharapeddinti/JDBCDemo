package MovieIMDB;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@SuppressWarnings("serial")
class ShorterMovieNameException extends Exception {
    
	public ShorterMovieNameException(String s)
    {
    	super(s);
    }
    public ShorterMovieNameException()
    {
    	super();
    }
}


//Unchecked User Exception


@SuppressWarnings("serial")
class LesserProductionCostException extends RuntimeException
{
	public LesserProductionCostException(String s)
	{
		super(s);
	}
	public LesserProductionCostException()
	{
		super();
	}
	
}

public class MovieControl 
{
	private static final String URL="jdbc:mysql://localhost:3306/IMDB";
	private static final String Username="root";
    private static final String Password="Ramya29@2000";
	
	 public static void main(String[] args) {
		 
		 MovieControl m=new MovieControl();
		 Movie n=new Movie();
		   try {
	            Class.forName("com.mysql.cj.jdbc.Driver");
	            Connection connection = DriverManager.getConnection(URL,Username,Password);
	            System.out.println("Connected to the database");

	            Scanner scanner = new Scanner(System.in);
	            int choice = 0;

	            while (choice< 7) {
	                displayMenu();
	                System.out.print("Enter your choice: ");
	                choice = scanner.nextInt();
	                scanner.nextLine(); // Consume newline character

	                switch (choice) {
	                    case 1:
	                        m.insertMovie();
	                        break;
	                    case 2:
	                    	System.out.println("Enter moviename : ");
	                    	String mname=scanner.nextLine();
	                    	ArrayList<Movie> moviesByName = getMovie(mname);
	            	        for (Movie movie : moviesByName) {
	            	            System.out.println(movie.getMovieName());
	            	        }
	                        
	                        break;
	                    case 3:
	                    	//System.out.println("Enter productioncost : ");
	                    	//Double pcost=scanner.nextDouble();
	                    	ArrayList<Movie> moviesByCost = getMovie(100);
	                        for (Movie movie : moviesByCost) {
	                            System.out.println(movie.getMovieName());
	                        }
	                    	
	                    	break;
	                    case 4:
	                    	System.out.println("Enter releasedate in yyyy-MM-dd: ");
	                    	String reldate=scanner.nextLine();
	                    	ArrayList<Movie> moviesByDate = getMovie(Date.valueOf(reldate));
	                        for (Movie movie : moviesByDate) {
	                            System.out.println(movie.getMovieName());
	                        }
	                    	
	                    	
	                    	break;
	                    case 5:
	                        deleteMovie(connection);
	                        break;
	                    case 6:
	                    	getAllMovies(connection);
	                        break;
	                    case 7:
	                        System.out.println("Exiting program...");
	                        break;
	                    default:
	                        System.out.println("Invalid choice! Please try again.");
	                        break;
	                }
	            }

	            connection.close();
	            scanner.close();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    

		private static void displayMenu() {
	        System.out.println("\n--- Menu ---");
	        System.out.println("1. Insert a new member into the table");
	        System.out.println("2. Get Movie By MovieName");
	        System.out.println("3. Get Movie By ProductionCost");
	        System.out.println("4. Get Movie By ReleaseDate");
	        System.out.println("5. Delete membership details");
	        System.out.println("6. Display details of all members");
	        System.out.println("7. Exit");
	    }

	       
	        
	    private static void getAllMovies(Connection connection) throws SQLException{
	    	String selectQuery = "SELECT * FROM Movies";
	        Statement statement = connection.createStatement();
	        ResultSet resultSet = statement.executeQuery(selectQuery);

	        System.out.println("\n--- Movie Details ---");
	        System.out.println("MovieName\tReleaseDate\tproductionCost\tnoOfScreensReleased\tdirectedBy\tproducedBy\tstatus");

	        while (resultSet.next()) {
	            String movieName = resultSet.getString("movieName");
	            String releasedate = resultSet.getString("releaseDate");
	            Double productionCost = resultSet.getDouble("productionCost");
	            int noOfScreensReleased=resultSet.getInt("noOfScreensReleased");
	            String directedBy=resultSet.getString("directedBy");
	            String producedBy=resultSet.getString("producedBy");
	            boolean status=resultSet.getBoolean("status");
	            

	            System.out.println(movieName + "\t\t" + releasedate + "\t\t" + productionCost + "\t\t" + noOfScreensReleased + "\t\t" +
	            		directedBy+"\t\t"+ producedBy+"\t\t"+status );
	        }

	        resultSet.close();
	        statement.close();
	        
	    
	    }

	    private static void deleteMovie(Connection connection) throws SQLException {
	        Scanner scanner = new Scanner(System.in);

	        System.out.print("Enter movie name: ");
	        String moviename = scanner.nextLine();
	        //scanner.nextLine(); // Consume newline character

	        String deleteQuery = "DELETE FROM Movies WHERE movieName = ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
	        preparedStatement.setString(1, moviename);

	        int rowsAffected = preparedStatement.executeUpdate();
	        System.out.println("Rows deleted: " + rowsAffected);
	        scanner.close();
	    }

	    

		

	    public static ArrayList<Movie> getMovie(double productionCost) {
	        ArrayList<Movie> movies = new ArrayList<>();

	        try (Connection connection = DriverManager.getConnection(URL, Username, Password)) {
	            String query = "SELECT * FROM Movies WHERE productionCost > ?";
	            PreparedStatement statement = connection.prepareStatement(query);
	            statement.setDouble(1, productionCost);
	            ResultSet resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                // Create Movie object and set its properties
	                Movie movie = new Movie();
	                movie.setMovieName(resultSet.getString("movieName"));
	                movie.setReleaseDate(resultSet.getString("releaseDate"));
	                movie.setProductionCost(resultSet.getDouble("productionCost"));
	                movie.setNoOfScreensReleased(resultSet.getInt("noOfScreensReleased"));
	                movie.setDirectedBy(resultSet.getString("directedBy"));
	                movie.setProducedBy(resultSet.getString("producedBy"));
	                movie.setStatus(resultSet.getBoolean("status"));

	                movies.add(movie);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return movies;
	    }
	    public static ArrayList<Movie> getMovie(String movieName) {
	        ArrayList<Movie> movies = new ArrayList<>();

	        try (Connection connection = DriverManager.getConnection(URL, Username, Password)) {
	            String query = "SELECT * FROM Movies WHERE movieName = ?";
	            PreparedStatement statement = connection.prepareStatement(query);
	            statement.setString(1, movieName);
	            ResultSet resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                // Create Movie object and set its properties
	                Movie movie = new Movie();
	                movie.setMovieName(resultSet.getString("movieName"));
	                movie.setReleaseDate(resultSet.getString("releaseDate"));
	                movie.setProductionCost(resultSet.getDouble("productionCost"));
	                movie.setNoOfScreensReleased(resultSet.getInt("noOfScreensReleased"));
	                movie.setDirectedBy(resultSet.getString("directedBy"));
	                movie.setProducedBy(resultSet.getString("producedBy"));
	                movie.setStatus(resultSet.getBoolean("status"));

	                movies.add(movie);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return movies;
	        
	    }

	    public static ArrayList<Movie> getMovie(Date releaseDate) {
	        ArrayList<Movie> movies = new ArrayList<>();

	        try (Connection connection = DriverManager.getConnection(URL, Username, Password)) {
	            String query = "SELECT * FROM Movies WHERE releaseDate = ?";
	            PreparedStatement statement = connection.prepareStatement(query);
	            statement.setDate(1, releaseDate);
	            ResultSet resultSet = statement.executeQuery();

	            while (resultSet.next()) {
	                // Create Movie object and set its properties
	                Movie movie = new Movie();
	                movie.setMovieName(resultSet.getString("movieName"));
	                movie.setReleaseDate(resultSet.getString("releaseDate"));
	                movie.setProductionCost(resultSet.getDouble("productionCost"));
	                movie.setNoOfScreensReleased(resultSet.getInt("noOfScreensReleased"));
	                movie.setDirectedBy(resultSet.getString("directedBy"));
	                movie.setProducedBy(resultSet.getString("producedBy"));
	                movie.setStatus(resultSet.getBoolean("status"));

	                movies.add(movie);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return movies;
	    }
	    
	    @SuppressWarnings("resource")
		public void insertMovie() {
	    	try
			{
				 Scanner sc=new Scanner(System.in);
				// Accept input from the user
		         System.out.print("Enter movie name: ");
		         String movieName = sc.nextLine();
		         
		         //check length of movie name
		         
		         if(movieName.length()<3)
		         {
		        	 throw new ShorterMovieNameException();
		         }
		         
		         System.out.print("Enter release date yyyy-MM-dd: ");
		         String releaseDate = sc.nextLine();
		         
		         System.out.print("Enter production cost (in Cr): ");
		         double productionCost = sc.nextDouble();

		         // Check productionCost
		         if (productionCost < 10) 
		         {
		           throw new LesserProductionCostException();
		         }
		         
		         System.out.print("Enter number of screens released: ");
		         int noOfScreensReleased = sc.nextInt();
		         sc.nextLine(); // Consume the newline character

		         System.out.print("Enter director's name: ");
		         String directedBy = sc.nextLine();

		         System.out.print("Enter producer's name: ");
		         String producedBy = sc.nextLine();

		         System.out.print("Enter status: ");
		         Boolean status1 = sc.nextBoolean();	
		         
		         // Create a Movie object
		         Movie movie = new Movie(movieName, releaseDate, productionCost, noOfScreensReleased, directedBy, producedBy, status1);

		         // Insert the movie data into the Movies table
		         
		         
		 	     
		 	    try (Connection connection = DriverManager.getConnection(URL, Username, Password)) 
		 	    {
	                String sql = "INSERT INTO Movies (movieName, releaseDate, productionCost, noOfScreensReleased, directedBy, producedBy, status) " +
	                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
	                PreparedStatement statement = connection.prepareStatement(sql);
	                statement.setString(1, movie.getMovieName());
	                statement.setString(2, movie.getReleaseDate());
	                statement.setDouble(3, movie.getProductionCost());
	                statement.setInt(4, movie.getNoOfScreensReleased());
	                statement.setString(5, movie.getDirectedBy());
	                statement.setString(6, movie.getProducedBy());
	                statement.setBoolean(7, movie.getStatus());
	                
	                int rowsAffected = statement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Movie data inserted successfully.");
	                } else {
	                    System.out.println("Failed to insert movie data.");
	                }

	                statement.close();
			     } 
		 	    
		 	   catch (SQLException e) {
	               e.printStackTrace();
	           }

	           sc.close();
	       } 
			
			
			catch (ShorterMovieNameException e) 
			{
	            System.out.println("Error: Movie name should be a minimum of 3 characters.");
	            insertMovie(); // Ask the user to reenter the movie name
	        } catch (LesserProductionCostException e) 
			{
	            System.out.println("Error: Production cost should be more than 10 Cr.");
	            insertMovie(); // Ask the user to reenter the production cost
	        }
			
		}
	    
}
