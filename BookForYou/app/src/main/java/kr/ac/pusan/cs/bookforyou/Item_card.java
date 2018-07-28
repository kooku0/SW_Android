package kr.ac.pusan.cs.bookforyou;

public class Item_card {
    private String title;
    private String author;
    private String company;




    public Item_card(String title, String author, String company) {
        this.title = title;
        this.author = author;
        this.company = company;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    public void setCompany(String company){
        this.company=company;
    }

    public String getCompany() {
        return company;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }




}
