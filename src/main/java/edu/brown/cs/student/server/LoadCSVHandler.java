package edu.brown.cs.student.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.parse.CSVParser;
import edu.brown.cs.student.search.SearchObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadCSVHandler implements Route {

    private CSVState state;

    public LoadCSVHandler(CSVState state) {
        this.state = state;
    }

    public record LoadResponseData(String filepath){}

    public record LoadSuccessResponse(String response_type, LoadResponseData responseMap){
        public LoadSuccessResponse(LoadResponseData data){
            this("success", data);
        }

        String serialize(){
            Moshi moshi = new Moshi.Builder().build();
            JsonAdapter<LoadSuccessResponse> adapter = moshi.adapter(LoadSuccessResponse.class);
            return adapter.toJson(this);
        }
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //here we will call our parse,

//        Map<String, Object> responseMap = new HashMap<>();
        String filePath = request.queryParams("file");
        LoadResponseData data = new LoadResponseData(filePath);
        return new LoadSuccessResponse(data).serialize();
//        if(filePath == null){
//            //return the failure response
//        }
//        String path = "data/" + filePath;
//        if(path.contains("..")){
//            //return failure response
//        }
//
//        try{
//            FileReader reader = new FileReader(path);
//            CSVParser<List<String>> parser = new CSVParser<>(reader, new SearchObject());
//            this.state.fileSuccess();
//            this.state.setResults(parser.getResults());
//            this.state.setFilePath(path);
//            //return a new success response
//        } catch(FileNotFoundException e){
//            //return the failure response
//        }
//
//        //expect a factory failure exception
//        //if something goes wrong, updatr the current state to be an error and we can throw an error code????
//        //return a failure JSON
//        return "this is working as expected";
    }
}
