/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tabstrial;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author LulzSec
 */
public class DatabaseController implements Initializable {

    @FXML
    private Button button;
    @FXML
    private Label label;
    @FXML
    private TableView<RegClass> RegTable;
    @FXML
    private TableColumn<RegClass, Integer> RegRow;
    @FXML
    private TableColumn<RegClass, String> RegReg;
    @FXML
    private TableColumn<RegClass, String> RegName;
    @FXML
    private TableColumn<RegClass, String> RegCode;
    @FXML
    private TableColumn<RegClass, String> fileColumn;
    @FXML
    private ComboBox<String> RegNameCombo;
    @FXML
    private TextField RegTextField;
    @FXML
    private Button RegButton;
    @FXML
    private TextField RegTF;
    @FXML
    private TextField SubTF;
    @FXML
    private TextField CodeTF;
    @FXML
//    private Label ChosenImage;
//    @FXML
    private Button selectFile;
    
    String fileNameUpdate;
    
    int count;
    ObservableList<RegClass> list  = FXCollections.observableArrayList();
    @FXML
    private TextField progTf;
    @FXML
    private TableColumn<?, ?> progCol;
    
    int rowId;
    @FXML
    private Button syllabusUpdateButton;
    @FXML
    private TextField ChosenImage;
    @FXML
    private Button deleteRegButton;
    
