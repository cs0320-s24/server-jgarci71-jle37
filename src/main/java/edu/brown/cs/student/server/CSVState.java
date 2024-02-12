package edu.brown.cs.student.server;

import java.util.ArrayList;
import java.util.List;

public class CSVState {

    private String filePath = "";
    private List<List<String>> loadResults = new ArrayList<>();
    private State currState = State.EMPTY;

    public enum State{
        LOADED, FAILED, EMPTY;
    }

    public CSVState(){

    }

    public void fileSuccess(){
        this.currState = State.LOADED;
    }
    public void fileFailed(){
        this.currState = State.FAILED;
    }
    public void setFilePath(String newPath){
        this.filePath = newPath;
    }

    public void setResults(List<List<String>> results){
        this.loadResults = results;
    }

    public State getState(){
        return this.currState;
    }

    public String getFilePath(){
        return this.filePath;
    }

    public List<List<String>> getLoadResults() {
        return this.loadResults;
    }
}
