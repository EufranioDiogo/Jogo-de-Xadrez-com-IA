package chessgameai.xadrez.engine.player.agente;

import chessgameai.xadrez.engine.tabuleiro.Movimento;
import chessgameai.xadrez.engine.tabuleiro.Tabuleiro;

/**
 *
 * @author ed
 */
public interface EstrategiaMovimento {
    Movimento executar(Tabuleiro tabuleiro);
}
