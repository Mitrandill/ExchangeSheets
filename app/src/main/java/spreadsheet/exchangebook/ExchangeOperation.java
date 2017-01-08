package spreadsheet.exchangebook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by mixail on 12/6/16.
 */

public class ExchangeOperation {

    private Integer id;
    private String operatorHash;
    private Integer fromValue;
    private String fromCurrency;
    private Integer toUAH;
    private String toOperation;
    private String created;
    private String comment;
    private String hash;

    public ExchangeOperation(
            Integer id,
            Integer fromValue,
            String fromCurrency,
            Integer toUAH,
            String toOperation,
            String created,
            String hash,
            String comment) {

        this.operatorHash = "dr2rfwererw";

        this.id = id;
        this.fromValue = fromValue;
        this.fromCurrency = fromCurrency;
        this.toUAH = toUAH;
        this.toOperation = toOperation;
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

    public String getToOperation() {
        return toOperation;
    }

    public void setToOperation(String toOperation) {
        this.toOperation = toOperation;
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

    public long getUnixTime() {

        long unixtime = 0;

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

        try {
            Date date = dateformat.parse(this.created);
            unixtime = date.getTime() / 1000;

        } catch (ParseException e) {

        }

        return unixtime;
    }

    public long getUtcOffset() {

        long utfOffset = 0;

        Calendar cal = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()) ;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());

        try {
            Date date = dateformat.parse(this.created);

            cal.setTime(date);
            TimeZone mTimeZone = cal.getTimeZone();
            int mGMTOffset = mTimeZone.getRawOffset();

            utfOffset = TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS) * 60 * 60;

        } catch (ParseException e) {

        }

        return utfOffset;
    }

    public String toJSON () {

        String action = "";
        String currencyCodeToBuy = "";
        String currencyCodeToSell = "";
        String amountToBuy = "";
        String amountToSell = "";

        if ("Продажа".equals(this.toOperation)) {
            action = "sell";
            amountToBuy = Integer.toString(this.toUAH);
            amountToSell = Integer.toString(this.fromValue);


            currencyCodeToBuy = "uah";
            currencyCodeToSell = this.fromCurrency.toLowerCase();
        } else {
            action = "buy";
            //         amountToBuy = String.format("%.2f",(double)this.fromValue );
            //         amountToBuy = Float.toString(this.toUAH / 100);
            amountToBuy = Integer.toString(this.fromValue);
            amountToSell = Integer.toString(this.toUAH);
            currencyCodeToBuy = this.fromCurrency.toLowerCase();
            currencyCodeToSell = "uah";
        }

        String json = "{" +
            "\"operatorHash\": \"" + this.operatorHash + "\"," +
            "\"action\" : \"" + action + "\"," +
            "\"currencyCodeToBuy\" : \"" + currencyCodeToBuy + "\"," +
            "\"currencyCodeToSell\" : \"" + currencyCodeToSell + "\"," +
            "\"amountToBuy\" : \"" + amountToBuy + "\"," +
            "\"amountToSell\" : \"" + amountToSell + "\"," +
            "\"unixTime\" : \"" + getUnixTime() + "\"," +
            "\"utcOffset\" : \"" + getUtcOffset() + "\"," +
            "\"comment\" : \"" + this.comment + "\"," +
            "\"hash\" : \"" + this.hash + "\"" +
        "}";

        return json;
    }
}
