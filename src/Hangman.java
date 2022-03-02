import acm.graphics.*;
import acm.program.GraphicsProgram;
import java.awt.event.ActionEvent;

import svu.csc213.Dialog;

import javax.swing.*;
import java.awt.*;

public class Hangman extends GraphicsProgram{


    /**
     * * Show the word once you lose
     * ? Add a game loop (A replay button, a skip button, maybe key presses to hotkey to the buttons)
     * ? Add difficulties(Change the amount of items drawn and the list of words the word is chosen from)
     * * Add a penalty for guessing the word incorrectly (Instant loss)
     * * Install git
     * ? Write comments throughout the project
     * * If the letter guessed has already been used do not allow it
     */
    Man man = new Man();
    Word word = new Word();
    Gallows gallows = new Gallows(20, getHeight() + 400);

    int life = 6;

    String usedWord = word.getWord();
    char[] wrongLetters = new char[25];
    Letter letter = new Letter(usedWord, 0);
    Letter[] letArray = new Letter[usedWord.length()];

    GRect[] dashArray = new GRect[usedWord.length()];
    GRect letterBank = new GRect(300, 50);

    public JButton guessLetterBtn;
    public JButton guessWordBtn;
    public JButton quit;
    int charShown = usedWord.length();

    int lettersGuessed = 0;
    int lettersRight = 0;

    GRect dash;

    @Override
    public void run() {
        genDashes();
        genLet();
        addActionListeners();
    }

    @Override
    public void init() {
        add(gallows);
        dash = new GRect(10, 1);

        setBackground(Color.LIGHT_GRAY);

        add(letter, 500, 500);
        add(man, 194, 235);
        add(letterBank, getWidth() - letterBank.getWidth() - 10, 10);
        guessLetterBtn = new JButton("Guess letter");
        add(guessLetterBtn, SOUTH);
        guessWordBtn = new JButton("Guess word");
        add(guessWordBtn, SOUTH);
        quit = new JButton("Quit");
        add(quit, SOUTH);


    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Guess letter":
                guessLetter();
                break;
            case "Guess word":
                guessWord();
                break;
            case "Quit":
                System.exit(0);
                break;

        }
    }


    private void guessLetter() {
        String guess = Dialog.getString("Guess A Letter");
        for (int i = 0; i < wrongLetters.length; i++) {
            if(guess.equals(String.valueOf(wrongLetters[i])) ){
                Dialog.showMessage("that letter has already been guessed");
                return;
            }
        }
        guessLet(guess.charAt(0));
    }

    private void guessWord(){
        String guess = Dialog.getString("Guess the word");
        if (guess.equals(usedWord)){
            win();
        } else lose();
    }


    private void genDashes(){
        for (int i = 0; i < usedWord.length(); i++) {
            dashArray[i] = new GRect(20, 1);
            add(dashArray[i], getWidth()/2 - dash.getWidth() * dashArray.length + 25*(i), getHeight()/8*7.5);
        }
    }

    private void genLet(){
        for (int i = 0; i < usedWord.length(); i++) {

            letArray[i] = new Letter(usedWord, i);

            add(letArray[i], dashArray[i].getX(), dashArray[i].getY());
        }
    }

    private void guessLet(char a){
        int check = charShown;
        for (int i = 0; i < usedWord.length(); i++) {
            if (letArray[i].getLetter() == a){
                letArray[i].setVis(true);
                charShown += 1;
                lettersRight += 1;
                if(lettersRight == usedWord.length()){
                    win();
                }
            }
        }

            if (check == charShown) {
                life -= 1;
                man.addParts();
                addToBank(a);
                if (life == 0) {
                    lose();
                }
            }
    }

    private void addToBank(char a){
        lettersGuessed += 1;
        add(new GLabel(String.valueOf(a)), letterBank.getX() + lettersGuessed * 25, letterBank.getY() + 10);

        for (int i = 0; i < wrongLetters.length; i++) {
            if(wrongLetters[i] == 0){
                wrongLetters[i] = a;
            }
        }
    }

    private void win(){

        for (int i = 0; i < usedWord.length(); i++) {
            letArray[i].setVis(true);
        }
        Dialog.showMessage("You win");

    }

    private void lose(){
        for (int i = 0; i < letArray.length; i++) {
            if(!letArray[i].isVisible()){
                letArray[i].setVis(true);
            }
        }
        Dialog.showMessage("You lost");
        System.exit(0);
    }

    public static void main(String[] args) {
        new Hangman().start();
    }
}