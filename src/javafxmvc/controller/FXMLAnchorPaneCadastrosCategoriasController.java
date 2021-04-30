
package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;


public class FXMLAnchorPaneCadastrosCategoriasController implements Initializable {

    @FXML
    private TableView<Categoria> tableViewCategorias;
    @FXML
    private TableColumn<Categoria, String> tableColumnCategoriasDescricao;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelCategoriasCodigo;
    @FXML
    private Label labelCategoriasDescricao;
    
    
    
    private List<Categoria> listCategorias;
    private ObservableList<Categoria> observableListCategorias;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     categoriaDAO.setConnection(connection);
        
        carregarTableViewCategoria();
        
        //Listener acionado diante de quaisquer alterações na seleção de itens do Table View
        tableViewCategorias.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selecionarItemTableViewCategorias(newValue));
    } 
    
    
    public void carregarTableViewCategoria(){
       
        tableColumnCategoriasDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao")); 
        
        
        listCategorias = categoriaDAO.listar();
        
        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        tableViewCategorias.setItems(observableListCategorias);
    }
    
    public void selecionarItemTableViewCategorias(Categoria categoria){
        if(categoria != null){
            
        labelCategoriasCodigo.setText(String.valueOf(categoria.getCdCategoria()));
        labelCategoriasDescricao.setText(categoria.getDescricao());
                
        } else{
            
        labelCategoriasCodigo.setText("");
        labelCategoriasDescricao.setText("");
        
            
        }
        
    }
   
    @FXML
    public void handleButtonInserir() throws IOException {
        Categoria categoria = new Categoria();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
        if(buttonConfirmarClicked){
            categoriaDAO.inserir(categoria);
            carregarTableViewCategoria();
        }
        
    }
    
    @FXML
    public void handleButtonAlterar() throws IOException{
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if(categoria != null){
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
            if(buttonConfirmarClicked){
                categoriaDAO.alterar(categoria);
                carregarTableViewCategoria();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na Tabela!");
            alert.show();
        }
    }
    
    @FXML
    public void handleButtonRemover() throws IOException {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if(categoria != null){
            categoriaDAO.remover(categoria);
            carregarTableViewCategoria();
            
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na Tabela!");
            alert.show();
        }
        
    }
    
   public boolean showFXMLAnchorPaneCadastrosCategoriasDialog(Categoria categoria) throws IOException{
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(FXMLAnchorPaneCadastrosCategoriasDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosCategoriasDialog.fxml"));
       AnchorPane page = (AnchorPane) loader.load();  
       
       //Criando um estágio de diálogo (Stage Dialog)
       Stage dialogStage = new Stage();
       dialogStage.setTitle("Cadastro de Categorias");
       Scene scene = new Scene(page);
       dialogStage.setScene(scene);
       
       //Setando a categoria no Controller
       FXMLAnchorPaneCadastrosCategoriasDialogController controller = loader.getController();
       controller.setDialogStage(dialogStage);
       controller.setCategoria(categoria);
       
       //Mostra o Dialog e espera ate que o usuario feche
       dialogStage.showAndWait();
       
       return controller.isButtonConfirmarClicked();
       
   } 
   
  
}