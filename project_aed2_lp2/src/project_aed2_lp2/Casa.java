/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Classe Casa !
 *
 * @author Pedro Costa & Elisio Pinheiro
 */
public class Casa extends Node implements FileFunctions, java.io.Serializable {

    private ArrayList consumoTotalHistorico = new ArrayList();
    private Float potenciaContratada;
    private Float potenciaAtual;
    private String Zona;
    private SeparateChainingHashST<Integer, Equipamento> STEquipamento = new SeparateChainingHashST<>();
    private RedBlackBST<Long, Integer> STtime = new RedBlackBST<>();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    /**
     * Usado para ficheiros, pois cria com ID já de origem !
     *
     * @param id
     * @param consumoTotal
     * @param potenciaContratada
     * @param Zona
     */
    public Casa(Integer id, float potenciaContratada, String Zona) {
        super(id);
        this.potenciaContratada = potenciaContratada;
        this.Zona = Zona;
        this.potenciaAtual = (float) 0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.logger(timestamp + " -Casa " + super.getId() + " criada");
    }

    /**
     *
     * @param id
     * @param potenciaContratada
     * @param zona
     */
    public Casa(float potenciaContratada, String zona) {
        super();
        this.potenciaContratada = potenciaContratada;
        this.Zona = zona;
        this.potenciaAtual = (float) 0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.logger(timestamp + " -Casa " + super.getId() + " criada");
    }

    /**
     *
     * @return
     */
    public String getZona() {
        return Zona;
    }

    public void setZona(String Zona) {
        this.Zona = Zona;
    }

    public float getPotenciaContratada() {
        return potenciaContratada;
    }

    public void setPotenciaContratada(float potenciaContratada) {
        this.potenciaContratada = potenciaContratada;
    }

    public Float getPotenciaAtual() {
        return potenciaAtual;
    }

    public void setPotenciaAtual(Float potenciaAtual) {
        this.potenciaAtual = potenciaAtual;
    }

    @Override
    public Integer getId() {
        return super.getId();
    }

    public ArrayList getConsumoTotalHistorico() {
        return consumoTotalHistorico;
    }

    public void setConsumoTotalHistorico(float consumo) {
        this.consumoTotalHistorico.add(consumo);
    }

    public SeparateChainingHashST<Integer, Equipamento> getSTEquipamento() {
        return STEquipamento;
    }

    public void setSTEquipamento(Equipamento e) {
        e.setIDcasa(this.getId());
        this.STEquipamento.put(e.getId(), e);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.logger(timestamp + "-Equipamento " + e.getNome() + " adicionado a casa " + this.getId());

    }

    public RedBlackBST<Long, Integer> getSTtime() {
        return STtime;
    }

    public void setSTtime(Long time, Integer e) {
        this.STtime.put(time, e);
    }

    public void deleteSTEquipamento(Integer id) {
        this.STEquipamento.delete(id);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.logger(timestamp + "-Equipamento" + id + " eliminado da casa " + this.getId());
    }

    //imprimir todos os equipamentos de uma casa
    public String printEquipamentos() {
        String s = "Equipamentos:\n";

        for (Integer key : this.STEquipamento.keys()) {
            s = s + "[" + this.STEquipamento.get(key).getNome() + "] - ";
            if (this.STEquipamento.get(key).getTipo() == 0) {
                s = s + "Consumidor: " + this.STEquipamento.get(key).getWattsConsumo() + "W - ";
            } else {
                s = s + "Gerador: " + this.STEquipamento.get(key).getWattsGerado() + "W - ";
            }

            if (this.STEquipamento.get(key).getLigado() == 1) {
                s = s + "[ON]\n";
            } else {
                s = s + "[OFF]\n";
            }

        }
        return s;
    }

    //imprimir historico de tmepos
    public void printTimeStamp() {
        Timestamp t;
        for (Long key : this.STtime.keys()) {

            t = new Timestamp(key);
            System.out.println("ligou as: " + t + " -> " + this.STtime.get(key));

        }
    }

    //editar nome de um equipamento
    public boolean editaEquipamento(Integer id, String nome) {
        if (this.STEquipamento.contains(id)) {
            this.STEquipamento.get(id).setNome(nome);
            return true;
        }
        return false;
    }

    /**
     * verificar se excede a potencia atual dada pela rede
     *
     * @param id
     * @return
     */
    public boolean isOverload(Integer id) {
        float consumoAtual = this.calcularWattsEquipamentosLigados();
        consumoAtual += this.STEquipamento.get(id).getWattsConsumo();
        if (consumoAtual > this.potenciaAtual) {
            return false;
        }
        return true;
    }

