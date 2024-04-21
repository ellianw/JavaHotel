import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class App {
    private static final int NUM_QUARTOS = 12;
    public static void main(String[] args) throws ParseException {
        ArrayList<Reserva> reservas = new ArrayList<>();
        //carregarReservasDoArquivo(reservas);
        String menu = "===== Sistema de Reservas de Hotel =====\n"+
                "1. Cadastrar reserva\n"+
                "2. Consultar reserva\n"+
                "3. Atualizar reserva\n"+
                "4. Cancelar reserva\n"+
                "5. Exibir relatório de reservas\n"+
                "6. Sair do sistema";
        int opcao = Entrada.leiaInt(menu);
        while (opcao != 6) {
            if (opcao == 1) {
                cadastrarReserva(reservas);
            } else if (opcao == 2) {
                consultarReserva(reservas);
            } else if (opcao == 3) {
                atualizarReserva(reservas);
            } else if (opcao == 4) {
                cancelarReserva(reservas);
            } else if (opcao == 5) {
                exibirRelatorio(reservas);
            } else {
                Entrada.leiaString("Opção inválida. Tente novamente.");
            }
            opcao = Entrada.leiaInt(menu);
        }

        //salvarReservasNoArquivo(reservas);
        System.exit(0);
    }

    public static void cadastrarReserva(ArrayList<Reserva> reservas) throws ParseException {
        if (NUM_QUARTOS-reservas.size() == 0) {
            Entrada.leiaString("Desculpe, não há quartos disponíveis.");
            return;
        }

        String nomeHospede = Entrada.leiaString("===== Cadastro de Reserva (1/4)=====\n" +
                "Digite o nome do hóspede: ");

        if (nomeHospede.equals("null")) {
            Entrada.leiaString("Nome inválido!");
            return;
        }

        int numeroQuarto = Entrada.leiaInt("===== Cadastro de Reserva (2/4)=====\n" +
                "Digite o número do quarto: ");

        if (verificarQuartoInvalido(numeroQuarto)) {
            return;
        }
        if (verificarQuartoOcupado(reservas,numeroQuarto)) {
            Entrada.leiaString("O quarto não está disponível");
            return;
        }

        String inputEntrada = Entrada.leiaString("===== Cadastro de Reserva (3/4)=====\n" +
                "Digite a data de entrada: ");

        while (dataInvalida(inputEntrada)){
            inputEntrada = Entrada.leiaString("===== Cadastro de Reserva (3/4)=====\n" +
                    "Data inválida! " +
                    "Digite a data de entrada: ");
        }

        String inputSaida = Entrada.leiaString("===== Cadastro de Reserva (4/4)=====\n" +
                "Digite a data de  saída: ");

        boolean dataValida = false;

        while (!dataValida){
            if (dataInvalida(inputSaida)) {
                inputSaida = Entrada.leiaString("===== Cadastro de Reserva (4/4)=====\n" +
                        "Data inválida! " +
                        "Digite a data de  saída: ");
                continue;
            }
            if (saidaInvalida(inputEntrada,inputSaida)){
                inputSaida = Entrada.leiaString("===== Cadastro de Reserva (4/4)=====\n" +
                        "Data anterior a entrada! " +
                        "Digite a data de  saída: ");
                continue;
            }
            dataValida = true;
        }

        Reserva tmpReserv = new Reserva(numeroQuarto,nomeHospede,inputEntrada,inputSaida);
        reservas.add(tmpReserv);

        Entrada.leiaString("Reserva cadastrada com sucesso.");
    }

    public static void consultarReserva(ArrayList<Reserva> reservas) {
        int numeroQuarto = Entrada.leiaInt("===== Consulta de Reserva =====\n" +
                "Digite o número do quarto: ");

        if (verificarQuartoInvalido(numeroQuarto)){
            return;
        }
        if (!verificarQuartoOcupado(reservas,numeroQuarto)){
            Entrada.leiaString("Quarto vazio.");
        } else {
            Entrada.leiaString(reservas.get(buscaReservaPorQuarto(reservas,numeroQuarto)).toString());
        }
    }

    public static void atualizarReserva(ArrayList<Reserva> reservas) throws ParseException {
        int numeroQuarto = Entrada.leiaInt("===== Atualização de Reserva =====\n" +
                "Digite o número do quarto: ");

        if (verificarQuartoInvalido(numeroQuarto)){
            return;
        }
        if (!verificarQuartoOcupado(reservas,numeroQuarto)) {
            Entrada.leiaString("Não há reserva para este quarto");
            return;
        }

        Reserva reserva = reservas.get(buscaReservaPorQuarto(reservas,numeroQuarto));

        String nomeHospede = Entrada.leiaString("===== Cadastro de Reserva (1/3)=====\n" +
                "Digite o nome do hóspede: ");

        if (nomeHospede.equals("null")) {
            Entrada.leiaString("Nome inválido!");
            return;
        }

        String inputEntrada = Entrada.leiaString("===== Cadastro de Reserva (2/3)=====\n" +
                "Digite a data de entrada: ", reserva.getDataEntradaString());

        while (dataInvalida(inputEntrada)) {
            inputEntrada = Entrada.leiaString("===== Cadastro de Reserva (2/3)=====\n" +
                    "Data inválida! " +
                    "Digite a data de entrada: ");
        }

        String inputSaida = Entrada.leiaString("===== Cadastro de Reserva (3/3)=====\n" +
                "Digite a data de saída: ", reserva.getDataSaidaString());

        boolean dataValida = false;

        while (!dataValida) {
            if (dataInvalida(inputSaida)) {
                inputSaida = Entrada.leiaString("===== Cadastro de Reserva (4/4)=====\n" +
                        "Data inválida! " +
                        "Digite a data de saída: ");
                continue;
            }
            if (saidaInvalida(inputEntrada, inputSaida)) {
                inputSaida = Entrada.leiaString("===== Cadastro de Reserva (4/4)=====\n" +
                        "Data anterior a entrada! " +
                        "Digite a data de saída: ");
                continue;
            }
            dataValida = true;
        }
        reserva.setDataEntrada(inputEntrada);
        reserva.setDataSaida(inputSaida);
        reserva.setHospede(nomeHospede);
        reservas.add(numeroQuarto-1,reserva);

        Entrada.leiaString("Reserva atualizada com sucesso.");
    }

    public static void cancelarReserva(ArrayList<Reserva> reservas) {
        int numeroQuarto = Entrada.leiaInt("===== Cancelamento de Reserva =====\n" +
                "Digite o número do quarto: ");
        if (verificarQuartoInvalido(numeroQuarto)) {
            return;
        }
        if (verificarQuartoOcupado(reservas,numeroQuarto)) {
            reservas.remove(buscaReservaPorQuarto(reservas,numeroQuarto));
            Entrada.leiaString("Reserva cancelada com sucesso.");
        } else {
            Entrada.leiaString("Quarto vazio. Não é possível cancelar a reserva.");
        }
    }

    public static void exibirRelatorio(ArrayList<Reserva> array) {
        StringBuilder vagos = new StringBuilder();
        StringBuilder ocupados = new StringBuilder();
        for (int i=0;i<array.size();i++) {
            if (array.get(i)!=null){
                ocupados.append(i + 1).append(" ");
            } else {
                vagos.append(i + 1).append(" ");
            }
        }
        Entrada.leiaString("===== Relatório de Reservas =====\n" +
                "Quartos locados: " + ocupados+ "\n" +
                "Quartos livres: " + vagos);
    }

    public static boolean verificarQuartoInvalido(int numeroQuarto) {
        boolean bool = (numeroQuarto <= 0 || numeroQuarto > NUM_QUARTOS);
        if (bool) {
            Entrada.leiaString("Número de quarto inválido.");
            return true;
        }
        return false;
    }

    public static boolean verificarQuartoOcupado(ArrayList<Reserva> array,int numeroQuarto) {
        return (buscaReservaPorQuarto(array,numeroQuarto) != -1);
    }

    public static int buscaReservaPorQuarto(ArrayList<Reserva> array,int quarto){
        for (Reserva reserva : array) {
            if (reserva.getQuarto() == quarto) {
                return array.indexOf(reserva);
            }
        }
        return -1;
    }

    public static boolean saidaInvalida(String entrada, String saida) {
        String[] arrEnt = entrada.split("/");
        String[] arrSai = saida.split("/");
        Calendar dataEnt = Calendar.getInstance();
        dataEnt.set(Integer.parseInt(arrEnt[2]), Integer.parseInt(arrEnt[1])-1,Integer.parseInt(arrEnt[0]));
        Calendar dataSai = Calendar.getInstance();
        dataSai.set(Integer.parseInt(arrSai[2]),Integer.parseInt(arrSai[1])-1,Integer.parseInt(arrSai[0]));
        return dataEnt.compareTo(dataSai) > 0;
    }

    public static boolean dataInvalida(String data) {
        String[] str = data.split("/");
        if (str.length != 3)
            return false;
        if (str[2].length() != 4)
            return false;
        boolean ok = false;
        int d = Integer.parseInt(str[0]);
        int m = Integer.parseInt(str[1]);
        int a = Integer.parseInt(str[2]);

        int[] udm = {31,28,31,30,31,30,31,31,30,31,30,31};
        if ((a % 4 == 0 && a % 100 != 0) || (a % 400 == 0))
        {
            udm[1] = 29;
        }

        if (a > 1582)
        {
            if (m >= 1 && m <= 12)
            {
                if (d >= 1 && d <= udm[m-1] )
                {
                    ok = true;
                }
            }
        }
        return !ok;
    }

    public static void carregarReservasDoArquivo(ArrayList<Reserva> array) throws ParseException {
        Arquivo arquivo = new Arquivo("reservas.txt");

        if (arquivo.abrirLeitura()) {
            String linha = arquivo.lerLinha();
            String str = "";
            while (linha != null) {
                str = linha;
                linha = arquivo.lerLinha();
            }
            String[] arrNome = str.split(";")[0].split(",");
            String[] arrQuar = str.split(";")[1].split(",");
            String[] arrEnt = str.split(";")[2].split(",");
            String[] arrSai = str.split(";")[3].split(",");
            for (int i=0; i<NUM_QUARTOS;i++) {
                int quarto;
                try {
                    quarto = Integer.parseInt(arrQuar[i]);
                } catch (NumberFormatException ex){
                    quarto = i;
                }
                if (quarto==i && !arrNome[i].equals("null")){
                    Reserva reserv = new Reserva(quarto,arrNome[i-1],arrEnt[i-1],arrSai[i-1]);
                    array.add(reserv);
                } else {
                    array.add(null);
                }
            }
        }
        arquivo.fecharArquivo();
    }

    public static void salvarReservasNoArquivo(ArrayList<Reserva> array) {
        Arquivo arquivo = new Arquivo("reservas.txt");
        String str = "";
        StringBuilder strNomes = new StringBuilder();
        StringBuilder strQuartos = new StringBuilder();
        StringBuilder strEntradas = new StringBuilder();
        StringBuilder strSaidas = new StringBuilder();
        if (arquivo.abrirEscrita()) {
            for (Reserva reserva : array) {
                strNomes.append(reserva.getHospede()).append(",");
                strQuartos.append(reserva.getHospede()).append(",");
                strEntradas.append(reserva.getHospede()).append(",");
                strSaidas.append(reserva.getHospede()).append(",");
            }
            str = strNomes.substring(0,strNomes.length()-1)+strQuartos.substring(0,strQuartos.length()-1)+strEntradas.substring(0,strEntradas.length()-1)+strSaidas.substring(0,strSaidas.length()-1);
        }
        arquivo.escreverLinha(str);
        arquivo.fecharArquivo();
    }
}
