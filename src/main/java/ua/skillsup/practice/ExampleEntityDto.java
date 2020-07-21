package ua.skillsup.practice;

import java.math.BigDecimal;
import java.time.Instant;

public class ExampleEntityDto {
    private Long id;
    private String title;
    private Instant dateIn;
    private BigDecimal price;

    public ExampleEntityDto(ExampleEntity exampleEntity) {
        id = exampleEntity.getId();
        title = exampleEntity.getTitle();
        dateIn = exampleEntity.getDateIn();
        price = exampleEntity.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getDateIn() {
        return dateIn;
    }

    public void setDateIn(Instant dateIn) {
        this.dateIn = dateIn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ExampleEntity that = (ExampleEntity) o;
//        return Objects.equals(id, that.id) &&
//                Objects.equals(title, that.title) &&
//                Objects.equals(dateIn, that.dateIn) &&
//                Objects.equals(price, that.price);
//    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, title, dateIn, price);
//    }

    @Override
    public String toString() {
        return "ExampleEntityDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateIn=" + dateIn +
                ", price=" + price +
                '}';
    }
}
