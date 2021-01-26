/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessgameai.xadrez.engine.player.agente;

import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;

/**
 *
 * @author ed
 */
public interface AvaliadorTabuleiro {
    public int avaliar(Tabuleiro tabuleiro, int profundidade);
}
