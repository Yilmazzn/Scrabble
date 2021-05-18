package gui.components;


import game.components.Tile;

import java.io.Serializable;

/**
 * @author mnetzer
 * Bag with tiles
 */
public class Bag implements Serializable {

    private final BagField[] fields;   //Bag with tiles
    private final int size;

    public Bag(int size){
        fields = new BagField[size];
        this.size = size;
    }

    public int getCount(){
        int count = 1;
        for (int i=0; i<fields.length; i++){
            if(!fields[i].isEmpty()){
                count++;
            }
        }
        return count;
    }

    public int getSize(){
        return size;
    }

    /**
     * Set given tile at given column
     *
     * @param tile Tile to place
     * @param id  id number of a certain tile (starting from 0)
     */
    public void placeTile(Tile tile, int id) {
        fields[id].setTile(tile);
        //fields[id].setId(id);
    }

    /**
     * Returns true if field is empty
     *
     * @param id number of a certain tile (starting from 0)
     * @return true, if field at given id is empty
     */
    public boolean isEmpty(int id) {
        return fields[id].isEmpty();
    }

    public Tile getTile(int id){
        return fields[id].getTile();
    }

    public int getBagFieldId(){
        int random;
        do {
            random = (int)((Math.random()) * fields.length);
        } while (fields[random].isEmpty());
        return random;
    }

    public Tile drawOneTile(int id){
        Tile tile = fields[id].getTile();
        fields[id].removeTile();
        return tile;
    }

    public Tile[] drawTiles(int amount){
        Tile []tiles = new Tile[amount];
        for(int i=0; i<amount; i++){
            tiles[i] = drawOneTile(getBagFieldId());
        }
        return tiles;
    }

    public Tile switchOneTile(Tile oldTile){
        int id = getBagFieldId();
        Tile newTile = drawOneTile(id);
        fields[id].setTile(oldTile);
        return newTile;
    }

    public Tile[] switchTiles(Tile []tiles){
        Tile[] newTiles = new Tile[tiles.length];
        for (int i=0; i<tiles.length; i++){
            newTiles[i] = switchOneTile(tiles[i]);
        }
        return newTiles;
    }

}
