package com.example.android.news;

public class CustomObject {

    String Headline;
    String Description;
    String URL;
    String Category;
    String Section;
    String ImageUrl;

    public CustomObject(String headline, String description, String url, String category, String section, String imageUrl){
        Headline = headline;
        Description = description;
        URL = url;
        Section = section;
        Category = category;
        ImageUrl = imageUrl;
    }

    public String getHeadline() {
        return Headline;
    }

    public String getDescription() {
        return Description;
    }

    public String getURL() {
        return URL;
    }

    public String getCategory() {
        return Category;
    }

    public String getSection() {
        return Section;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
