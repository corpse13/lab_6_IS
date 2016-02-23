package lab_6_IS;

public class BookBean {
	int id, year, left;
	String englishTitle, originalTitle, author, country, genre, price;
	
	@Override
    public String toString() {
        return "Book:: ID=" + this.id + " English Title=" + this.englishTitle + " Original Title=" + this.originalTitle + " Author=" + this.author +
               " Genre=" + this.genre + " Year=" + this.year + " Country=" + this.country + " Price=" + this.price + " Left=" + this.left;
    }
	
	public Object[] toObjectArray(){
		Object[] obj = {this.id, this.englishTitle, this.originalTitle, this.author, this.year, this.genre, this.country, this.price, this.left};
		return obj;
	}
	
	public static String[] getTableHeaders(){
		String[] headers= {"id", "englishTitle", "originalTitle", "author", "year", "genre", "country", "price", "left"};
		return headers;
	}
	
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public String getEnglishTitle() {
		return englishTitle;
	}
	public void setEnglishTitle(String englishTitle) {
		this.englishTitle = englishTitle;
	}
	public String getOriginalTitle() {
		return originalTitle;
	}
	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
}
