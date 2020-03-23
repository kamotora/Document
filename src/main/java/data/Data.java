package data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.util.Pair;
import model.Document;
import model.Organization;
import model.Post;
import model.Product;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Data {

    private static final String productsFile = "/data/products.txt";
    private static final String measuresFile = "/data/measures.txt";
    private static final String headerFile = "/data/header.txt";
    private static final String organizationsFile = "/data/organizations.txt";
    private static final String unitsFile = "/data/units.txt";
    private static final String postsFile = "/data/posts.txt";
    //Creating the ObjectMapper object
    ObjectMapper mapper = new ObjectMapper();

    public Pair <HashMap <String, String>, HashMap <String, String>> getCodes() {
        String[] position;
        HashMap <String, String> productCodes = new HashMap<>();
        HashMap <String, String> measuresCodes = new HashMap<>();

        try {
            FileInputStream stream = new FileInputStream(Product.class.getResource(productsFile).getPath());
            BufferedReader bufferedReader = new BufferedReader
                    (new InputStreamReader(stream));
            String line;

            while((line = bufferedReader.readLine()) != null ) {
                position = line.split("#");
                productCodes.put(position[0], position[1]);
            }

            stream = new FileInputStream(Product.class.getResource(measuresFile).getPath());
            bufferedReader = new BufferedReader(new InputStreamReader(stream));

            while ((line = bufferedReader.readLine()) != null) {
                position = line.split("#");
                measuresCodes.put(position[0], position[1]);
            }
        }

        catch (IOException exception) {
            exception.printStackTrace();
        }

        return new Pair<> (productCodes, measuresCodes);
    }

    /**
     * Создаём объекты для теста
     * */
    public void creatingObjects() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(Data.class.getResource(Organization.FILE).getPath()));
        String str = mapper.writeValueAsString(new Organization("ИП Иванов И.И.", "02842708", "56.10.1", Arrays.asList("Склад", "Кухня")));
        writer.append(str);
        writer.append('\n');
        writer.append(mapper.writeValueAsString(new Organization("ООО 'Жираф'", "02842709", "56.10.2", Arrays.asList("Горячий цех", "Холодный цех", "Кладовая"))));
        writer.append('\n');
        writer.append(mapper.writeValueAsString(new Organization("ООО 'Маняшины разносолы'", "02842710", "56.9.1", Arrays.asList("Цех 1", "Цех 2", "Цех 3"))));
        writer.append('\n');
        writer.close();

        writer = new BufferedWriter(new FileWriter(Data.class.getResource(Post.RESP_FILE).getPath()));
            writer.append(mapper.writeValueAsString(new Post("Кладовщик")));
        writer.append('\n');
        writer.append(mapper.writeValueAsString(new Post("Администратор")));
        writer.append('\n');
        writer.append(mapper.writeValueAsString(new Post("Старший администратор")));
        writer.append('\n');
        writer.close();


        writer = new BufferedWriter(new FileWriter(Data.class.getResource(Post.CHECKING_FILE).getPath()));
        writer.append(mapper.writeValueAsString(new Post("Директор")));
        writer.append('\n');
        writer.append(mapper.writeValueAsString(new Post("Зам.директора")));
        writer.append('\n');
        writer.append(mapper.writeValueAsString(new Post("Бухгалтер")));
        writer.append('\n');
        writer.close();
    }


    public void getHeader(Document document) {

        ArrayList <Organization> organizations = new ArrayList <>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(Data.class.getResource(Organization.FILE).getPath()), StandardCharsets.UTF_8);
            for(String line :lines){
                Organization organization = mapper.readValue(line, Organization.class);
                if(organization != null)
                    organizations.add(organization);
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        document.setOrganizations(organizations);
    }

    public void getFooter(Document document) {
        ArrayList <Post> respPosts = new ArrayList <>();
        ArrayList <Post> checkPosts = new ArrayList <>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(Data.class.getResource(Post.RESP_FILE).getPath()), StandardCharsets.UTF_8);

            for(String line :lines) {
                Post post = mapper.readValue(line, Post.class);
                if (post != null)
                    respPosts.add(post);
            }
            lines = Files.readAllLines(Paths.get(Data.class.getResource(Post.CHECKING_FILE).getPath()), StandardCharsets.UTF_8);
            for(String line :lines) {
                Post post = mapper.readValue(line, Post.class);
                if (post != null)
                    checkPosts.add(post);
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        document.setResponsiblePosts(respPosts);
        document.setCheckingPosts(checkPosts);
    }
}
