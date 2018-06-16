/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Classe Equipamento
 * @author Pedro Costa & Elisio Pinheiro
 */
public class Equipamento implements FileFunctions,java.io.Serializable {

    private Integer id;
    private String nome;
    private Integer tipo; // 0 - consumidor 1- gerador
    private Integer IDcasa;
    private float wattsConsumo;
    private ArrayList wattsHistorico = new ArrayList();
    private float wattsGerado;
    private Integer ligado; // 0-desligado 1- ligado

    //private SeparateChainingHashST<Integer, Data> STlog = new SeparateChainingHashST<>();
    //USADO PARA FICHEIRO
    public Equipamento(Integer id, String nome, Integer tipo, Integer IDcasa, float wattsConsumo, float wattsGerado, Integer ligado) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.IDcasa = IDcasa;
        this.wattsConsumo = wattsConsumo; // por hora 
        this.wattsGerado = wattsGerado; // por hora
        this.ligado = ligado;
    }

    public Equipamento(Integer id, String nome, Integer tipo, float wattsConsumo, float wattsGerado) {
        if (tipo == 1) {
            equipamentoGerador(id, nome, tipo, wattsGerado);
        } else if (tipo == 0) {
            equipamentoConsumidor(id, nome, tipo, wattsConsumo);
        }
    }

    private void equipamentoGerador(Integer id, String nome, Integer tipo, float wattsGerado) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.wattsGerado = wattsGerado;
        this.ligado = 0;
        this.logger("Equipamento" + this.nome + "criado");
    }

    private void equipamentoConsumidor(Integer id, String nome, Integer tipo, float wattsConsumo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.wattsConsumo = wattsConsumo;
        this.ligado = 0;
        this.logger("Equipamento " + this.nome + " criado");
    }

    public Integer getLigado() {
        return ligado;
    }

    public void setLigado(Integer ligado) {
        this.ligado = ligado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getIDcasa() {
        return IDcasa;
    }

    public void setIDcasa(Integer IDcasa) {
        this.IDcasa = IDcasa;
    }

    public float getWattsConsumo() {
        return wattsConsumo;
    }

    public void setWattsConsumo(float wattsConsumo) {
        this.wattsConsumo = wattsConsumo;
    }

    public float getWattsGerado() {
        return wattsGerado;
    }

    public void setWattsGerado(float wattsGerado) {
        this.wattsGerado = wattsGerado;
    }

    public ArrayList getWattsHistorico() {
        return wattsHistorico;
    }

    public void setWattsHistorico(float consumo) {
        this.wattsHistorico.add(consumo);
    }

    public String printMyInfo() {
        String s = "       [" + this.getId() + "] - " + this.getNome() + " - ";
        
        if (this.tipo == 0) {
            s = s + "Consumidor (" + this.getWattsConsumo() + " W/h)\n";
        } else {
            s = s + "Gerador (" + this.getWattsGerado() + " W/h)\n";
        }
        //s = s + "ID da casa \n" + this.getIDcasa() + "\n";
        return s;
    }

    public float totalConsumeHistory() {
        float total = 0;
        for (int i = 0; i < this.wattsHistorico.size(); i++) {
            total += (float) this.wattsHistorico.get(i);
        }
        return total;
    }

    @Override
    public Equipamento LoadFromFileEquipemanto(Integer ID) {
        Equipamento e = null;
        In in = new In(".//data//equipamentos.txt"); // abertura do ficheiro/stream de entrada     
        while (!in.isEmpty()) {
            String[] splitStr = in.readLine().split(";");
            Integer id = Integer.parseInt(splitStr[0]);
            String nome = splitStr[1];
            Integer tipo = Integer.parseInt(splitStr[2]);
            Integer IDcasa = Integer.parseInt(splitStr[3]);
            float wattsconsumo = Float.parseFloat(splitStr[4]);
            float wattsgerado = Float.parseFloat(splitStr[5]);
            if (id == ID) {
                e = new Equipamento(id, nome, tipo, wattsconsumo, wattsgerado);
                e.setIDcasa(IDcasa);
                return e;
            }
        }
        return e;
    }

    @Override
    public void SaveToFile() {
        Out out = new Out(".//data//equipamentos.txt");
        out.println(this.id + ";" + this.nome + ";" + this.tipo + ";" + this.IDcasa + ";" + this.wattsConsumo + ";" + this.wattsGerado + ";" + this.ligado + ";");
        out.println();
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
    public void logger(String msg) {
        try (FileWriter fw = new FileWriter(".//data//logFile.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(msg);
        } catch (IOException e) {
            System.out.println("Exception thrown  :" + e);
        }
    }
}
