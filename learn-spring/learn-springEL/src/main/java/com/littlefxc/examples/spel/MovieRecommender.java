package com.littlefxc.examples.spel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MovieRecommender {

    private String movieName;
    private String defaultLocale;

    @Autowired
    public MovieRecommender(@Value("#{ systemProperties['user.country'] }") String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    @Override
    public String toString() {
        return "MovieRecommender{" +
                "movieName='" + movieName + '\'' +
                ", defaultLocale='" + defaultLocale + '\'' +
                '}';
    }
}