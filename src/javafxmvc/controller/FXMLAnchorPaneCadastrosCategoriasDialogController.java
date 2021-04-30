
package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Categoria;


public class FXMLAnchorPaneCadastrosCategoriasDialogController implements Initializable {

    
    
    @FXML
    private Label labelCategoriasDescricao;
   
    @FXML
    private TextField textFieldCategoriasDescricao;
    
    @FXML
    private Button buttonConfirmar;
    
    @FXML
    private Button buttonCancelar;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Categoria categoria;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        this.textFieldCategoriasDescricao.setText(categoria.getDescricao());
        
    }

    @FXML
    public void handleButtonConfirmar() {

        if (validarEntradaDeDados()) {

            categoria.setDescricao(textFieldCategoriasDescricao.getText());
            
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar() {
        dialogStage.close();
    }

    //Validar entrada de dados para o cadastro
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldCategoriasDescricao.getText() == null || textFieldCategoriasDescricao.getText().length() == 0) {
            errorMessage += "Categoria inválida!\n";
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