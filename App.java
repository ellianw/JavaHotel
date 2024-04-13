import java.util.Calendar;

public class App {
    public static void main(String[] args) {
        int NUM_QUARTOS = 12;
        String[] nomes = new String[NUM_QUARTOS];
        String[] dtEntrada = new String[NUM_QUARTOS];
        String[] dtSaida = new String[NUM_QUARTOS];
        carregarReservasDoArquivo(nomes, dtEntrada, dtSaida);
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
                cadastrarReserva(nomes, dtEntrada, dtSaida);
            } else if (opcao == 2) {
                consultarReserva(nomes, dtEntrada, dtSaida);
            } else if (opcao == 3) {
                atualizarReserva(nomes, dtEntrada, dtSaida);
            } else if (opcao == 4) {
                cancelarReserva(nomes, dtEntrada, dtSaida);
            } else if (opcao == 5) {
                exibirRelatorio(nomes);
            } else {
                Entrada.leiaString("Opção inválida. Tente novamente.");
            }
            opcao = Entrada.leiaInt(menu);
        }

        salvarReservasNoArquivo(nomes, dtEntrada, dtSaida);
        System.exit(0);
    }

    public static void cadastrarReserva(String[] nomes, String[] dtEntrada, String[] dtSaida) {
        if (quantidadeQuartosDisponiveis(nomes) == 0) {
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

        if (verificarQuartoInvalido(numeroQuarto,nomes)) {
            return;
        }
        if (verificarQuartoOcupado(numeroQuarto, nomes)) {
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
        nomes[numeroQuarto - 1] = nomeHospede;
        dtEntrada[numeroQuarto - 1] = inputEntrada;
        dtSaida[numeroQuarto - 1] = inputSaida;


        Entrada.leiaString("Reserva cadastrada com sucesso.");
    }

    public static void consultarReserva(String[] nomes, String[] entrada, String[] saida) {
        int numeroQuarto = Entrada.leiaInt("===== Consulta de Reserva =====\n" +
                "Digite o número do quarto: ");

        if (verificarQuartoInvalido(numeroQuarto,nomes)) {
            return;
        }

        if (nomes[numeroQuarto - 1] != null) {
            Entrada.leiaString("Hóspede: " + nomes[numeroQuarto - 1] + "\n" +
                    "Quarto: " + (numeroQuarto) + "\n" +
                    "Entrada: " + entrada[numeroQuarto - 1] + "\n" +
                    "Saida: " + saida[numeroQuarto - 1]);
        } else {
            Entrada.leiaString("Quarto vazio.");
        }
    }

    public static void atualizarReserva(String[] nomes, String[] dtEntrada, String[] dtSaida) {
        int numeroQuarto = Entrada.leiaInt("===== Atualização de Reserva =====\n" +
                "Digite o número do quarto: ");

        if (verificarQuartoInvalido(numeroQuarto,nomes)) {
            return;
        }
        if (!verificarQuartoOcupado(numeroQuarto,nomes)) {
            Entrada.leiaString("Não há reserva para este quarto");
            return;
        }

        if (nomes[numeroQuarto - 1] != null) {
            String nomeHospede = Entrada.leiaString("===== Cadastro de Reserva (1/3)=====\n" +
                    "Digite o nome do hóspede: ");

            if (nomeHospede.equals("null")) {
                Entrada.leiaString("Nome inválido!");
                return;
            }

            String inputEntrada = Entrada.leiaString("===== Cadastro de Reserva (2/3)=====\n" +
                    "Digite a data de entrada: ", dtEntrada[numeroQuarto - 1]);

            while (dataInvalida(inputEntrada)){
                inputEntrada = Entrada.leiaString("===== Cadastro de Reserva (2/3)=====\n" +
                        "Data inválida! " +
                        "Digite a data de entrada: ");
            }

            String inputSaida = Entrada.leiaString("===== Cadastro de Reserva (3/3)=====\n" +
                    "Digite a data de saída: ", dtSaida[numeroQuarto - 1]);

            boolean dataValida = false;

            while (!dataValida){
                if (dataInvalida(inputSaida)) {
                    inputSaida = Entrada.leiaString("===== Cadastro de Reserva (4/4)=====\n" +
                            "Data inválida! " +
                            "Digite a data de saída: ");
                    continue;
                }
                if (saidaInvalida(inputEntrada,inputSaida)){
                    inputSaida = Entrada.leiaString("===== Cadastro de Reserva (4/4)=====\n" +
                            "Data anterior a entrada! " +
                            "Digite a data de saída: ");
                    continue;
                }
                dataValida = true;
            }

            dtEntrada[numeroQuarto - 1] = inputEntrada;
            dtSaida[numeroQuarto - 1] = inputSaida;
            nomes[numeroQuarto - 1] = nomeHospede;

            Entrada.leiaString("Reserva atualizada com sucesso.");
        } else {
            Entrada.leiaString("Quarto vazio. Não é possível atualizar a reserva.");
        }
    }

    public static void cancelarReserva(String[] nomes, String[] dtEntrada, String[] dtSaida) {
        int numeroQuarto = Entrada.leiaInt("===== Cancelamento de Reserva =====\n" +
                "Digite o número do quarto: ");
        if (verificarQuartoInvalido(numeroQuarto,nomes)) {
            return;
        }
        if (nomes[numeroQuarto - 1] != null) {
            nomes[numeroQuarto - 1] = null;
            dtEntrada[numeroQuarto - 1] = null;
            dtSaida[numeroQuarto - 1] = null;
            Entrada.leiaString("Reserva cancelada com sucesso.");
        } else {
            Entrada.leiaString("Quarto vazio. Não é possível cancelar a reserva.");
        }
    }

    public static void exibirRelatorio(String[] nomes) {
        StringBuilder vagos = new StringBuilder();
        StringBuilder ocupados = new StringBuilder();
        for (int i=0;i<nomes.length;i++){
            if (nomes[i] != null)
                ocupados.append(i + 1).append(" ");
            else
                vagos.append(i + 1).append(" ");
        }
        Entrada.leiaString("===== Relatório de Reservas =====\n" +
                "Quartos locados: " + ocupados+ "\n" +
                "Quartos livres: " + vagos);
    }

    public static int quantidadeQuartosDisponiveis(String[] nomes) {
        int contador = 0;
        for (String nome : nomes) {
            if (nome == null || nome.equals("null")) {
                contador++;
            }
        }
        return contador;
    }

    public static boolean verificarQuartoInvalido(int numeroQuarto, String[] nomes) {
        boolean bool = (numeroQuarto <= 0 || numeroQuarto > nomes.length);
        if (bool) {
            Entrada.leiaString("Número de quarto inválido.");
            return true;
        }
        return false;
    }

    public static boolean verificarQuartoOcupado(int numeroQuarto, String[] nomes) {
        return (nomes[numeroQuarto - 1] != null);
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

    public static void carregarReservasDoArquivo(String[] nomes, String[] dtEntrada, String[] dtSaida) {
        Arquivo arquivo = new Arquivo("reservas.txt");

        if (arquivo.abrirLeitura()) {
            String linha = arquivo.lerLinha();
            while (linha != null) {
                String[] arrNome = linha.split(";")[0].split(",");
                String[] arrEnt = linha.split(";")[1].split(",");
                String[] arrSai = linha.split(";")[2].split(",");
                for (int i=0; i<nomes.length;i++) {
                    if (arrNome[i].equals("null"))
                        nomes[i] = null;
                    else
                        nomes[i] = arrNome[i];
                }
                for (int i=0; i<dtEntrada.length;i++) {
                    if (arrEnt[i].equals("null"))
                        arrEnt[i] = null;
                    else
                        dtEntrada[i] = arrEnt[i];
                }
                for (int i=0; i<dtSaida.length;i++) {
                    if (arrSai[i].equals("null"))
                        dtSaida[i] = null;
                    else
                        dtSaida[i] = arrSai[i];
                }
                linha = arquivo.lerLinha();
            }
        }
        arquivo.fecharArquivo();
    }

    public static void salvarReservasNoArquivo(String[] nomes, String[] entrada, String[] saida) {
        Arquivo arquivo = new Arquivo("reservas.txt");
        String str = "";
        StringBuilder aux = new StringBuilder();
        if (arquivo.abrirEscrita()) {
            for (String i : nomes) {
                aux.append(i).append(",");
            }
            str = str+aux.substring(0,aux.length()-1)+";";
            aux = new StringBuilder();
            for (String i : entrada) {
                aux.append(i).append(",");
            }
            str = str+aux.substring(0,aux.length()-1)+";";
            aux = new StringBuilder();
            for (String i : saida) {
                aux.append(i).append(",");
            }
            str = str+aux.substring(0,aux.length()-1);
            //aux = new StringBuilder();
        }
        arquivo.escreverLinha(str);
        arquivo.fecharArquivo();
    }
}