    /**
     * desligar todos os equipamentos
     *
     * @throws InterruptedException
     */
    public void emergencyShutdown() throws InterruptedException {
        for (Integer key : this.STEquipamento.keys()) {
            if (this.STEquipamento.get(key).getLigado() == 1) {
                this.powerOff(this.STEquipamento.get(key).getId(), 2);
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                this.logger(timestamp + "Emergency shutodwn");
            }
        }
    }

    /**
     * ligar um equipamento numa casa
     *
     * @param id
     * @return
     * @throws InterruptedException
     */
    public boolean powerOn(Integer id) throws InterruptedException {
        //se nao existir equipamento na casa, return
        if (!this.STEquipamento.contains(id)) {
            return false;
        }
        if (this.getSTEquipamento().get(id).getLigado() == 1) {
            return false;
        }
        //se nao exceder potencia contratada, ligar
        if (this.STEquipamento.get(id).getTipo() == 0) {
            if (!this.isOverload(id)) {
                return false;
            } else {
                this.STEquipamento.get(id).setLigado(1);
                System.out.println("[" + this.STEquipamento.get(id).getNome() + "] FOI LIGADO NA [CASA " + this.getId() + "]");

                //adicionar à ST time
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Long unixT = timestamp.getTime();
                this.setSTtime(unixT, id);

                this.logger(timestamp + "-Equipamento " + id + " ligado na casa " + this.getId());
                return true;
            }
        }

        //se for gerador
        if (this.STEquipamento.get(id).getTipo() == 1) {

            //ligar equipamento
            this.STEquipamento.get(id).setLigado(1);
            System.out.println("[" + this.STEquipamento.get(id).getNome() + "] FOI LIGADO NA [CASA " + this.getId() + "]");

            //adicionar para a ST time
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long unixT = timestamp.getTime();
            this.setSTtime(unixT, id);

            this.logger(timestamp + "- Equipamento" + id + " ligado na casa " + this.getId());
            return true;
        }

        return false;
    }

    /**
     * Devolve o consumo medio da casa
     *
     * @return
     */
    public float consumoMedioCasa() {
        float consumo = 0;
        float media = 0;
        for (Integer key : this.STEquipamento.keys()) {
            for (int i = 0; i < this.STEquipamento.get(key).getWattsHistorico().size(); i++) {
                consumo += (float) this.STEquipamento.get(key).getWattsHistorico().get(i);
            }
            media += consumo / this.STEquipamento.get(key).getWattsHistorico().size();
        }
        return media;
    }

    /**
     * Devolve a media de nergia gerada
     *
     * @return
     */
    public float geradoMediaCasa() {
        float consumo = 0;
        for (Integer key : this.STEquipamento.keys()) {
            if (this.STEquipamento.get(key).getTipo() == 1) {
                consumo += this.STEquipamento.get(key).getWattsGerado();
            }

        }
        return consumo;
    }

    /**
     * Devolve o consumo medio de um equipamento
     *
     * @param id
     * @return
     */
    public float consumoMedioEquipamento(Integer id) {
        float consumo = 0;
        float media = 0;
        for (int i = 0; i < this.STEquipamento.get(id).getWattsHistorico().size(); i++) {
            consumo += (float) this.STEquipamento.get(id).getWattsHistorico().get(i);
        }
        media += consumo / this.STEquipamento.get(id).getWattsHistorico().size();
        return media;
    }

    /**
     * Desliga equipamento
     *
     * @param id
     * @param time
     * @return
     * @throws InterruptedException
     */
    public boolean powerOff(Integer id, Integer time) throws InterruptedException {
        //se nao existir equipamento na casa, return
        if (!this.STEquipamento.contains(id)) {
            return false;
        }

        if (this.STEquipamento.get(id).getLigado() == 1) {
            //desligar equipamento
            this.STEquipamento.get(id).setLigado(0);

            //adicionar para a ST time
            TimeUnit.SECONDS.sleep(time);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long unixT = timestamp.getTime();

            //colocar id negativo
            Integer ide = id * (-1);
            this.setSTtime(unixT, ide);

            //calcular consumo deste equipamento
            System.out.println("\n[" + this.STEquipamento.get(id).getNome() + "] FOI DESLIGADO NA [CASA " + this.getId() + "]");
            //calcularWattsConsumidos(id);
            lastWattsConsumed(ide);
            this.logger(timestamp + "-Equipamento " + id + " desligado na casa " + this.getId());
            return true;

        }
        return false;
    }

