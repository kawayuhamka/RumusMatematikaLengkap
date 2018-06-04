package edlib.rumusmatematikalengkap.Model;

public class RcModel {
    private String title;
    private String img;

    public RcModel() {
    }

    public RcModel(String title, String img) {
        this.title = title;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
