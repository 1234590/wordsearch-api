package io.javabrains.wordsearchapi.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WordGridService {



    private List<Cordinate> cordinates = new ArrayList<>();
    public enum Direction {
        HORIZONTAL,
        VERTICAL,
        DIAGONAL
    }

    private class Cordinate {
        int x;
        int y;

        Cordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


    public char[][] fillGrid(int gridSize, List<String> words) {


        char[][] contents = new char[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                cordinates.add(new Cordinate(i, j));
                contents[i][j] = '_';
            }
        }

        Collections.shuffle(cordinates);
        for (String word : words) {
            for (Cordinate cordinate : cordinates) {
                int x = cordinate.x;
                int y = cordinate.y;
                Direction selectedDirection = getDirectionForFit(contents,word, cordinate);
                if (selectedDirection != null) {
                    switch (selectedDirection) {
                        case HORIZONTAL:
                            for (char c : word.toCharArray()) {
                                contents[x][y++] = c;
                            }
                            break;
                        case VERTICAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y] = c;
                            }
                            break;
                        case DIAGONAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y++] = c;
                            }
                            break;
                    }
                    break;
                }
            }
        }
        randomFillGrid(contents);
        return contents;
    }

    public void displayGrid(char [][] contents) {
        int gridSize  =  contents[0].length;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(contents[i][j] + " ");
            }
            System.out.println("");
        }
    }



        private  void randomFillGrid( char [][] contents ){
            int gridSize = contents[0].length;
            String allCapLettres = "ABCDEFGKLMNJOPIQRSTVWYZ";
            for (int i = 0; i< gridSize; i++){
                for (int j = 0; j < gridSize; j++){
                    if ( contents[i][j] =='_' ) {
                        int randomIndex = ThreadLocalRandom.current().nextInt(0, allCapLettres.length());
                        contents[i][j] = allCapLettres.charAt(randomIndex);
                    }
                }
            }
        }
        private Direction getDirectionForFit(char[][] contents, String word, Cordinate cordinate) {
            List<Direction> directions = Arrays.asList(Direction.values());
            Collections.shuffle(directions);
            for (Direction direction : directions){
                if (doesFit(contents ,word,direction,cordinate)){
                    return direction;
                }
            }

            return null;
        }


        public  boolean doesFit(char[][] contents, String word , Direction direction , Cordinate cordinate){
            int gridSize = contents[0].length;
            int wordLength = word.length();
            switch (direction){

                case HORIZONTAL:
                    if (cordinate.y + wordLength > gridSize) return false;
                    for (int i = 0; i < wordLength; i++){
                        if(contents[cordinate.x][cordinate.y +i] != '_') return false;
                    }

                    break;
                case VERTICAL:
                    if (cordinate.x + wordLength > gridSize) return false;
                    for (int i = 0; i < wordLength; i++){
                        if(contents[cordinate.x + i][cordinate.y] != '_') return false;
                    }
                    break;
                case DIAGONAL:
                    if (cordinate.x + wordLength > gridSize || cordinate.y + wordLength > gridSize) return  false;
                    for (int i = 0; i < wordLength; i++){
                        if(contents[cordinate.x + i][cordinate.y + i] != '_') return false;
                    }
                    break;
            }
            return true;
        }
    }
