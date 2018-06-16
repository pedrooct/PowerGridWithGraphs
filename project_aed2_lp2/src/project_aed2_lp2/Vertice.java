/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import java.awt.Point;

/**
 *
 * @author elisi
 */
public class Vertice {
    
    public Vertice() {
        ponto = new Point();
    }
    
    private Integer id;
    private Point ponto;

    //getters setters

 
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Point getPonto() {
        return ponto;
    }

   

    public void setPonto(Point ponto) {
        this.ponto = ponto;
    }
    
}
