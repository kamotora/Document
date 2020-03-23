package scene;

import data.Data;
import data.Excel;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import model.Document;
import model.Organization;
import model.Product;
import model.Total;
import org.controlsfx.control.PrefixSelectionComboBox;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Controller {

    private static final int mainTableColumnWidth = 135;
    private static final int costTableColumnWidth = 200;
    public Label OCDP;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static Document document;

    /*  Шапка документа  */
    @FXML
    private Label title;


    @FXML
    private Label valueOCPO;

    @FXML
    private PrefixSelectionComboBox<Organization> organization;

    @FXML
    private PrefixSelectionComboBox <String> unit;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private DatePicker dateTo;

    /*  Поля таблицы  */
    @FXML
    private TableView <Product> mainTable;

    @FXML
    private TableColumn <Product, Integer> number;

    @FXML
    private TableColumn <Product, String> name;

    @FXML
    private TableColumn <Product, String> productCode;

    @FXML
    private TableColumn <Product, String> measures;

    @FXML
    private TableColumn <Product, String> measuresCode;

    @FXML
    private TableColumn <Product, Double> cost;

    @FXML
    private TableView <Total> costTable;

    @FXML
    private TextField numDocument;

    @FXML
    private DatePicker dateDocument;

    @FXML
    private Button saveButton;

    private ArrayList <TableColumn <Product, Pair <TableColumn <Product, Integer>,
            TableColumn <Product, Double>>>> remains;
    private ArrayList <TableColumn <Total, Double>> totals;
    @FXML
    public void initialize() {
        mainTable.setPlaceholder(new Label("Для начала работы установите отчётный период"));
        costTable.setPlaceholder(new Label("Для начала работы установите отчётный период"));
        document = Document.getInstance();

        remains = new ArrayList <>();
        totals = new ArrayList <>();

        Data data = new Data();

        data.getHeader(document);

        organization.getItems().addAll(document.getOrganizations());
        organization.setValue(document.getOrganizations().get(0));
        organizationSelecting();


        setDate();

        numDocument.setText(document.getNumber() );
        numDocument.focusedProperty().addListener((obsVal, isFocusOld, isFocusNew) -> {
            //Если фокус пропал - закончили редактировать
            if(!isFocusNew)
                document.setNumber(numDocument.getText());
        });
        dateDocument.setValue(LocalDate.now());
        valueOCPO.setText(document.getOCPO());

        initMainTable();
    }

    @FXML
    public void organizationSelecting() {
        document.setCurrentOrganization(this.organization.getSelectionModel().getSelectedItem());
        unit.getItems().clear();
        unit.getItems().addAll(document.getCurrentOrganization().getUnits());
        OCDP.setText(document.getOCDP());
        valueOCPO.setText(document.getOCPO());
    }

    @FXML
    public void unitSelecting() {
        document.setCurrentUnit(this.unit.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void dateFromAction() {
        document.setDateFrom(dateFrom.getValue());

        if (dateFrom.getValue() != null && dateTo.getValue() != null) {
            countMonthDifference();

            for (Product product : document.getProducts()) {
                setRemains(product);
            }

            setSum();
        }
    }

    @FXML
    public void dateToAction() {
        document.setDateTo(dateTo.getValue());
        if (dateFrom.getValue() != null && dateTo.getValue() != null) {
            if (countMonthDifference()) {
                ObservableList<Product> list = mainTable.getItems();
                if (list.isEmpty()) {
                    addRow();
                }
                else {
                    for (Product product : document.getProducts()) {
                        setRemains(product);
                    }
                    setSum();
                }
            }
        }
    }

    private void initMainTable() {
        number.setCellValueFactory(new PropertyValueFactory<>("position"));
        name.setCellValueFactory(new PropertyValueFactory<>("title"));
        productCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        measures.setCellValueFactory(new PropertyValueFactory<>("measures"));
        measuresCode.setCellValueFactory(new PropertyValueFactory<>("OKEI"));
        cost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        initMainTableEditableColumns();
    }

    private void initMainTableEditableColumns() {
        number.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        number.setOnEditCommit(event ->
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setPosition(event.getNewValue()));

        createComboBoxCell(name, Product.getProductCodes());


        productCode.setCellFactory(TextFieldTableCell.forTableColumn());
        productCode.setEditable(false);

        createComboBoxCell(measures, Product.getMeasuresCodes());

        measuresCode.setCellFactory(TextFieldTableCell.forTableColumn());
        measuresCode.setEditable(false);

        cost.setCellFactory(new Callback<>() {

            @Override
            public TableCell <Product, Double> call(TableColumn <Product, Double> parameters) {

                final TextField field = new TextField();
                field.setMaxWidth(cost.getMaxWidth());
                field.setPrefWidth(cost.getWidth());

                TableCell <Product, Double> cell = new TableCell<>() {

                    @Override
                    protected void updateItem(Double reason, boolean empty) {

                        super.updateItem(reason, empty);

                        if (empty) {
                            setGraphic(null);
                        }

                        else {
                            field.setText(Double.toString(reason));
                            setGraphic(field);
                        }
                    }
                };

                field.focusedProperty().addListener((observableValue, isFocusedOld, isFocusedNew) -> {
                    if(isFocusedNew)
                        return;
                    Product product = mainTable.getItems().get(cell.getIndex());
                    product.setCost(Double.parseDouble(field.getText()));
                    mainTable.getItems().get(cell.getIndex()).setRemainsSum(product.getCost());
                    mainTable.refresh();

                    Total total = costTable.getItems().get(0);

                    if (cell.getIndex() < Document.maxFrontSideRows) {
                        total.setSideSum(product.getCost(), document.getProducts(), product);
                    }

                    costTable.getItems().get(0).setSum(product.getCost(), document.getProducts(), product);
                    costTable.refresh();
                });

                return cell;
            }
        });

        mainTable.setEditable(true);
        costTable.setEditable(false);
    }

    private void createComboBoxCell(TableColumn <Product, String> column, HashMap <String, String> codes) {
        ObservableList <String> items = FXCollections.observableArrayList(codes.keySet());
        column.setCellFactory(new Callback <>() {

            @Override
            public TableCell <Product, String> call(TableColumn <Product, String> parameters) {

                final PrefixSelectionComboBox <String> comboBox = new PrefixSelectionComboBox<>();
                comboBox.setItems(items);
                comboBox.setMaxWidth(column.getMaxWidth());
                comboBox.setPrefWidth(column.getWidth());

                TableCell <Product, String> cell = new TableCell <>() {

                    @Override
                    protected void updateItem(String reason, boolean empty) {

                        super.updateItem(reason, empty);

                        if (empty) {
                            setGraphic(null);
                        }

                        else {
                            comboBox.setValue(reason);
                            setGraphic(comboBox);
                        }
                    }
                };

                comboBox.setOnAction(event -> {
                    Product product = mainTable.getItems().get(cell.getIndex());

                    if (column.getText().equals("Наименование товара")) {
                        product.setTitle(comboBox.getSelectionModel().getSelectedItem());
                        mainTable.getItems().get(cell.getIndex())
                                .setCode(codes.get(product.getTitle()));
                        addRow();
                    }

                    else {
                        product.setMeasures(comboBox.getSelectionModel().getSelectedItem());
                        mainTable.getItems().get(cell.getIndex())
                                .setOKEI(codes.get(product.getMeasures()));
                    }

                    mainTable.refresh();
                });

                return cell;
            }
        });
    }

    private void addRow() {
        if (Product.counter == 1) {
            Product product = new Product(Product.counter++);
            Total total = new Total();
            setRemains(product);
            setSum();
            document.add(product);
            this.mainTable.getItems().add(product);
            this.costTable.getItems().add(total);
        }

        else if (Product.counter < Document.maxRows &&
                this.mainTable.getItems().get(this.mainTable.getItems().size() - 1).getTitle() != null) {
            Product product = new Product(Product.counter++);
            setRemains(product);
            document.add(product);
            this.mainTable.getItems().add(product);
        }
    }

    private void setRemains(Product product) {
        TreeMap <String, Pair <Integer, Double>> remains = new TreeMap <>();
        int index = 0;

        for (TableColumn <Product, Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>>>
                column : this.remains) {
            remains.put(index + ", " + column.getText().substring(11), new Pair <>(0, 0.0));
            index++;
        }

        product.setRemains(remains);
    }

    private void setSum() {
        ArrayList <Double> totals = new ArrayList <>();

        while (totals.size() != this.totals.size()) {
            totals.add(0.0);
        }

        Total.setSum(totals);
        Total.setSideSum(totals);
    }

    private boolean countMonthDifference() {
        long difference = ChronoUnit.MONTHS.between(dateFrom.getValue(),
                dateTo.getValue());
        if (difference < 1) {
            Msg.showErrorWindow("Минимальный выбранный период должен составлять 1 месяц");
            return false;
        }

        else if (difference >= 5) {
            Msg.showErrorWindow("Максимальный выбранный период должен составлять 5 месяцев");
            return false;
        }

        else {
            addRemainsColumns(difference);
            return true;
        }

    }

    private void addRemainsColumns(long difference) {
        mainTable.getColumns().removeAll(remains);
        costTable.getColumns().removeAll(totals);

        remains.clear();
        totals.clear();

        LocalDate date = dateFrom.getValue();

        for (int i = 0; i <= difference; i++) {
            int index = i;

            //TODO добавить в заголовок редактирование даты
            String headerDate = getDate(date);

            TableColumn <Product,
                    Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>>> mainTableColumn =
                    new TableColumn <>("Остатки на " + headerDate);
            mainTableColumn.setPrefWidth(mainTableColumnWidth);
            mainTableColumn.setResizable(false);

            String key = index + ", " + headerDate;

            Pair <TableColumn <Product, Integer>, TableColumn <Product, Double>> columnPair =
                    new Pair <>(new TableColumn <>("Количество"), new TableColumn <>("Сумма"));

            TableColumn <Product, Integer> remainsCount = columnPair.getKey();
            remainsCount.setCellValueFactory(
                    productIntegerCellDataFeatures -> new SimpleIntegerProperty(productIntegerCellDataFeatures
                            .getValue().getRemains().get(key).getKey()).asObject());

            remainsCount.setCellFactory(new Callback<>() {

                @Override
                public TableCell <Product, Integer> call(TableColumn <Product, Integer> parameters) {

                    final int[] oldCount = new int[1];
                    final TextField field = new TextField();
                    field.setMaxWidth(cost.getMaxWidth());
                    field.setPrefWidth(cost.getWidth());

                    TableCell <Product, Integer> cell = new TableCell<>() {

                        @Override
                        protected void updateItem(Integer reason, boolean empty) {

                            super.updateItem(reason, empty);

                            if (empty) {
                                setGraphic(null);
                            }

                            else {
                                field.setText(Integer.toString(reason));
                                setGraphic(field);
                                oldCount[0] = reason;
                            }
                        }
                    };

                    field.focusedProperty().addListener((observableValue, isFocusedOld, isFocusedNew) -> {
                        if(isFocusedNew)
                            return;
                        ObservableList<Product> products = mainTable.getItems();
                        if(products == null || products.isEmpty())
                            return;
                        int kek = cell.getIndex();
                        if(kek < 0){
                            System.out.println("lolkek");
                            return;
                        }
                        Product product = products.get(kek);
                        product.setRemainsCount(key, Integer.parseInt(field.getText()));
                        mainTable.getItems().get(cell.getIndex()).setRemainsOneSum(key, product.getCost() *
                                Integer.parseInt(field.getText()));
                        mainTable.refresh();

                        Total total = costTable.getItems().get(0);
                        double cost = product.getCost();
                        double oldValue = oldCount[0] * cost;
                        double newValue = Double.parseDouble(field.getText()) * cost;
                        double sum = total.getOneSum(index);
                        double sideSum = total.getOneSideSum(index);

                        if (document.getProducts().size() < Document.maxFrontSideRows) {
                            total.setOneSideSum(sideSum - oldValue + newValue, index);
                        }

                        costTable.getItems().get(0).setOneSum(sum - oldValue + newValue, index);
                        costTable.refresh();

                    });

                    return cell;
                }
            });

            TableColumn <Product, Double> remainsSum = columnPair.getValue();
            remainsSum.setCellValueFactory(
                    productIntegerCellDataFeatures -> new SimpleDoubleProperty(productIntegerCellDataFeatures
                            .getValue().getRemains().get(key).getValue()).asObject());
            remainsSum.setEditable(false);

            mainTableColumn.getColumns().add(remainsCount);
            mainTableColumn.getColumns().add(remainsSum);

            remains.add(mainTableColumn);

            TableColumn <Total, Double> costTableColumn =
                    new TableColumn<>("На " + headerDate);
            costTableColumn.setPrefWidth(costTableColumnWidth);
            costTableColumn.setResizable(false);
            costTableColumn.setCellValueFactory(productIntegerCellDataFeatures
                    -> new SimpleDoubleProperty(productIntegerCellDataFeatures
                    .getValue().getSum().get(index)).asObject());
            costTableColumn.setEditable(false);
            totals.add(costTableColumn);

            date = date.plusMonths(1);

            if (date.getDayOfMonth() == 30 && date.plusDays(1).getDayOfMonth() == 31) {
                date = date.plusDays(1);
            }

            else if (date.getDayOfMonth() == 28) {
                date = date.plusDays(3);
            }

            else if (date.getDayOfMonth() == 29) {
                date = date.plusDays(2);
            }

        }

        mainTable.getColumns().addAll(remains);
        costTable.getColumns().addAll(totals);
    }

    private String getDate(LocalDate date) {
        return date.format(formatter);
    }

    private void setDate() {
        document.setDate(LocalDate.now().format(formatter));
    }

    private static Document getDocument() {
        return document;
    }


    public void save() {
        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Файл Microsoft Excel",
                "*.xlsx");
        fileChooser.getExtensionFilters().add(filter);
        fileChooser.setInitialFileName("table.xlsx");

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            Excel excel = new Excel(Controller.getDocument());
            excel.setOutput(file);
            excel.write();
        }
    }

    public void showPeoples(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(Controller.class.getResource("/peoples.fxml"));
        Stage curWindow = new Stage();
        Parent root;
        try{
            root = loader.load();
        }catch (IOException ex){
            Msg.showErrorWindow("Не получилось открыть новое окно");
            ex.printStackTrace();
            return;
        }
        curWindow.setTitle("Ответстввенные лица");
        curWindow.setScene(new Scene(root));
        curWindow.initModality(Modality.WINDOW_MODAL);
        curWindow.showAndWait();
    }
}