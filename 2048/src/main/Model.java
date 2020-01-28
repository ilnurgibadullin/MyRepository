package main;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles;
    protected int score;
    protected int maxTile;
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public void saveState(Tile[][] tiles) {
        Tile[][] filesToSave = new Tile[tiles.length][tiles[0].length];
        previousScores.push(score);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                filesToSave[i][j] = new Tile(tiles[i][j].value);
            }
        }
        previousStates.push(filesToSave);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousStates.empty() && !previousScores.empty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public Model() {
        resetGameTiles();
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (emptyTiles.size() > 0) {
            emptyTiles.get((int) (Math.random() * emptyTiles.size())).value = (Math.random() < 0.9 ? 2 : 4);
        }
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean isChanged = false;
        int count = 0;
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i].value != 0) {
                tiles[count++].value = tiles[i].value;
            }
        }
        while (count < tiles.length) {
            if (tiles[count].value != 0) {
                isChanged = true;
            }
            tiles[count++].value = 0;
        }
        return isChanged;
    }

    private boolean mergeTiles(Tile[] tiles) {
        boolean isChanged = false;
        for (int i = 0; i < tiles.length-1; i++) {
            if (tiles[i].value == tiles[i+1].value) {
                tiles[i].value = 2 * tiles[i+1].value;
                tiles[i+1].value = 0;
                compressTiles(tiles);
                int oldScore = score;
                score += tiles[i].value;
                if (tiles[i].value > maxTile) {
                    maxTile = tiles[i].value;
                }
                if (oldScore != score) {
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }

    public void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
        }
        boolean isAdd = false;
        for (int i = 0; i < gameTiles.length; i++) {
            if (compressTiles(gameTiles[i])) isAdd = true;
            if (mergeTiles(gameTiles[i])) isAdd = true;
        }
        if (isAdd) {
            addTile();
        }
        isSaveNeeded = true;
    }

    public void right() {
        saveState(gameTiles);
        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    public void up() {
        saveState(gameTiles);
        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }

    public void down() {
        saveState(gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                up();
                break;
            case 3:
                down();
                break;
        }
    }

    public boolean hasBoardChanged() {
        int weightPresent = 0;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[0].length; j++) {
                weightPresent += gameTiles[i][j].value;
            }
        }
        int weightPast = 0;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[0].length; j++) {
                weightPast += previousStates.peek()[i][j].value;
            }
        }
        if (weightPresent != weightPast) {
            return true;
        } else {
            return false;
        }
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4, Collections.reverseOrder());
        queue.offer(new MoveEfficiency(getEmptyTiles().size(), score, new Move() {
            @Override
            public void move() {
                left();
            }
        }));
        queue.offer(new MoveEfficiency(getEmptyTiles().size(), score, new Move() {
            @Override
            public void move() {
                up();
            }
        }));
        queue.offer(new MoveEfficiency(getEmptyTiles().size(), score, new Move() {
            @Override
            public void move() {
                right();
            }
        }));
        queue.offer(new MoveEfficiency(getEmptyTiles().size(), score, new Move() {
            @Override
            public void move() {
                down();
            }
        }));

        queue.peek().getMove().move();

    }

    public MoveEfficiency getMoveEfficiency(Move move) {
        move.move();
        MoveEfficiency moveEfficiency;
        if (hasBoardChanged()){
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        } else {
            moveEfficiency = new MoveEfficiency(-1, 0, move);
        }
        rollback();
        return moveEfficiency;
    }

    public void rotate() {
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int r = 0; r < gameTiles.length; r++) {
            for (int c = 0; c < gameTiles[0].length; c++) {
                temp[c][gameTiles.length-1-r] = gameTiles[r][c];
            }
        }
        gameTiles = temp.clone();
    }


    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (gameTiles[i][j].value == 0) {
                    emptyTiles.add(gameTiles[i][j]);
                }
            }
        }
        return emptyTiles;
    }

    public boolean canMove() {
        boolean can = false;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[0].length; j++) {
                if (gameTiles[i][j].value == 0) {
                    can = true;
                    break;
                }
            }
        }

        for (int i = 0; i < gameTiles.length-1; i++) {
            for (int j = 0; j < gameTiles[i].length-1; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j+1].value) {
                    can = true;
                    break;
                }
            }
        }

        for (int i = 0; i < gameTiles.length-1; i++) {
            for (int j = 0; j < gameTiles[i].length-1; j++) {
                if (gameTiles[i][j].value == gameTiles[i+1][j].value) {
                    can = true;
                    break;
                }
            }
        }

        return can;
    }


    public void resetGameTiles() {
        gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[j][i] = new Tile();
            }
        }

        addTile();
        addTile();

        score = 0;
        maxTile = 0;
    }
}

