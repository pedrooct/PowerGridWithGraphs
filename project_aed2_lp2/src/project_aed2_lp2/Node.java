/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import java.awt.Point;

/**
 * Permite atribuir um ID unico a uma casa, Postos e equipamentos / começa em dois para reservar os vertices no grafo 0 e 1 
 * @author Pedro Costa & Elisio Pinheiro
 */
public class Node implements java.io.Serializable {

    private Integer id;
    static Integer counter = 2;
    private Point ponto;

    public Node() {
        this.id = counter;
        counter++;
    }
    
    /**
     * Verifica se é o menor ID a entrar !
     * @param id
     */
    public Node(Integer id) {
        this.id = id;
        if (id >= counter) {
            counter = id;
            counter++;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Point getPonto() {
        return ponto;
    }   

    public void setPonto(Point ponto) {
        this.ponto = ponto;
    }
}
