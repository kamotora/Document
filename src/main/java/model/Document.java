package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Document {

    private static final String headerFile = "/data/header.txt";
    private static final String organizationsFile = "/data/organizations.txt";
    private static final String unitsFile = "/data/units.txt";
    private static final String postsFile = "/data/posts.txt";

    public static final int maxRows = 32;
    public static final int maxFrontSideRows = 10;

    /*  Шапка документа */
    private ArrayList <Organization> organizations;
    private String  number;
    private String date;
    private final String OCUD = "0330516";
    private String OCPO;
    private String OCDP;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    /*  Подвал документа  */
    private ArrayList <Post> responsiblePosts;
    private ArrayList <Post> checkingPosts;

    private ArrayList <Product> products;
    private Total total;
    private Organization currentOrganization;
    private String currentUnit;
    private Post currentResponsiblePost;
    private Post currentCheckingPost;

    private String currentResponsibleFace;
    private String currentCheckingFace;

    private static Document instance = null;

    private Document() {
        this.total = new Total();
    }

    public static Document getInstance(){
        if(instance == null)
            instance = new Document();
        return instance;
    }
    public void add(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }

        this.products.add(product);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public LocalDate getDateInLocalDate() {
        return LocalDate.parse(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOCUD() {
        return OCUD;
    }

    public String getOCPO() {
        return OCPO;
    }

    public void setOCPO(String OCPO) {
        this.OCPO = OCPO;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public ArrayList<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(ArrayList<Organization> organizations) {
        this.organizations = organizations;
    }

    public ArrayList<Post> getResponsiblePosts() {
        return responsiblePosts;
    }

    public void setResponsiblePosts(ArrayList<Post> responsiblePosts) {
        this.responsiblePosts = responsiblePosts;
    }

    public ArrayList<Post> getCheckingPosts() {
        return checkingPosts;
    }

    public void setCheckingPosts(ArrayList<Post> checkingPosts) {
        this.checkingPosts = checkingPosts;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }

    public Organization getCurrentOrganization() {
        return currentOrganization;
    }

    public void setCurrentOrganization(Organization currentOrganization) {
        this.currentOrganization = currentOrganization;
        this.OCPO = currentOrganization.getOKPO();
        this.OCDP = currentOrganization.getOKDP();
    }

    public String getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(String currentUnit) {
        this.currentUnit = currentUnit;
    }

    public Post getCurrentResponsiblePost() {
        return currentResponsiblePost;
    }

    public void setCurrentResponsiblePost(Post currentResponsiblePost) {
        this.currentResponsiblePost = currentResponsiblePost;
    }

    public Post getCurrentCheckingPost() {
        return currentCheckingPost;
    }

    public void setCurrentCheckingPost(Post currentCheckingPost) {
        this.currentCheckingPost = currentCheckingPost;
    }

    public String getCurrentResponsibleFace() {
        return currentResponsibleFace;
    }

    public void setCurrentResponsibleFace(String currentResponsibleFace) {
        this.currentResponsibleFace = currentResponsibleFace;
    }

    public String getCurrentCheckingFace() {
        return currentCheckingFace;
    }

    public void setCurrentCheckingFace(String currentCheckingFace) {
        this.currentCheckingFace = currentCheckingFace;
    }

    public String getOCDP() {
        return OCDP;
    }

    public void setOCDP(String OCDP) {
        this.OCDP = OCDP;
    }
}
