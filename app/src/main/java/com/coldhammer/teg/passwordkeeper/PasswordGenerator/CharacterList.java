package com.coldhammer.teg.passwordkeeper.PasswordGenerator;

public class CharacterList{
    int amount;
    char[] charSet;

    public CharacterList(char[] charSet, int amount){
        this.charSet = charSet;
        this.amount = amount;
    }

    char[] getCharacters(){
        return charSet;
    }

    int getAmount(){
        if(amount > 0)
            return amount--;
        return amount;
    }
}