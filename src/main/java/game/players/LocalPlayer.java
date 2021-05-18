package game.players;

import client.Client;
import client.PlayerProfile;
import game.components.Board;
import game.components.BoardField;
import game.components.Tile;
import gui.components.Rack;
import gui.components.RackField;
import gui.controllers.GameViewController;

import java.time.LocalDate;
import java.util.*;

public class LocalPlayer extends Player{

    private Client client;
    private GameViewController controller;

    private PlayerProfile profile;
    private int score = 0;
    private Board board = new Board();
    private Rack rack = new Rack();
    //todo get the playerprofile Array from server
    private PlayerProfile[] profiles;
    private int[] scores;

    public LocalPlayer(Client client, GameViewController controller){
        super(true);
        this.client = client;
        this.controller = controller;
        this.profile = client.getSelectedProfile();

        rack.add(new Tile('H', 1));
        rack.add(new Tile('E', 2));
        rack.add(new Tile('L', 1));
        rack.add(new Tile('L', 1));
        rack.add(new Tile('O', 4));

        profiles =  new PlayerProfile[]{
            new PlayerProfile("TestUser1", 0, 0, 0, 0, LocalDate.now(), LocalDate.now()),
            new PlayerProfile("TestUser2", 0, 0, 0, 0, LocalDate.now(), LocalDate.now()),
            new PlayerProfile("TestUser3", 0, 0, 0, 0, LocalDate.now(), LocalDate.now()),
        };

        scores = new int[]{14, 12, 6};

    }


    @Override
    public void quit() {

    }

    @Override
    public void addTilesToRack(Collection<Tile> tiles) {
        tiles.forEach(tile -> {
            rack.add(tile);
        });
        controller.updateRack();
    }

    public Rack getRack(){
        return rack;
    }

    public int getScore(){
        return score;
    }

    public void selectTile(int position){
        rack.getField(position).setSelected(!rack.isSelected(position));
    }

    public boolean tileSelected(int position){
        return rack.getField(position).isSelected();
    }

    public Board getBoard(){
        return board;
    }

    public void setScoreboard(PlayerProfile[] profiles, int[] scores){ //todo use in netclient to set profiles and update scores of all
        this.profiles = profiles;
        this.scores = scores;
        controller.updateScoreboard();
    }

    public Map<String, Integer> getScoreboard(){
        Map<String, Integer> map = new LinkedHashMap<>();

        for(int i = 0; i < profiles.length; i++){
            String profileName = map.keySet().contains(profiles[i].getName()) ? profiles[i].getName() + "(1)" : profiles[i].getName();
            map.put(profileName, scores[i]);
        }

        return map;
    }

  public PlayerProfile[] getProfiles() {
    return profiles;
    }

  public void placeTile(int position, int row, int col){
        if(!turn){
            return;
        }
        if(board.isEmpty(row, col)){
            placements.add(board.getField(row, col));
            board.placeTile(rack.getField(position).getTile(), row, col);
            rack.remove(position);
        }
    }

    public void removePlacement(int row, int col){
        if(placements.contains(board.getField(row, col))){
            addTilesToRack(Arrays.asList(board.getTile(row, col)));
            board.getField(row, col).setTile(null);
            placements.remove(board.getField(row, col));
        }
    }

    public void setTurn(boolean turn){
        placements.clear();
        this.turn = turn;
    }

    public boolean isTurn(){
        return turn;
    }


}
