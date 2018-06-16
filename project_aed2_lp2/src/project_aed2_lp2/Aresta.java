/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

/**
 *
 * @author elisi
 */
public class Aresta {
    
     public Aresta() {
        
    }
    
    private Vertice origem;//no origem
    private Vertice destino;// No destino
    private int peso;// peso

    //getters setters

    public Vertice getOrigem() {
        return origem;
    }

    public Vertice getDestino() {
        return destino;
    }

    public int getPeso() {
        return peso;
    }

    public void setOrigem(Vertice origen) {
        this.origem = origen;
    }

    public void setDestino(Vertice destino) {
        this.destino = destino;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }
    
}
