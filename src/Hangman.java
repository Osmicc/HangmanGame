import acm.graphics.GLabel;
import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import svu.csc213.Dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Hangman extends GraphicsProgram {


    public JButton guessLetterBtn;
    public JButton guessWordBtn;
    public JButton quit;
    public JButton playBtn;
    /**
     * * Show the word once you lose
     * ? Add a game loop (A replay button, a skip button, maybe key presses to hotkey to the buttons)
     * ? Add difficulties(Change the amount of items drawn and the list of words the word is chosen from)
     * * Add a penalty for guessing the word incorrectly (Instant loss)
     * * Install git
     * * Write comments throughout the project
     * * If the letter guessed has already been used do not allow
     * ? Fix the bug where if you guess a letter that is already been used in the word than it will not add remaining letters
     */
    Man man = new Man();
    Word word = new Word();
    Gallows gallows = new Gallows(20, getHeight() + 400);
    int life = 6;
    String usedWord = word.getWord();
    Letter letter = new Letter(usedWord, 0);
    Letter[] letArray = new Letter[usedWord.length()];
    GRect[] dashArray = new GRect[usedWord.length()];
    int charShown = usedWord.length();
    char[] wrongLetters = new char[25];
    GRect letterBank = new GRect(300, 50);
    int lettersGuessed = 0;
    int lettersRight = 0;

    GRect dash;


    @Override
    public void init() {
        // Adds a new instance of the gallows class based on the arbitrary numbers used when declared globally
        add(gallows);
        // Creates the dashes that the letters are found on
        dash = new GRect(10, 1);

        setBackground(Color.LIGHT_GRAY);

        // creates all the objects used throughout the project
        add(man, 194, 235);
        add(letterBank, getWidth() - letterBank.getWidth() - 10, 10);
        guessLetterBtn = new JButton("Guess letter");
        add(guessLetterBtn, SOUTH);
        guessWordBtn = new JButton("Guess word");
        add(guessWordBtn, SOUTH);
        playBtn = new JButton("Play");

        playBtn.setVisible(false);
        quit = new JButton("Quit");
        add(quit, SOUTH);
        add(playBtn, SOUTH);
        genDashes();
        genLet();
        addActionListeners();
    }


    // Listens for the buttons on the screen being pressed and calls the respective methods
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
            case "Play":
                Play();

        }
    }

    private void Play() {
        removeAll();
        new Hangman().start();
    }

    // A method that asks for a letter guessed and if that letter has not already been guessed it calls the guessLet method
    private void guessLetter() {
        String guess = Dialog.getString("Guess A Letter");
        for (int i = 0; i < wrongLetters.length; i++) {
            if (guess.equals(String.valueOf(wrongLetters[i]))) {
                Dialog.showMessage("that letter has already been guessed");
                return;
            }
        }
        guessLet(guess.charAt(0));
    }

    // If the player guesses the whole word it checks to see if correct, if not it is an instant loss
    private void guessWord() {
        String guess = Dialog.getString("Guess the word");
        if (guess.equals(usedWord)) {
            win();
        } else lose();
    }

    // Creates the appropriate number of dashes for the amount of letters in the word
    private void genDashes() {
        for (int i = 0; i < usedWord.length(); i++) {
            dashArray[i] = new GRect(20, 1);
            add(dashArray[i], getWidth() / 2 - dash.getWidth() * dashArray.length + 25 * (i), getHeight() / 8 * 7.5);
        }
    }


    // creates the letters that are in the word on top of the dashes
    private void genLet() {
        for (int i = 0; i < usedWord.length(); i++) {

            letArray[i] = new Letter(usedWord, i);

            add(letArray[i], dashArray[i].getX(), dashArray[i].getY());
        }
    }

    // Checks to see if the letter guessed is contained in the word
    private void guessLet(char a) {
        // A variable used to check if any letters have been added to the screen since the last time the method was performed
        int check = charShown;
        // Checks if the letter is correct for each space and if it is, it is shown
        for (int i = 0; i < usedWord.length(); i++) {
            if (letArray[i].getLetter() == a) {
                letArray[i].setVis(true);
                charShown += 1;
                lettersRight += 1;
                // if all the letters in the word are guessed then the player wins
                if (lettersRight == usedWord.length()) {
                    win();
                }
            }
        }
        // If no new letters are added a life is lost
        if (check == charShown) {
            life -= 1;
            man.addParts();
            addToBank(a);
            // if the player runs out of lives they lose
            if (life == 0) {
                lose();
            }
        }
    }

    private void addToBank(char a) {
        // If the letter guessed was incorrect it is added to the word bank
        lettersGuessed += 1;
        add(new GLabel(String.valueOf(a)), letterBank.getX() + lettersGuessed * 25, letterBank.getY() + 10);

        for (int i = 0; i < wrongLetters.length; i++) {
            if (wrongLetters[i] == 0) {
                wrongLetters[i] = a;
            }
        }
    }

    private void win() {
        // if all the letters are not already shown they are set to visible
        for (int i = 0; i < usedWord.length(); i++) {
            letArray[i].setVis(true);
        }
        Dialog.showMessage("You win");
        System.exit(0);
    }

    private void lose() {
        // Shows all the letters that were missed
        for (int i = 0; i < letArray.length; i++) {
            if (!letArray[i].isVisible()) {
                letArray[i].setVis(true);
            }
        }
        Dialog.showMessage("You lost");
        System.exit(0);
    }

    public static void main(String[] args) {
        Hangman x = new Hangman();
        x.start();

    }

}