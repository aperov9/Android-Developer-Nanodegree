package com.example.javalib;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokeProvider {

    private static List<String> jokeList = new ArrayList<String>() {{
        add("The first computer dates back to Adam and Eve. It was an Apple with limited memory, just one byte. And then everything crashed.;");
        add("I bought some shoes from a drug dealer. I don't know what he laced them with, but I've been tripping all day.");
        add("My dad died when we couldn't remember his blood type. As he died, he kept insisting for us to be positive, but it's hard without him.");
        add("I just found out I'm colorblind. The diagnosis came completely out of the purple.");
    }};


    public String getJoke() {
        Random random = new Random();
        int randomNum = random.nextInt((jokeList.size() - 1) + 1);
        return jokeList.get(randomNum);
    }
}
