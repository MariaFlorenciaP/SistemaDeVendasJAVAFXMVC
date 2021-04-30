
package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Produto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;


public class FXMLAnchorPaneRelatoriosQuantidadeProdutosController implements Initializable {

         
        @FXML
        private TableView<Produto> tableViewProdutos;
        @FXML
        private TableColumn<Produto, Integer> tableColumnProdutoCodigo;
        @FXML
        private TableColumn<Produto, String> tableColumnProdutoNome;
        @FXML
        private TableColumn<Produto, Double> tableColumnProdutoPreco;
        @FXML
        private TableColumn<Produto, Integer> tableColumnProdutoQuantidade;
        @FXML
        private TableColumn<Produto, Categoria> tableColumnProdutoCategoria;
        @FXML
        private Button buttonImprimir;
        
        private List<Produto> listProdutos;
        private ObservableList<Produto> observableListProdutos;
        
        //Atributos para manipulação de Banco de Dados
        private final Database database = DatabaseFactory.getDatabase("mysql");
        private final Connection connection = database.conectar();
        private final ProdutoDAO produtoDAO = new ProdutoDAO();
        
        
        
         @Override
         public void initialize(URL url, ResourceBundle rb) {
         produtoDAO.setConnection(connection);
         carregarTableViewProdutos();
         
        
        }    
         
        public void carregarTableViewProdutos(){
            tableColumnProdutoCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
            tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
            tableColumnProdutoPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
            tableColumnProdutoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
            tableColumnProdutoCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
            
            listProdutos = produtoDAO.listar();
            
            observableListProdutos = FXCollections.observableArrayList(listProdutos);
            tableViewProdutos.setItems(observableListProdutos);
             
                       
        } 
        
        public void handleImprimir() throws JRException{
            URL url = getClass().getResource("/javafxmvc/relatorios/JAVAFXMVCRelatorioProdutos.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(url);
            
            //null: caso não existam filtros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, connection);
            
            //false: não deixa fechar a aplicação principal
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
            
        }
    
}
