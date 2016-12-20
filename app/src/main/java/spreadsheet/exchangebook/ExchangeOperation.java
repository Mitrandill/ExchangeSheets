package spreadsheet.exchangebook;

/**
 * Created by mixail on 12/6/16.
 */

public class ExchangeOperation {

    private Integer id;
    private Integer fromValue;
    private String fromCurrency;
    private Integer toUAH;
    private String toCurrency;
    private String created;
    private String comment;
    private String hash;

    public ExchangeOperation(Integer id, Integer fromValue, String fromCurrency, Integer toUAH, String toCurrency, String created, String hash, String comment) {
        this.id = id;
        this.fromValue = fromValue;
        this.fromCurrency = fromCurrency;
        this.toUAH = toUAH;
        this.toCurrency = toCurrency;
        this.created = created;
        this.hash = hash;
        this.comment = comment;

    }

    public Integer getFromValue() {
        return fromValue;
    }

    public void setFromValue(Integer fromValue) {
        this.fromValue = fromValue;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public Integer gettoUAH() {
        return toUAH;
    }

    public void setToUAH(Integer toUAH) {
        this.toUAH = toUAH;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
