package edlib.rumusmatematikalengkap.Model;

public class RcModel {
    private String title;
    private String img;
    private String body;
    private String name;
    private int idCat;

    public RcModel(String title, String img, String body, String name, int idCat) {
        this.title = title;
        this.img = img;
        this.body = body;
        this.name = name;
        this.idCat = idCat;
    }

    public RcModel() {
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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }
}
