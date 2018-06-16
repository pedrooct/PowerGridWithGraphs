/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.RedBlackBST;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * Classe Central Eletrica
 *
 * @author Pedro Costa & Elisio Pinheiro
 */
public class CentralEletrica extends Node implements FileFunctions, java.io.Serializable {

    private String zona;
    private Float wattsProduzidos;
    private RedBlackBST<Long, FlowEdge> STtime = new RedBlackBST<>();

    public CentralEletrica(Float wattsProduzidos, String zona) {
        super();
        this.wattsProduzidos = wattsProduzidos;
        this.zona = zona;
    }

    public CentralEletrica(Integer id, Float wattsProduzidos, String zona) {
        super(id);
        this.wattsProduzidos = wattsProduzidos;
        this.zona = zona;
        this.logger("central eletrica criada: " + id);
    }

    public Float getWattsProduzidos() {
        return wattsProduzidos;
    }

    public void setWattsProduzidos(Float wattsProduzidos) {
        this.wattsProduzidos = wattsProduzidos;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void removeSTtime(FlowEdge e) {
        for (Long key : this.STtime.keys()) {
            if (this.STtime.get(key).equals(e)) {
                this.STtime.delete(key);
            }
        }
    }

    public RedBlackBST<Long, FlowEdge> getSTtime() {
        return STtime;
    }

    public void setSTtime(Long time, FlowEdge e) {
        this.STtime.put(time, e);
    }

    /**
     * Verifica se ainda aguante com mais alguma casa ! atinge maximo de
     * segurança de 80%
     *
     * @return
     */
    public boolean canHandle() {
        if (capacityPercentage() < 80) {
            return true;
        }
        return false;
    }

    /**
     * Calcula a carga atual com base na redblack de flowEdge
     *
     * @return
     */
    public Float getPowerUsage() {
        float power = 0;
        for (Long key : this.STtime.keys()) {
            power += this.STtime.get(key).flow();
        }
        return power;
    }

    /**
     * Devolve a percentagem a que se encontra a fonte
     *
     * @return
     */
    public Float capacityPercentage() {
        Float power = getPowerUsage();
        return (power * 100) / this.getWattsProduzidos();
    }

    public String printST() {
        String s = "";

        for (Long key : this.STtime.keys()) {
            Timestamp timestamp = new Timestamp(key);
            s += "Data da conexao:" + timestamp + "\nLigado ao Nó " + this.STtime.get(key).to() + " \ncom capacidade de " + this.STtime.get(key).capacity() + "\ne com flow de " + this.STtime.get(key).flow();
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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try (FileWriter fw = new FileWriter(".//data//logFile.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)) {
            out.println(timestamp + msg);
        } catch (IOException e) {
            System.out.println("Exception thrown  :" + e);
        }
    }
}