    /**
     * Calcular watts TOTAIS consumidos de um equipamento
     *
     * @param e
     */
    public void calcularWattsConsumidos(Integer e) {

        float consumo = 0;
        int totalSeconds = 0;
        int flag = 0;
        int flagtotal = 0;
        int count = 0;
        Timestamp end = null;
        Timestamp start = null;

        Integer equipamento = e * (-1);

        for (Long key : this.STtime.keys()) {
            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {
                count++;
            }
        }

        for (Long key : this.STtime.keys()) {

            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {

                flag++;
                flagtotal++;

                if (flag == 1) { //primeiro timestamp que encontra é o tempo inicial
                    start = new Timestamp(key);
                } else if (flag > 1 && flagtotal < count) { //segundo timestamp que encontra é o tempo que foi desligado
                    end = new Timestamp(key);
                    long tempo = end.getTime() - start.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                    int seconds = (int) tempo / 1000;
                    seconds = (seconds % 3600) % 60;//passar para segundos
                    totalSeconds += seconds;
                    consumo += (this.STEquipamento.get(e).getWattsConsumo() * seconds) / 3600;
                    flag = 0; //reset flag
                } else if (flag > 1 && flagtotal == count) { //se for o ultimo tempo existente na ST
                    end = new Timestamp(key);
                    long tempo = end.getTime() - start.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                    int seconds = (int) tempo / 1000;
                    seconds = (seconds % 3600) % 60;//passar para segundos
                    totalSeconds += seconds;
                    consumo += (this.STEquipamento.get(e).getWattsConsumo() * seconds) / 3600;
                    flag = 0; //reset flag
                    break;
                }
            }
        }

        System.out.println("\n" + this.STEquipamento.get(e).getNome() + " esteve ligado durante: " + totalSeconds + " segundos no TOTAL.");
        System.out.println("CONSUMIU: " + consumo + " Watts\n");
    }

    /**
     * Calcular watts TOTAIS gerados de um equipamento
     *
     * @param e
     */
    public void calcularWattsGerados(Integer e) {

        float gerado = 0;
        int totalSeconds = 0;
        int flag = 0;
        int flagtotal = 0;
        int count = 0;
        Timestamp end = null;
        Timestamp start = null;

        Integer equipamento = e * (-1);

        //se for um consumidor, sair
        if (this.STEquipamento.get(e).getTipo() == 0) {
            return;
        }

        for (Long key : this.STtime.keys()) {
            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {
                count++;
            }
        }

        for (Long key : this.STtime.keys()) {

            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {

                flag++;
                flagtotal++;

                if (flag == 1) { //primeiro timestamp que encontra é o tempo inicial
                    start = new Timestamp(key);
                } else if (flag > 1 && flagtotal < count) { //segundo timestamp que encontra é o tempo que foi desligado
                    end = new Timestamp(key);
                    long tempo = end.getTime() - start.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                    int seconds = (int) tempo / 1000;
                    seconds = (seconds % 3600) % 60;//passar para segundos
                    totalSeconds += seconds;
                    gerado += (this.STEquipamento.get(e).getWattsGerado() * seconds) / 3600;
                    flag = 0; //reset flag
                } else if (flag > 1 && flagtotal == count) { //se for o ultimo tempo existente na ST
                    end = new Timestamp(key);
                    long tempo = end.getTime() - start.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                    int seconds = (int) tempo / 1000;
                    seconds = (seconds % 3600) % 60;//passar para segundos
                    totalSeconds += seconds;
                    gerado += (this.STEquipamento.get(e).getWattsGerado() * seconds) / 3600;
                    flag = 0; //reset flag
                    break;
                }
            }
        }

        System.out.println("\n" + this.STEquipamento.get(e).getNome() + " esteve ligado durante: " + totalSeconds + " segundos no TOTAL.");
        System.out.println("GEROU: " + gerado + " Watts\n");
    }

    /**
     * função para calcular watts consumidos a ultima vez que esteve ligado
     *
     * @param e
     */
    public void lastWattsConsumed(Integer e) {

        //Integer equipamento = Math.abs(e);
        float consumo = 0;
        int flag = 0;
        Timestamp end = null;
        Timestamp start = null;
        int count = 0;
        int seconds = 0;

        Integer equipamento = Math.abs(e);

        //contar numero de tempos do equipamento
        for (Long key : this.STtime.keys()) {
            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {
                count++;
            }
        }

        for (Long key : this.STtime.keys()) {

            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {

                flag++;

                if (flag == count - 1) { //primeiro timestamp que encontra é o tempo inicial
                    start = new Timestamp(key);
                    System.out.println(" ->> LIGOU AS:    " + start);
                } else if (flag == count) { //segundo timestamp que encontra é o tempo que foi desligado
                    end = new Timestamp(key);
                    System.out.println(" ->> DESLIGOU AS: " + end);
                    long tempo = end.getTime() - start.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                    seconds = (int) tempo / 1000;
                    seconds = (seconds % 3600) % 60;//passar para segundos
                    consumo = (this.STEquipamento.get(equipamento).getWattsConsumo() * seconds) / 3600;
                    this.STEquipamento.get(equipamento).setWattsHistorico(consumo);
                }
            }
        }

        System.out.println(" ->> " + this.STEquipamento.get(equipamento).getNome() + " esteve ligado durante: " + seconds + " segundos.");
        System.out.println(" ->> CONSUMIU: " + consumo + " Watts\n");
    }

