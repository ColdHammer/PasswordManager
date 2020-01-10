package com.coldhammer.teg.passwordkeeper.PasswordGenerator;

import java.security.SecureRandom;

public class PasswordGenerator{

    public static char[] generatePassword(int length, int[] pattern, SecureRandom secureRandom, CharacterList... characterList)
    {
        char[] pass = new char[length];
        int nextNumber;
        for(int i = 0; i < pass.length; i++)
        {
            boolean breakLoop = false;
            int j = 0;
            do{
                do{
                    nextNumber = secureRandom.nextInt(characterList.length);
                }while(pattern[i] != 0 && pattern[i] != nextNumber + 1 && j <= 3);
                if(characterList[nextNumber].getAmount() != 0 || j == 4){
                    int randCharNumber = secureRandom.nextInt(characterList[nextNumber].getCharacters().length);
                    pass[i] = characterList[nextNumber].getCharacters()[randCharNumber];
                    breakLoop = true;
                }
                j++;
                pattern[i] = 0;
            }while(!breakLoop);
        }
        return pass;
    }
}