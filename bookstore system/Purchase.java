import java.util.Date;

public class Purchase {

    private int cid;
    private String club;
    private String title;
    private int year;
    private Date when;
    private int qnty;

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public int getQnty() {
        return qnty;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }

    @Override
    public String toString() {
        return "Insert Purchase Success {" +
                "cid=" + cid +
                ", club='" + club + '\'' +
                ", title='" + title + '\'' +
                ", year=" + year +
                ", when=" + when +
                ", qnty=" + qnty +
                '}';
    }
}
