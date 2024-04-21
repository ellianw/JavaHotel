import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Reserva {
    private int quarto;
    private String hospede;
    private Calendar dataEntrada = Calendar.getInstance();
    private Calendar dataSaida = Calendar.getInstance();
    private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public Reserva(int quarto, String hospede, String dataEntrada, String dataSaida) throws ParseException {
        this.quarto = quarto;
        this.hospede = hospede;
        this.dataEntrada.setTime(df.parse(dataEntrada));
        this.dataSaida.setTime(df.parse(dataSaida));
    }

    public int getQuarto() {
        return quarto;
    }
    public String getHospede() {
        return hospede;
    }
    public Calendar getDataEntrada() {
        return dataEntrada;
    }
    public String getDataEntradaString() {
        return df.format(dataEntrada.getTime());
    }
    public Calendar getDataSaida() {
        return dataSaida;
    }
    public String getDataSaidaString() {
        return df.format(dataSaida.getTime());
    }

    public void setQuarto(int quarto) {
        this.quarto = quarto;
    }
    public void setHospede(String hospede) {
        this.hospede = hospede;
    }
    public void setDataEntrada(String data) throws ParseException {
        dataEntrada.setTime(df.parse(data));
    }
    public void setDataSaida(String data) throws ParseException {
        dataSaida.setTime(df.parse(data));
    }

    @Override
    public String toString() {
        return "Reserva:\n" +
                "Quarto número " + quarto +
                "\nHospede: " + hospede +
                "\nData de Entrada: " + df.format(dataEntrada.getTime())+
                "\nData de Saída " + df.format(dataSaida.getTime());
    }
}
