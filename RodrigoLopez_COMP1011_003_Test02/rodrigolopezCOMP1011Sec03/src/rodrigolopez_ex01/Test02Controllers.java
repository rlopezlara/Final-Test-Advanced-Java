package rodrigolopez_ex01;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.AnchorPane;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class Test02Controllers {
//	Connection stablic by Lamp services provided at Georgian College

    private static final String DATABASE_URL = "jdbc:mysql://172.31.22.43:3306/Rodrigo200549271";
    private static final String USERNAME = "Rodrigo200549271";
    private static final String PASSWORD = "B7gAmUkjma";

    private static final String DEFAULT_QUERY = "SELECT * FROM ProductsDB";

    private ResultSetTableModel tableModel;
    private TableRowSorter<TableModel> sorter;
    
    @FXML
    private Button MinimumProductBtn;
    
    @FXML
    private TextArea queryTextArea;
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TextField searchNameInput;
    
    @FXML
    private TextField searchProductInput;
    
    @FXML
    private TextArea outputAverage;
    
    @FXML
    private Button displayAveragebtn;
    
    @FXML
    private TextArea outputMinimun;    
  
    // Declare statement variable
    private Statement statement;

    public void initialize() {
        try {
        	// Initialize statement
            Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            // Create ResultSetTableModel for results of DEFAULT_QUERY
            tableModel = new ResultSetTableModel(DATABASE_URL, USERNAME, PASSWORD);
            
            // Call setQuery with default query
            tableModel.setQuery(DEFAULT_QUERY);

            // Create JTable based on the tableModel
            JTable resultTable = new JTable(tableModel);

            // Set up row sorting for JTable
            sorter = new TableRowSorter<TableModel>(tableModel);
            resultTable.setRowSorter(sorter);

            // Configure SwingNode to display JTable, then add to AnchorPane
            SwingNode swingNode = new SwingNode();
            swingNode.setContent(new JScrollPane(resultTable));
            AnchorPane.setTopAnchor(swingNode, 16.0);
            AnchorPane.setLeftAnchor(swingNode, 10.0);
            AnchorPane.setRightAnchor(swingNode, 212.0);
            AnchorPane.setBottomAnchor(swingNode, 120.0);
            anchorPane.getChildren().add(swingNode);
        } catch (SQLException sqlException) {
            displayAlert(AlertType.ERROR, "Database Error", sqlException.getMessage());
            if (tableModel != null) {
                tableModel.disconnectFromDatabase();
            }
            System.exit(1);
        }
        
       
    }

    @FXML
    void displayAllProducts(ActionEvent event) {
       // perform a new query
       try {
          tableModel.setQuery(DEFAULT_QUERY);
       } catch (SQLException sqlException) {
          displayAlert(AlertType.ERROR, "Database Error", sqlException.getMessage());
          
          // try to recover from invalid user query 
          // by executing default query
          try {
             tableModel.setQuery(DEFAULT_QUERY);
          } catch (SQLException sqlException2) {
             displayAlert(AlertType.ERROR, "Database Error", sqlException2.getMessage());
             tableModel.disconnectFromDatabase(); // close connection  
             System.exit(1); // terminate application
          } 
       } 
    }
    
    @FXML
    void DisplayAverageButtonClicked(ActionEvent event) {
        try {
            // Execute query to calculate average product price
            String query = "SELECT AVG(PricePerUnit) FROM ProductsDB";
            ResultSet resultSet = statement.executeQuery(query);

            // Retrieve average price from result set
            if (resultSet.next()) {
                double averagePrice = resultSet.getDouble(1);
                outputAverage.setText("Average: $" + String.format("%.2f", averagePrice));
            } else {
                outputAverage.setText("No data available.");
            }

            // Close result set
//            resultSet.close();
//            
//            statement.close();
        } catch (SQLException sqlException) {
            displayAlert(AlertType.ERROR, "Database Error", sqlException.getMessage());
        }
    }
    
    @FXML
    void displayMinimumProduct(ActionEvent event) {
    	try {                      
    		 
    		// Query to get the minimum price and related product information
            String query = "SELECT * FROM ProductsDB WHERE PricePerUnit = (SELECT MIN(PricePerUnit) FROM ProductsDB)";
            
            // Execute the query
            ResultSet resultSet = statement.executeQuery(query);
            
            // Check if there is a result
            if (resultSet.next()) {
                // Retrieve the product information
                String productName = resultSet.getString("productName");
                String manufactureName = resultSet.getString("Manufacture_Name");
                double minPrice = resultSet.getDouble("PricePerUnit");
                
                // Display the minimum price and product information in the TextArea
                outputMinimun.setText("Minimum Price: $" + String.format("%.2f", minPrice) +
                                      "\nProduct Name: " + productName +
                                      "\nManufacturer: " + manufactureName);
            } else {
                // No result found
                outputMinimun.setText("No minimum price found.");
            }
            
            // Close the resources
//            resultSet.close();
//            statement.close();
             
        } catch (SQLException e) {
            // Handle any SQL exceptions
            displayAlert(AlertType.ERROR, "Database Error", e.getMessage());
        }
    }
    @FXML
    void  SearchButtonClicked(ActionEvent event) {
        String productName = searchNameInput.getText();
        String productId = searchProductInput.getText();
        
        // Build the query based on the provided filters
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ProductsDB WHERE 1=1");
        if (!productName.isEmpty()) {
            queryBuilder.append(" AND productName LIKE '%" + productName + "%'");
        }
        if (!productId.isEmpty()) {
            queryBuilder.append(" AND productId = '" + productId + "'");
        }
        searchNameInput.setText("");
        searchProductInput.setText("");
        
        // Execute the query
        try {
            tableModel.setQuery(queryBuilder.toString());
        } catch (SQLException sqlException) {
            displayAlert(AlertType.ERROR, "Database Error", sqlException.getMessage());
        }
    }
    @FXML
    void  ClearButtonClicked(ActionEvent event) {
        try {
            // Clear the data from the TableView
        	tableModel.clearData();
        } catch (Exception e) {
            displayAlert(AlertType.ERROR, "Error", "An error occurred while clearing data: " + e.getMessage());
        }
    }

    private void displayAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }   
   
 
}
