/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import edu.princeton.cs.algs4.FlowEdge;

/**
 * Class ligacao que permite ligar um flowEdge a uma distancia para o disktra
 *
 * @author pedro & elisio
 */
public class Ligacao extends FlowEdge {

    private double distancia;

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public Ligacao(int v, int w, double capacity) {
        super(v, w, capacity);
    }

    public Ligacao(FlowEdge e, double distancia) {
        super(e);
        this.distancia = distancia;

    }

    public Ligacao(int v, int w, double capacity, double flow, double distance) {
        super(v, w, capacity, flow);
        this.distancia = distancia;
    }
}
