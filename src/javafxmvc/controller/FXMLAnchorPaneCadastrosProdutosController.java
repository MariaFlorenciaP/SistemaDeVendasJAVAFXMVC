
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
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Produto;


public class FXMLAnchorPaneCadastrosProdutosController implements Initializable {

     @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutosNome;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutosQuantidade;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    @FXML
    private Label labelProdutosCodigo;
    @FXML
    private Label labelProdutosNome;
    @FXML
    private Label labelProdutosPreco;
    @FXML
    private Label labelProdutosQuantidade;
    @FXML
    private Label labelProdutosCategoria;
    
    
    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    produtoDAO.setConnection(connection);
        
        carregarTableViewProduto();
        
        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
        (observable, oldValue, newValue) -> selecionarItemTableViewProdutos(newValue));
    } 
    
    
    public void carregarTableViewProduto(){
       
        tableColumnProdutosNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnProdutosQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        
        listProdutos = produtoDAO.listar();
        
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        tableViewProdutos.setItems(observableListProdutos);
    }
    
    public void selecionarItemTableViewProdutos(Produto produto){
        if(produto != null){
            
        labelProdutosCodigo.setText(String.valueOf(produto.getCdProduto()));
        labelProdutosNome.setText(produto.getNome());
        labelProdutosPreco.setText(String.valueOf(produto.getPreco()));
        labelProdutosQuantidade.setText(String.valueOf(produto.getQuantidade()));
        labelProdutosCategoria.setText(produto.getCategoria().toString());
        
        } else{
            
        labelProdutosCodigo.setText("");
        labelProdutosNome.setText("");
        labelProdutosPreco.setText("");
        labelProdutosQuantidade.setText("");
        labelProdutosCategoria.setText("");
            
        }
        
    }
   
    @FXML
    public void handleButtonInserir() throws IOException {
        Produto produto = new Produto();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
        if(buttonConfirmarClicked){
            produtoDAO.inserir(produto);
            carregarTableViewProduto();
        }
        
    }
    
    @FXML
    public void handleButtonAlterar() throws IOException{
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if(produto != null){
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
            if(buttonConfirmarClicked){
                produtoDAO.alterar(produto);
                carregarTableViewProduto();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela!");
            alert.show();
        }
    }
    
    @FXML
    public void handleButtonRemover() throws IOException {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if(produto != null){
            produtoDAO.remover(produto);
            carregarTableViewProduto();
            
        } else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela!");
            alert.show();
        }
        
    }
    
   public boolean showFXMLAnchorPaneCadastrosProdutosDialog(Produto produto) throws IOException{
       FXMLLoader loader = new FXMLLoader();
       loader.setLocation(FXMLAnchorPaneCadastrosProdutosDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastrosProdutosDialog.fxml"));
       AnchorPane page = (AnchorPane) loader.load();  
       
       //Criando um estágio de diálogo (Stage Dialog)
       Stage dialogStage = new Stage();
       dialogStage.setTitle("Cadastro de Produtos");
       Scene scene = new Scene(page);
       dialogStage.setScene(scene);
       
       //Setando o produto no Controller
       FXMLAnchorPaneCadastrosProdutosDialogController controller = loader.getController();
       controller.setDialogStage(dialogStage);
       controller.setProduto(produto);
       
       //Mostra o Dialog e espera ate que o usuario feche
       dialogStage.showAndWait();
       
       return controller.isButtonConfirmarClicked();
       
   } 
   
  
}