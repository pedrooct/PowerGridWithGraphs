/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

/**
 *
 * @author Pedro Costa & Elisio Pinheiro
 */
public interface FileFunctions {
    
    public Equipamento LoadFromFileEquipemanto(Integer ID);
    public Casa LoadFromFileCasa(Integer ID);
    public CentralEletrica LoadFromFileCentralEletrica(Integer ID);
    public FonteEnergetica LoadFromFileFonteEnergetica(Integer ID);
    public void SaveToFile();
    public void logger(String msg);
}
