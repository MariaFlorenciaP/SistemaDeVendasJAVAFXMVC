
package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;


public class FXMLAnchorPaneCadastrosProdutosDialogController implements Initializable {

    @FXML
    private Label labelProdutosNome;
    @FXML
    private Label labelProdutosQuantidade;
    @FXML
    private Label labelProdutosPreco;
    @FXML
    private Label labelProdutosCategoria;
    @FXML
    private TextField textFieldProdutosNome;
    @FXML
    private TextField textFieldProdutosQuantidade;
    @FXML
    private TextField textFieldProdutosPreco;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private ComboBox<Categoria> comboBoxProdutosCategoria;
    
    private List<Categoria> listaCategorias;
    private ObservableList<Categoria> observableListCategorias;
    
    //atributos para manipulação do banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarComboBoxCategorias();
    }    
    
    public void carregarComboBoxCategorias(){
        listaCategorias = categoriaDAO.listar();
        observableListCategorias = FXCollections.observableArrayList(listaCategorias);
        comboBoxProdutosCategoria.setItems(observableListCategorias);
    }
    
     public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        textFieldProdutosNome.setText(produto.getNome());
        textFieldProdutosQuantidade.setText(Integer.toString(produto.getQuantidade()));
        textFieldProdutosPreco.setText(Double.toString(produto.getPreco()));
        comboBoxProdutosCategoria.getSelectionModel().select(produto.getCategoria());
    }

    @FXML
    public void handleButtonConfirmar() { 

        if (validarEntradaDeDados()) {

            produto.setNome(textFieldProdutosNome.getText());
            produto.setQuantidade(Integer.parseInt(textFieldProdutosQuantidade.getText()));
            produto.setPreco(Double.parseDouble(textFieldProdutosPreco.getText()));
            produto.setCategoria(comboBoxProdutosCategoria.getSelectionModel().getSelectedItem());

            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        dialogStage.close();
    }

    //Validar entrada de dados para o cadastro   ///precisa disto?
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldProdutosNome.getText() == null || textFieldProdutosNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }

        if (textFieldProdutosQuantidade.getText() == null || textFieldProdutosQuantidade.getText().length() == 0) {
            errorMessage += "Quantidade inválida!\n";
        }

        if (textFieldProdutosPreco.getText() == null || textFieldProdutosPreco.getText().length() == 0) {
            errorMessage += "Preco inválido!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            //Mostrando mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija....");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }

}
    