    int ID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        RegNameCombo.setValue("R15");
            initiatecols();
            LoadData();
            try {
        Connection c = connect();
        Statement stmt = null;
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");
        stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery( "SELECT * FROM Regulations" );
        String Reg;
        while ( rs.next() ) {
              
            Reg = rs.getString("TableName");
            RegNameCombo.getItems().addAll(Reg);
        
        }
        rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);    
        }
            RegTable.setRowFactory( tv -> {
    TableRow<RegClass> row = new TableRow<>();
    row.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
             try {
//      Class.forName("org.sqlite.JDBC");
      
      Connection c = connect();
      Statement stmt = null;
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery( "SELECT * FROM "+RegNameCombo.getValue()+" WHERE rowID="+rowId+"" );
     
      while ( rs.next() ) {
          
         
           RegTF.setText(rs.getString("SubShortName"));
           progTf.setText(rs.getString("Program"));
          SubTF.setText(rs.getString("SubjectName"));
          CodeTF.setText(rs.getString("SubjectCode"));
          fileNameUpdate = rs.getString("column1");
          ChosenImage.setText(rs.getString("column1"));

      }
       String selectSQL = "SELECT Image_Reg FROM "+RegNameCombo.getValue()+ " WHERE rowID="+rowId+"";
       rs = stmt.executeQuery(selectSQL);
       InputStream is =rs.getBinaryStream("Image_Reg");
                OutputStream os = new FileOutputStream(new  File("update.pdf"));
                byte[] content = new byte[1024];
                int size = 0;
                while((size = is.read(content)) !=-1){
                    os.write(content,0,size);
                }
      rs.close();
      stmt.close();
      c.close();
   } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
   }
        }
    });
    return row ;
});
    }    
    
    private void LoadData(){
        list.removeAll(list);
        
        
        
//        list.addAll(new RegClass(2,"R10","Magic Science","45ae34"));
        try {
//      Class.forName("org.sqlite.JDBC");
      
      Connection c = connect();
      Statement stmt = null;
      c.setAutoCommit(false);
      System.out.println("Opened database successfully");

      stmt = c.createStatement();
      ResultSet rs = stmt.executeQuery( "SELECT * FROM "+RegNameCombo.getValue()+"" );
      
      String Reg,SubNam,SubCod,fName,program;
      while ( rs.next() ) {
          
         int id = rs.getInt("rowID");
           Reg = rs.getString("SubShortName");
           program = rs.getString("Program");
          SubNam  = rs.getString("SubjectName");
          SubCod = rs.getString("SubjectCode");
          fName = rs.getString("column1");
         
         
         
         list.addAll(new RegClass(id,Reg,SubNam,SubCod,program,fName));
         
        
      }
      rs.close();
      stmt.close();
      c.close();
   } catch ( Exception e ) {
      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
      System.exit(0);
   }
        
        RegTable.getItems().addAll(list);
        
        
    }
    
   private void initiatecols() {
        
        RegRow.setCellValueFactory(new PropertyValueFactory<>("RowID"));
        RegReg.setCellValueFactory(new PropertyValueFactory<>("Regulation"));
        RegName.setCellValueFactory(new PropertyValueFactory<>("SubjectName"));
        RegCode.setCellValueFactory(new PropertyValueFactory<>("SubjectCode"));
        progCol.setCellValueFactory(new PropertyValueFactory<>("Program"));
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        
    }

    @FXML
    private void addRow(ActionEvent event) {
        count=0;
        count++;
        list.removeAll(list);
        Connection c = connect();
      PreparedStatement stmt;
      Statement stmt2;
      
        try {
             stmt2 = c.createStatement();
      ResultSet rs = stmt2.executeQuery( "SELECT * FROM "+RegNameCombo.getValue()+"" );
      
     
      while ( rs.next() ) {
          count++;
  
      }
            
            c.setAutoCommit(false);
            String sql = "INSERT INTO "+RegNameCombo.getValue()+" (rowID,SubShortName,SubjectName,SubjectCode,Program,column1,Image_Reg) " +
                        "VALUES ("+count+",'"+ RegTF.getText()+"', '"+SubTF.getText()+"','"+CodeTF.getText()+"','"+progTf.getText()+"',?,?)";
            stmt = c.prepareStatement(sql);
//            stmt.executeUpdate(sql);
            
            File file = new File(ChosenImage.getText());
            FileInputStream input = new FileInputStream(file);
            stmt.setString(1,file.getName());
            // set parameters
            stmt.setBinaryStream(2, input,(int)file.length());
            System.out.println("Store file in the database.");
//            pstmt.setInt(2, rowID);
 
            // store the resume file in database
            System.out.println("Reading file " + file.getAbsolutePath());
            System.out.println("Store file in the database.");
            stmt.executeUpdate();
            list.addAll(new RegClass(count,RegTF.getText(),SubTF.getText(),CodeTF.getText(),progTf.getText(),file.getName()));
            
            rs.close();
            stmt.close();
            c.commit();
            c.close();
            input.close();
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch ( Exception e ) {
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
        RegTable.getItems().addAll(list);
    }

    @FXML
   
//    private void displaySelected(MouseEvent event) {
//        RegClass rg = RegTable.getSelectionModel().getSelectedItem();
//        int ID;
//
//            ID = rg.getRowID();
//        
//        
//        String selectSQL = "SELECT Image_Reg FROM "+RegNameCombo.getValue()+ " WHERE rowID="+ID+"";
//        ResultSet rs = null;
//        FileOutputStream fos = null;
//        Connection conn = null;
//        Statement stmt = null;
// 
//        try {
//            conn = connect();
//            stmt = conn.createStatement();
//            
//            rs = stmt.executeQuery(selectSQL);
 
            // write binary stream into file
//            File file = new File("Image_Reg");
//            fos = new FileOutputStream(file);
// 
//            System.out.println("Writing BLOB to file " + file.getAbsolutePath());
//            while (rs.next()) {
//                InputStream input = rs.getBinaryStream("picture");
//                byte[] buffer = new byte[1024];
//                while (input.read(buffer) > 0) {
//                    fos.write(buffer);
//                }
//            }

//                InputStream is =rs.getBinaryStream("Image_Reg");
//                OutputStream os = new FileOutputStream(new  File("Photo.pdf"));
//                byte[] content = new byte[1024];
//                int size = 0;
//                while((size = is.read(content)) !=-1){
//                    os.write(content,0,size);
//                }
//                
//                
//                
//        } catch (SQLException | IOException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (stmt != null) {
//                    stmt.close();
//                }
// 
//                if (conn != null) {
//                    conn.close();
//                }
//                if (fos != null) {
//                    fos.close();
//                }
// 
//            } catch (SQLException | IOException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }

//    @FXML
    private void changeTable(ActionEvent event) {
        list.removeAll(list);
        RegTable.getItems().clear();
        LoadData();
    }

    @FXML
    private void addRegulation(ActionEvent event) {
        
        ID=0;
        ID++;
        RegNameCombo.getItems().addAll(RegTextField.getText());
        Connection c = connect();
        Statement stmt = null;
        Statement stmt2 = null;
        
        try {
             stmt2 = c.createStatement();
             ResultSet rs = stmt2.executeQuery( "SELECT * FROM Regulations" );
      
     
            while ( rs.next() ) {
                ID++;
            }
            rs.close();
            stmt2.close();
  
            stmt = c.createStatement();
            String sql = "CREATE TABLE " +RegTextField.getText()+
                        "(rowID INT PRIMARY KEY NOT NULL," +
                        " SubShortName TEXT NOT NULL, " + 
                        " SubjectName TEXT NOT NULL, " +
                        " SubjectCode VARCHAR NOT NULL, "+
                        " Program VARCHAR NOT NULL,"+
                        " column1 VARCHAR,  " + 
                        " Image_Reg BLOB)"; 
            stmt.executeUpdate(sql);
         
            String sql2="INSERT INTO Regulations (ID,TableName) VALUES ("+ID+",'"+RegTextField.getText()+"')";
            stmt.executeUpdate(sql2);
         
            stmt.close();
            c.close();
        }
        catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }

    

    @FXML
    private void selectedFilePath(ActionEvent event) {
                
        selectFile = new Button("Select Image File");
        String path;
        Stage stage = (Stage) new Stage();

        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            path = file.toString();
            ChosenImage.setText(path);
               
        } else {
            ChosenImage.setText("select Regulation file");
        }
        
    }

    @FXML
    private void deleteSubjectClick(MouseEvent event) {
        
        
        RegClass rg = RegTable.getSelectionModel().getSelectedItem();
        
        rowId=rg.getRowID();
    }
    
   
    

    @FXML
    private void deleteSubjectButton(ActionEvent event) {
        
        count=0;
        
        try {
            Connection c = connect();
            Statement stmt = null;
            String sql = "DELETE FROM "+RegNameCombo.getValue()+" WHERE rowID ="+rowId;
            
            
            stmt = c.createStatement();
            
            stmt.executeUpdate(sql);
            ResultSet rs = stmt.executeQuery( "SELECT * FROM "+RegNameCombo.getValue() );
      
      
            while ( rs.next()) {
               
               Statement stmt2 = null;
//               Connection c2 = connect();
               stmt2 = c.createStatement();

                count++;
//                StringBuffer sql2 = new StringBuffer( "UPDATE Programs SET rowID = "+progCount+" WHERE rowID ="+rs.getInt("rowID"));
                stmt2.executeUpdate("UPDATE "+RegNameCombo.getValue()+" SET rowID = "+count+" WHERE rowID ="+rs.getInt("rowID"));
            }

            stmt.close();
            
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(RegulationsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        RegTable.getItems().clear();
//        ProgramsTable.getItems().addAll(programsList);
        LoadData();
//            initiatecolsPrograms();
    }

    @FXML
    private void updateRow(ActionEvent event) {
        
        Connection c = connect();
        PreparedStatement update = null;
        String sql;
        if(ChosenImage.getText().equals(fileNameUpdate)){
    
        try {
            update = c.prepareStatement("UPDATE "+RegNameCombo.getValue()+" SET SubShortName=?, SubjectName=?, SubjectCode=?,Program=? WHERE rowID ="+rowId);
            
            update.setString(1,RegTF.getText());
            update.setString(2,SubTF.getText());
            update.setString(3,CodeTF.getText());
            update.setString(4,progTf.getText());
            
            update.executeUpdate();
            
            update.close();
            c.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
        else{
            
            try {
                update = c.prepareStatement("UPDATE "+RegNameCombo.getValue()+" SET SubShortName=?, SubjectName=?, SubjectCode=?,Program=?,column1=?,Image_Reg=? WHERE rowID ="+rowId);
                
                update.setString(1,RegTF.getText());
                update.setString(2,SubTF.getText());
                update.setString(3,CodeTF.getText());
                update.setString(4,progTf.getText());
                
                File file = new File(ChosenImage.getText());
                FileInputStream input = new FileInputStream(file);
                update.setString(5,file.getName());
                // set parameters
                update.setBinaryStream(6, input,(int)file.length());
                System.out.println("Store file in the database.");
                
                 update.executeUpdate();
            
            update.close();
            c.close();
                
                
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        RegTable.getItems().clear();
        LoadData();   
    }

    @FXML
    private void DeleteRegulation(ActionEvent event) {
        
        ID=0;
         
        try{
            int id;
            Connection c = connect();
            PreparedStatement stmt = null;
            PreparedStatement stmt2 = null;
            stmt2 = c.prepareStatement("SELECT * FROM Regulations WHERE TableName=?");
            Statement stmt3 = c.createStatement();
            
//            String sql = "DELETE FROM regulationTables WHERE rowid =?";
//            
            stmt2.setString(1,RegTextField.getText());
//            stmt = c.prepareStatement(sql);
//            
            ResultSet rs = stmt2.executeQuery();
//            String Reg;
            while ( rs.next() ) {
        
                id = rs.getInt("ID");
                System.out.println(id+"hi");
                System.out.println("hi");
                String sql = "DELETE FROM Regulations WHERE TableName=?";
                stmt = c.prepareStatement(sql);
                stmt.setString(1,RegTextField.getText());
                stmt.executeUpdate();
            }
            rs.close();
            
            String drop = "DROP TABLE "+RegTextField.getText();
            stmt = c.prepareStatement(drop);
            stmt.executeUpdate();
            
            
            stmt.close();
            
            ResultSet rs2 = stmt3.executeQuery( "SELECT * FROM Regulations" );
            while ( rs2.next()) {
           
                Statement stmt4 = null;
//               Connection c2 = connect();
                stmt4 = c.createStatement();
//                StringBuffer sql2 = new StringBuffer( "UPDATE Programs SET rowID = "+progCount+" WHERE rowID ="+rs.getInt("rowID"));
                stmt4.executeUpdate("UPDATE Regulations SET ID = "+ID+" WHERE ID ="+rs2.getInt("ID"));
                ID++;
                stmt4.close();
            }
            rs2.close();
            stmt3.close();
            stmt2.close();
            
            c.close();
            
            } catch (SQLException ex) {
            Logger.getLogger(DatabaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        RegNameCombo.getItems().remove(RegTextField.getText());
    }
    
   

   
   
    
    public class RegClass{
         private final SimpleStringProperty Regulation;
         private final SimpleStringProperty program;
         private final SimpleStringProperty SubjectName;
         private  SimpleStringProperty SubjectCode;
         private  SimpleStringProperty fileName;
         private  SimpleIntegerProperty RowID;

       

         

        public RegClass(int RowID,String Regulation, String SubjectName, String SubjectCode,String program, String fileName) {
            this.RowID= new SimpleIntegerProperty(RowID);
            this.SubjectName= new SimpleStringProperty(SubjectName);
            this.SubjectCode= new SimpleStringProperty(SubjectCode);
            this.fileName = new SimpleStringProperty(fileName);
            this.program = new SimpleStringProperty(program);
            this.Regulation = new SimpleStringProperty(Regulation);
        }

         public String getProgram() {
            return program.get();
        }
         public String getFileName() {
            return fileName.get();
        }

        public String getRegulation() {
            return Regulation.get();
        }

        public String getSubjectName() {
            return SubjectName.get();
        }

        public String getSubjectCode() {
            return SubjectCode.get();
        }

        public Integer getRowID() {
            return RowID.get();
        }

         
    }
    
    private Connection connect() {
        // SQLite connection string
        Connection c = null;
       
        try {
            c = DriverManager.getConnection("jdbc:sqlite:blobdb.db");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }

    
    
}