    /**
     * função para calcular watts gerados a ultima vez que esteve ligado
     *
     * @param e
     */
    public void lastWattsGenerated(Integer e) {

        float gerado = 0;
        int flag = 0;
        Timestamp end = null;
        Timestamp start = null;
        int count = 0;
        int seconds = 0;

        Integer equipamento = Math.abs(e);

        //se for equipamento consumidor sair
        if (this.STEquipamento.get(e).getTipo() == 0) {
            return;
        }

        //contar numero de tempos do equipamento
        for (Long key : this.STtime.keys()) {
            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {
                count++;
            }
        }

        for (Long key : this.STtime.keys()) {

            if (this.STtime.get(key).compareTo(equipamento) == 0 || this.STtime.get(key).compareTo(e) == 0) {

                flag++;

                if (flag == count - 1) { //primeiro timestamp que encontra é o tempo inicial
                    start = new Timestamp(key);
                    System.out.println(" ->> LIGOU AS:    " + start);
                } else if (flag == count) { //segundo timestamp que encontra é o tempo que foi desligado
                    end = new Timestamp(key);
                    System.out.println(" ->> DESLIGOU AS: " + end);
                    long tempo = end.getTime() - start.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                    seconds = (int) tempo / 1000;
                    seconds = (seconds % 3600) % 60;//passar para segundos
                    gerado = (this.STEquipamento.get(equipamento).getWattsGerado() * seconds) / 3600;
                }
            }
        }

        System.out.println(" ->> " + this.STEquipamento.get(equipamento).getNome() + " esteve ligado durante: " + seconds + " segundos.");
        System.out.println(" ->> GEROU: " + gerado + " Watts\n");
    }

    public float calcularWattsEquipamentosLigados() {
        float consumo = 0;
        for (Integer key : this.STEquipamento.keys()) {
            if (this.STEquipamento.get(key).getLigado() == 1 && this.STEquipamento.get(key).getTipo() == 0) {
                consumo += this.STEquipamento.get(key).getWattsConsumo();
            }
        }
        return consumo;
    }

    public float calcularWattsGeradoresLigados() {
        float gerado = 0;
        for (Integer key : this.STEquipamento.keys()) {
            if (this.STEquipamento.get(key).getLigado() == 1 && this.STEquipamento.get(key).getTipo() == 1) {
                gerado += this.STEquipamento.get(key).getWattsGerado();
            }
        }
        return gerado;
    }

    /**
     * Devolve estimativa de consumo total num perido de tempo
     *
     * @param inicio
     * @param fim
     * @return
     */
    public float calcularWattsBetween(Timestamp inicio, Timestamp fim) {

        float consumo = 0;
        int flag = 0;
        Timestamp data;
        Timestamp ligou = null;
        Timestamp desligou = null;
        Integer equipamento;

        for (Integer key : this.STEquipamento.keys()) {//percorrer todos equipanentos

            for (Long time : this.STtime.keys()) {//percorrer toda a ST e calcular o consumo total de cada equipamento

                equipamento = this.STEquipamento.get(key).getId() * (-1);//incluir os powerOFFs

                if (this.STtime.get(time).compareTo(equipamento) == 0 || this.STtime.get(time).compareTo(this.STEquipamento.get(key).getId()) == 0) {

                    data = new Timestamp(time);

                    if (data.after(inicio) && data.before(fim)) { //verificar se a data do registo está entre inicio e fim

                        if (flag == 0 && this.STtime.get(time) > 0) { //verificar se é um powerON                            
                            ligou = new Timestamp(time);
                            flag++;
                        } else if (flag == 1 && this.STtime.get(time) < 0) {//verificar se é um powerOFF 
                            desligou = new Timestamp(time);

                            long tempo = desligou.getTime() - ligou.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                            int seconds = (int) tempo / 1000;
                            seconds = (seconds % 3600) % 60;//passar para segundos
                            consumo += (this.STEquipamento.get(this.STEquipamento.get(key).getId()).getWattsConsumo() * seconds) / 3600;

                            flag = 0;
                        }
                    }

                }
            }
        }

        System.out.println("\nCONSUMO ENTRE: " + inicio + " & " + fim + " DA [CASA " + this.getId() + "]: \n ->> " + consumo + " Watts."); //calcular

        return consumo;
    }

