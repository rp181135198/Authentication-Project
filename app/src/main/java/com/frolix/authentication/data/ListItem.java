package com.frolix.authentication.data;

public class ListItem {
    private String title;
    private String author;
    private String image;

    public ListItem() {}

    public ListItem(String bookName, String author, String imageUrl) {
        this.title = bookName;
        this.author = author;
        this.image = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
