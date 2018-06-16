/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project_aed2_lp2;

import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pedro Costa & Elisio Pinheiro
 */
public class GestorMain {

    public static In inG = new In(".//data//grafo.txt");
    public static SeparateChainingHashST<Integer, Equipamento> equipamentosST = new SeparateChainingHashST<>();
    public static SeparateChainingHashST<Integer, Casa> casasST = new SeparateChainingHashST<>();
    public static SeparateChainingHashST<Integer, FonteEnergetica> fonteST = new SeparateChainingHashST<>();
    public static SeparateChainingHashST<Integer, CentralEletrica> centralST = new SeparateChainingHashST<>();
    public static FlowNetwork dg = new FlowNetwork(0);
    public static EdgeWeightedDigraph g = new EdgeWeightedDigraph(0);

    /**
     * Arraylist para armazenar arestas e pesos
     */
    public static ArrayList<Ligacao> ArL = new ArrayList<>();

    /**
     * Grava o arraylist de ligacao
     *
     * @param ar
     */
    public static void saveArLigacao(ArrayList<Ligacao> ar) {
        Out out = new Out(".//data//ligacao.txt");
        for (int i = 0; i < ar.size(); i++) {
            out.println(ar.get(i).from() + ";" + ar.get(i).to() + ";" + ar.get(i).flow() + ";" + ar.get(i).capacity() + ";" + ar.get(i).getDistancia() + ";");
        }
    }

    /**
     * Lê de um ficheiro a informação sobre o arraylist e grava no arraylist
     */
    public static void loadArLigacao() {
        In in = new In(".//data//ligacao.txt");
        while (!in.isEmpty()) {
            String[] splitStr = in.readLine().split(";");
            Integer from = Integer.parseInt(splitStr[0]);
            Integer to = Integer.parseInt(splitStr[1]);
            Double flow = Double.parseDouble(splitStr[2]);
            Double cap = Double.parseDouble(splitStr[3]);
            Double dist = Double.parseDouble(splitStr[4]);
            FlowEdge ed = new FlowEdge(from, to, cap, flow);
            Ligacao l = new Ligacao(ed, dist);
            ArL.add(l);
        }

    }

