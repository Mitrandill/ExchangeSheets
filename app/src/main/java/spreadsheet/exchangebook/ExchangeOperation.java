package spreadsheet.exchangebook;

/**
 * Created by mixail on 12/6/16.
 */

public class ExchangeOperation {

    private Integer id;
    private Integer fromValue;
    private String fromCurrency;
    private Integer toValue;
    private String toCurrency;
    private String created;

    public ExchangeOperation(Integer id, Integer fromValue, String fromCurrency, Integer toValue, String toCurrency, String created) {
        this.id = id;
        this.fromValue = fromValue;
        this.fromCurrency = fromCurrency;
        this.toValue = toValue;
        this.toCurrency = toCurrency;
        this.created = created;
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

    public Integer getToValue() {
        return toValue;
    }

    public void setToValue(Integer toValue) {
        this.toValue = toValue;
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
}
