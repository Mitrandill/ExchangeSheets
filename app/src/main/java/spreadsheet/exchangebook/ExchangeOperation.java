package spreadsheet.exchangebook;

/**
 * Created by mixail on 12/6/16.
 */

public class ExchangeOperation {

    public ExchangeOperation(Integer fromValue, String fromCurrency, Integer toValue, String toCurrency ) {
        this.fromValue = fromValue;
        this.fromCurrency = fromCurrency;
        this.toValue = toValue;
        this.toCurrency = toCurrency;
    }

    private Integer fromValue;
    private String fromCurrency;
    private Integer toValue;
    private String toCurrency;

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
}