    /**
     * Devolve estimativa de consumo total num perido de tempo
     *
     * @param inicio
     * @param fim
     * @return
     */
    public float calcularWattsGeradosBetween(Timestamp inicio, Timestamp fim) {

        float gerado = 0;
        int flag = 0;
        Timestamp data;
        Timestamp ligou = null;
        Timestamp desligou = null;
        Integer equipamento;

        for (Integer key : this.STEquipamento.keys()) {//percorrer todos equipanentos

            //verificar se é um gerador
            if (this.STEquipamento.get(key).getTipo() == 1) {

                for (Long time : this.STtime.keys()) {//percorrer toda a ST e calcular os watts total  gerados de cada equipamento

                    equipamento = this.STEquipamento.get(key).getId() * (-1);//incluir os powerOFFs

                    if (this.STtime.get(time).compareTo(equipamento) == 0 || this.STtime.get(time).compareTo(this.STEquipamento.get(key).getId()) == 0) {

                        data = new Timestamp(time);

                        if (data.after(inicio) && data.before(fim)) { //verificar se a data do registo está entre inicio e fim

                            if (flag == 0 && this.STtime.get(time) > 0) { //verificar se é um powerON                            
                                ligou = new Timestamp(time);
                                flag++;
                            } else if (flag == 1 && this.STtime.get(time) < 0) {//verificar se é um powerOFF 
                                desligou = new Timestamp(time);

                                long tempo = desligou.getTime() - ligou.getTime(); //tempo que foi desligado - tempo que foi ligado = tempo que esteve a consumir
                                int seconds = (int) tempo / 1000;
                                seconds = (seconds % 3600) % 60;//passar para segundos
                                gerado += (this.STEquipamento.get(this.STEquipamento.get(key).getId()).getWattsGerado() * seconds) / 3600;

                                flag = 0;
                            }
                        }

                    }
                }
            }
        }

        System.out.println("\nCONSUMO ENTRE: " + inicio + " & " + fim + " DA [CASA " + this.getId() + "]: \n ->> " + gerado + " Watts."); //calcular

        return gerado;
    }

    /**
     * Equipamento que consome mais
     *
     * @return
     */
    public String BiggestConsumer() {
        String s = "";
        float max = -1;
        float aux;
        int count = 0;
        for (Integer key : this.STEquipamento.keys()) {
            if (this.STEquipamento.get(key).getLigado() == 1) {
                count++;
                aux = this.STEquipamento.get(key).getWattsConsumo();
                if (aux > max) {
                    max = aux;
                    s = "Equipamento " + this.STEquipamento.get(key).getNome() + " ,consumo total de " + max;
                }
            }
        }
        
        if (count == 0){
            s = "Não existem equipamentos ligados na casa! ";
        }
        return s;
    }

    /**
     * Equipamento que consome menos
     *
     * @return
     */
    public String lowestConsumer() {
        String s = "";
        float min = 1000000000;
        float aux;
        for (Integer key : this.STEquipamento.keys()) {
            aux = this.STEquipamento.get(key).totalConsumeHistory();
            if (aux < min) {
                min = aux;
                s = "Equipamento " + this.STEquipamento.get(key).getNome() + " ,consumo total de " + min;
            }
        }
        return s;
    }

    public String printHistoricoConsumoEquipamento(Integer e) {
        String s = null;
        if (!this.STEquipamento.contains(e)) {
            return "não existe";
        }
        s = "consumos equipamento " + this.STEquipamento.get(e).getNome() + ": ";
        for (int i = 0; i < this.STEquipamento.get(e).getWattsHistorico().size(); i++) {
            s += this.STEquipamento.get(e).getWattsHistorico().get(i) + " ";
        }
        return s;

    }

    @Override
    public Equipamento LoadFromFileEquipemanto(Integer ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Casa LoadFromFileCasa(Integer ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CentralEletrica LoadFromFileCentralEletrica(Integer ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FonteEnergetica LoadFromFileFonteEnergetica(Integer ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void SaveToFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void logger(String msg) {
        try (FileWriter fw = new FileWriter(".//data//logFile.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(msg);
            //more code
        } catch (IOException e) {
            System.out.println("Exception thrown  :" + e);
        }
    }
}
