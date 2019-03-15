package netflix.simulation.entities.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.Random;

public class ReadJSONFile {

    public static JSONObject parseFile(String file) {

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(file));
            JSONArray jsonArray = (JSONArray) obj;
            //size 2683 records
            return (JSONObject) jsonArray.get(new Random().nextInt(jsonArray.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
