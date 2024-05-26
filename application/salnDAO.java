package application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class salnDAO {
	
	// setting database connectivity in 'saln' database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/saln";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "Jaycee@300130";
    
    // Declare a static variables to store the values of the EmployeeID and the DeclarationYear
    private static String employeeID;
    private static String declarationYear;
    
    // gets connection from the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // saves the data entered in textfields at the Declarant Scene to the database
    public static void saveDataforDeclarant(String EmployeeID, String declarationYear, String FilingType,
    		String declarantName, String declarantAddress, String declarantPosition,
    		String declarantAgency, String declarantOfficeAdd) throws SQLException {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO declarant (EmployeeID, DeclarationYear, FilingType, DeclarantName, "
            		+ "DeclarantAddress, DeclarantPosition, DeclarantAgency, DeclarantOfficeAddress) "
            		+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, EmployeeID);
            statement.setString(2, declarationYear);
            statement.setString(3, FilingType);
            statement.setString(4, declarantName);
            statement.setString(5, declarantAddress);
            statement.setString(6, declarantPosition);
            statement.setString(7, declarantAgency);
            statement.setString(8, declarantOfficeAdd);
            
            statement.executeUpdate();
            
            // sets the values in the String created
            employeeID = EmployeeID;
            salnDAO.declarationYear = declarationYear;
            
        } catch (SQLException e) {
    	e.printStackTrace();
    	}
    }
    
    // saves the data entered in textfields at the Spouse Scene to the database
    public static void saveDataforSpouse(String spouseName, String spouseAgency, 
    		String spousePosition, String spouseOfficeAdd) throws SQLException {
        try (Connection connection = getConnection()) {
        	String sql = "UPDATE declarant SET SpouseName = ?, SpousePosition = ?, SpouseAgency = ?, "
                    + "SpouseOfficeAddress = ? WHERE EmployeeID = ? AND DeclarationYear = ?";

            
            PreparedStatement statement = connection.prepareStatement(sql);
            // Use the stored values in the Declarant scene
            statement.setString(1, spouseName);
            statement.setString(2, spousePosition);
            statement.setString(3, spouseAgency);
            statement.setString(4, spouseOfficeAdd);
            statement.setString(5, employeeID);
            statement.setString(6, declarationYear);
            
            statement.executeUpdate();
        } catch (SQLException e) {
    	e.printStackTrace();
    	}
    }
    
    // saves the data entered in textfields at the ChildrenScene to the database
    public static void saveDataforUnmarriedChilrenBelow18(String UnmarriedChildrenBelow18ID, String UnmarriedChildrenNameBelow18, 
    		LocalDate UnmarriedChildrenBelow18Birthdate, int UnmarriedChildrenBelow18Age) throws SQLException {
        try (Connection connection = getConnection()) {
        	  String sql = "INSERT INTO unmarriedchildrenbelow18 (UnmarriedChildrenBelow18ID, UnmarriedChildrenNameBelow18,"
        	  		+ "UnmarriedChildrenBelow18Birthdate, UnmarriedChildrenBelow18Age, EmployeeID, DeclarationYear) "
              		+ "VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, UnmarriedChildrenBelow18ID);
            statement.setString(2, UnmarriedChildrenNameBelow18);
            statement.setDate(3, Date.valueOf(UnmarriedChildrenBelow18Birthdate));
            statement.setInt(4, UnmarriedChildrenBelow18Age);
            statement.setString(5, employeeID);
            statement.setString(6, declarationYear);
            
            statement.executeUpdate();            
        } catch (SQLException e) {
    	e.printStackTrace();
    	}
    }
    
    // get the latest ChildrenID from  the database
    public static int getLatestChildIDFromDatabase() {
        int latestID = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String query = "SELECT MAX(UnmarriedChildrenBelow18ID) FROM unmarriedchildrenbelow18";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                latestID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return latestID;
    }
    
    // saves the data entered in textfields at the RealProperty scene to the database
    public static void saveDataforRealProperty(String realPropertyID, String realPropDesc, String Kind, String exactLocation,
            int assessedValue, int marketValue, int realPropAcqYear,
            String realPropAcqMode, int realPropAcqCost) {
        try (Connection connection = getConnection()) {
        	String sql = "INSERT INTO realproperty (RealPropertyID, RealPropertyDescription, KInd, ExactLocation, " 
        			+ "AssessedValue, MarketValue, AcquisitionYear, AcquisitionMode, RealPropertyAcqCost, "
        			+ "EmployeeID, DeclarationYear) " 
        			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, realPropertyID);
			statement.setString(2, realPropDesc);
			statement.setString(3,  Kind);
			statement.setString(4, exactLocation);
			statement.setInt(5, assessedValue);
			statement.setInt(6, marketValue);
			statement.setInt(7, realPropAcqYear);
			statement.setString(8, realPropAcqMode);
			statement.setInt(9, realPropAcqCost);
			statement.setString(10, employeeID);
            statement.setString(11, declarationYear);
			
			statement.executeUpdate();
		} catch (SQLException e) {
		e.printStackTrace();
		}
	}
    
    // get the latest PersonalPropertyID from  the database
    public static int getLatestRealPropertyIDFromDatabase() {
        int latestID = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String query = "SELECT MAX(RealPropertyID) FROM realproperty";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                latestID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return latestID;
    }
    
    // saves the data entered in textfields at the PersonalProperty Scene to the database
    public static void saveDataforPersonalProperty(int personalPropertyID, String personalPropertyDesc, 
            int yearAcquired, int acq_costField) throws SQLException {
    	
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO personalproperty (PersonalPropertyID, PersonalPropertyDescription, "
                    + "YearAcquired, PersonalPropertyAcqCost, EmployeeID, DeclarationYear) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            // Use the stored values in the Declarant scene
            statement.setInt(1, personalPropertyID);
            statement.setString(2, personalPropertyDesc);
            statement.setInt(3, yearAcquired);
            statement.setInt(4, acq_costField);
            statement.setString(5, employeeID);
            statement.setString(6, declarationYear);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // get the latest PersonalPropertyID from  the database
    public static int getLatestPersonalPropertyIDFromDatabase() {
        int latestID = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String query = "SELECT MAX(PersonalPropertyID) FROM personalproperty";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                latestID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return latestID;
    }
    
    // saves the data entered in textfields at the Liability Scene to the database
    public static void saveDataforLiability(int LiabilityID, String Nature, 
            String CreditorName, int OutstandingBalance) throws SQLException {
    	
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO liability (LiabilityID, Nature, "
                    + "CreditorName, OutstandingBalance, EmployeeID, DeclarationYear) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            // Use the stored values in the Declarant scene
            statement.setInt(1, LiabilityID);
            statement.setString(2, Nature);
            statement.setString(3, CreditorName);
            statement.setInt(4, OutstandingBalance);
            statement.setString(5, employeeID);
            statement.setString(6, declarationYear);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // get the latest PersonalPropertyID from  the database
    public static int getLatestLiabilityIDFromDatabase() {
        int latestID = 0;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            String query = "SELECT MAX(LiabilityID) FROM liability";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                latestID = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return latestID;
    }
    
    // Get the sum of RealPropertyAcqCost for a given employeeID and declarationYear
    public static int getSumOfRealPropertyAcqCost(String employeeID, String declarationYear) throws SQLException {
        int sum = 0;

        try (Connection connection = getConnection()) {
            String sql = "SELECT SUM(RealPropertyAcqCost) FROM realproperty WHERE EmployeeID = ? AND DeclarationYear = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employeeID);
            statement.setString(2, declarationYear);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                sum = resultSet.getInt(1);
            }
        }

        return sum;
    }
    
    // Get the sum of PersonalPropertyAcqCost for a given employeeID and declarationYear
    public static int getSumOfPersonalPropertyAcqCost(String employeeID, String declarationYear) throws SQLException {
        int sum = 0;

        try (Connection connection = getConnection()) {
            String sql = "SELECT SUM(PersonalPropertyAcqCost) FROM personalproperty WHERE EmployeeID = ? AND DeclarationYear = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employeeID);
            statement.setString(2, declarationYear);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                sum = resultSet.getInt(1);
            }
        }

        return sum;
    }
    
    // Get the sum of liability Outstanding Balance for a given employeeID and declarationYear
    public static int getSumOfOutstandingBalance(String employeeID, String declarationYear) throws SQLException {
        int sum = 0;

        try (Connection connection = getConnection()) {
            String sql = "SELECT SUM(OutstandingBalance) FROM liability WHERE EmployeeID = ? AND DeclarationYear = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, employeeID);
            statement.setString(2, declarationYear);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                sum = resultSet.getInt(1);
            }
        }

        return sum;
    }
    
    // saves the data entered in textfields at the Summary Scene to the database
    public static void saveDataforSummary(int RPsum, int PPsum, int liabilitySum, 
    		int totalAsset, double networth) throws SQLException {
    	
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO summary (EmployeeID, DeclarationYear, "
                    + "RealPropertySubtotal, PersonalPropertySubtotal, TotalAssets, "
                    + "TotalLiabilities, NetWorth) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            // Use the stored values in the Declarant scene
            statement.setString(1, employeeID);
            statement.setString(2, declarationYear);
            statement.setInt(3, RPsum);
            statement.setInt(4, PPsum);
            statement.setInt(5, totalAsset);
            statement.setInt(6, liabilitySum);
            statement.setDouble(7, networth);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // manages searching records from the database
    public void searchSalnData(String employeeId, String year, TableView<String[]> resultTable, String tableName) {
        try (Connection connection = getConnection()) {
            resultTable.getColumns().clear();
            resultTable.getItems().clear();

            // Generate the SELECT query
            String query = "SELECT * FROM " + tableName + " WHERE EmployeeID = ? AND DeclarationYear = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, employeeId);
            statement.setString(2, year);
            ResultSet resultSet = statement.executeQuery();

            // Generate table columns based on result set metadata
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            // Hide EmployeeID and DeclarationYear columns for tables other than "declarant"
            boolean hideColumns = !tableName.equals("declarant");

            for (int i = 1; i <= columnCount; i++) {
                int columnIndex = i;
                String columnName = resultSetMetaData.getColumnName(i);
                if (hideColumns && (columnName.equals("EmployeeID") || columnName.equals("DeclarationYear"))) {
                    continue; // Skip these columns for tables other than "declarant"
                }
                TableColumn<String[], String> column = new TableColumn<>(columnName);
                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[columnIndex - 1]));

                resultTable.getColumns().add(column);
            }

            // Populate table with data
            while (resultSet.next()) {
                String[] rowData = new String[columnCount];
                for (int i = 0; i < columnCount; i++) {
                	rowData[i] = resultSet.getString(i + 1);
                }
                resultTable.getItems().add(rowData);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
    // updates the data entered in textfields at the declarant Scene to the database
    public static void updateDataforDeclarant(String EmployeeID, String declarationYear, 
    		String FilingType, String declarantName, String declarantAddress, String declarantPosition,
    		String declarantAgency, String declarantOfficeAdd) throws SQLException {
        try (Connection connection = getConnection()) {
        	String sql = "UPDATE declarant SET FilingType = ?, DeclarantName = ?, DeclarantAddress = ?, "
                    + "DeclarantPosition = ?, DeclarantAgency = ?, DeclarantOfficeAddress = ? WHERE EmployeeID = ? AND DeclarationYear = ?";

            
            PreparedStatement statement = connection.prepareStatement(sql);
            // Use the stored values in the Declarant scene
            statement.setString(1, FilingType);
            statement.setString(2, declarantName);
            statement.setString(3, declarantAddress);
            statement.setString(4, declarantPosition);
            statement.setString(5, declarantAgency);
            statement.setString(6, declarantOfficeAdd);
            statement.setString(7, EmployeeID);
            statement.setString(8, declarationYear);
            
            statement.executeUpdate();                       
        } catch (SQLException e) {
    	e.printStackTrace();
    	}
    }
    
    // updates the data entered in textfields at the Spouse Scene to the database
    public static void updateDataforSpouse(String spouseName, String spousePosition, 
    		String spouseAgency, String spouseOfficeAdd, String employeeID, String declarationYear) throws SQLException {
        try (Connection connection = getConnection()){
        	String sql = "UPDATE declarant SET SpouseName = ?, SpousePosition = ?, SpouseAgency = ?, "
                    + "SpouseOfficeAddress = ? WHERE EmployeeID = ? AND DeclarationYear = ?";
            
            PreparedStatement statement = connection.prepareStatement(sql);
            // Use the stored values in the Declarant scene
            statement.setString(1, spouseName);
            statement.setString(2, spousePosition);
            statement.setString(3, spouseAgency);
            statement.setString(4, spouseOfficeAdd);
            statement.setString(5, employeeID);
            statement.setString(6, declarationYear);
            
            statement.executeUpdate();
        } catch (SQLException e) {
    	e.printStackTrace();
    	}
    }
    
    // updates the data entered in textfields at the ChildrenScene to the database
    public static void updateDataforUnmarriedChilrenBelow18(int UnmarriedChildrenBelow18ID, String UnmarriedChildrenNameBelow18,
            LocalDate UnmarriedChildrenBelow18Birthdate, int UnmarriedChildrenBelow18Age) throws SQLException {

        try (Connection connection = getConnection()) {
            String sql = "UPDATE unmarriedchildrenbelow18 SET "
                    + "UnmarriedChildrenNameBelow18 = ?, UnmarriedChildrenBelow18Birthdate = ?, "
                    + "UnmarriedChildrenBelow18Age = ? "
                    + "WHERE UnmarriedChildrenBelow18ID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, UnmarriedChildrenNameBelow18);

            if (UnmarriedChildrenBelow18Birthdate != null) {
                statement.setDate(2, Date.valueOf(UnmarriedChildrenBelow18Birthdate));
            } else {
                statement.setNull(2, Types.DATE);
            }

            statement.setInt(3, UnmarriedChildrenBelow18Age);
            statement.setInt(4, UnmarriedChildrenBelow18ID);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // updates the data entered in textfields at the Real Property Scene to the database
    public static void updateDataForRealProperty(int realPropertyID, String description, String kind, String exactLocation,
            int assessedValue, int marketValue, String acquisitionYear, String acquisitionMode, int acquisitionCost) throws SQLException {

        try (Connection connection = getConnection()) {
            String sql = "UPDATE realproperty SET RealPropertyDescription = ?, Kind = ?, ExactLocation = ?, " +
                    "AssessedValue = ?, MarketValue = ?, AcquisitionYear = ?, " +
                    "AcquisitionMode = ?, RealPropertyAcqCost = ? WHERE RealPropertyID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, description);
            statement.setString(2, kind);
            statement.setString(3, exactLocation);
            statement.setInt(4, assessedValue);
            statement.setInt(5, marketValue);
            statement.setString(6, acquisitionYear);
            statement.setString(7, acquisitionMode);
            statement.setInt(8, acquisitionCost);
            statement.setInt(9, realPropertyID);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    // updates the data entered in textfields at the Personal Property Scene to the database
    public static void updateDataForPersonalProperty(int personalPropertyID, String personal_property_description, 
    		String yearAcquired, int PersonalProperty_acqCost) throws SQLException {

        try (Connection connection = getConnection()) {
        	String sql = "UPDATE personalproperty SET PersonalPropertyDescription = ?, YearAcquired = ?, PersonalPropertyAcqCost = ? " +
                    "WHERE PersonalPropertyID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, personal_property_description);
            statement.setString(2, yearAcquired);
            statement.setInt(3, PersonalProperty_acqCost);
            statement.setInt(4, personalPropertyID);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // updates the data entered in textfields at the Liability Scene to the database
    public static void updateDataForLiability(int liabilityID, String Nature, 
    		String creditorName, int Outstanding_Balance) throws SQLException {

        try (Connection connection = getConnection()) {
        	String sql = "UPDATE liability SET Nature = ?, CreditorName = ?, OutstandingBalance = ? " +
                    "WHERE LiabilityID = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, Nature);
            statement.setString(2, creditorName);
            statement.setInt(3, Outstanding_Balance);
            statement.setInt(4, liabilityID);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean deleteRecord(String tableName, String[] rowData) {
        String sql = "DELETE FROM " + tableName + " WHERE ";
        String[] columnNames = getColumnNames(tableName);

        for (int i = 0; i < columnNames.length; i++) {
            if (i > 0) {
                sql += " AND ";
            }
            sql += columnNames[i] + " = ?";
        }

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < columnNames.length; i++) {
                statement.setString(i + 1, rowData[i]);
            }

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String[] getColumnNames(String tableName) {
        try (Connection connection = getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getColumns(null, null, tableName, null);

            List<String> columnNames = new ArrayList<>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                columnNames.add(columnName);
            }

            return columnNames.toArray(new String[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }


    
}