    /**
     * Funçaõ que permite criar um EdgeWeigthedGraph com base no grafo , sendo o
     * peso atribuido por o flow
     *
     * @param fn
     * @return
     */
    @Deprecated
    public static EdgeWeightedDigraph exportFlowToWDiGraph(FlowNetwork fn) {
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(fn.V());

        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    DirectedEdge k = new DirectedEdge(e.from(), e.to(), e.flow());
                    g.addEdge(k);
                }
            }
        }
        return g;
    }

    /**
     * Usa o arraylist de ligações para criar um grafo com distancias como pesos
     * para usar no diskstra
     *
     * @param ar
     * @return
     */
    public static EdgeWeightedDigraph createGraphDist(ArrayList<Ligacao> ar) {
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(dg.V());
        for (int i = 0; i < ar.size(); i++) {
            DirectedEdge e = new DirectedEdge(ar.get(i).from(), ar.get(i).to(), ar.get(i).getDistancia());
            g.addEdge(e);
        }
        return g;
    }

    /**
     * Permite atualizar o arrayList com objetos ligacao, tentando assim
     * garantir consistencia
     *
     * @param fn
     */
    public static void updateLigacao(FlowNetwork fn) {
        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                for (int j = 0; j < ArL.size(); j++) {
                    if (e.to() == ArL.get(j).to() && e.from() == ArL.get(j).from()) {
                        ArL.get(j).setFlow(e.flow());
                    }
                }
            }
        }
    }

    /**
     * Usa o arraylist de ligações para criar um grafo com distancias como pesos
     * para usar no diskstra e permite manipular externamente o grafo com uma
     * load na rede !!
     *
     * @param ar
     * @param Load
     * @return
     */
    public static EdgeWeightedDigraph createGraphDist(ArrayList<Ligacao> ar, float Load) {
        EdgeWeightedDigraph g = new EdgeWeightedDigraph(dg.V());
        for (int i = 0; i < ar.size(); i++) {
            if (((ar.get(i).flow() * 100) / ar.get(i).capacity()) > Load) {
                DirectedEdge e = new DirectedEdge(ar.get(i).from(), ar.get(i).to(), ar.get(i).getDistancia() * Load);
                g.addEdge(e);

            } else {
                DirectedEdge e = new DirectedEdge(ar.get(i).from(), ar.get(i).to(), ar.get(i).getDistancia());
                g.addEdge(e);
            }
        }
        return g;
    }

    /**
     * Apaga o flow do grafo se necessário , isto premite continuar a calcular o
     * flow on demand
     *
     * @param fg
     * @return
     */
    public static FlowNetwork deleteFlow(FlowNetwork fg) {
        FlowNetwork temp = new FlowNetwork(fg.V());
        for (int i = 0; i < fg.V(); i++) {
            for (FlowEdge e : fg.adj(i)) {
                if (e.from() == i) {
                    e.setFlow((double) 0);
                    temp.addEdge(e);
                }
            }
        }
        return temp;
    }

    /**
     * Adiciona uma central A ST de centrais!
     *
     * @param centralST
     * @param c
     * @return
     */
    public static boolean AddCentralEnergia(SeparateChainingHashST<Integer, CentralEletrica> centralST, CentralEletrica c) {
        if (!centralST.contains(c.getId())) {
            centralST.put(c.getId(), c);
            if (c.getId() > dg.V()-2) {
                dg = IncreaseGraphSize(dg);
            }
            return true;
        }
        return false;

    }

    /**
     * Permite obter a que percentagem de energia a central se encontra
     * atualemente
     *
     * @param centralST
     * @param id
     * @return
     */
    public static float getCentralPercentage(SeparateChainingHashST<Integer, CentralEletrica> centralST, Integer id) {
        if (centralST.contains(id)) {
            return (getCentralPower(centralST, id) * 100) / centralST.get(id).getWattsProduzidos();
        }
        return -1;
    }

    /**
     * Obtem a percentagem de uma fonte de energia
     *
     * @param fonteST
     * @param id
     * @return
     */
    public static float getFontePercentage(SeparateChainingHashST<Integer, FonteEnergetica> fonteST, Integer id) {
        if (fonteST.contains(id)) {
            return (getFontePower(fonteST, id) * 100) / fonteST.get(id).getWattsCapacidade();
        }
        return -1;
    }

    /**
     * Obtem em watts a carga de uma central
     *
     * @param centralST
     * @param id
     * @return
     */
    public static float getCentralPower(SeparateChainingHashST<Integer, CentralEletrica> centralST, Integer id) {
        float power = 0;
        if (centralST.contains(id)) {
            for (FlowEdge e : dg.adj(id)) {
                if (e.from() == id) {
                    power += e.flow();
                }
            }
            return power;
        }
        return -1;
    }

    /**
     * Obtem o power de uma central em watts
     *
     * @param fonteST
     * @param id
     * @return
     */
    public static float getFontePower(SeparateChainingHashST<Integer, FonteEnergetica> fonteST, Integer id) {
        float power = 0;
        if (fonteST.contains(id)) {
            for (FlowEdge e : dg.adj(id)) {
                if (e.from() == id) {
                    power += e.flow();
                }
            }
            return power;
        }
        return -1;
    }

    /**
     * Obtem o consumo de todas as centrais de energia
     *
     * @param centralST
     * @return
     */
    public static float getCentralPower(SeparateChainingHashST<Integer, CentralEletrica> centralST) {
        float consumo = 0;
        for (Integer k : centralST.keys()) {
            consumo += centralST.get(k).getPowerUsage();
        }
        return consumo;
    }

    /**
     * Obtem de todas as fontes na st o consumo atual, ou a carga em watts
     *
     * @param fonteST
     * @return
     */
    public static float getFontePower(SeparateChainingHashST<Integer, FonteEnergetica> fonteST) {
        float consumo = 0;
        for (Integer k : fonteST.keys()) {
            consumo += fonteST.get(k).getPowerUsage();
        }
        return consumo;
    }

    /**
     * Adiciona uam fonte á ST de fontes
     *
     * @param fonteST
     * @param f
     * @return
     */
    public static boolean AddFonteEnergia(SeparateChainingHashST<Integer, FonteEnergetica> fonteST, FonteEnergetica f) {
        if (!fonteST.contains(f.getId())) {
            fonteST.put(f.getId(), f);
            if (f.getId() > dg.V()-2) {
                dg = IncreaseGraphSize(dg);
            }
            return true;
        }
        return false;
    }

    /**
     * Permite apagar uma central de energia de uma ST
     *
     * @param centralST
     * @param id
     * @return
     */
    public static boolean DeleteCentralEnergia(SeparateChainingHashST<Integer, CentralEletrica> centralST, Integer id) {
        if (centralST.contains(id)) {
            centralST.delete(id);
            dg = removeFlowEdge(dg, id);
            return true;
        }
        return false;
    }

    /**
     * Retira um posto de energia da ST
     *
     * @param fonteST
     * @param id
     * @return
     */
    public static boolean DeleteFonteEnergia(SeparateChainingHashST<Integer, FonteEnergetica> fonteST, Integer id) {
        if (fonteST.contains(id)) {
            fonteST.delete(id);
            dg = removeFlowEdge(dg, id);
            return true;
        }
        return false;
    }

    /**
     * Conecta no grafo Objectos casa -> fonteEnergetica
     *
     * @param c
     * @param e
     * @return
     */
    public static boolean ConnectVertices(Casa c, FonteEnergetica e) {
        FlowEdge ed = new FlowEdge(c.getId(), e.getId(), e.getWattsCapacidade());
        dg.addEdge(ed);
        FlowEdge ef = new FlowEdge(c.getId(), 0, c.geradoMediaCasa());
        dg.addEdge(ef);
        dg = applyFordFulkersonInternal(dg, 1, 0);
        return true;
    }

    /**
     * Conecta no grafo Objectos fonteEnergetica -> casa
     *
     * @param e
     * @param c
     * @return
     */
    public static boolean ConnectVertices(FonteEnergetica e, Casa c) {
        if (e.canHandle()) {
            FlowEdge ed = new FlowEdge(e.getId(), c.getId(), c.getPotenciaContratada());
            dg.addEdge(ed);
            FlowEdge ef = new FlowEdge(c.getId(), 0, c.getPotenciaContratada());
            dg.addEdge(ef);
            dg = applyFordFulkersonInternal(dg, 1, 0);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long unixT = timestamp.getTime();
            e.setSTtime(unixT, dg.getEdge(ed));
            c.setPotenciaAtual((float) dg.getEdge(ed).flow());
            return true;
        }
        return false;
    }

    /**
     * Conecta no grafo Objectos fonte -> fonte
     *
     * @param e
     * @param ee
     * @return
     */
    public static boolean ConnectVertices(FonteEnergetica e, FonteEnergetica ee) {
        if (e.canHandle() && ee.canHandle()) {
            FlowEdge ed = new FlowEdge(e.getId(), ee.getId(), e.getWattsCapacidade());
            dg.addEdge(ed);
            dg = applyFordFulkersonInternal(dg, 1, 0);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long unixT = timestamp.getTime();
            e.setSTtime(unixT, dg.getEdge(ed));
            return true;

        }
        return false;
    }

    /**
     * Conecta no grafo Objectos central -> fonte
     *
     * @param c
     * @param e
     * @return
     */
    public static boolean ConnectVertices(CentralEletrica c, FonteEnergetica e) {
        if (c.canHandle() && e.canHandle()) {
            FlowEdge ed = new FlowEdge(c.getId(), e.getId(), e.getWattsCapacidade());
            dg.addEdge(ed);
            FlowEdge ef = new FlowEdge(0, c.getId(), c.getWattsProduzidos());
            dg.addEdge(ef);
            dg = applyFordFulkersonInternal(dg, 1, 0);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long unixT = timestamp.getTime();
            c.setSTtime(unixT, dg.getEdge(ed));
            return true;
        }
        return false;
    }

    /**
     * Esta é a função mais importante , conecta vertices entre si A função
     * verifica de te tipo de vertice é com base nisso cria o flowEdge Ao mesmo
     * tempo , este calcula o fordFulkerson , garantindo assim que energia
     * circula logo para a casa. ao mesmo tempo , pode ser enviado um peso
     * distancia para este ser armazenado como aresta para mais tarde ser usado
     * no diskstra
     *
     * @param v
     * @param w
     * @param dist
     * @return
     */
    public static boolean ConnectVertices(Integer v, Integer w, Integer dist) {
        if (casasST.contains(v)) {
            if (fonteST.contains(w)) {
                FlowEdge ed = new FlowEdge(casasST.get(v).getId(), fonteST.get(w).getId(), fonteST.get(w).getWattsCapacidade());
                dg.addEdge(ed);
                Ligacao l = new Ligacao(ed, dist);
                ArL.add(l);
                dg = applyFordFulkersonInternal(dg, 1, 0);
                casasST.get(v).setPotenciaAtual((float) dg.getEdge(ed).flow());
                return true;
            }
        }
        if (fonteST.contains(v)) {
            if (casasST.contains(w)) {
                if (fonteST.get(v).canHandle()) {
                    FlowEdge ed = new FlowEdge(fonteST.get(v).getId(), casasST.get(w).getId(), casasST.get(w).getPotenciaContratada());
                    dg.addEdge(ed);
                    FlowEdge ef = new FlowEdge(casasST.get(w).getId(), 0, casasST.get(w).getPotenciaContratada());
                    dg.addEdge(ef);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());// grava a hora que foi ligado ao vertice respetivo !!
                    Long unixT = timestamp.getTime();
                    fonteST.get(v).setSTtime(unixT, dg.getEdge(ed));
                    dg = applyFordFulkersonInternal(dg, 1, 0);
                    casasST.get(w).setPotenciaAtual((float) dg.getEdge(ed).flow());
                    Ligacao l = new Ligacao(ed, dist);
                    ArL.add(l);
                    return true;
                }
                return false;
            } else if ((fonteST.contains(w))) {
                if (fonteST.get(v).canHandle() && fonteST.get(w).canHandle()) {
                    FlowEdge ed = new FlowEdge(fonteST.get(v).getId(), fonteST.get(w).getId(), fonteST.get(w).getWattsCapacidade());
                    dg.addEdge(ed);
                    dg = applyFordFulkersonInternal(dg, 1, 0);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Long unixT = timestamp.getTime();
                    fonteST.get(v).setSTtime(unixT, dg.getEdge(ed));
                    Ligacao l = new Ligacao(ed, dist);
                    ArL.add(l);
                    return true;
                }
                return false;
            }
        }
        if (centralST.contains(v)) {
            if ((fonteST.contains(w))) {
                if (centralST.get(v).canHandle() && fonteST.get(w).canHandle()) {
                    FlowEdge ed = new FlowEdge(centralST.get(v).getId(), fonteST.get(w).getId(), fonteST.get(w).getWattsCapacidade());
                    dg.addEdge(ed);
                    FlowEdge ef = new FlowEdge(1, centralST.get(v).getId(), centralST.get(v).getWattsProduzidos());
                    dg.addEdge(ef);
                    dg = applyFordFulkersonInternal(dg, 1, 0);
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Long unixT = timestamp.getTime();
                    centralST.get(v).setSTtime(unixT, dg.getEdge(ed));
                    Ligacao l = new Ligacao(ed, dist);
                    ArL.add(l);
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    /**
     * Insere um Objecto casa na casaST, os seus equipamentos no equipamentoST
     *
     * @param casasST
     * @param equipamentosST
     * @param c
     * @return
     */
    public static boolean addCasasST(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST, Casa c) {
        if (!casasST.contains(c.getId())) {
            if (!c.getSTEquipamento().isEmpty()) {
                for (Integer e : c.getSTEquipamento().keys()) {
                    addEquipamentosST(equipamentosST, c.getSTEquipamento().get(e));
                }
            }
            casasST.put(c.getId(), c);
            if (c.getId() > dg.V()-2) {
                dg = IncreaseGraphSize(dg);                
            }
            return true;
        }
        return false;
    }

    /**
     * Insere um Objecto casa na casaST
     *
     * @param casasST
     * @param c
     * @return
     */
    public static boolean addCasasST(SeparateChainingHashST<Integer, Casa> casasST, Casa c) {
        if (!casasST.contains(c.getId())) {
            casasST.put(c.getId(), c);
            if (c.getId() > dg.V()-2) {
                dg = IncreaseGraphSize(dg);                
            }
            return true;
        }
        return false;
    }

    /**
     * Apaga uma casa da ST de casa e os seus equipamentos , tambem garante a
     * remoção dos Nós do grafo se existirem.
     *
     * @param casasST
     * @param equipamentosST
     * @param id
     * @return
     */
    public static boolean deleteCasasST(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST, Integer id) {
        if (casasST.contains(id)) {
            for (Integer key : equipamentosST.keys()) {
                equipamentosST.delete(key);
            }
            dg = removeFlowEdge(dg, id);
            casasST.delete(id);
            return true;
        }
        return false;
    }

    /**
     * Apaga os equipamentos da ST de equipamentos e tambem se existirem numa
     * casa apaga-os dessa ST interna.
     *
     * @param casasST
     * @param equipamentosST
     * @param id
     * @return
     */
    public static boolean deleteEquipamentosST(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST, Integer id) {
        if (equipamentosST.contains(id)) {
            if (casasST.contains(equipamentosST.get(id).getIDcasa())) {
                casasST.get(equipamentosST.get(id).getIDcasa()).getSTEquipamento().delete(id);
            }
            equipamentosST.delete(id);
        }
        return false;
    }

    /**
     * Adiciona equipamento a ST de equipamentos
     *
     * @param equipamentosST
     * @param e
     * @return
     */
    public static boolean addEquipamentosST(SeparateChainingHashST<Integer, Equipamento> equipamentosST, Equipamento e) {
        if (!equipamentosST.contains(e.getId())) {
            equipamentosST.put(e.getId(), e);
            return true;
        }
        //equipamentosST.put(equipamentosST.size(), e);
        return false;
    }

    /**
     * Permite editar caracteristicas de uma casa
     *
     * @param casasST
     * @param id
     * @param zona
     * @param wattscontratado
     * @return
     */
    public static boolean EditCasaST(SeparateChainingHashST<Integer, Casa> casasST, Integer id, String zona, Float wattscontratado) {
        if (casasST.contains(id)) {
            casasST.get(id).setZona(zona);
            casasST.get(id).setPotenciaContratada(wattscontratado);
            return true;
        }
        return false;
    }

    /**
     * Permite editar objeto de tipo casa
     *
     * @param c
     * @param id
     * @param zona
     * @param wattscontratado
     * @return
     */
    public static boolean EditCasa(Casa c, Integer id, String zona, Float wattscontratado) {
        c.setZona(zona);
        c.setPotenciaContratada(wattscontratado);
        return true;
    }

    /**
     * Permite editar um equipamento da ST de uma casa e da ST de equipamentos ,
     * tentando assim garatir consistencia
     *
     * @param casasST
     * @param equipamentosST
     * @param idc
     * @param ide
     * @param nome
     * @param watts
     * @return
     */
    public static boolean EditEquipamentoST(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST, Integer idc, Integer ide, String nome, Float watts) {
        if (casasST.contains(idc)) {
            if (casasST.get(idc).getSTEquipamento().contains(ide)) {
                if (casasST.get(idc).getSTEquipamento().get(ide).getTipo() == 0) {
                    casasST.get(idc).getSTEquipamento().get(ide).setNome(nome);
                    casasST.get(idc).getSTEquipamento().get(ide).setWattsConsumo(watts);
                    return true;
                } else {
                    casasST.get(idc).getSTEquipamento().get(ide).setNome(nome);
                    casasST.get(idc).getSTEquipamento().get(ide).setWattsGerado(watts);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Edita um objecto equipamento
     *
     * @param e
     * @param nome
     * @param watts
     * @return
     */
    public static boolean EditEquipamento(Equipamento e, String nome, Float watts) {
        if (e.getTipo() == 0) {
            e.setNome(nome);
            e.setWattsConsumo(watts);
            return true;
        } else {
            e.setNome(nome);
            e.setWattsGerado(watts);
            return true;
        }
    }

    public static boolean AddEquipamentoCasa(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST, Integer idcasa, Equipamento e) {
        if (casasST.contains(idcasa)) {
            if (!equipamentosST.contains(e.getId())) { // se não existir na ST equipamentos
                equipamentosST.put(e.getId(), e);
                casasST.get(idcasa).getSTEquipamento().put(e.getId(), e);
                casasST.get(idcasa).getSTEquipamento().get(e.getId()).setIDcasa(idcasa);
            } else if (!casasST.get(idcasa).getSTEquipamento().contains(e.getId())) { // se não existir na minha ST de casa
                casasST.get(idcasa).getSTEquipamento().put(e.getId(), e);
                casasST.get(idcasa).getSTEquipamento().get(e.getId()).setIDcasa(idcasa);
            }/* else { // se existir o equipamento e se já existir na casa atualiza o existente
                casasST.get(idcasa).getSTEquipamento().put(e.getId(), e);
                casasST.get(idcasa).getSTEquipamento().get(e.getId()).setIDcasa(idcasa);
            }*/
            return true;
        }
        return false;
    }

    /**
     * Apaga uma casa da ST de casas e tenta garantir que as arestas são
     * removidas
     *
     * @param casasST
     * @param equipamentosST
     * @param c
     * @return
     */
    public static boolean DeleteCasa(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST, Integer c) {
        if (casasST.contains(c)) {
            for (Integer k : casasST.get(c).getSTEquipamento().keys()) {
                if (equipamentosST.contains(casasST.get(c).getSTEquipamento().get(k).getId()) && equipamentosST.get(casasST.get(c).getSTEquipamento().get(k).getId()).getIDcasa().equals(casasST.get(c).getId())) {
                    equipamentosST.delete(casasST.get(c).getSTEquipamento().get(k).getId());
                }
            }
            dg = removeFlowEdge(dg, c);
            casasST.delete(c);
        }
        return false;
    }

    /**
     * Elimina um equipamento de uma casa
     *
     * @param casasST
     * @param equipamentosST
     * @param idc
     * @param e
     * @return
     */
    public static boolean DeleteEquipamentoCasa(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST, Integer idc, Integer e) {

        if (casasST.contains(idc)) {
            if (casasST.get(idc).getSTEquipamento().contains(e)) {
                casasST.get(idc).getSTEquipamento().delete(e);
            }
            return true;
        }
        return false;
    }

    /**
     * Apaga uma central da ST e remove as suas arestas no grafo
     *
     * @param centralST
     * @param id
     * @return
     */
    public static boolean deleteCentrais(SeparateChainingHashST<Integer, CentralEletrica> centralST, Integer id) {
        if (centralST.contains(id)) {
            dg = removeFlowEdge(dg, id);
            centralST.delete(id);
            return true;
        }
        return false;
    }

    /**
     * Apaga uma fontes da ST e remove as suas arestas no grafo
     *
     * @param fonteST
     * @param id
     * @return
     */
    public static boolean deleteFontes(SeparateChainingHashST<Integer, FonteEnergetica> fonteST, Integer id) {
        if (fonteST.contains(id)) {
            dg = removeFlowEdge(dg, id);
            fonteST.delete(id);
            return true;
        }
        return false;
    }

    /**
     * Edita uma central na ST
     *
     * @param centralST
     * @param id
     * @param capacidade
     * @return
     */
    public static boolean editCentrais(SeparateChainingHashST<Integer, CentralEletrica> centralST, Integer id, Float capacidade) {
        if (centralST.contains(id)) {
            centralST.get(id).setWattsProduzidos(capacidade);
            return true;
        }
        return false;
    }

    /**
     * Edita uma fonte na ST
     *
     * @param fonteST
     * @param id
     * @param capacidade
     * @return
     */
    public static boolean editFontes(SeparateChainingHashST<Integer, FonteEnergetica> fonteST, Integer id, Float capacidade) {
        if (fonteST.contains(id)) {
            fonteST.get(id).setWattsCapacidade(capacidade);
            return true;
        }
        return false;
    }

    /**
     * Atualiza informação das casas apos ler o grafo
     *
     * @param casasST
     * @param f
     * @return
     */
    public static boolean UpdateCurrentCasas(SeparateChainingHashST<Integer, Casa> casasST, FlowNetwork f) {

        for (int i = 0; i < f.V(); i++) {
            for (FlowEdge e : f.adj(i)) {
                if (casasST.contains(e.to())) {
                    casasST.get(e.to()).setPotenciaAtual((float) e.flow());
                }
            }
        }
        return true;
    }

    /**
     * Atualiza informação das fontes apos ler o grafo
     *
     * @param centralST
     * @param fonteST
     * @param f
     * @return
     */
    public static boolean UpdateCentral(SeparateChainingHashST<Integer, CentralEletrica> centralST, SeparateChainingHashST<Integer, FonteEnergetica> fonteST, FlowNetwork f) {

        for (int i = 0; i < f.V(); i++) {
            for (FlowEdge e : f.adj(i)) {
                if (centralST.contains(e.from()) && fonteST.contains(e.to())) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Long unixT = timestamp.getTime();
                    centralST.get(e.from()).setSTtime(unixT, e);
                }
            }
        }
        return true;
    }

    /**
     * Atualiza informação das fontes apos ler o grafo
     *
     * @param fonteST
     * @param casasST
     * @param f
     * @return
     */
    public static boolean UpdateFonte(SeparateChainingHashST<Integer, FonteEnergetica> fonteST, SeparateChainingHashST<Integer, Casa> casasST, FlowNetwork f) {

        for (int i = 0; i < f.V(); i++) {
            for (FlowEdge e : f.adj(i)) {
                if (fonteST.contains(e.from()) && fonteST.contains(e.to())) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Long unixT = timestamp.getTime();
                    fonteST.get(e.from()).setSTtime(unixT, e);

                }
                if (fonteST.contains(e.from()) && casasST.contains(e.to())) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Long unixT = timestamp.getTime();
                    fonteST.get(e.from()).setSTtime(unixT, e);
                }
            }
        }
        return true;
    }

    /**
     * Lê todas componentes para formar um grafo , incluindo o arralist de
     * ligação
     *
     * @return
     */
    public static FlowNetwork LoadGraphFile() {
        FlowNetwork f = new FlowNetwork(inG);
        loadArLigacao();
        f = readFlow(f);
        UpdateCurrentCasas(casasST, f);
        UpdateCentral(centralST, fonteST, f);
        UpdateFonte(fonteST, casasST, f);
        return f;

    }

    /**
     * Cria um novo grafo com base num inteiro
     *
     * @param s
     * @return
     */
    public static FlowNetwork LoadGraph(Integer s) {
        return new FlowNetwork(s);
    }

    /**
     * Guarda casas para binario
     *
     * @param casasST
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void SaveToFileCasasBin(SeparateChainingHashST<Integer, Casa> casasST) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(".//data//casasBin.bin");
        ObjectOutputStream osw = new ObjectOutputStream(fos);
        osw.writeObject(casasST);
        osw.flush();
        fos.close();
    }

    /**
     * Guarda equipamentos para binario
     *
     * @param equipamentosST
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void SaveToFileEquipamentosBin(SeparateChainingHashST<Integer, Equipamento> equipamentosST) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(".//data//equipamentosBin.bin");
        ObjectOutputStream osw = new ObjectOutputStream(fos);
        osw.writeObject(equipamentosST);
        osw.flush();
        fos.close();
    }

    /**
     * Guarda fontes de energia para binario
     *
     * @param fonteST
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void SaveToFileFonteEnergiaBin(SeparateChainingHashST<Integer, FonteEnergetica> fonteST) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(".//data//fontesBin.bin");
        ObjectOutputStream osw = new ObjectOutputStream(fos);
        osw.writeObject(fonteST);
        osw.flush();
        fos.close();
    }

    /**
     * Guarda centrais para binario
     *
     * @param centralST
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void SaveToFileCentralEnergiaBin(SeparateChainingHashST<Integer, CentralEletrica> centralST) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileOutputStream fos = new FileOutputStream(".//data//centralBin.bin");
        ObjectOutputStream osw = new ObjectOutputStream(fos);
        osw.writeObject(centralST);
        osw.flush();
        fos.close();
    }

    /**
     * Lê do ficheiro para a ST de casas binario
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static SeparateChainingHashST<Integer, Casa> LoadToFileCasasBin() throws FileNotFoundException, IOException {
        try {
            SeparateChainingHashST<Integer, Casa> casas = new SeparateChainingHashST<>();
            try (FileInputStream fos = new FileInputStream(new File(".//data//casasBin.bin"))) {
                ObjectInputStream osw = new ObjectInputStream(fos);
                casas = (SeparateChainingHashST) osw.readObject();
                osw.close();
            }
            return casas;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestorMain.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Lê do ficheiro para a ST de equipamentos binario
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static SeparateChainingHashST<Integer, Equipamento> LoadToFileEquipamentosBin() throws FileNotFoundException, IOException {
        try {
            SeparateChainingHashST<Integer, Equipamento> equip = new SeparateChainingHashST<>();
            FileInputStream fos = new FileInputStream(new File(".//data//equipamentosBin.bin"));
            ObjectInputStream osw = new ObjectInputStream(fos);
            equip = (SeparateChainingHashST) osw.readObject();
            osw.close();
            fos.close();
            return equip;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestorMain.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Lê do ficheiro para a ST de fontes binario
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static SeparateChainingHashST<Integer, FonteEnergetica> LoadToFileFonteEnergicaBin() throws FileNotFoundException, IOException {
        try {
            SeparateChainingHashST<Integer, FonteEnergetica> fonte = new SeparateChainingHashST<>();
            FileInputStream fos = new FileInputStream(new File(".//data//fontesBin.bin"));
            ObjectInputStream osw = new ObjectInputStream(fos);
            fonte = (SeparateChainingHashST) osw.readObject();
            osw.close();
            fos.close();
            return fonte;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestorMain.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Lê do ficheiro para a ST de centrais binario
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static SeparateChainingHashST<Integer, CentralEletrica> LoadToFileCentralEnergicaBin() throws FileNotFoundException, IOException {
        try {
            SeparateChainingHashST<Integer, CentralEletrica> central = new SeparateChainingHashST<>();
            FileInputStream fos = new FileInputStream(new File(".//data//centralBin.bin"));
            ObjectInputStream osw = new ObjectInputStream(fos);
            central = (SeparateChainingHashST) osw.readObject();
            osw.close();
            fos.close();
            return central;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GestorMain.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Guarda tudo para que estás nas ST para ficheiro binário
     *
     * @param equipamentosST
     * @param casasST
     * @param fonteST
     * @param centralST
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static void SaveToFileBin(SeparateChainingHashST<Integer, Equipamento> equipamentosST, SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, FonteEnergetica> fonteST, SeparateChainingHashST<Integer, CentralEletrica> centralST) throws UnsupportedEncodingException, IOException {
        SaveToFileCasasBin(casasST);
        SaveToFileEquipamentosBin(equipamentosST);
        SaveToFileFonteEnergiaBin(fonteST);
        SaveToFileCentralEnergiaBin(centralST);

    }

    /**
     * Guarda o Grafo atual. as ligações e o flow atual
     *
     * @param fn
     */
    public static void SaveGraph(FlowNetwork fn) {
        Out out = new Out(".//data//grafo.txt");
        out.println(fn.V());
        out.println(fn.E());
        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge k : fn.adj(i)) {
                if (i == k.from()) {
                    out.println(k.from() + " " + k.to() + " " + k.capacity());
                }
            }
        }
        saveArLigacao(ArL);
        saveFlow(fn);
        out.close();
    }

    /**
     * Remove uma ligacao do arraylist de ligações , tentando garantir
     * consistencia de informação, esta apaga apenas com base num vertice,
     * apagando todas as arestas a incider ou a serem incididas
     *
     * @param v
     */
    public static void removeFlowEdgeLigacao(Integer v) {
        for (int i = 0; i < ArL.size(); i++) {
            if (ArL.get(i).from() == v || ArL.get(i).to() == v) {
                ArL.remove(i);
            }
        }
    }

    /**
     * Remove a Aresta do ArrayList, garantido assim que não entra para o
     * diskstra, com base no v e w
     *
     * @param v
     * @param w
     */
    public static void removeFlowEdgeLigacao(Integer v, Integer w) {
        for (int i = 0; i < ArL.size(); i++) {
            if (ArL.get(i).from() == v && ArL.get(i).to() == w) {
                ArL.remove(i);
            }
        }
    }

    /**
     * Remove flow Egde com base em ID
     *
     * @param fn
     * @param v
     * @return
     */
    public static FlowNetwork removeFlowEdge(FlowNetwork fn, Integer v) {
        FlowNetwork temp = new FlowNetwork(fn.V());
        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    if (e.from() != v || e.to() != v) {
                        temp.addEdge(e);
                    } else {
                        removeFlowEdgeLigacao(v);
                    }
                }
            }
        }
        return applyFordFulkersonInternal(temp, 1, 0);
    }

    /**
     * Remove aresta de um grafo, faz isso, criando um novo grafo e retornando
     * isso sem essa aresta !!
     *
     * @param fn
     * @param v
     * @param w
     * @return
     */
    public static FlowNetwork removeFlowEdge(FlowNetwork fn, Integer v, Integer w) {
        FlowNetwork temp = new FlowNetwork(fn.V());
        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    if (e.from() != v && e.to() != w) {
                        temp.addEdge(e);
                    } else {
                        removeFlowEdgeLigacao(v, w);
                    }
                }
            }
        }
        return applyFordFulkersonInternal(temp, 1, 0);
    }

    /**
     * Aumenta tamanho do grafo e retorna !
     *
     * @param fn
     * @return
     */
    public static FlowNetwork IncreaseGraphSize(FlowNetwork fn) {
        FlowNetwork temp = new FlowNetwork(fn.V() + 1);
        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    temp.addEdge(e);
                }
            }
        }
        return temp;
    }

    /**
     * Aumenta tamanho do grafo com um tamanho á escolha !
     *
     * @param fn
     * @param size
     * @return
     */
    public static FlowNetwork IncreaseGraphSize(FlowNetwork fn, Integer size) {
        FlowNetwork temp = new FlowNetwork(fn.V() + size);
        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                if (e.from() == i) {
                    temp.addEdge(e);
                }
            }
        }
        return temp;
    }

    /**
     * Está informação guarda toda a informação de uma casa, desde consumos ,
     * tempos , historicos , etc...
     *
     * @param casasST
     */
    public static void SaveFileCasas(SeparateChainingHashST<Integer, Casa> casasST) {
        Out outCasas = new Out(".//data//casas.txt");
        Out outCasasCH = new Out(".//data//casasConsumoHistorico.txt");
        Out outCasasRBlack = new Out(".//data//casasTimeConsumo.txt");
        Out outEquipamentos = new Out(".//data//equipamentos.txt");
        Out outEquipamentosCH = new Out(".//data//equipamentosConsumoHistorico.txt");
        for (Integer key : casasST.keys()) {
            outCasas.println(casasST.get(key).getId() + ";" + casasST.get(key).getPotenciaContratada() + ";" + casasST.get(key).getZona() + ";");
            if (!casasST.get(key).getConsumoTotalHistorico().isEmpty()) {
                outCasasCH.print(casasST.get(key).getId() + ";");
                for (int i = 0; i < casasST.get(key).getConsumoTotalHistorico().size(); i++) {
                    outCasasCH.print(casasST.get(key).getConsumoTotalHistorico().get(i) + ";");
                }
                outCasasCH.println("");
            }
            if (!casasST.get(key).getSTtime().isEmpty()) {
                outCasasRBlack.print(casasST.get(key).getId() + ";");
                for (Long time : casasST.get(key).getSTtime().keys()) {
                    outCasasRBlack.print(time + ";" + casasST.get(key).getSTtime().get(time) + ";");
                }
                outCasasRBlack.println("");
            }
            for (Integer equip : casasST.get(key).getSTEquipamento().keys()) {
                outEquipamentos.println(casasST.get(key).getSTEquipamento().get(equip).getId() + ";" + casasST.get(key).getSTEquipamento().get(equip).getNome() + ";" + casasST.get(key).getSTEquipamento().get(equip).getTipo() + ";" + casasST.get(key).getId() + ";" + casasST.get(key).getSTEquipamento().get(equip).getWattsConsumo() + ";" + casasST.get(key).getSTEquipamento().get(equip).getWattsGerado() + ";" + casasST.get(key).getSTEquipamento().get(equip).getLigado() + ";");
                if (!casasST.get(key).getSTEquipamento().get(equip).getWattsHistorico().isEmpty()) {
                    outEquipamentosCH.print(casasST.get(key).getSTEquipamento().get(equip).getId() + ";");
                    for (int i = 0; i < casasST.get(key).getSTEquipamento().get(equip).getWattsHistorico().size(); i++) {
                        outEquipamentosCH.print(casasST.get(key).getSTEquipamento().get(equip).getWattsHistorico().get(i) + ";");
                    }
                    outEquipamentosCH.println("");
                }
            }
        }
        outCasas.close();
        outCasasCH.close();
        outCasasRBlack.close();
        outEquipamentos.close();
        outEquipamentosCH.close();

    }

    /**
     * Guarda só o flow de um grafo !!
     *
     * @param dg
     */
    public static void saveFlow(FlowNetwork dg) {
        Out save = new Out(".//data//flows.txt");
        for (int i = 0; i < dg.V(); i++) {
            for (FlowEdge e : dg.adj(i)) {
                if (i == e.from()) {
                    save.println(e.flow());
                }

            }
        }
        save.close();
    }

    /**
     * Lê de um ficheiro o flow e insere no grafo
     *
     * @param dg
     * @return
     */
    public static FlowNetwork readFlow(FlowNetwork dg) {
        In read = new In(".//data//flows.txt");
        for (int i = 0; i < dg.V(); i++) {
            for (FlowEdge e : dg.adj(i)) {
                if (e.from() == i) {
                    Double val = read.readDouble();
                    e.setFlow(val);
                }
            }
        }
        return dg;
    }

    /**
     *
     * Função grava em 4 ficheiros a rede inteira
     *
     * @param centralST
     * @param fonteST
     * @param casasST
     * @param equipamentosST
     */
    public static void SaveToFileAll(SeparateChainingHashST<Integer, CentralEletrica> centralST, SeparateChainingHashST<Integer, FonteEnergetica> fonteST, SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST) {
        SaveGraph(dg);
        SaveFileCentralE(centralST);
        SaveFileFonteE(fonteST);
        SaveFileCasas(casasST);
        SaveEquipamentos(equipamentosST);
    }

    /**
     * Grava em ficheiro a ST de equipamentos
     *
     * @param equipamentosST
     */
    public static void SaveEquipamentos(SeparateChainingHashST<Integer, Equipamento> equipamentosST) {
        Out outEquipamentos = new Out(".//data//onlyEquipamentos.txt");// guarda todos os equipamentos , mesmo nao tendo casa associada
        for (Integer key : equipamentosST.keys()) {
            outEquipamentos.println(equipamentosST.get(key).getId() + ";" + equipamentosST.get(key).getNome() + ";" + equipamentosST.get(key).getTipo() + ";" + equipamentosST.get(key).getWattsConsumo() + ";" + equipamentosST.get(key).getWattsGerado() + ";");
        }
    }

    /**
     * Grava em ficheiro a ST de fontes
     *
     * @param fonteST
     */
    public static void SaveFileFonteE(SeparateChainingHashST<Integer, FonteEnergetica> fonteST) {
        Out outFonte = new Out(".//data//fontes.txt");
        for (Integer key : fonteST.keys()) {
            outFonte.println(fonteST.get(key).getId() + ";" + fonteST.get(key).getZona() + ";");
        }

    }

    /**
     * Grava em ficheiro a ST central
     *
     * @param centralST
     */
    public static void SaveFileCentralE(SeparateChainingHashST<Integer, CentralEletrica> centralST) {
        Out outFonte = new Out(".//data//fontes.txt");
        for (Integer key : centralST.keys()) {
            outFonte.println(centralST.get(key).getId() + ";" + centralST.get(key).getZona() + ";");
        }

    }

    /**
     * Le do ficheiro todas as fontes e insere na ST
     *
     * @param fonteST
     */
    public static void LoadFromFileFonteE(SeparateChainingHashST<Integer, FonteEnergetica> fonteST) {
        In in = new In(".//data//fontes.txt");
        while (!in.isEmpty()) {
            String[] splitStr = in.readLine().split(";");
            Integer id = Integer.parseInt(splitStr[0]);
            String zona = splitStr[1];
            Float watts = Float.parseFloat(splitStr[2]);
            FonteEnergetica fe = new FonteEnergetica(id, zona, watts);
            AddFonteEnergia(fonteST, fe);
        }

    }

    /**
     * Le do ficheiro todas as centrais e insere na ST
     *
     * @param centralST
     */
    public static void LoadFromFileCentralE(SeparateChainingHashST<Integer, CentralEletrica> centralST) {
        In in = new In(".//data//centrais.txt");
        while (!in.isEmpty()) {
            String[] splitStr = in.readLine().split(";");
            Integer id = Integer.parseInt(splitStr[0]);
            float wattsproduzidos = Float.parseFloat(splitStr[1]);
            String zona = splitStr[2];
            CentralEletrica fe = new CentralEletrica(id, wattsproduzidos, zona);
            AddCentralEnergia(centralST, fe);
        }

    }

    /**
     * Le do ficheiro todas as casas e insere na ST com path
     *
     * @param casasST
     * @param filepath
     */
    public static void LoadFromFileCasa(SeparateChainingHashST<Integer, Casa> casasST, String filepath) {
        In in = new In(filepath); // abertura do ficheiro/stream de entrada  
        while (!in.isEmpty()) {
            String[] splitStr = in.readLine().split(";");
            Integer id = Integer.parseInt(splitStr[0]);
            float potenciaContratada = Float.parseFloat(splitStr[1]);
            String zona = splitStr[2];
            Casa c = new Casa(id, potenciaContratada, zona);
            addCasasST(casasST, c);
        }
    }

    /**
     * Le do ficheiro todas as casas e devidas informacões e insere na ST com
     * path pre-definido
     *
     * @param casasST
     */
    public static void LoadFromFileCasa(SeparateChainingHashST<Integer, Casa> casasST) {
        In in = new In(".//data//casas.txt"); // abertura do ficheiro/stream de entrada  
        while (!in.isEmpty()) {
            In casasCH = new In(".//data//casasConsumoHistorico.txt");
            In CasasRBlack = new In(".//data//casasTimeConsumo.txt");
            String[] splitStr = in.readLine().split(";");
            Integer id = Integer.parseInt(splitStr[0]);
            //float consumoTotal = Float.parseFloat(splitStr[1]);
            float potenciaContratada = Float.parseFloat(splitStr[1]);
            String zona = splitStr[2];
            //Integer idCentral= Integer.parseInt(splitStr[0]);
            Casa c = new Casa(id, potenciaContratada, zona);
            while (!casasCH.isEmpty()) {
                String[] CH = casasCH.readLine().split(";");
                Integer idCH = Integer.parseInt(CH[0]);
                if (c.getId().equals(idCH)) {
                    for (int j = 1; j < CH.length; j++) {
                        c.setConsumoTotalHistorico(Float.parseFloat(CH[j]));
                    }
                }
            }
            casasCH.close();
            while (!CasasRBlack.isEmpty()) {
                String[] RB = CasasRBlack.readLine().split(";");
                Integer idRB = Integer.parseInt(RB[0]);
                if (c.getId().equals(idRB)) {
                    for (int j = 1; j < RB.length; j += 2) {
                        Long auxtime = Long.parseLong(RB[j]);
                        Integer auxid = Integer.parseInt(RB[j + 1]);
                        c.setSTtime(auxtime, auxid);
                    }
                }
            }
            CasasRBlack.close();
            addCasasST(casasST, c);
        }
        in.close();
    }

    /**
     * Le os equipemntos do ficheiro , grava e tenta associar a uma casa se
     * assim for necessário
     *
     * @param casasST
     * @param equipamentosST
     */
    public static void LoadFromFileEquipamentos(SeparateChainingHashST<Integer, Casa> casasST, SeparateChainingHashST<Integer, Equipamento> equipamentosST) {

        In in = new In(".//data//equipamentos.txt"); // abertura do ficheiro/stream de entrada       
        while (!in.isEmpty()) {
            In EquipamentosCH = new In(".//data//equipamentosConsumoHistorico.txt");
            String[] splitStr = in.readLine().split(";");
            Integer id = Integer.parseInt(splitStr[0]);
            String nome = splitStr[1];
            Integer tipo = Integer.parseInt(splitStr[2]);
            Integer idcasa = Integer.parseInt(splitStr[3]);
            float consumo = Float.parseFloat(splitStr[4]);
            float gerado = Float.parseFloat(splitStr[5]);
            Integer ligado = Integer.parseInt(splitStr[6]);
            Equipamento e = new Equipamento(id, nome, tipo, idcasa, consumo, gerado, ligado);
            while (!EquipamentosCH.isEmpty()) {
                String[] CH = EquipamentosCH.readLine().split(";");
                Integer idCH = Integer.parseInt(CH[0]);
                if (e.getId().equals(idCH)) {
                    for (int j = 1; j < CH.length; j++) {
                        e.setWattsHistorico(Float.parseFloat(CH[j]));
                    }
                }
            }
            if (casasST.contains(idcasa)) {
                casasST.get(idcasa).setSTEquipamento(e);
            } else {
                addEquipamentosST(equipamentosST, e);
            }
            EquipamentosCH.close();
        }
        in.close();
    }

    /**
     * Le dos ficheiros equipamentos e grava na ST , mas não tenta inserir numa
     * casa, com path atribuido pelo utilizador
     *
     * @param equipamentosST
     * @param filepath
     */
    public static void LoadFromFileEquipamentos(SeparateChainingHashST<Integer, Equipamento> equipamentosST, String filepath) {
        In in = new In(filepath); // abertura do ficheiro/stream de entrada  
        while (!in.isEmpty()) {
            String[] splitStr = in.readLine().split(";");
            Integer id = Integer.parseInt(splitStr[0]);
            String nome = splitStr[1];
            Integer tipo = Integer.parseInt(splitStr[2]);
            float consumo = Float.parseFloat(splitStr[3]);
            float gerado = Float.parseFloat(splitStr[4]);
            Equipamento e = new Equipamento(id, nome, tipo, consumo, gerado);
            addEquipamentosST(equipamentosST, e);
        }
    }

    /**
     * Imprime informação de uma casa da ST
     *
     * @param casasST
     * @param id
     * @return
     */
    public static String printCasaAllInfo(SeparateChainingHashST<Integer, Casa> casasST, Integer id) {
        String s = "";
        if (casasST.contains(id)) {
            s += "casa:" + casasST.get(id).getId() + "-" + casasST.get(id).getZona() + ";" + casasST.get(id).getPotenciaContratada() + ";\n";
            for (Integer key : casasST.get(id).getSTEquipamento().keys()) {
                s += casasST.get(id).getSTEquipamento().get(key).printMyInfo();
            }
            for (Long key : casasST.get(id).getSTtime().keys()) {
                Timestamp time = new Timestamp(key);
                if (casasST.get(id).getSTtime().get(key) < 0) {

                    s += time + "-desligado ;";
                } else {
                    s += time + "-ligado ;";
                }
                s += "\n";
            }
            s += "Historico" + casasST.get(id).getConsumoTotalHistorico().toString();
            s += "\n\n";
            return s;
        }
        return "não existe casa com esse ID";
    }

    /**
     * Imprime Informação de todas as casas
     *
     * @param casasST
     * @return
     */
    public static String printCasaAllInfo(SeparateChainingHashST<Integer, Casa> casasST) {
        String s = "";
        if (!casasST.isEmpty()) {
            for (Integer k : casasST.keys()) {
                s += "CASA [" + casasST.get(k).getId() + "] -> " + casasST.get(k).getZona() + " -> " + casasST.get(k).getPotenciaContratada() + " Watts\n";
                s += "  ->> [LISTA EQUIPAMENTOS]\n";
                for (Integer key : casasST.get(k).getSTEquipamento().keys()) {
                    s += casasST.get(k).getSTEquipamento().get(key).printMyInfo();
                }
                s += "          <<HISTORICO>>\n";
                for (Long key : casasST.get(k).getSTtime().keys()) {
                    Timestamp time = new Timestamp(key);
                    if (casasST.get(k).getSTtime().get(key) < 0) {
                        s += "          [OFF] - [" + Math.abs(casasST.get(k).getSTtime().get(key)) + "] - " + time + ";";
                    } else {
                        s += "          [ON ] - [" + Math.abs(casasST.get(k).getSTtime().get(key)) + "] - " + time + ";";
                    }
                    s += "\n";
                }
                s += "          [CONTADOR ELETRICO]: " + casasST.get(k).getConsumoTotalHistorico().toString() + "\n\n";

            }
            return s;
        }
        return "\n!! NÃO EXISTEM CASAS !!\n";
    }

    /**
     * Imprime as casas por zona , Ex: porto
     *
     * @param casasST
     * @param zona
     * @return
     */
    public static String printCasasZona(SeparateChainingHashST<Integer, Casa> casasST, String zona) {
        String s = "";

        for (Integer key : casasST.keys()) {

            if (casasST.get(key).getZona().matches(zona)) {
                s += printCasaAllInfo(casasST, casasST.get(key).getId()) + "\n";
            }

        }
        return s;
    }

    /**
     * Consumo medio de todas as casas
     *
     * @param casasST
     * @return
     */
    public static float consumoMedioTotal(SeparateChainingHashST<Integer, Casa> casasST) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {
            consumo += casasST.get(key).calcularWattsEquipamentosLigados();
        }
        return consumo;
    }

    /**
     * consumo medio de todas as casas nas respetivas zonas
     *
     * @param casasST
     * @param zona
     * @return
     */
    public static float consumoMedioTotal(SeparateChainingHashST<Integer, Casa> casasST, String zona) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {
            if (casasST.get(key).getZona().matches(zona)) {
                consumo += casasST.get(key).calcularWattsEquipamentosLigados();
            }
        }
        return consumo;
    }

    /**
     * Consumo medio de uma casa especifico
     *
     * @param casasST
     * @param id
     * @return
     */
    public static float consumoMedioCasa(SeparateChainingHashST<Integer, Casa> casasST, Integer id) {
        if (casasST.contains(id)) {
            return casasST.get(id).calcularWattsEquipamentosLigados();
        }
        return -1;
    }

    /**
     * Função que desliga todas as casas
     *
     * @param casasST
     * @throws InterruptedException
     */
    public static void desligarCasas(SeparateChainingHashST<Integer, Casa> casasST) throws InterruptedException {
        for (Integer key : casasST.keys()) {
            casasST.get(key).emergencyShutdown();
        }
    }

    /**
     * Liga equipamento de uma casa
     *
     * @param casasST
     * @param casa
     * @param equip
     * @return
     * @throws InterruptedException
     */
    public static boolean ligarEquipamentoCasa(SeparateChainingHashST<Integer, Casa> casasST, Integer casa, Integer equip) throws InterruptedException {
        if (casasST.contains(casa)) {
            if (casasST.get(casa).getSTEquipamento().contains(equip)) {
                return casasST.get(casa).powerOn(equip);
            }
        }
        return false;
    }

    /**
     * Desliga equipamentos de uma casa
     *
     * @param casasST
     * @param casa
     * @param equip
     * @throws InterruptedException
     */
    public static void desligarEquipamentoCasa(SeparateChainingHashST<Integer, Casa> casasST, Integer casa, Integer equip) throws InterruptedException {
        if (casasST.contains(casa)) {
            if (casasST.get(casa).getSTEquipamento().contains(equip)) {
                casasST.get(casa).powerOff(equip, 2);
            }
        }
    }

    /**
     * Desliga todas as casas de uma zona
     *
     * @param casasST
     * @param zona
     * @throws InterruptedException
     */
    public static void desligarCasasZona(SeparateChainingHashST<Integer, Casa> casasST, String zona) throws InterruptedException {
        for (Integer key : casasST.keys()) {
            if (casasST.get(key).getZona().matches(zona)) {
                casasST.get(key).emergencyShutdown();
            }

        }
    }

    /**
     * Desliga todos os equipamentos de uma casa
     *
     * @param casasST
     * @param id
     * @throws InterruptedException
     */
    public static void desligarCasas(SeparateChainingHashST<Integer, Casa> casasST, Integer id) throws InterruptedException {
        if (casasST.contains(id)) {
            casasST.get(id).emergencyShutdown();
        }
    }

    /**
     * Permite procurar consumo de uma determinada data de todas as casas
     *
     * @param casasST
     * @param inicio
     * @param fim
     * @return
     */
    public static Float consumoCasasTempo(SeparateChainingHashST<Integer, Casa> casasST, Timestamp inicio, Timestamp fim) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {
            consumo += casasST.get(key).calcularWattsBetween(inicio, fim);
        }
        return consumo;

    }

    /**
     * Permite procurar consumo de uma determinada data de todas as casas de uma
     * determinada zona
     *
     * @param casasST
     * @param zona
     * @param inicio
     * @param fim
     * @return
     */
    public static Float consumoCasasTempo(SeparateChainingHashST<Integer, Casa> casasST, String zona, Timestamp inicio, Timestamp fim) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {
            if (casasST.get(key).getZona().matches(zona)) {
                consumo += casasST.get(key).calcularWattsBetween(inicio, fim);
            }
        }
        return consumo;

    }

    public static String printAllCentralInfo(FlowNetwork g, Integer c) {
        String s = "";
        if (centralST.contains(c)) {
            s += "Central: ID:" + centralST.get(c).getId() + "\n" + "Zona: " + centralST.get(c).getZona() + "\n" + "Watts Produzidos:" + centralST.get(c).getWattsProduzidos() + "\nLigado a \n";
            for (FlowEdge e : g.adj(c)) {
                if (fonteST.contains(e.to())) {
                    s += "Fonte Eletrica:" + fonteST.get(e.to()).getId() + " ; Zona:" + fonteST.get(e.to()).getZona() + "\n";
                    s += "Capacidade de " + e.capacity() + "\nCom uma corrente(flow) de " + e.flow() + "\n";
                }
            }
        }
        return s;
    }

    public static String printAllFonteInfo(FlowNetwork g, Integer c) {
        String s = "";
        if (fonteST.contains(c)) {
            s += "Fonte Eletrica:" + fonteST.get(c).getId() + " ; Zona:" + fonteST.get(c).getZona() + "\n" + "Capacidade :" + fonteST.get(c).getWattsCapacidade() + "\nLigado a \n";
            for (FlowEdge e : g.adj(c)) {
                if (casasST.contains(e.to())) {
                    s += printCasaAllInfo(casasST, e.to());
                } else if (fonteST.contains(e.to())) {
                    s += "Fonte Eletrica:" + fonteST.get(e.to()).getId() + " ; Zona:" + fonteST.get(e.to()).getZona() + "\n" + "Capacidade :" + fonteST.get(e.to()).getWattsCapacidade() + "\n";
                }
                s += "Capacidade de " + e.capacity() + "\nCom uma corrente(flow) de " + e.flow() + "\n";
            }
        }
        return s;
    }

    public static String printAllGraphInfo(FlowNetwork g) {
        String s = "";
        for (int i = 0; i < g.V(); i++) {
            if (casasST.contains(i)) {
                s += printCasaAllInfo(casasST, i);
            } else if (fonteST.contains(i)) {
                s += "Fonte ID: " + fonteST.get(i).getId() + " Zona: " + fonteST.get(i).getZona() + "\n";
            } else if (centralST.contains(i)) {
                s += "Central ID: " + centralST.get(i).getId() + " Zona: " + centralST.get(i).getZona() + "\n";
            }
            for (FlowEdge e : g.adj(i)) {
                if (casasST.contains(e.to())) {
                    s += printCasaAllInfo(casasST, e.to());
                }
                if (fonteST.contains(e.to())) {
                    s += " Fonte:ID: " + fonteST.get(e.to()).getId() + fonteST.get(e.to()).getZona() + "\n";
                }
                if (centralST.contains(e.to())) {
                    s += "Central:ID: " + centralST.get(e.to()).getId() + centralST.get(e.to()).getZona() + "\n";
                }
                s += "Capacidade da rede: " + e.capacity() + " Flow atual: " + e.flow() + "\n";
            }
        }
        return s;
    }

    /**
     * Consumo de todas as casas atualmente
     *
     * @param casasST
     * @return
     */
    public static Float consumoCasasAgora(SeparateChainingHashST<Integer, Casa> casasST) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {
            consumo += casasST.get(key).calcularWattsEquipamentosLigados();
        }
        return consumo;

    }

    /**
     * Consumo de todas as casas de uma determinada zona atualmente
     *
     * @param casasST
     * @param zona
     * @return
     */
    public static Float ConsumoCasasZona(SeparateChainingHashST<Integer, Casa> casasST, String zona) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {

            if (casasST.get(key).getZona().matches(zona)) {
                consumo += casasST.get(key).calcularWattsEquipamentosLigados();
            }
        }
        return consumo;
    }

    /**
     * Casas de uma determinada zona que estão a gerar energia
     *
     * @param casasST
     * @param zona
     * @return
     */
    public static Float GeradoCasasZona(SeparateChainingHashST<Integer, Casa> casasST, String zona) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {

            if (casasST.get(key).getZona().matches(zona)) {
                consumo += casasST.get(key).calcularWattsGeradoresLigados();
            }
        }
        return consumo;
    }

    /**
     * Casas que estão a gerar energia para a rede.
     *
     * @param casasST
     * @return
     */
    public static Float GeradoCasas(SeparateChainingHashST<Integer, Casa> casasST) {
        float consumo = 0;
        for (Integer key : casasST.keys()) {
            consumo += casasST.get(key).calcularWattsGeradoresLigados();
        }
        return consumo;
    }

    /**
     * Retorna o valor gerado por uma casa
     *
     * @param casasST
     * @param id
     * @return
     */
    public static Float GeradoCasas(SeparateChainingHashST<Integer, Casa> casasST, Integer id) {
        return casasST.get(id).calcularWattsGeradoresLigados();
    }

    /**
     * Calcula o consumo atual de uma casa de todos os equipamentos ligados
     *
     * @param casasST
     * @param id
     * @return
     */
    public static Float consumoCasasAgora(SeparateChainingHashST<Integer, Casa> casasST, Integer id) {
        if (casasST.contains(id)) {
            return casasST.get(id).calcularWattsEquipamentosLigados();
        }
        return (float) -1;
    }

    /**
     * Obtem o consumo de uma casa em um determinado intervalo de tempo
     *
     * @param casasST
     * @param id
     * @param inicio
     * @param fim
     * @return
     */
    public static Float consumoCasasTempo(SeparateChainingHashST<Integer, Casa> casasST, Integer id, Timestamp inicio, Timestamp fim) {
        if (casasST.contains(id)) {
            return casasST.get(id).calcularWattsBetween(inicio, fim);
        }
        return (float) -1;

    }

    /**
     * Obtem a capacidade de dois vertices
     *
     * @param v
     * @param w
     * @return
     */
    public static float getCapacityNetwork(int v, int w) {
        for (FlowEdge e : dg.edges()) {
            if (e.to() == v && e.from() == w) {
                return (float) e.capacity();
            }
        }
        return 0;
    }

    /**
     * Obtem o flow de dois vertices
     *
     * @param v
     * @param w
     * @return
     */
    public static float getFlowNetwork(int v, int w) {
        for (FlowEdge e : dg.edges()) {
            if (e.to() == v && e.from() == w) {
                return (float) e.flow();
            }
        }
        return 0;
    }

    /**
     * Obtem o flow de uma casa
     *
     * @param g
     * @param vcasa
     * @return
     */
    public static Double getCurrentCasa(FlowNetwork g, Integer vcasa) {
        if (!casasST.contains(vcasa)) {
            return (double) 0;
        }
        if (vcasa < 0 || vcasa > g.V()) {
            return (double) 0;
        }
        for (int i = 0; i < g.V(); i++) {
            for (FlowEdge e : g.adj(i)) {
                if (e.to() == vcasa) {
                    return e.flow();
                }
            }
        }
        return (double) 0;
    }

    /**
     * Retorna o max Flow
     *
     * @param graph
     * @return
     */
    public static String FindMaxFlow(FlowNetwork graph) {
        String s = "";
        for (int v = 0; v < graph.V(); v++) {
            for (FlowEdge e : graph.adj(v)) {
                if ((v == e.from()) && e.flow() > 0) {
                    s += "   " + e + "\n";
                }
            }
        }
        return s;
    }

    /**
     * Permite cortar a energia a uma casa
     *
     * @param g
     * @param idc
     * @return
     */
    public static boolean cutPowerCasa(FlowNetwork g, Integer idc) {
        if (casasST.contains(idc)) {
            for (int i = 0; i < g.V(); i++) {
                for (FlowEdge e : g.adj(i)) {
                    if (e.from() == idc) {

                        try {
                            casasST.get(idc).setPotenciaAtual((float) 0);
                            desligarCasas(casasST, idc);

                        } catch (InterruptedException ex) {
                            Logger.getLogger(GestorMain.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * Permite aplicar o algoritmo fordFulkerson e receber uma string com a
     * devida informação
     *
     * @param graph
     * @param source
     * @param destination
     * @return
     */
    public static String getApplyFordFulkerson(FlowNetwork graph, Integer source, Integer destination) {
        String s = "";
        FlowNetwork fg = deleteFlow(graph);
        FordFulkerson maxflow = new FordFulkerson(fg, source, destination);
        s += "Max flow : \n";
        for (int v = 0; v < fg.V(); v++) {
            for (FlowEdge e : fg.adj(v)) {
                if ((v == e.from()) && e.flow() > 0) {
                    s += "   " + e + "\n";
                }
            }
        }
        s += "Min Flow: \n";
        for (int v = 0; v < fg.V(); v++) {
            if (maxflow.inCut(v)) {
                s += (v + " ");
            }
        }
        return s;
    }

    /**
     * Permite aplicar o algoritmo um demand
     *
     * @param graph
     * @param source
     * @param destination
     * @return
     */
    public static FlowNetwork applyFordFulkerson(FlowNetwork graph, Integer source, Integer destination) {
        FordFulkerson maxflow = new FordFulkerson(graph, source, destination);
        return graph;
    }

    /**
     * Função usada para aplicar o algoritmo fordFulkerson , atualizando assim o
     * grafo em tempo real
     *
     * @param graph
     * @param source
     * @param destination
     * @return
     */
    public static FlowNetwork applyFordFulkersonInternal(FlowNetwork graph, Integer source, Integer destination) {
        FlowNetwork temp = deleteFlow(graph);
        FordFulkerson maxflow = new FordFulkerson(temp, source, destination);
        updateLigacao(temp);
        return temp;
    }

    /**
     * Permite usar o diskstra para descubrir o caminho de um vertice para o
     * outro com base na distancia e na carga !!
     *
     * @param ar
     * @param s
     * @param d
     * @return
     */
    public static String findPath(ArrayList<Ligacao> ar, int s, int d, float load) {
        EdgeWeightedDigraph g = createGraphDist(ar, load);
        DijkstraSP dSP = new DijkstraSP(g, s);
        EdgeWeightedDigraph temp = new EdgeWeightedDigraph(g.V());
        if (dSP.hasPathTo(d)) {
            for (DirectedEdge e : dSP.pathTo(d)) {
                temp.addEdge(e);
            }
            return temp.toString();
        }
        return "não tem caminho \n";
    }

    /**
     * Permite usar o diskstra para descubrir o caminho de um vertice para o
     * outro com base na distancia
     *
     * @param ar
     * @param s
     * @param d
     * @return
     */
    public static String findPath(ArrayList<Ligacao> ar, int s, int d) {
        EdgeWeightedDigraph g = createGraphDist(ar);
        DijkstraSP dSP = new DijkstraSP(g, s);
        EdgeWeightedDigraph temp = new EdgeWeightedDigraph(g.V());
        if (dSP.hasPathTo(d)) {
            for (DirectedEdge e : dSP.pathTo(d)) {
                temp.addEdge(e);
            }
            return temp.toString();
        }
        return "não tem caminho \n";
    }

    public static void graphoFor(FlowNetwork fn) {
        for (int i = 0; i < fn.V(); i++) {
            for (FlowEdge e : fn.adj(i)) {
                System.out.println(e.toString());
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws InterruptedException, ParseException {
        // TODO code application logic here

        LoadFromFileFonteE(fonteST);
        LoadFromFileCentralE(centralST);
        LoadFromFileCasa(casasST);
        LoadFromFileEquipamentos(casasST, equipamentosST);
        //dg = LoadGraphFile();
        dg = LoadGraph(casasST.size() + fonteST.size() + centralST.size() + 1 + 2);
        Casa c1 = new Casa(10,(float) 500.0, "porto");
        addCasasST(casasST, c1);
        //System.out.println(c1.getId());
        ConnectVertices(5, 4, 100);
        ConnectVertices(4, 3, 200);
        ConnectVertices(4, 7, 300);
        ConnectVertices(4, 8, 50);
        ConnectVertices(4, 9, 2);
        ConnectVertices(4, 6, 20);
        ConnectVertices(6, 2, 10);
        ConnectVertices(4, 10, 10);      

        // LoadFromFileFonteE(fonteST);
        // LoadFromFileCentralE(centralST);
        //LoadFromFileCasa(casasST);
        //LoadFromFileEquipamentos(casasST, equipamentosST);
        dg = LoadGraphFile();
        dg = LoadGraph(casasST.size() + fonteST.size() + centralST.size() + 1 + 2);
        ConnectVertices(5, 4, 100);
        //ConnectVertices(4, 3, 200);
        //ConnectVertices(4, 7, 300);
        //ConnectVertices(4, 8, 50);
        //ConnectVertices(4, 9, 2);
        //ConnectVertices(4, 6, 20);
        //ConnectVertices(6, 2, 10);

        //graphoFor(dg);

        System.out.println(findPath(ArL, 5, 2));
        System.out.println(findPath(ArL, 5, 2, 10));
        dg = applyFordFulkersonInternal(dg, 1, 0);

        //dg = removeFlowEdge(dg, 6, 2);

        dg = removeFlowEdge(dg, 6, 2);

        //ConnectVertices(6, 2, 10);
        //dg = applyFordFulkersonInternal(dg, 1, 0);
        //dg = LoadGraphFile();
        //System.out.println(ArL.toString());
        //System.out.println(dg.toString());
        //updateLigacao(dg);
        //System.out.println(getApplyFordFulkerson(dg, 1, 0));
        //System.out.println(centralST.get(5).capacityPercentage());
        //System.out.println(dg.toString());
        //g = exportFlowToWDiGraph(dg);
        //System.out.println(g.toString());
        //SaveGraph(dg);
        //System.out.println(findPath(dg, 5, 2));
        //System.out.println(printAllCentralInfo(dg, 5));
        //System.out.println(printAllFonteInfo(dg,4));
        //ConnectVertices(1, 0, 320);
        //System.out.println(PrintCentrais(centralST));
        //System.out.println(printCasaAllInfo(casasST));
        // System.out.println(dg.toString());
        //System.out.println(getFlowCasa(dg,2));
        //dg = IncreaseGraphSize(dg, 2);
        //SaveGraph(dg);
        //System.out.println(dg.toString());
        //SaveToFileCasasBin(casasST);
        //casasST = LoadToFileCasasBin();
        //System.out.println(printCasaAllInfo(casasST));
        //criar casas
        /*Casa casa3 = new Casa(3, 300, "braga");
            Equipamento test = new Equipamento(1, "frigorifico", 0, 400, 0);
            casa3.setSTEquipamento(test);
            System.out.println(printCasaAllInfo(casasST));
            addCasasST(casasST, equipamentosST, casa3);
           
            DeleteEquipamentoCasa(casasST, equipamentosST, 3, 1);
            //ligarEquipamentoCasa(casasST, 1, 1);
            System.out.println(printCasaAllInfo(casasST));
            //casa casa2 = new casa(2, 500, "porto");
            SaveToFileAll(casasST);
            
            //criar equipamentos (0 - consumidor / 1 - gerador)
            equipamento tv = new equipamento(1, "TV", 0, 100, 0);
            
            equipamento tv1 = new equipamento(1, "TV", 0, 100, 0);
            equipamento pc1 = new equipamento(2, "PC", 0, 400, 0);
            
            //adicionar às STEquipamento de cada casa
            casa1.setSTEquipamento(tv);
            casa1.setSTEquipamento(pc);
            casa2.setSTEquipamento(tv1);
            casa2.setSTEquipamento(pc1);
            //casasST.put(2, casa2);
            //System.out.println(casa1.getSTEquipamento().get(1).printMyInfo());
            //System.out.println(printCasaAllInfo(casasST));
            //addCasasST(casasST, casa2);
            //------------ CASA 1 -----------
            casa1.powerOn(1);
            Thread.sleep(2000);
            casa1.powerOn(2);
            
            casa2.powerOn(1);
            Thread.sleep(2000);
            casa2.powerOn(2);
            
            casa1.powerOff(1, 2);
            casa2.powerOff(1, 2);
            casa2.powerOff(2, 2);
            casa1.powerOn(1);
            casa1.powerOff(1, 2);
            casa1.powerOff(2, 2); //poweroff tem input de tempo
            
            casa1.calcularWattsConsumidos(1);//total de watts consumidos por equipamento 1(TV)
            
            //CALCULAR CONSUMO NUM INTERVALO
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            Timestamp inicio;
            Timestamp fim;
            
            Date datestart = dateFormat.parse("23/09/2007");
            Date dateend = dateFormat.parse("30/11/2018");
            long timestart = datestart.getTime();
            long timeend = dateend.getTime();
            
            inicio = new Timestamp(timestart);
            fim = new Timestamp(timeend);
            
            casa1.calcularWattsBetween(inicio, fim);
            //System.out.println(inicio + " - " + fim);
            //------------ CASA 2 -----------
            //casa2.powerOn(1);
            //casa1.printTimeStamp();
            StdOut.println(casa1.printHistoricoConsumoEquipamento(1));
            
            //StdOut.println(casa1.BiggestConsumer());
            //StdOut.println("Consumo total "+ casa1.getConsumoTotal());
            //imprimir equipamentos existentes na casa "c"
            //StdOut.println(casa1.printEquipamentos());
            //StdOut.println("Consumo final "+ casa1.getConsumoTotal());
            addCasasST(casasST, equipamentosST, casa1);
            addCasasST(casasST, equipamentosST, casa2);
            SaveToFileAll(casasST);*/
    }
}
